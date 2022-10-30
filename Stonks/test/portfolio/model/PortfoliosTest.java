package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;

public class PortfoliosTest {
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

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

    assertEquals(568.92, portfolios.getPortfolioValue(LocalDate.of(2022,10,28), 1),0.0);
    assertEquals(284.46, portfolios.getPortfolioValue(LocalDate.of(2022,10,28), 2),0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueForEmptyPortfolio() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();

    portfolios.createNewPortfolio(map);

    assertEquals("Invalid portfolioId\n",
      portfolios.getPortfolioValue(LocalDate.of(2022,10,28), 1));
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

    assertEquals("Invalid portfolioId\n", portfolios.getPortfolioValue(LocalDate.of(2022,10,28), 3));
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
        portfolios.getPortfolioValue(LocalDate.of(2023,10,28), 2));
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

  @Test
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
  }

  @Test
  public void testRetrievePortfolio() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));
    try {
      portfolios.retrievePortfolios();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
    System.out.println(portfolios.getPortfolioComposition(1));
  }

  @Test
  public void testRetrievePortfolioInvalidPath() {

  }

  @Test
  public void testRetrievePortfolioParsingError() {

  }

  @Test
  public void testRetrievePortfolioFileNotFoundError() {

  }

  @Test
  public void testSetStocksInPortfolio() {

  }

  @Test
  public void testSetStocksInPortfolioZeroStocks() {

  }
}