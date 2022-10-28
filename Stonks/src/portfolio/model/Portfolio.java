package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Portfolio implements IPortfolio {

  private static class Pair<S,T> {
    S s ;
    T t ;
    public Pair(S s, T t) {
      this.s = s ;
      this.t = t ;
    }
  }

  private final List<Pair<IStock, Integer>> stocks = new ArrayList<>();
  private final IStockService stockService;

  public Portfolio(IStockService stockService) {
    this.stockService = stockService;
  }

  @Override
  public void setPortfolioStocks(Map<IStock, Integer> stocks) {
    for (Map.Entry<IStock, Integer> entry:stocks.entrySet()) {
      this.stocks.add(new Pair<>(entry.getKey(), entry.getValue()));
    }
  }

  @Override
  public String getPortfolioComposition() {
    return stocks.size() > 0 ? stocks.stream().map(x -> x.s.getStockTicker() + " -> " + x.t + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public double getPortfolioValue(LocalDate date) {
    double portfolioValue = 0;

    for (Pair<IStock, Integer> stock: this.stocks) {
      portfolioValue += stock.s.getValue(date) * stock.t;
    }
    return portfolioValue;
  }

  @Override
  public boolean savePortfolio(String path) throws IllegalArgumentException {
    if (stocks.size() == 0) {
      return false;
    }
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("portfolio");
      doc.appendChild(rootElement);

      for (Pair<IStock, Integer> stock: this.stocks) {
        Element stockElement = doc.createElement("stock");

        Element stockTickerSymbol = doc.createElement("tickerSymbol");
        stockTickerSymbol.appendChild(doc.createTextNode(stock.s.getStockTicker()));

        Element stockQuantity = doc.createElement("stockQuantity");
        stockQuantity.appendChild(doc.createTextNode(String.valueOf(stock.t)));

        Element stockPrice = doc.createElement("stockPrice");
        stockPrice.appendChild(doc.createTextNode(String.valueOf(stock.s.getValue(LocalDate.now()))));

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

    } catch (Exception e) {
      throw new IllegalArgumentException("File path not found.");
    }
    return true;
  }

  @Override
  public boolean retrievePortfolio(String path)
      throws IOException, SAXException, ParserConfigurationException {
    if (this.stocks.size() != 0) return false;
    File inputFile = new File(path);
    if(!inputFile.isFile()) throw new FileNotFoundException("Cannot find file: " + path);
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
        int stockQuantity = Integer.parseInt(eElement.getElementsByTagName("stockQuantity")
            .item(0).getTextContent());
        this.stocks.add(new Pair<>(new Stock(tickerSymbol, new StockService()), stockQuantity));
      }
    }

    return true;
  }
}