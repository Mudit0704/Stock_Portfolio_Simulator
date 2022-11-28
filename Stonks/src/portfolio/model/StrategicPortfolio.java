package portfolio.model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class represents a StrategicPortfolio. This class implements the IStrategicPortfolio
 * interface and uses a class adapter over FlexiblePortfolio. This class offers methods to
 * invest in this portfolio using stocks after applying strategies.
 */
public class StrategicPortfolio extends FlexiblePortfolio implements IStrategicPortfolio {

  private static class Pair<S,T> {
    S s;
    T t;
    Pair(S s, T t) {
      this.s = s;
      this.t = t;
    }
  }

  private double transactionFee;

  protected List<Map<LocalDate, Map<IStock, Pair<Double, Double>>>> listOfScheduledStocks;
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee the transaction fee to be applied while purchasing stocks to create this
   *                       portfolio.
   * @param date           date on which this portfolio is created.
   */
  protected StrategicPortfolio(IStockService stockService, Map<IStock, Double> stocks,
    double transactionFee, LocalDate date) {
    super(stockService, stocks, transactionFee, date);
    listOfScheduledStocks = new ArrayList<>();
    this.creationDate = date;
  }

  protected void performCascadingUpdateForRetrospectiveBuy(Map<LocalDate, Double> historicQty,
      double quantity, LocalDate dateAfterPurchaseToUpdate, double costBasisUpdateFactor) {
    for(Map.Entry<LocalDate, Double> qtyOnDate:historicQty.entrySet()) {
      if (qtyOnDate.getKey().isAfter(dateAfterPurchaseToUpdate)) {
        Double qtyToUpdate = qtyOnDate.getValue();
        qtyToUpdate += quantity;
        historicQty.put(qtyOnDate.getKey(), qtyToUpdate);

        costBasisHistory.put(qtyOnDate.getKey(), costBasisHistory.get(qtyOnDate.getKey())
            + costBasisUpdateFactor);
      }
    }
  }

  @Override
  public void addStocksToPortfolio(IStock stock, Double quantity,
      LocalDate date, double transactionFee) {
    double stockQty = 0;

    if (isTransactionSequenceInvalid(stock, date, TransactionType.BUY)) {
      throw new IllegalArgumentException("Date given is not chronological based on previous "
        + "transaction dates\n");
    }

    Map<LocalDate, Double> historicQty = stockHistoryQty.getOrDefault(stock, new LinkedHashMap<>());
    List<LocalDate> listOfDates = new ArrayList<>(historicQty.keySet());
    LocalDate recentTxnDateForCurrent = getClosestDate(date, listOfDates);

    if (recentTxnDateForCurrent != null) {
      stockQty = historicQty.get(recentTxnDateForCurrent);
    }

    double updatedQty = stockQty + quantity;
    updateHistoricHoldings(stock, date, updatedQty);

    stockQuantityMap.put(stock, stockQuantityMap.getOrDefault(stock, 0d) + quantity);

    double prevCostBasis = this.getPortfolioCostBasisByDate(date);
    double updateCostBasisBy = stock.getValue(date) * quantity + transactionFee;
    costBasisHistory.put(date, prevCostBasis + updateCostBasisBy);

    performCascadingUpdateForRetrospectiveBuy(historicQty, quantity, date, updateCostBasisBy);
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee) {

    for(Map.Entry<IStock, Double> proportion:stockProportions.entrySet()) {
      addStocksToPortfolio(proportion.getKey(), proportion.getValue(), date, transactionFee);
    }
  }

  @Override
  protected boolean isTransactionSequenceInvalid(IStock stock, LocalDate date,
      TransactionType transactionType) {
    if (transactionType == TransactionType.BUY) {
      return false;
    } else {
      return super.isTransactionSequenceInvalid(stock, date, transactionType);
    }
  }

