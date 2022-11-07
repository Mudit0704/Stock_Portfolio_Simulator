package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
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

public class FlexiblePortfolioImpl extends AbstractPortfolio {
  private Map<LocalDate, Map<IStock, Long>> dateWiseStockQty;
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   */
  public FlexiblePortfolioImpl(IStockService stockService, Map<IStock, Long> stocks) {
    super(stockService, stocks);
    this.dateWiseStockQty = new HashMap<>();

    if(stocks.size() != 0) {
      this.dateWiseStockQty.put(this.creationDate, stocks);
    }
  }

  @Override
  public String getPortfolioComposition() {
    return stockQuantityMap.size() > 0 ? stockQuantityMap.entrySet().stream()
        .map(x -> x.getKey().getStockTicker() + " -> " + x.getValue() + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public void addStocksToPortfolio(IStock stock, Long quantity) {
    long stockQty = 0;
    if(stockQuantityMap.containsKey(stock)) {
      stockQty = stockQuantityMap.get(stock);
    }
    stockQuantityMap.put(stock, stockQty + quantity);
    this.dateWiseStockQty.put(LocalDate.now(), stockQuantityMap);
  }

  @Override
  public void sellStocksFromPortfolio(IStock stock, Long quantity) throws IllegalArgumentException {
    long stockQty = 0;
    if (!stockQuantityMap.containsKey(stock)) {
      throw new IllegalArgumentException("Cannot sell stock if portfolio doesn't contain it.");
    }

    stockQty = stockQuantityMap.get(stock);

    if (stockQty - quantity < 0) {
      throw new IllegalArgumentException("Do not have more than " + quantity + " shares presently");
    }

    stockQuantityMap.put(stock, stockQty - quantity);
    this.dateWiseStockQty.put(LocalDate.now(), stockQuantityMap);
  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    return 0;
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
      creationDateElement.appendChild(doc.createTextNode(creationDate.toString()));
      rootElement.appendChild(creationDateElement);

      for (Map.Entry<IStock, Long>  stock : this.stockQuantityMap.entrySet()) {
        Element stockElement = doc.createElement("stock");

        Element stockTickerSymbol = doc.createElement("tickerSymbol");
        stockTickerSymbol.appendChild(doc.createTextNode(stock.getKey().getStockTicker()));

        Element stockCurrentHoldings = doc.createElement("currentHolding");
        stockCurrentHoldings.setAttribute("date", LocalDate.now().toString());
        stockCurrentHoldings.appendChild(doc.createTextNode(String.valueOf(stock.getValue())));

        for(Map.Entry<LocalDate, Map<IStock, Long>> stockQuantity
            : this.dateWiseStockQty.entrySet()) {
          Map<IStock, Long> thisDateStockQty = stockQuantity.getValue();
          Element stockQuantityXML;
          if(!(thisDateStockQty.get(stock.getKey()) == null)) {
            stockQuantityXML = doc.createElement("stockQuantity");
            stockQuantityXML.appendChild(doc.createTextNode(String.valueOf(thisDateStockQty.get(stock.getKey()))));
          }
          else {
            continue;
          }

          stockQuantityXML.setAttribute("date", stockQuantity.getKey().toString());
          stockElement.appendChild(stockQuantityXML);
        }

        Element stockPrice = doc.createElement("stockPrice");
        stockPrice.appendChild(
          doc.createTextNode(String.valueOf(stock.getKey().getValue(LocalDate.now()))));

        stockElement.appendChild(stockTickerSymbol);
        stockElement.appendChild(stockPrice);
        stockElement.appendChild(stockCurrentHoldings);
        rootElement.appendChild(stockElement);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", 3);
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(path));
      transformer.transform(source, result);

    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (DateTimeParseException e) {
      throw new RuntimeException("API Failure...\n");
    }
    this.creationDate = LocalDate.now();
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
    NodeList nList = doc.getDocumentElement().getElementsByTagName("stock");

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Node nNode = nList.item(temp);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        String tickerSymbol = eElement.getElementsByTagName("tickerSymbol")
          .item(0).getTextContent();

        int numDates = eElement.getElementsByTagName("stockQuantity").getLength();
        int stockQuantityIdx = 0;

        IStock newStock = apiOptimizer.cacheGetObj(tickerSymbol);
        if (newStock == null) {
          newStock = new Stock(tickerSymbol, this.stockService);
          apiOptimizer.cacheSetObj(tickerSymbol, newStock);
        }

        while(stockQuantityIdx < numDates) {
          String modificationDate = eElement.getElementsByTagName("stockQuantity")
            .item(stockQuantityIdx).getAttributes().getNamedItem("date").getNodeValue();

          long stockQuantity = Long.parseLong(eElement.getElementsByTagName("stockQuantity")
            .item(stockQuantityIdx).getTextContent());

          LocalDate modDate = LocalDate.parse(modificationDate);
          Map<IStock, Long> stockQty = null;
          if(this.dateWiseStockQty.containsKey(modDate)) {
            stockQty = this.dateWiseStockQty.get(modDate);
          } else {
            stockQty = new HashMap<>();
          }

          stockQty.put(newStock, stockQuantity);
          this.dateWiseStockQty.put(modDate, stockQty);
          stockQuantityIdx++;
        }

//        String creationTime = eElement.getElementsByTagName("created")
//          .item(0).getTextContent();
//        this.creationDate = LocalDate.parse(creationTime);
        long currentHolding = Long.parseLong(eElement.getElementsByTagName("currentHolding")
          .item(0).getTextContent());
        this.stockQuantityMap.put(newStock, currentHolding);
      }
    }
  }

  @Override
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    if(date.isBefore(this.creationDate)) {
      return 0.0;
    }

    double portfolioValue = 0;
    LocalDate closestDate = getClosestDate(date);
    if(closestDate == null || !this.dateWiseStockQty.containsKey(date)) {
      throw new IllegalArgumentException("Could not find portfolio value for the given date.");
    }

    Map<IStock, Long> stockQty = dateWiseStockQty.get(date);

    for (Map.Entry<IStock, Long> stock : stockQty.entrySet()) {
      portfolioValue += stock.getKey().getValue(date) * stock.getValue();
    }
    return portfolioValue;
  }

  private LocalDate getClosestDate(LocalDate date) {
    long minDiff = Long.MAX_VALUE;
    LocalDate result = null;

    for (Map.Entry<LocalDate, Map<IStock, Long>> stock : dateWiseStockQty.entrySet()) {
      LocalDate currDate = stock.getKey();
      long diff = ChronoUnit.DAYS.between(currDate, date);

      if (currDate.isBefore(date) && minDiff > diff) {
        minDiff = diff;
        result = currDate;
      }
    }
    return result;
  }
}