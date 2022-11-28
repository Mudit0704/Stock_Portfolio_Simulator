package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Represents a single flexible portfolio and the set of operations related to it. Extends
 * {@link AbstractPortfolio}.
 */
public class FlexiblePortfolio extends AbstractPortfolio {

  protected LocalDate creationDate;


  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   * @param date         date on which this portfolio is created.
   */
  public FlexiblePortfolio(IStockService stockService, Map<IStock, Double> stocks,
      double transactionFee, LocalDate date) {
    super(stockService, stocks);
    this.stockHistoryQty = new HashMap<>();

    costBasisHistory = new HashMap<>();
    double transactionFeeCostBasis = 0.0;

    if (stocks.size() != 0) {
      creationDate = date;
      for (Map.Entry<IStock, Double> mapEntry : stocks.entrySet()) {
        Map<LocalDate, Double> dateQtyMap = new HashMap<>();
        dateQtyMap.put(this.creationDate, mapEntry.getValue());
        this.stockHistoryQty.put(mapEntry.getKey(), dateQtyMap);
        transactionFeeCostBasis += transactionFee;
      }
      costBasisHistory.put(creationDate, getPortfolioValue(creationDate) + transactionFeeCostBasis);
    }
  }

  @Override
  public String getPortfolioComposition() {
    return stockQuantityMap.size() > 0 ? stockQuantityMap.entrySet().stream()
        .map(x -> x.getKey().getStockTicker() + " -> " + x.getValue() + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public void addStocksToPortfolio(IStock stock, Double quantity,
      LocalDate date, double transactionFee) {
    double stockQty = 0;

    if (isTransactionSequenceInvalid(stock, date, TransactionType.BUY)) {
      throw new IllegalArgumentException("Date given is not chronological based on previous "
          + "transaction dates\n");
    }

    if (stockQuantityMap.containsKey(stock)) {
      stockQty = stockQuantityMap.get(stock);
    }
    double updatedQty = stockQty + quantity;
    stockQuantityMap.put(stock, updatedQty);
    updateHistoricHoldings(stock, date, updatedQty);

    double prevCostBasis = this.getPortfolioCostBasisByDate(date);
    double updateCostBasisBy = stock.getValue(date) * quantity + transactionFee;
    costBasisHistory.put(date, prevCostBasis + updateCostBasisBy);
  }

  @Override
  public void sellStocksFromPortfolio(IStock stock, Double quantity,
      LocalDate date, double transactionFee)
      throws IllegalArgumentException {
    double stockQty;

    if (isTransactionSequenceInvalid(stock, date, TransactionType.SELL)) {
      throw new IllegalArgumentException("Date given is not chronological based on previous "
          + "transaction dates\n");
    }

    if (!stockQuantityMap.containsKey(stock)) {
      throw new IllegalArgumentException("Cannot sell stock if portfolio doesn't contain it.");
    }

    stockQty = stockQuantityMap.get(stock);

    if (stockQty - quantity < 0) {
      throw new IllegalArgumentException("Do not have more than " + stockQty + " shares presently");
    }

    Double updatedQty = stockQty - quantity;
    stockQuantityMap.put(stock, updatedQty);

    updateHistoricHoldings(stock, date, updatedQty);

    double prevCostBasis = this.getPortfolioCostBasisByDate(date);
    costBasisHistory.put(date, prevCostBasis + transactionFee);
  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    if (date.isBefore(this.creationDate)) {
      throw new IllegalArgumentException("Portfolio didn't exist at this date.");
    }
    List<LocalDate> listOfDates = new ArrayList<>(costBasisHistory.keySet());

    LocalDate recentDate = date;
    if (!costBasisHistory.containsKey(date)) {
      recentDate = getClosestDate(date, listOfDates);
    }
    if (recentDate == null) {
      return 0;
    }
    return costBasisHistory.get(recentDate);
  }

  @Override
  public String getPortfolioCompositionOnADate(LocalDate date) {

    if (date.isBefore(this.creationDate)) {
      throw new IllegalArgumentException("Portfolio didn't exist at this date");
    }
    StringBuilder composition = new StringBuilder();

    for (Map.Entry<IStock, Map<LocalDate, Double>> mapEntry : this.stockHistoryQty.entrySet()) {
      Map<LocalDate, Double> qtyHistory = mapEntry.getValue();
      LocalDate closestDate = date;

      List<LocalDate> listOfDates = new ArrayList<>(qtyHistory.keySet());

      if (!qtyHistory.containsKey(date)) {
        closestDate = getClosestDate(date, listOfDates);
      }

      double qty = 0;
      if (qtyHistory.containsKey(date) || closestDate != null) {
        qty = mapEntry.getValue().get(closestDate);
      }

      if (qty != 0) {
        composition.append(mapEntry.getKey().getStockTicker()).append(" -> ")
            .append(qty).append("\n");
      }
    }
    return composition.toString();
  }

  @Override
  public String getPortfolioPerformance(LocalDate start, LocalDate end) {
    if (start.isAfter(end) || start.isEqual(end)) {
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
    IPerformanceVisualizer visualizer;
    int timeSpanJump;

    if (timespan <= 30) {
      timeSpanJump = 1;
      visualizer = new DaysPerformanceVisualizer(this);
    } else if (timespan <= 150) {
      timeSpanJump = (int) (timespan / 5);
      tempDate = tempDate.plusDays(timeSpanJump - 1);
      visualizer = new DaysPerformanceVisualizer(this);
    } else if (timespan <= 912) {
      timeSpanJump = 1;
      visualizer = new MonthsPerformanceVisualizer(this);
    } else if (timespan <= 1826) {
      timeSpanJump = 2;
      visualizer = new MonthsPerformanceVisualizer(this);
    } else {
      timeSpanJump = 1;
      visualizer = new YearsPerformanceVisualizer(this);
    }
    return visualizer.visualize(tempDate, end, timeSpanJump, dateValue);
  }

  @Override
  public void savePortfolio(String path)
      throws RuntimeException, ParserConfigurationException {
    if (stockQuantityMap.size() == 0) {
      throw new RuntimeException("No portfolios to save\n");
    }
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("portfolio");
      doc.appendChild(rootElement);

      Element creationDateElement = doc.createElement("created");
      creationDateElement.appendChild(doc.createTextNode(this.creationDate.toString()));
      rootElement.appendChild(creationDateElement);

      fillCostBasisXMLHistory(doc, rootElement);

      for (Map.Entry<IStock, Double> stock : this.stockQuantityMap.entrySet()) {
        Element stockElement = doc.createElement("stock");

        Element stockTickerSymbol = doc.createElement("tickerSymbol");
        stockTickerSymbol.appendChild(doc.createTextNode(stock.getKey().getStockTicker()));

        Element stockCurrentHoldings = doc.createElement("currentHolding");
        stockCurrentHoldings.setAttribute("date", LocalDate.now().toString());
        stockCurrentHoldings.appendChild(doc.createTextNode(String.valueOf(stock.getValue())));

        fillStockQuantityXMLHistory(stock.getKey(), doc, stockElement);

        Element stockPrice = doc.createElement("stockPrice");
        stockPrice.appendChild(
            doc.createTextNode(String.valueOf(stock.getKey().getValue(LocalDate.now()))));

        stockElement.appendChild(stockTickerSymbol);
        stockElement.appendChild(stockPrice);
        stockElement.appendChild(stockCurrentHoldings);
        rootElement.appendChild(stockElement);
      }

      writeXMLFile(doc, path);

    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (DateTimeParseException e) {
      throw new RuntimeException("API Failure...\n");
    }
  }

  @Override
  public void retrievePortfolio(String path)
      throws IOException, SAXException, ParserConfigurationException, RuntimeException {
    if (this.stockQuantityMap.size() > 0) {
      throw new RuntimeException("Portfolios already populated\n");
    }
    File inputFile = new File(path);
    if (!inputFile.isFile()) {
      throw new FileNotFoundException("Cannot find file: " + path);
    }
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(inputFile);
    doc.getDocumentElement().normalize();
    String creationTime = doc.getDocumentElement().getElementsByTagName("created")
        .item(0).getTextContent();
    this.creationDate = LocalDate.parse(creationTime);

    populateCostBasisHistoryFromXML(doc);

    NodeList nList = doc.getDocumentElement().getElementsByTagName("stock");

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Node nNode = nList.item(temp);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        String tickerSymbol = eElement.getElementsByTagName("tickerSymbol")
            .item(0).getTextContent();

        IStock newStock = apiOptimizer.cacheGetObj(tickerSymbol);
        if (newStock == null) {
          newStock = new Stock(tickerSymbol, this.stockService);
          apiOptimizer.cacheSetObj(tickerSymbol, newStock);
        }

        getHistoricQtyFromXML(eElement, newStock);

        double currentHolding = Double.parseDouble(eElement.getElementsByTagName("currentHolding")
            .item(0).getTextContent());
        this.stockQuantityMap.put(newStock, currentHolding);
      }
    }
  }

  @Override
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    if (date.isBefore(this.creationDate)) {
      return 0.0;
    }

    double portfolioValue = 0;

    for (Map.Entry<IStock, Map<LocalDate, Double>> mapEntry : this.stockHistoryQty.entrySet()) {
      Map<LocalDate, Double> qtyHistory = mapEntry.getValue();
      LocalDate closestDate = date;

      List<LocalDate> listOfDates = new ArrayList<>(qtyHistory.keySet());

      if (!qtyHistory.containsKey(date)) {
        closestDate = getClosestDate(date, listOfDates);
      }

      double qty = 0;
      if (qtyHistory.containsKey(date) || closestDate != null) {
        qty = mapEntry.getValue().get(closestDate);
      }

      double stockValue;
      try {
        stockValue = mapEntry.getKey().getValue(date);
      } catch (IllegalArgumentException e) {
        stockValue = 0.0;
      }

      portfolioValue += stockValue * qty;
    }
    return portfolioValue;
  }

