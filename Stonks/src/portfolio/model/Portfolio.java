package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

  private final List<AbstractStock> stocks = new ArrayList<>();

  public Portfolio setPortfolioStocks(Map<String, Integer> stocks) {
    for (Map.Entry<String, Integer> stock : stocks.entrySet()) {
      this.stocks.add(new Stock(stock.getKey(), stock.getValue()));
    }

    return this;
  }

  @Override
  public String getPortfolioComposition() {
    return stocks.size() > 0 ? stocks.stream().map(x -> x.tickerSymbol + " -> " + x.stockQuantity + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public String getPortfolioValue(Date date) {
    return "0";
  }

  @Override
  public void savePortfolio(String path) throws IllegalArgumentException {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("portfolio");
      doc.appendChild(rootElement);

      for (AbstractStock stock : this.stocks) {
        Element stockElement = doc.createElement("stock");
        Element stockTickerSymbol = doc.createElement("tickerSymbol");
        stockTickerSymbol.appendChild(doc.createTextNode(stock.tickerSymbol));
        Element stockQuantity = doc.createElement("stockQuantity");
        stockQuantity.appendChild(doc.createTextNode(String.valueOf(stock.stockQuantity)));
        stockElement.appendChild(stockTickerSymbol);
        stockElement.appendChild(stockQuantity);
        rootElement.appendChild(stockElement);
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(path));
      transformer.transform(source, result);

    } catch (Exception e) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException {
    if (this.stocks.size() != 0) return false;
    File inputFile = new File(path);
    if(!inputFile.isFile()) throw new FileNotFoundException();
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
        this.stocks.add(new Stock(tickerSymbol, stockQuantity));
      }
    }

    return true;
  }
}
