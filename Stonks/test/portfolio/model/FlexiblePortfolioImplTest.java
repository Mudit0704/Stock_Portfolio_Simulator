package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class FlexiblePortfolioImplTest {

  private IStockService mockStockService;
  private IStockService mockExtensive;
  private AbstractPortfolio testAnyDateObj;
  private AbstractPortfolio testModifyPortfolioAnyDate;

  @Before
  public void setup() throws IOException, ParserConfigurationException, SAXException {
    mockStockService = new MockStockService("/test/testData.txt");
    mockExtensive = new MockStockService("/test/testExtensiveData.txt");
    testAnyDateObj = new FlexiblePortfolioImpl(new MockStockService("/test/testExtensiveData.txt"), new HashMap<>(), 0);
    testAnyDateObj.retrievePortfolio("test/test_any_date.xml");
    testModifyPortfolioAnyDate = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);
    testModifyPortfolioAnyDate.retrievePortfolio("test/test_multiple_transaction.xml");
  }

  @Test
  public void testGetPortfolioComposition() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
  }

  @Test
  public void testAddStocksToPortfolio() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.addStocksToPortfolio(new Stock("AAPL", mockStockService), 1L, LocalDate.now(), 20);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("AAPL -> 1\n"));
    assertEquals(713.74, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  @Test
  public void testAddStocksToPortfolioAnyDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 1L, LocalDate.of(2019,12,12), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }
    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioBeforeCreationDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 1L, LocalDate.of(2019,10,23), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }
    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioFutureDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 1L, LocalDate.of(2023,10,23), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }
    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioIntermediateDate() throws ParserConfigurationException {
    IStock msft = new Stock("MSFT", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 3L, LocalDate.of(2019,10,27), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }


    try {
      testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,11,28), 10);
    } catch (Exception e) {
      throw e;
    }
    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 1L, LocalDate.of(2019,10,28), 10);

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testAddStocksToPortfolioAfterSell() throws ParserConfigurationException {
    IStock msft = new Stock("MSFT", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 3L, LocalDate.of(2019,10,27), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }


    try {
      testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,11,28), 10);
    } catch (Exception e) {
      throw e;
    }
    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 1L, LocalDate.of(2019,12,01), 10);

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("MSFT -> 3\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 1035.87, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testAddStocksToPortfolioNonExistent() {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    IStock apple = new Stock("AAPL", mockStockService);

    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.addStocksToPortfolio(apple, 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("AAPL -> 1\n"));

    assertEquals(703.74, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  @Test
  public void testSellStocksFromPortfolio() {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.sellStocksFromPortfolio(google, 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 2\n"));
    assertEquals(608.92, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioNonExistingStock() {
    Map<IStock, Long> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 3L);
    map.put(new Stock("PUBM", mockStockService), 1L);
    map.put(new Stock("MSFT", mockStockService), 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.sellStocksFromPortfolio(new Stock("AAPL", mockStockService), 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 2\n"));
    assertEquals(713.74, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioInvalidQty() {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.sellStocksFromPortfolio(google, 4L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 2\n"));
    assertEquals(608.92, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }



  /*

   */

  @Test
  public void testSellStocksFromPortfolioAnyDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 3L, LocalDate.of(2019,12,12), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 2L, LocalDate.of(2020,1,10), 10);

    String result = testModifyPortfolioAnyDate.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 3\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioBeforeCreationDate() throws ParserConfigurationException {
    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);
    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.sellStocksFromPortfolio(pubMatic, 1L, LocalDate.of(2019,10,23), 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioFutureDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 1L, LocalDate.of(2023,10,23), 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioIntermediateDate() throws ParserConfigurationException {
    IStock msft = new Stock("MSFT", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 3L, LocalDate.of(2019,10,27), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }


    try {
      testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,11,28), 10);
    } catch (Exception e) {
      throw e;
    }
    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 1L, LocalDate.of(2019,10,28), 10);

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSellStocksFromPortfolioAfterSell() throws ParserConfigurationException {
    IStock msft = new Stock("MSFT", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 3L, LocalDate.of(2019,10,27), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);

    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }


    try {
      testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,11,28), 10);
    } catch (Exception e) {
      throw e;
    }
    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 1L, LocalDate.of(2019,12,01), 10);

    String result = retrievedPortfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("MSFT -> 3\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 1035.87, retrievedPortfolio.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSellStocksFromPortfolioNonExistent() {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    IStock apple = new Stock("AAPL", mockStockService);

    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.addStocksToPortfolio(apple, 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("AAPL -> 1\n"));

    assertEquals(703.74, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  /*
   *
   */

  @Test
  public void testGetPortfolioCostBasisByDate() {
    assertEquals(746.26,
        testAnyDateObj.getPortfolioCostBasisByDate(LocalDate.of(2019, 10, 30)), 0.0);

    assertEquals(921.06,
        testAnyDateObj.getPortfolioCostBasisByDate(LocalDate.of(2022, 10, 30)), 0.1);

    assertEquals(472.26,
        testAnyDateObj.getPortfolioCostBasisByDate(LocalDate.of(2019, 10, 25)), 0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCostBasisByDateBeforeCreation() {
    assertEquals(746.26,
        testAnyDateObj.getPortfolioCostBasisByDate(LocalDate.of(2019, 10, 23)), 0.0);
  }

  @Test
  public void testGetPortfolioCostBasisByDateFutureDate() {
    assertEquals(921.06,
        testAnyDateObj.getPortfolioCostBasisByDate(LocalDate.of(2023, 10, 30)), 0.1);
  }

  @Test
  public void testGetPortfolioPerformance() {

  }

  @Test
  public void testSavePortfolio() throws ParserConfigurationException {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

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

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testRetrievePortfolio() {

  }

  @Test
  public void testRetrievePortfolioIncorrectTransactionSequence() {

  }

  @Test
  public void testGetPortfolioValue() {
    Map<IStock, Long> map = new HashMap<>();
    IStock google = new Stock("GOOG", mockStockService);
    IStock pubMatic = new Stock("PUBM", mockStockService);
    IStock microsoft = new Stock("MSFT", mockStockService);
    map.put(google, 3L);
    map.put(pubMatic, 1L);
    map.put(microsoft, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.sellStocksFromPortfolio(google, 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 2\n"));
    assertEquals(608.92, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);

    //check this
    assertEquals(474.1, portfolio.getPortfolioValue(LocalDate.now()), 0.1);
  }

  @Test
  public void testGetPortfolioValuePastDate() {
    String result = testAnyDateObj.getPortfolioComposition();

    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertTrue(result.contains("GOOG -> 4\n"));

    assertEquals(5050.48, testAnyDateObj.getPortfolioValue(LocalDate.of(2019, 10, 29)), 0.0);
    assertEquals(7567.74, testAnyDateObj.getPortfolioValue(LocalDate.of(2019, 10, 30)), 0.0);
    assertEquals(10678.96, testAnyDateObj.getPortfolioValue(LocalDate.of(2019, 11, 15)), 0.0);
  }
}