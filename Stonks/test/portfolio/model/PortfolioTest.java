package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * A JUnit class to test Portfolio class.
 */
public class PortfolioTest {
  private IStockService mockStockService;

  @Before
  public void Setup() {
    mockStockService = new MockStockService("/test/testData.txt");
  }

  @Test
  public void testSetPortfolioStocks() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
  }

  @Test
  public void testSetPortfolioZeroStocks() {
    IPortfolio portfolio = new Portfolio(mockStockService, new HashMap<>());

    String result = portfolio.getPortfolioComposition();
    assertEquals("No stocks in the portfolio", result);
  }

  @Test
  public void getPortfolioComposition() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
  }

  @Test
  public void testGetPortfolioValue() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    assertEquals(568.92, portfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueInvalidDate() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    assertEquals(568.92, portfolio.getPortfolioValue(LocalDate.of(2012, 10, 28)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueInvalidFutureDate() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    assertEquals(568.92, portfolio.getPortfolioValue(LocalDate.of(2023, 10, 28)), 0.0);
  }

  @Test
  public void testGetEmptyPortfolioValue() {
    IPortfolio portfolio = new Portfolio(mockStockService, new HashMap<>());

    assertEquals(0, portfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);
  }

  @Test
  public void testSavePortfolio() throws ParserConfigurationException {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    String path = System.getProperty("user.dir") + "/test_save.xml";
    portfolio.savePortfolio(path);

    IPortfolio retrievedPortfolio = new Portfolio(mockStockService, new HashMap<>());

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertEquals(568.92, portfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = RuntimeException.class)
  public void testSaveEmptyPortfolio() throws IOException, ParserConfigurationException {
    IPortfolio portfolio = new Portfolio(mockStockService, new HashMap<>());

    String path = System.getProperty("user.dir") + "/test_save.xml";
    portfolio.savePortfolio(path);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw e;
    }
  }

  @Test
  public void testSavePortfolioMultipleTimes() throws IOException, ParserConfigurationException {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    String path = System.getProperty("user.dir") + "/test_multiple_save.xml";
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);


    IPortfolio retrievedPortfolio = new Portfolio(mockStockService, new HashMap<>());

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertEquals(568.92, retrievedPortfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IOException.class)
  public void retrievePortfolioInvalidPath()
    throws IOException, ParserConfigurationException, SAXException {
    String path = System.getProperty("user.dir") + "/invalid.xml";

    IPortfolio retrievedPortfolio = new Portfolio(mockStockService, new HashMap<>());

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (Exception e) {
      throw e;
    }

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertEquals(568.92, retrievedPortfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);
  }

  @Test(expected = RuntimeException.class)
  public void testRetrievePortfolioMultipleTimes() throws IOException, ParserConfigurationException {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);
    IPortfolio portfolio = new Portfolio(mockStockService, map);

    String path = System.getProperty("user.dir") + "/test_multiple_save.xml";
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);
    portfolio.savePortfolio(path);


    IPortfolio retrievedPortfolio = new Portfolio(mockStockService, new HashMap<>());

    try {
      retrievedPortfolio.retrievePortfolio(path);
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertEquals(568.92, retrievedPortfolio.getPortfolioValue(LocalDate.of(2022, 10, 28)), 0.0);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}