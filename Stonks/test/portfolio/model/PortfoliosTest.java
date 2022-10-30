package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;


public class PortfoliosTest {

  @Test
  public void testGetPortfolioValue() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1);
    map.put("PUBM", 2);

    portfolios.createNewPortfolio(map);

    assertEquals(568.92, portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 1), 0.0);
    assertEquals(284.46, portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 2), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForEmptyPortfolio() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

//    assertEquals(0.0,
    System.out.println(portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForInvalidPortfolioId() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1);
    map.put("PUBM", 2);
    map.put("MSFT", 3);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
      portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForNonEmptyZeroPortfolioId() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1);
    map.put("PUBM", 2);
    map.put("MSFT", 3);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
      portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForInvalidDate() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1);
    map.put("PUBM", 2);
    map.put("MSFT", 3);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
      portfolios.getPortfolioValue(LocalDate.of(2023, 10, 28), 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForNegativeId() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1);
    map.put("PUBM", 2);
    map.put("MSFT", 3);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
      portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), -1));
  }

  @Test
  public void testGetPortfolioComposition() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    String result1 = portfolios.getPortfolioComposition(1);
    assertTrue(result1.contains("Portfolio1\n"));
    assertTrue(result1.contains("GOOG -> 3\n"));
    assertTrue(result1.contains("PUBM -> 2\n"));
    assertTrue(result1.contains("MSFT -> 1\n"));
    assertTrue(result1.contains("MUN -> 12\n"));

    String result2 = portfolios.getPortfolioComposition(2);
    assertTrue(result2.contains("Portfolio2\n"));
    assertTrue(result2.contains("AAPL -> 7\n"));
    assertTrue(result2.contains("OCL -> 9\n"));

    String result3 = portfolios.getPortfolioComposition(3);
    assertTrue(result3.contains("Portfolio3\n"));
    assertTrue(result3.contains("A -> 12\n"));
    assertTrue(result3.contains("IBM -> 7\n"));
    assertTrue(result3.contains("ROCL -> 9\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionZeroId() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    portfolios.getPortfolioComposition(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIdPortfolioComposition() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    assertEquals("Portfolio2\nA -> 12\nIBM -> 7", portfolios.getPortfolioComposition(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidEmptyPortfolioComposition() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    assertEquals("No portfolios\n", portfolios.getPortfolioComposition(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIndexEmptyPortfolioComposition() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    assertEquals("No portfolios\n", portfolios.getPortfolioComposition(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIdPortfolioCompositionNegativeVal() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    assertEquals("Portfolio2\nA -> 12\nIBM -> 7", portfolios.getPortfolioComposition(-1));
  }

  @Test
  public void testIsTickerSymbolValid() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    assertFalse(portfolios.isTickerSymbolValid("ABCDEFGHIJKLMNOGOOGPQRSTUVW"));

    assertTrue(portfolios.isTickerSymbolValid("ROCL"));

    assertFalse(portfolios.isTickerSymbolValid(""));

    String empty = null;
    assertFalse(portfolios.isTickerSymbolValid(empty));
  }

  @Test
  public void testSavePortfolio() {

    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfolios retrievedPortfolios = new MockPortfolios(
      new MockStockService("/test/testData.txt"));

    try {
      assertTrue(retrievedPortfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
    String result1 = retrievedPortfolios.getPortfolioComposition(1);
    assertTrue(result1.contains("Portfolio1\n"));
    assertTrue(result1.contains("GOOG -> 3\n"));
    assertTrue(result1.contains("PUBM -> 2\n"));
    assertTrue(result1.contains("MSFT -> 1\n"));
    assertTrue(result1.contains("MUN -> 12\n"));

    String result2 = retrievedPortfolios.getPortfolioComposition(2);
    assertTrue(result2.contains("Portfolio2\n"));
    assertTrue(result2.contains("AAPL -> 7\n"));
    assertTrue(result2.contains("OCL -> 9\n"));

    String result3 = retrievedPortfolios.getPortfolioComposition(3);
    assertTrue(result3.contains("Portfolio3\n"));
    assertTrue(result3.contains("A -> 12\n"));
    assertTrue(result3.contains("IBM -> 7\n"));
    assertTrue(result3.contains("ROCL -> 9\n"));

    try {
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio1.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio2.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio3.xml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSaveSinglePortfolio() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);
    portfolios.savePortfolios();

    IPortfolios retrievedPortfolios = new MockPortfolios(
      new MockStockService("/test/testData.txt"));

    try {
      assertTrue(retrievedPortfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
    String result1 = retrievedPortfolios.getPortfolioComposition(1);
    assertTrue(result1.contains("Portfolio1\n"));
    assertTrue(result1.contains("GOOG -> 3\n"));
    assertTrue(result1.contains("PUBM -> 2\n"));
    assertTrue(result1.contains("MSFT -> 1\n"));
    assertTrue(result1.contains("MUN -> 12\n"));

    try {
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio1.xml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNoStockPortfolioSaveRetrieve() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.savePortfolios();

    IPortfolios retrievedPortfolios = new MockPortfolios(
      new MockStockService("/test/testData.txt"));

    try {
      assertFalse(retrievedPortfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSaveRetrieveMultiplePortfolio() {

    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfolios retrievedPortfolios = new MockPortfolios(
      new MockStockService("/test/testData.txt"));

    try {
      assertTrue(retrievedPortfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
    String result1 = retrievedPortfolios.getPortfolioComposition(1);
    assertTrue(result1.contains("Portfolio1\n"));
    assertTrue(result1.contains("GOOG -> 3\n"));
    assertTrue(result1.contains("PUBM -> 2\n"));
    assertTrue(result1.contains("MSFT -> 1\n"));
    assertTrue(result1.contains("MUN -> 12\n"));

    String result2 = retrievedPortfolios.getPortfolioComposition(2);
    assertTrue(result2.contains("Portfolio2\n"));
    assertTrue(result2.contains("AAPL -> 7\n"));
    assertTrue(result2.contains("OCL -> 9\n"));

    String result3 = retrievedPortfolios.getPortfolioComposition(3);
    assertTrue(result3.contains("Portfolio3\n"));
    assertTrue(result3.contains("A -> 12\n"));
    assertTrue(result3.contains("IBM -> 7\n"));
    assertTrue(result3.contains("ROCL -> 9\n"));

    try {
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio1.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio2.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio3.xml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSaveMultipleRetrievePortfolios() throws AttributeNotFoundException {

    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfolios retrievedPortfolios = new MockPortfolios(
      new MockStockService("/test/testData.txt"));

    try {
      assertTrue(retrievedPortfolios.retrievePortfolios());
      assertFalse(retrievedPortfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }

    assertEquals("Portfolio1\nPortfolio2\nPortfolio3\n", portfolios.getAvailablePortfolios());
    String result1 = retrievedPortfolios.getPortfolioComposition(1);
    assertTrue(result1.contains("Portfolio1\n"));
    assertTrue(result1.contains("GOOG -> 3\n"));
    assertTrue(result1.contains("PUBM -> 2\n"));
    assertTrue(result1.contains("MSFT -> 1\n"));
    assertTrue(result1.contains("MUN -> 12\n"));

    String result2 = retrievedPortfolios.getPortfolioComposition(2);
    assertTrue(result2.contains("Portfolio2\n"));
    assertTrue(result2.contains("AAPL -> 7\n"));
    assertTrue(result2.contains("OCL -> 9\n"));

    String result3 = retrievedPortfolios.getPortfolioComposition(3);
    assertTrue(result3.contains("Portfolio3\n"));
    assertTrue(result3.contains("A -> 12\n"));
    assertTrue(result3.contains("IBM -> 7\n"));
    assertTrue(result3.contains("ROCL -> 9\n"));

    try {
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio1.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio2.xml"));
      Files.delete(Path.of(System.getProperty("user.dir") + "/portfolio3.xml"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testRetrievePortfolioWithNoFiles() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));
    try {
      assertFalse(portfolios.retrievePortfolios());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
  }

  //test this in portfolio
//  @Test(expected = SAXException.class)
//  public void testRetrievePortfolioParsingError()
//    throws SAXException, IOException, ParserConfigurationException {
//
//    String path = System.getProperty("user.dir") + "/invalid_portfolio.xml";
//    System.out.println(path);
//    try {
//      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//      Document doc = dBuilder.newDocument();
//
//      Element rootElement = doc.createElement("portfolio>");
//      doc.appendChild(rootElement);
//
//        Element stockElement = doc.createElement("st<o/>ck");
//
//        Element stockTickerSymbol = doc.createElement("t>ickerSymbol");
//        stockTickerSymbol.appendChild(doc.createTextNode("ticker"));
//
//        Element stockQuantity = doc.createElement("stockQuantity");
//        stockQuantity.appendChild(doc.createTextNode("23"));
//
//        Element stockPrice = doc.createElement("stockPrice");
//        stockPrice.appendChild(doc.createTextNode("89"));
//
//        stockElement.appendChild(stockTickerSymbol);
//        stockElement.appendChild(stockQuantity);
//        stockElement.appendChild(stockPrice);
//        rootElement.appendChild(stockElement);
//
//      TransformerFactory transformerFactory = TransformerFactory.newInstance();
//      Transformer transformer = transformerFactory.newTransformer();
//      DOMSource source = new DOMSource(doc);
//
//      StreamResult result = new StreamResult(new File(path));
//      transformer.transform(source, result);
//
//    } catch (Exception e) {
//      throw new IllegalArgumentException("File path not found. : " + e.getMessage());
//    }
//
//
//    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));
//    portfolios.retrievePortfolios();
//  }

  @Test(expected = AttributeNotFoundException.class)
  public void testSetStocksInPortfolioZeroStocks() throws AttributeNotFoundException {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));
    Map<String, Integer> map = new HashMap<>();

    portfolios.createNewPortfolio(map);
    portfolios.createNewPortfolio(map);
    portfolios.createNewPortfolio(map);
    assertEquals("No portfolios\n", portfolios.getAvailablePortfolios());
  }

  @Test
  public void testGetAvailablePortfolios() throws AttributeNotFoundException {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));
    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);
    map.put("MUN", 12);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7);
    map.put("OCL", 9);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7);
    map.put("ROCL", 9);
    map.put("A", 12);

    portfolios.createNewPortfolio(map);
    assertEquals("Portfolio1\nPortfolio2\nPortfolio3\n", portfolios.getAvailablePortfolios());
  }
}