  protected void updateHistoricHoldings(IStock stock, LocalDate date, Double updatedQty) {
    Map<LocalDate, Double> map = this.stockHistoryQty.getOrDefault(stock, new HashMap<>());
    map.put(date, updatedQty);
    this.stockHistoryQty.put(stock, map);
  }

  protected boolean isTransactionSequenceInvalid(IStock stock, LocalDate date,
    TransactionType transactionType) {
    if (date.isBefore(this.creationDate)) {
      throw new IllegalArgumentException("Portfolio didn't exist at this date.");
    }

    if (stockHistoryQty.containsKey(stock) && stockHistoryQty.get(stock).size() > 1) {
      Optional<LocalDate> maxStockHistory = this.stockHistoryQty.get(stock).keySet().stream()
          .max(LocalDate::compareTo);
      Optional<LocalDate> minStockHistory = this.stockHistoryQty.get(stock).keySet().stream()
          .min(LocalDate::compareTo);
      return (date.isAfter(minStockHistory.get()) && date.isBefore(maxStockHistory.get()))
          || date.isBefore(minStockHistory.get());
    } else if (stockHistoryQty.containsKey(stock) && stockHistoryQty.get(stock).size() == 1) {
      Optional<LocalDate> minStockHistory = this.stockHistoryQty.get(stock).keySet().stream()
          .min(LocalDate::compareTo);
      return date.isBefore(minStockHistory.get());
    }
    return false;
  }

