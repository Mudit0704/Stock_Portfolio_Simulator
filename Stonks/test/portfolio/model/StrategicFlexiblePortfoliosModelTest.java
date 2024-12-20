package portfolio.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * A JUnit test class for testing the StrategicFlexiblePortfoliosModel class.
 */
public class StrategicFlexiblePortfoliosModelTest {
  IStrategicFlexiblePortfolioModel portfolio;
  private IStockService mockStockService;
  private IStrategicFlexiblePortfolioModel mockSaveModel;
  private IStrategicFlexiblePortfolioModel mockSaveFutureTransaction;

  @Before
  public void setup() {
    portfolio = new StrategicFlexiblePortfoliosModel();
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
    mockSaveModel = new MockForStrategicFlexiblePortfoliosModel();
    mockSaveFutureTransaction = new MockSavePartialTxn();
  }

  @Test
  public void testCreateNewDollarCostAvgPortfolioOnADate()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25), 30, totalAmount);

    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 6.84"));
    assertEquals(10172.08,
        portfolio.getPortfolioValue(LocalDate.of(2015, 11, 30), 1), 0.1);
  }

  @Test
  public void testCostBasisForDollarCostAvg()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    assertEquals(20000.0, portfolio.getCostBasis(LocalDate.of(2016,1,30), 1), 0.0);
    assertEquals(20355.48,
        portfolio.getPortfolioValue(LocalDate.of(2016, 1, 30), 1), 0.1);
  }

  @Test
  public void testDollarCostAvgCreationOnHoliday()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    assertEquals(0.0, portfolio.getPortfolioValue(LocalDate.of(2015,10,25), 1), 0.0);
    assertEquals(0.0, portfolio.getCostBasis(LocalDate.of(2015,10,25), 1), 0.0);
  }

  @Test
  public void testInvestNormallyStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.0\n"));
    assertTrue(result.contains("ALGT -> 5.0\n"));
    assertTrue(result.contains("AMAO -> 3.0\n"));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 7.56"));
    assertTrue(result.contains("AMAO -> 4.54"));
  }

  @Test
  public void testInvestNormallyWithBuyStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 7.56"));
    assertTrue(result.contains("AMAO -> 4.54"));

    portfolio.addStocksToPortfolio("ALGT", 2d, 1, LocalDate.of(2016,10,30));
    portfolio.addStocksToPortfolio("ALGT", 2d, 1, LocalDate.of(2016,9,30));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 9.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 7.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 11.56"));
    assertTrue(result.contains("AMAO -> 4.54"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvestNormallyWithSellStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2016,10,30));
  }

  @Test
  public void testInvestNormallyBeforeSellStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2016,10,30));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 3.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 5.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 5.56"));
    assertTrue(result.contains("AMAO -> 4.54"));
  }

  @Test
  public void testInvestNormallyAfterSellStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2017,10,30));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 5.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 5.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 5.56"));
    assertTrue(result.contains("AMAO -> 4.54"));
  }

  @Test
  public void testPurchaseOnDollarCostAvgPortfolio()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 6.84"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 10, 25));
    assertEquals("Portfolio1\n\n", result);

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 6.84"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.now());
    assertTrue(result.contains("AMAM -> 16.47"));
    assertTrue(result.contains("AMAO -> 24.71"));
    assertTrue(result.contains("ALGT -> 41.19"));

    portfolio.addStocksToPortfolio("ALGT", 2d, 1, LocalDate.of(2015,11,30));
    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015,11,30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 8.84"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.now());
    assertTrue(result.contains("AMAM -> 16.47"));
    assertTrue(result.contains("AMAO -> 24.71"));
    assertTrue(result.contains("ALGT -> 43.19"));
  }

  @Test
  public void testInvestBetweenMultipleSellsStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2017,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2017,10,20));

    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2017,10,30));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    String result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 5.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 20));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("ALGT -> 3.0"));
    assertTrue(result.contains("AMAO -> 3.0"));

    result = portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30));
    assertTrue(result.contains("AMAM -> 3.02"));
    assertTrue(result.contains("ALGT -> 3.56"));
    assertTrue(result.contains("AMAO -> 4.54"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellOnDollarCostAvgPortfolio()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    portfolio.sellStockFromPortfolio("ALGT", 2d, 1, LocalDate.of(2015,11,30));
  }

  @Test
  public void testSaveRetrieve()
      throws NoSuchFieldException, IllegalAccessException,
      ParserConfigurationException, IOException, SAXException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    String result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 6.84"));

    result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.now());
    assertTrue(result.contains("AMAM -> 16.47"));
    assertTrue(result.contains("AMAO -> 24.71"));
    assertTrue(result.contains("ALGT -> 41.19"));

    mockSaveModel.addStocksToPortfolio("ALGT", 2d, 1, LocalDate.of(2015,11,30));
    result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 8.84"));

    result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.now());
    assertTrue(result.contains("AMAM -> 16.47"));
    assertTrue(result.contains("AMAO -> 24.71"));
    assertTrue(result.contains("ALGT -> 43.19"));

    mockSaveModel.savePortfolios();

    IStrategicFlexiblePortfolioModel model = new MockForStrategicFlexiblePortfoliosModel();
    model.retrievePortfolios();

    result = model.getPortfolioCompositionOnADate(1,LocalDate.of(2015,12,29));

    assertTrue(result.contains("AMAM -> 4.05"));
    assertTrue(result.contains("AMAO -> 6.07"));
    assertTrue(result.contains("ALGT -> 12.12"));

    model.addStocksToPortfolio("ALGT", 2d, 1, LocalDate.of(2015,12,30));
    result = model.getPortfolioCompositionOnADate(1,LocalDate.of(2015,12,30));
    assertTrue(result.contains("AMAM -> 4.05"));
    assertTrue(result.contains("AMAO -> 6.07"));
    assertTrue(result.contains("ALGT -> 14.12"));

    result = model.getPortfolioCompositionOnADate(1,LocalDate.now());
    assertTrue(result.contains("AMAM -> 16.47"));
    assertTrue(result.contains("AMAO -> 24.71"));
    assertTrue(result.contains("ALGT -> 45.19"));

    model.savePortfolios();
  }

  @Test
  public void testCreateMultipleNewDollarCostAvgPortfolioOnADate()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);

    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    String result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 2.73"));
    assertTrue(result.contains("AMAO -> 4.10"));
    assertTrue(result.contains("ALGT -> 6.84"));

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
        15,
        totalAmount);

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);
    mockSaveModel.investStrategicPortfolio(map, 1);
    result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30));
    assertTrue(result.contains("AMAM -> 4.11"));
    assertTrue(result.contains("AMAO -> 6.16"));
    assertTrue(result.contains("ALGT -> 10.28"));
  }

  @Test
  public void testDollarCostAvgPortfolioOnAFutureDate()
      throws NoSuchFieldException, IllegalAccessException, ParserConfigurationException,
      IOException, SAXException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveFutureTransaction, mockStockService);

    mockSaveFutureTransaction.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2022,10,25),
        LocalDate.of(2023,10,25),
        30,
        totalAmount);

    mockSaveFutureTransaction.createStrategicPortfolio(map, LocalDate.of(2022,10,25));
    String result = mockSaveFutureTransaction.getPortfolioCompositionOnADate(1, LocalDate.now());
    assertTrue(result.contains("ALGT -> 49.12"));
    assertTrue(result.contains("AMAO -> 29.47"));
    assertTrue(result.contains("AMAM -> 19.64"));
    mockSaveFutureTransaction.savePortfolios();

    IStrategicFlexiblePortfolioModel model = new MockSavePartialTxn();
    model.retrievePortfolios();

    assertEquals(10000.0, model.getCostBasis(LocalDate.now(), 1), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDateRangesForDollarCostAvg()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2016,10,25),
        LocalDate.of(2016,10,25),
        30,
        totalAmount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroTimeFrameForDollarCostAvg()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2016,10,25),
        LocalDate.of(2016,10,25),
        0,
        totalAmount);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimeFrameForDollarCostAvg()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2016,10,25),
        LocalDate.of(2016,11,25),
        40,
        totalAmount);
  }

  @Test
  public void testGetPath() {
    AbstractPortfolioModel model = new StrategicFlexiblePortfoliosModel();
    assertEquals("flexiblePortfolio/", model.getPath());
  }

  @Test
  public void testInvestmentScheduling() throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 5d);
    map.put("AMAM", 2d);
    map.put("AMAO", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);

    portfolio.setStrategy(StrategyType.NORMAL,
        LocalDate.of(2023,10,25),
        null,
        0,
        totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2022, 10, 25));

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    String result = portfolio.getPortfolioComposition(1);

    assertTrue(result.contains("ALGT -> 5.0"));
    assertTrue(result.contains("AMAM -> 2.0"));
    assertTrue(result.contains("AMAO -> 3.0"));
  }

  @Test
  public void testCreateMultipleNewDollarCostAvgPortfolioFutureDates()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2018,10,25),
        LocalDate.of(2023,10,25),
        30,
        totalAmount);

    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2018,10,25));
    String result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2018, 11, 30));
    assertTrue(result.contains("AMAM -> 1.86"));
    assertTrue(result.contains("AMAO -> 2.79"));
    assertTrue(result.contains("ALGT -> 4.66"));

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2018,10,25),
        LocalDate.of(2023,10,25),
        15,
        totalAmount);

    map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);
    mockSaveModel.investStrategicPortfolio(map, 1);
    result = mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2018, 11, 30));
    assertTrue(result.contains("AMAM -> 2.80"));
    assertTrue(result.contains("AMAO -> 4.20"));
    assertTrue(result.contains("ALGT -> 7.01"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInvestmentAmount()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", 20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2018,10,25),
        LocalDate.of(2023,10,25),
        30,
        0.0);

    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2018,10,25));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInvestmentQty()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("ALGT", 50d);
    map.put("AMAM", -20d);
    map.put("AMAO", 30d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2018,10,25),
        LocalDate.of(2023,10,25),
        30,
        0.0);

    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2018,10,25));
  }

  @After
  public void tearDown() {
    MockForStrategicFlexiblePortfoliosModel model = new MockForStrategicFlexiblePortfoliosModel();
    String userDirectory = System.getProperty("user.dir") + "/" + model.getPath();
    File dir = new File(userDirectory);
    File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
    for (File file:files) {
      file.delete();
    }

    MockSavePartialTxn mockSavePartialTxn = new MockSavePartialTxn();
    userDirectory = System.getProperty("user.dir") + "/" + mockSavePartialTxn.getPath();
    dir = new File(userDirectory);
    files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
    for (File file:files) {
      file.delete();
    }
  }
}