package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
 * Represents a single portfolio and the set of operations related to it. Implements
 * {@link IPortfoliosModel}.
 */
class Portfolio implements IPortfolio {

  //region Class Members
  private static class Pair<S, T> {

    S s;
    T t;

    public Pair(S s, T t) {
      this.s = s;
      this.t = t;
    }
  }

  private final List<Pair<IStock, Double>> stocks = new ArrayList<>();
  private final IStockService stockService;
  private final IStockAPIOptimizer apiOptimizer;
  //endregion

  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   */
  public Portfolio(IStockService stockService, Map<IStock, Double> stocks) {
    this.stockService = stockService;
    setPortfolioStocks(stocks);
    apiOptimizer = StockCache.getInstance();
  }

  //region Public Methods
  @Override
  public String getPortfolioComposition() {
    return stocks.size() > 0 ? stocks.stream().map(x -> x.s.getStockTicker() + " -> " + x.t + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    double portfolioValue = 0;

    for (Pair<IStock, Double> stock : this.stocks) {
      try {
        portfolioValue += stock.s.getValue(date) * stock.t;
      } catch (DateTimeParseException e) {
        throw new RuntimeException("API failure...\n");
      }
    }
    return portfolioValue;
  }

  @Override
  public void savePortfolio(String path)
      throws RuntimeException, ParserConfigurationException {
    if (stocks.size() == 0) {
      throw new RuntimeException("No portfolios to save\n");
    }
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("portfolio");
      doc.appendChild(rootElement);

      for (Pair<IStock, Double> stock : this.stocks) {
        Element stockElement = doc.createElement("stock");

        Element stockTickerSymbol = doc.createElement("tickerSymbol");
        stockTickerSymbol.appendChild(doc.createTextNode(stock.s.getStockTicker()));

        Element stockQuantity = doc.createElement("stockQuantity");
        stockQuantity.appendChild(doc.createTextNode(String.valueOf(stock.t)));

        Element stockPrice = doc.createElement("stockPrice");
        stockPrice.appendChild(
            doc.createTextNode(String.valueOf(stock.s.getValue(LocalDate.now()))));

        stockElement.appendChild(stockTickerSymbol);
        stockElement.appendChild(stockQuantity);
        stockElement.appendChild(stockPrice);
        rootElement.appendChild(stockElement);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(path));
      transformer.transform(source, result);

    } catch (TransformerException e) {
      throw new RuntimeException(e);
    } catch (DateTimeParseException e) {
      throw new RuntimeException("API Failure...\n");
    }
  }

  @Override
  public void retrievePortfolio(String path)
      throws IOException, SAXException, ParserConfigurationException, RuntimeException {
    if (this.stocks.size() > 0) {
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
        double stockQuantity = Double.parseDouble(eElement.getElementsByTagName("stockQuantity")
            .item(0).getTextContent());
        IStock newStock = apiOptimizer.cacheGetObj(tickerSymbol);
        if (newStock == null) {
          newStock = new Stock(tickerSymbol, this.stockService);
          apiOptimizer.cacheSetObj(tickerSymbol, newStock);
        }
        this.stocks.add(new Pair<>(newStock, stockQuantity));
      }
    }
  }
  //endregion

  //region Private Methods
  private void setPortfolioStocks(Map<IStock, Double> stocks) {
    for (Map.Entry<IStock, Double> entry : stocks.entrySet()) {
      this.stocks.add(new Pair<>(entry.getKey(), entry.getValue()));
    }
  }
  //endregion
}