  protected void populateCostBasisHistoryFromXML(Document doc) {
    int numCostBasisDates = doc.getDocumentElement().getElementsByTagName("cost-basis").getLength();
    int costBasisIdx = 0;
    while (costBasisIdx < numCostBasisDates) {
      String date = doc.getDocumentElement().getElementsByTagName("cost-basis")
          .item(costBasisIdx).getAttributes().getNamedItem("date").getNodeValue();

      double costBasis = Double.parseDouble(
          doc.getDocumentElement().getElementsByTagName("cost-basis")
              .item(costBasisIdx).getTextContent());

      this.costBasisHistory.put(LocalDate.parse(date), costBasis);
      costBasisIdx++;
    }
  }

  protected void getHistoricQtyFromXML(Element eElement, IStock newStock) {
    int numDates = eElement.getElementsByTagName("stockQuantity").getLength();
    int stockQuantityIdx = 0;

    Map<LocalDate, Double> dateQtyMap = new HashMap<>();
    List<LocalDate> dateList = new ArrayList<>();

    while (stockQuantityIdx < numDates) {
      String modificationDate = eElement.getElementsByTagName("stockQuantity")
          .item(stockQuantityIdx).getAttributes().getNamedItem("date").getNodeValue();

      double stockQuantity = Double.parseDouble(eElement.getElementsByTagName("stockQuantity")
          .item(stockQuantityIdx).getTextContent());

      LocalDate modDate = LocalDate.parse(modificationDate);
      if (dateList.size() > 0) {
        Optional<LocalDate> minStockHistory = dateList.stream().min(LocalDate::compareTo);

        if ((modDate.isAfter(minStockHistory.get()))) {
          throw new IllegalArgumentException("Invalid transaction sequence\n");
        }
      }
      dateList.add(modDate);
      dateQtyMap.put(modDate, stockQuantity);
      stockQuantityIdx++;
    }

    this.stockHistoryQty.put(newStock, dateQtyMap);
  }

  protected void fillCostBasisXMLHistory(Document doc, Element rootElement) {
    for (Map.Entry<LocalDate, Double> costBasis : this.costBasisHistory.entrySet()) {
      Element costBasisXML;
      costBasisXML = doc.createElement("cost-basis");
      costBasisXML.appendChild(doc.createTextNode(String.valueOf(costBasis.getValue())));

      costBasisXML.setAttribute("date", costBasis.getKey().toString());
      rootElement.appendChild(costBasisXML);
    }
  }

  protected void fillStockQuantityXMLHistory(IStock stock, Document doc, Element stockElement) {
    Map<LocalDate, Double> historicQty = this.stockHistoryQty.get(stock);

    historicQty = historicQty.entrySet().stream()
      .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
        (e1, e2) -> e2, LinkedHashMap::new));

    for (Map.Entry<LocalDate, Double> stockQuantity : historicQty.entrySet()) {
      Element stockQuantityXML;
      stockQuantityXML = doc.createElement("stockQuantity");
      stockQuantityXML.appendChild(
          doc.createTextNode(String.valueOf(stockQuantity.getValue())));

      stockQuantityXML.setAttribute("date", stockQuantity.getKey().toString());
      stockElement.appendChild(stockQuantityXML);
    }
  }

  protected void writeXMLFile(Document doc, String path) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute("indent-number", 3);
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(path));
    transformer.transform(source, result);
  }
}