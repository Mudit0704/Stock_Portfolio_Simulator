package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class FlexiblePortfolioImplTest {

  private IStockService mockStockService;
  private IStockService mockExtensive;
  private AbstractPortfolio testAnyDateObj;
  private AbstractPortfolio testModifyPortfolioAnyDate;
  private AbstractPortfolio testInvalidSequence;
  private AbstractPortfolio testPastAvailableDatePortfolio;
  private AbstractPortfolio testCostBasisAfterTransactions;

  @Before
  public void setup() throws IOException, ParserConfigurationException, SAXException {
    mockStockService = new MockStockService("/test/testData.txt");
    mockExtensive = new MockStockService("/test/testExtensiveData.txt");

    testAnyDateObj = new FlexiblePortfolioImpl(new MockStockService("/test/testExtensiveData.txt"), new HashMap<>(), 0);
    testAnyDateObj.retrievePortfolio("test/test_any_date.xml");

    testModifyPortfolioAnyDate = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);
    testModifyPortfolioAnyDate.retrievePortfolio("test/test_multiple_transaction.xml");

    testInvalidSequence = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);

    testPastAvailableDatePortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);
    testPastAvailableDatePortfolio.retrievePortfolio("test/test_past_oldest.xml");

    testCostBasisAfterTransactions = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);
    testCostBasisAfterTransactions.retrievePortfolio("test/test_cost_basis_txn.xml");
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
    sell tests
   */

  @Test
  public void testSellStocksFromPortfolioAnyDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 3L, LocalDate.of(2019,12,12), 10);

    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";
    testModifyPortfolioAnyDate.savePortfolio(path);

    testModifyPortfolioAnyDate.sellStocksFromPortfolio(pubMatic, 2L, LocalDate.of(2020,1,10), 10);

    String result = testModifyPortfolioAnyDate.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 847.53, testModifyPortfolioAnyDate.getPortfolioValue(LocalDate.now()), 0.1);

    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioBeforeCreationDate() throws ParserConfigurationException {
    String path = System.getProperty("user.dir") + "/test/test_multiple_transaction_save.xml";

    AbstractPortfolio retrievedPortfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 10);
    try {
      retrievedPortfolio.retrievePortfolio(path);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new RuntimeException(e);
    }

    IStock pubMatic = new Stock("PUBM", mockExtensive);

    retrievedPortfolio.sellStocksFromPortfolio(pubMatic, 1L, LocalDate.of(2019,10,23), 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioFutureDate() throws ParserConfigurationException {
    IStock pubMatic = new Stock("PUBM", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(pubMatic, 1L, LocalDate.of(2023,10,23), 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioIntermediateDate() {
    IStock msft = new Stock("MSFT", mockExtensive);

    testModifyPortfolioAnyDate.addStocksToPortfolio(msft, 3L, LocalDate.of(2019,10,27), 10);


    try {
      testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,11,28), 10);
    } catch (Exception e) {
      throw e;
    }
    testModifyPortfolioAnyDate.sellStocksFromPortfolio(msft, 1L, LocalDate.of(2019,10,28), 10);

    String result = testModifyPortfolioAnyDate.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertEquals( 848.18, testModifyPortfolioAnyDate.getPortfolioValue(LocalDate.now()), 0.1);
  }

  @Test
  public void testSellStocksFromPortfolioAfterAdd() throws ParserConfigurationException {
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
    map.put(apple, 2L);

    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, map, 10);

    portfolio.sellStocksFromPortfolio(apple, 1L, LocalDate.now(), 10);
    String result = portfolio.getPortfolioComposition();
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("MSFT -> 2\n"));
    assertTrue(result.contains("PUBM -> 1\n"));
    assertTrue(result.contains("AAPL -> 1\n"));

    assertEquals(808.56, portfolio.getPortfolioCostBasisByDate(LocalDate.now()), 0.0);
  }

  /*
   Cost basis tests
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
  public void testGetPortfolioCostBasisByDateAfterAdd()
    throws ParserConfigurationException, IOException, SAXException {
    assertEquals(7342.56, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));

    IStock pubMatic = new Stock("PUBM", mockExtensive);
    testCostBasisAfterTransactions.addStocksToPortfolio(pubMatic, 2L,
        LocalDate.of(2019,11,30), 10);

    assertEquals(9962.48, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));
  }

  @Test
  public void testGetPortfolioCostBasisByDateAfterSell() {
    assertEquals(7342.56, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));

    IStock pubMatic = new Stock("PUBM", mockExtensive);
    testCostBasisAfterTransactions.addStocksToPortfolio(pubMatic, 2L,
      LocalDate.of(2019,11,30), 10);

    assertEquals(9962.48, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));

    testCostBasisAfterTransactions.sellStocksFromPortfolio(pubMatic, 1L,
      LocalDate.of(2019,11,30), 10);

    assertEquals(9972.48, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));
  }

  @Test
  public void testGetPortfolioCostBasisByDateAfterSellAdd() {
    assertEquals(7342.56, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));

    IStock pubMatic = new Stock("PUBM", mockExtensive);
    testCostBasisAfterTransactions.addStocksToPortfolio(pubMatic, 2L,
      LocalDate.of(2019,11,30), 10);

    assertEquals(9962.48, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));

    testCostBasisAfterTransactions.sellStocksFromPortfolio(pubMatic, 1L,
      LocalDate.of(2019,11,30), 10);

    testCostBasisAfterTransactions.addStocksToPortfolio(pubMatic, 2L,
      LocalDate.of(2019,11,30), 10);

    assertEquals(12592.4, testCostBasisAfterTransactions.getPortfolioCostBasisByDate(LocalDate.now()));
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
  public void testGetPortfolioCompositionAtGivenDate() {
    String result1 = testAnyDateObj.getPortfolioCompositionOnADate(LocalDate.of(2019,10,27));
    assertTrue(result1.contains("GOOG -> 2\n"));
    assertTrue(result1.contains("AAPL -> 2\n"));
    assertFalse(result1.contains("A -> 2\n"));


    String result2 = testAnyDateObj.getPortfolioCompositionOnADate(LocalDate.of(2019,10,30));
    assertTrue(result2.contains("GOOG -> 2\n"));
    assertTrue(result2.contains("AAPL -> 2\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionBeforeCreation() {
    String result1 = testAnyDateObj.getPortfolioCompositionOnADate(LocalDate.of(2017,10,23));
    assertTrue(result1.contains("GOOG -> 2\n"));
    assertTrue(result1.contains("AAPL -> 2\n"));
    assertFalse(result1.contains("A -> 2\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRetrievePortfolioIncorrectTransactionSequence()
    throws IOException, ParserConfigurationException, SAXException {
    testInvalidSequence.retrievePortfolio("test/test_invalid_seq.xml");
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

  @Test
  public void testGetPortfolioValuePastOldestAvailableDate() {
    String result = testPastAvailableDatePortfolio.getPortfolioComposition();

    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("A -> 2\n"));
    assertTrue(result.contains("GOOG -> 4\n"));

    assertEquals(0.0, testPastAvailableDatePortfolio.getPortfolioValue(LocalDate.of(2013, 10, 29)), 0.0);
  }

  @Test(expected = RuntimeException.class)
  public void testSaveEmptyPortfolio() throws ParserConfigurationException {
    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0.0);
    portfolio.savePortfolio("test/test_empty.xml");
  }

  @Test(expected = RuntimeException.class)
  public void testRetrieveAlreadyRetrievedPortfolio()
    throws IOException, ParserConfigurationException, SAXException {
    AbstractPortfolio portfolio = new FlexiblePortfolioImpl(mockStockService, new HashMap<>(), 0.0);
    portfolio.retrievePortfolio("test/test_any_date.xml");
    portfolio.retrievePortfolio("test/test_past_oldest.xml");
  }

  @Test
  public void testGetPortfolioValuePastCreationDate() {
    assertEquals(0.0, testModifyPortfolioAnyDate.getPortfolioValue(LocalDate.of(2019,10,23)));
  }

  @Test
  public void testGetPortfolioPerformanceOverDays() {
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    LocalDate startDate = LocalDate.of(2019,10,24);
    LocalDate endDate = LocalDate.of(2019,11,23);
    AbstractPerformanceVisualizer daysPerformanceVisualizer
        = new DaysPerformanceVisualizer(testAnyDateObj);

    daysPerformanceVisualizer.populatePortfolioValues(startDate,
        endDate, 1, dateValue);
    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);
    int scale = AbstractPerformanceVisualizer.getScale(minValue.orElseThrow(),
      maxValue.orElseThrow(), 1);

    StringBuilder expectedString =
        new StringBuilder("Performance of portfolio XXX"
          + " from " + startDate + " to " + endDate + "\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      expectedString.append(mapEntry.getKey().toString()).append(": ");
      AbstractPerformanceVisualizer.populateBar(minValue.orElseThrow(), scale, expectedString,
        mapEntry);
    }
    expectedString.append("Scale: * = $").append(scale).append("\n");

    String actualString = testAnyDateObj.getPortfolioPerformance(startDate, endDate);
    assertEquals(expectedString.toString(), actualString);
  }

  @Test
  public void testGetPortfolioPerformanceOverMultipleDays() {
    LocalDate startDate = LocalDate.of(2019,10,24);
    LocalDate endDate = LocalDate.of(2019,11,30);

    String expectedString = "Performance of portfolio XXX from 2019-10-24 to 2019-11-30\n"
      + "2019-10-31: \n"
      + "2019-11-07: *****\n"
      + "2019-11-14: ******\n"
      + "2019-11-21: *************************************************\n"
      + "2019-11-28: **************************************************\n"
      + "Scale: * = $59\n";


    String actualString = testAnyDateObj.getPortfolioPerformance(startDate, endDate);
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testGetPortfolioPerformanceOverMonths() {
    LocalDate startDate = LocalDate.of(2019,10,24);
    LocalDate endDate = LocalDate.of(2020,4,23);

    String expected = "Performance of portfolio XXX from 2019-10-24 to 2020-04-23\n"
      + "Oct2019: \n"
      + "Nov2019: *************************************\n"
      + "Dec2019: ****************************************\n"
      + "Jan2020: **************************************************\n"
      + "Feb2020: ****************************************\n"
      + "Mar2020: ***********************\n"
      + "Apr2020: *****************************************\n"
      + "Scale: * = $79\n";

    String actual = testAnyDateObj.getPortfolioPerformance(startDate, endDate);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetPortfolioPerformanceOverMultipleMonths() {
    LocalDate startDate = LocalDate.of(2019,10,24);
    LocalDate endDate = LocalDate.of(2022,5,23);

    String expected = "Performance of portfolio XXX from 2019-10-24 to 2022-05-23\n"
      + "Oct2019: \n"
      + "Dec2019: **********\n"
      + "Feb2020: **********\n"
      + "Apr2020: **********\n"
      + "Jun2020: ************\n"
      + "Aug2020: ******************\n"
      + "Oct2020: *****************\n"
      + "Dec2020: ********************\n"
      + "Feb2021: ***************************\n"
      + "Apr2021: *************************************\n"
      + "Jun2021: ***************************************\n"
      + "Aug2021: *************************************************\n"
      + "Oct2021: **************************************************\n"
      + "Dec2021: *************************************************\n"
      + "Feb2022: ********************************************\n"
      + "Apr2022: **********************************\n"
      + "Scale: * = $324\n";

    String actual = testAnyDateObj.getPortfolioPerformance(startDate, endDate);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioPerformanceOverInvalidYear() {
    System.out.println(testAnyDateObj.getPortfolioPerformance(LocalDate.of(2016,10,24),
      LocalDate.of(2020,11,23)));
  }

  @Test
  public void testGetPortfolioPerformanceOverYears()
    throws IOException, ParserConfigurationException, SAXException {
    LocalDate startDate = LocalDate.of(2016,10,24);
    LocalDate endDate = LocalDate.of(2022,5,23);

    AbstractPortfolio testVeryOldPortfolio =
        new FlexiblePortfolioImpl(mockExtensive, new HashMap<>(), 0);
    testVeryOldPortfolio.retrievePortfolio("test/test_very_old.xml");


    String expected = "Performance of portfolio XXX from 2016-10-24 to 2022-05-23\n"
      + "2016: **************\n"
      + "2017: *******************\n"
      + "2018: ******************\n"
      + "2019: ************************\n"
      + "2020: *******************************\n"
      + "2021: **************************************************\n"
      + "2022: \n"
      + "Scale: * = $463\n";

    String actual = testVeryOldPortfolio.getPortfolioPerformance(startDate, endDate);
    assertEquals(expected, actual);
  }
}