package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.io.FileNotFoundException;
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

/**
 * A JUnit class to test PortfoliosModel class.
 */
public class PortfoliosTest {

  @Test
  public void testGetPortfolioValue() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1L);
    map.put("PUBM", 2L);

    portfolios.createNewPortfolio(map);

    assertEquals(568.92, portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 1), 0.1);
    assertEquals(284.46, portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 2), 0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForEmptyPortfolio() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForInvalidPortfolioId() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1L);
    map.put("PUBM", 2L);
    map.put("MSFT", 3L);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
        portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForNonEmptyZeroPortfolioId() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1L);
    map.put("PUBM", 2L);
    map.put("MSFT", 3L);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
        portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForInvalidDate() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1L);
    map.put("PUBM", 2L);
    map.put("MSFT", 3L);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
        portfolios.getPortfolioValue(LocalDate.of(2023, 10, 28), 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForNegativeId() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("GOOG", 1L);
    map.put("PUBM", 2L);
    map.put("MSFT", 3L);

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
        portfolios.getPortfolioValue(LocalDate.of(2022, 10, 28), -1));
  }

  @Test
  public void testGetPortfolioComposition() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

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
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.getPortfolioComposition(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIdPortfolioComposition() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    assertEquals("Portfolio2\nA -> 12\nIBM -> 7", portfolios.getPortfolioComposition(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidEmptyPortfolioComposition() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    assertEquals("No portfolios\n", portfolios.getPortfolioComposition(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIndexEmptyPortfolioComposition() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    assertEquals("No portfolios\n", portfolios.getPortfolioComposition(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidIdPortfolioCompositionNegativeVal() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    assertEquals("Portfolio2\nA -> 12\nIBM -> 7", portfolios.getPortfolioComposition(-1));
  }

  @Test
  public void testIsTickerSymbolValid() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testStockSymbolValidation.txt"));

    assertFalse(portfolios.isTickerSymbolValid("ABCDEFGHIJKLMNOGOOGPQRSTUVW"));

    assertTrue(portfolios.isTickerSymbolValid("ROCL"));

    assertFalse(portfolios.isTickerSymbolValid(""));

    String empty = null;
    assertFalse(portfolios.isTickerSymbolValid(empty));
  }

  @Test
  public void testSavePortfolio() throws ParserConfigurationException {

    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfoliosModel retrievedPortfolios = new MockPortfoliosModel(
        new MockStockService("/test/testData.txt"));

    try {
      retrievedPortfolios.retrievePortfolios();
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
  public void testSaveSinglePortfolio() throws ParserConfigurationException {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);
    portfolios.savePortfolios();

    IPortfoliosModel retrievedPortfolios = new MockPortfoliosModel(
        new MockStockService("/test/testData.txt"));

    try {
      retrievedPortfolios.retrievePortfolios();
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

  @Test(expected = RuntimeException.class)
  public void testNoStockPortfolioSaveRetrieve() throws ParserConfigurationException {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.savePortfolios();

    IPortfoliosModel retrievedPortfolios = new MockPortfoliosModel(
        new MockStockService("/test/testData.txt"));

    try {
      retrievedPortfolios.retrievePortfolios();
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSaveRetrieveMultiplePortfolio()
      throws ParserConfigurationException, IOException, SAXException {

    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfoliosModel retrievedPortfolios = new MockPortfoliosModel(
        new MockStockService("/test/testData.txt"));

    retrievedPortfolios.retrievePortfolios();

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

  @Test (expected = RuntimeException.class)
  public void testSaveMultipleRetrievePortfolios()
      throws ParserConfigurationException, IOException, SAXException {

    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.savePortfolios();

    IPortfoliosModel retrievedPortfolios = new MockPortfoliosModel(
        new MockStockService("/test/testData.txt"));

    retrievedPortfolios.retrievePortfolios();
    try {
      retrievedPortfolios.retrievePortfolios();
    } catch (RuntimeException e) {
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
      } catch (IOException ex) {
        throw new IOException(ex);
      }

      throw new RuntimeException();
    }
  }

  @Test(expected = FileNotFoundException.class)
  public void testRetrievePortfolioWithNoFiles()
      throws IOException, ParserConfigurationException, SAXException {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));
    portfolios.retrievePortfolios();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetStocksInPortfolioZeroStocks() {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));
    Map<String, Long> map = new HashMap<>();

    portfolios.createNewPortfolio(map);
    portfolios.createNewPortfolio(map);
    portfolios.createNewPortfolio(map);
    assertEquals("No portfolios\n", portfolios.getAvailablePortfolios());
  }

  @Test
  public void testGetAvailablePortfolios() throws AttributeNotFoundException {
    IPortfoliosModel portfolios =
        new MockPortfoliosModel(new MockStockService("/test/testData.txt"));
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    map = new HashMap<>();
    map.put("IBM", 7L);
    map.put("ROCL", 9L);
    map.put("A", 12L);

    portfolios.createNewPortfolio(map);
    assertEquals("Portfolio1\nPortfolio2\nPortfolio3\n", portfolios.getAvailablePortfolios());
  }
}