  @Override
  protected void scheduleInvestment(LocalDate date, double amount, double transactionFee,
      Map<IStock, Double> stocks) {
    Map<LocalDate, Map<IStock, Pair<Double, Double>>> dateMap = new HashMap<>();
    Map<IStock, Pair<Double, Double>> stockQty = new HashMap<>();

    for(Map.Entry<IStock, Double> mapEntry:stocks.entrySet()) {
        stockQty.put(mapEntry.getKey(), new Pair<>(amount, mapEntry.getValue()));
    }
    dateMap.put(date, stockQty);
    listOfScheduledStocks.add(dateMap);
  }

  protected void saveStrategy(String path) throws ParserConfigurationException {
    if (listOfScheduledStocks.size() == 0) {
      return;
    }
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();
      Element transactionFee = doc.createElement("transaction-fee");
      transactionFee.appendChild(doc.createTextNode(String.valueOf(this.transactionFee)));
      Element rootElement = doc.createElement("strategy");
      doc.appendChild(rootElement);
      rootElement.appendChild(transactionFee);

      for(Map<LocalDate, Map<IStock, Pair<Double, Double>>> dateMap:this.listOfScheduledStocks) {
        dateMap = dateMap.entrySet().stream()
          .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (e1, e2) -> e2, LinkedHashMap::new));

        for(Map.Entry<LocalDate, Map<IStock, Pair<Double, Double>>> mapEntry:dateMap.entrySet()) {
          Element investmentElement = doc.createElement("investment");
          investmentElement.setAttribute("date", String.valueOf(mapEntry.getKey()));
          Map<IStock, Pair<Double, Double>> stockProportions = mapEntry.getValue();
          Double amount = 0d;
          for(Map.Entry<IStock, Pair<Double, Double>> stockProportion:stockProportions.entrySet()) {
            amount = stockProportion.getValue().s;

            Element stock = doc.createElement("stock");
            stock.appendChild(doc.createTextNode(stockProportion.getKey().getStockTicker()));
            stock.setAttribute("percentage", String.valueOf(stockProportion.getValue().t));
            investmentElement.appendChild(stock);
          }

          Element totalAmount = doc.createElement("amount");
          totalAmount.appendChild(doc.createTextNode(amount.toString()));
          investmentElement.appendChild(totalAmount);
          rootElement.appendChild(investmentElement);
        }
        super.writeXMLFile(doc, path);
      }
    } catch (DateTimeParseException e) {
      throw new RuntimeException("API Failure...\n");
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void savePortfolio(String path) throws ParserConfigurationException {
    super.savePortfolio(path);
    String[] name = path.split("\\.");
    String strategyPath = name[0] + "_strategy." + name[1];
    this.saveStrategy(strategyPath);
  }

  @Override
  public void retrievePortfolio(String path)
    throws IOException, ParserConfigurationException, SAXException {
    super.retrievePortfolio(path);
    String[] name = path.split("\\.");
    String strategyPath = name[0] + "_strategy." + name[1];
    this.retrieveStrategy(strategyPath);
    this.executeEligibleTransactions();
  }

  protected void executeEligibleTransactions() {
    for(Map<LocalDate, Map<IStock, Pair<Double, Double>>> dateMap:this.listOfScheduledStocks) {
      for(Map.Entry<LocalDate, Map<IStock, Pair<Double, Double>>> mapEntry:
        dateMap.entrySet()) {
        LocalDate date = mapEntry.getKey();
        Map<IStock, Pair<Double, Double>> stockQty = mapEntry.getValue();
        Double amount = 0d;
        Map<IStock, Double> stockRatios = new HashMap<>();
        for(Map.Entry<IStock, Pair<Double, Double>> stocks:stockQty.entrySet()) {
          amount = stocks.getValue().s;
          stockRatios.put(stocks.getKey(), stocks.getValue().t);
        }

        IStrategy strategy = new NormalStrategy.NormalStrategyBuilder()
          .setTotalAmount(amount)
          .setDate(date)
          .build();

        Map<LocalDate, Map<IStock, Double>> result = strategy.applyStrategy(stockRatios);

        for(Map.Entry<LocalDate, Map<IStock, Double>> resultEntry:result.entrySet()) {
          if (resultEntry.getKey().isAfter(LocalDate.now())) {
            break;
          }
          dateMap.remove(date);
          this.investStocksIntoStrategicPortfolio(resultEntry.getValue(),
            resultEntry.getKey(), this.transactionFee);
        }
      }
    }
  }

  protected void retrieveStrategy(String strategyPath)
    throws IOException, ParserConfigurationException, SAXException {
    if (this.listOfScheduledStocks.size() > 0) {
      throw new RuntimeException("Portfolios already populated\n");
    }

    File inputFile = new File(strategyPath);
    if (!inputFile.isFile()) {
      return;
    }
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(inputFile);
    doc.getDocumentElement().normalize();
    this.transactionFee = Double.parseDouble(doc.getDocumentElement().getElementsByTagName("transaction-fee").item(0).getTextContent());

    NodeList nList = doc.getDocumentElement().getElementsByTagName("investment");

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Map<LocalDate, Map<IStock, Pair<Double, Double>>> dateMap = new HashMap<>();
      Node nNode = nList.item(temp);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;

        LocalDate txnDate = LocalDate.parse(eElement.getAttributes().getNamedItem("date").getNodeValue());

        Double amount = Double.valueOf(eElement.getElementsByTagName("amount")
            .item(0).getTextContent());

        int numStocks = eElement.getElementsByTagName("stock").getLength();
        int idx = 0;
        IStock newStock = null;
        Map<IStock, Pair<Double, Double>> map = new HashMap<>();
        while(idx < numStocks) {
          String tickerSymbol = eElement.getElementsByTagName("stock")
            .item(idx).getTextContent();

          Double percentage = Double.valueOf(eElement.getElementsByTagName("stock")
              .item(idx).getAttributes().getNamedItem("percentage").getNodeValue());

          idx++;

          newStock = apiOptimizer.cacheGetObj(tickerSymbol);

          if (newStock == null) {
            newStock = new Stock(tickerSymbol, this.stockService);
            apiOptimizer.cacheSetObj(tickerSymbol, newStock);
          }
          map.put(newStock, new Pair<>(amount, percentage));
        }
        dateMap.put(txnDate, map);
        this.listOfScheduledStocks.add(dateMap);
      }
    }
  }

  public Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end) {
    if (start.isAfter(end) || start.isEqual(end) || end.isBefore(start)) {
      throw new IllegalArgumentException("Invalid dates\n");
    } else if (start.isBefore(this.creationDate)) {
      throw new IllegalArgumentException(
          "Invalid start date. It is before the portfolio creation date.");
    } else if (end.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Invalid end date. It is after today's date.");
    }

    LocalDate tempDate = start;
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    long timespan = ChronoUnit.DAYS.between(start, end);
    int timeSpanJump;

    if (timespan <= 30) {
      timeSpanJump = 1;
      new DaysPerformanceVisualizer(this).populatePortfolioValues(tempDate, end, timeSpanJump,
          dateValue);
    } else if (timespan <= 150) {
      timeSpanJump = (int) (timespan / 5);
      tempDate = tempDate.plusDays(timeSpanJump - 1);
      new DaysPerformanceVisualizer(this).populatePortfolioValues(tempDate, end, timeSpanJump, dateValue);
    } else if (timespan <= 912) {
      timeSpanJump = 1;
      new MonthsPerformanceVisualizer(this).populatePortfolioValues(tempDate, end, timeSpanJump, dateValue);
    } else if (timespan <= 1826) {
      timeSpanJump = 2;
      new MonthsPerformanceVisualizer(this).populatePortfolioValues(tempDate, end, timeSpanJump, dateValue);
    } else {
      timeSpanJump = 1;
      new YearsPerformanceVisualizer(this).populatePortfolioValues(tempDate, end, timeSpanJump, dateValue);
    }
    return dateValue;
  }

}