package portfolio.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import portfolio.model.DollarCostAvgStrategy.DollarCostAvgStrategyBuilder;

public class StrategicFlexiblePortfoliosModelTest {
  IStrategicFlexiblePortfolioModel portfolio;
  private IStockService mockStockService;
  private IStrategicFlexiblePortfolioModel mockSaveModel;

  @Before
  public void setup() {
    portfolio = new StrategicFlexiblePortfoliosModel();
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
    mockSaveModel = new MockForStrategicFlexiblePortfoliosModel();
  }

  @Test
  public void setStrategy() {
  }

  @Test
  public void testCreateNewDollarCostAvgPortfolioOnADate()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    double totalAmount = 5000d;

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
        LocalDate.of(2015,10,25),
        LocalDate.of(2016,10,25),
       30,
        totalAmount);

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
  }

  @Test
  public void testInvestNormallyStrategicPortfolio() throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.setStrategy(StrategyType.NORMAL,
      LocalDate.of(2017,10,25),
      null,
      0,
      totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
//    assertEquals();
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  @Test
  public void testInvestNormallyWithBuyStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

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
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
    portfolio.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2016,10,30));
    portfolio.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2016,9,30));


    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  // sell between buy and invest
  @Test(expected = IllegalArgumentException.class)
  public void testInvestNormallyWithSellStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

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
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2016,10,30));

    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  //invest between buy and sell
  @Test
  public void testInvestNormallyBeforeSellStrategicPortfolio()
      throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.setStrategy(StrategyType.NORMAL,
      LocalDate.of(2017,10,25),
      null,
      0,
      totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2016,10,30));

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  @Test
  public void testInvestNormallyAfterSellStrategicPortfolio()
    throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.setStrategy(StrategyType.NORMAL,
      LocalDate.of(2017,10,25),
      null,
      0,
      totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2017,10,30));

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 29)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  @Test
  public void testPurchaseOnDollarCostAvgPortfolio()
      throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    double totalAmount = 5000d;

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
      LocalDate.of(2015,10,25),
      LocalDate.of(2016,10,25),
      30,
      totalAmount);

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    System.out.println("THEN: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
    portfolio.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2015,11,30));
    System.out.println("THEN: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
  }

  @Test
  public void testInvestBetweenMultipleSellsStrategicPortfolio()
    throws IllegalAccessException, NoSuchFieldException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 5d);
    map.put("PUBM", 2d);
    map.put("MSFT", 3d);

    double totalAmount = 5000d;

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.setStrategy(StrategyType.NORMAL,
      LocalDate.of(2017,10,25),
      null,
      0,
      totalAmount);

    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015, 10, 25));

    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2017,10,20));

    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2017,10,30));

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2016, 10, 30)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 20)));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellOnDollarCostAvgPortfolio()
    throws NoSuchFieldException, IllegalAccessException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    double totalAmount = 5000d;

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
      LocalDate.of(2015,10,25),
      LocalDate.of(2016,10,25),
      30,
      totalAmount);

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    System.out.println("THEN: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
    portfolio.sellStockFromPortfolio("GOOG", 2d, 1, LocalDate.of(2015,11,30));
    System.out.println("THEN: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
  }

  @Test
  public void testSaveRetrieve()
      throws NoSuchFieldException, IllegalAccessException,
      ParserConfigurationException, IOException, SAXException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    double totalAmount = 5000d;

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
      LocalDate.of(2015,10,25),
      LocalDate.of(2016,10,25),
      30,
      totalAmount);

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);
    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    System.out.println("THEN: " + mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.now()));
    mockSaveModel.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2015,11,30));
    System.out.println("THEN: " + mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));
    System.out.println("NOW: " + mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.now()));

    mockSaveModel.savePortfolios();

    IStrategicFlexiblePortfolioModel model = new MockForStrategicFlexiblePortfoliosModel();
    model.retrievePortfolios();

    System.out.println(model.getPortfolioCompositionOnADate(1,LocalDate.of(2015,12,29)));
    model.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2015,12,30));
    System.out.println(model.getPortfolioCompositionOnADate(1,LocalDate.of(2015,12,30)));
    System.out.println("NOW: " + model.getPortfolioCompositionOnADate(1, LocalDate.now()));
  }

  @Test
  public void testCreateMultipleNewDollarCostAvgPortfolioOnADate()
    throws NoSuchFieldException, IllegalAccessException, ParserConfigurationException {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    double totalAmount = 5000d;

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
      LocalDate.of(2015,10,25),
      LocalDate.of(2016,10,25),
      30,
      totalAmount);

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(mockSaveModel, mockStockService);
    mockSaveModel.createStrategicPortfolio(map, LocalDate.of(2015,10,25));
    System.out.println(mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));

    mockSaveModel.setStrategy(StrategyType.DOLLARCOSTAVERAGING,
      LocalDate.of(2015,10,25),
      LocalDate.of(2016,10,25),
      15,
      totalAmount);

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);
    mockSaveModel.investStrategicPortfolio(map, 1);
    System.out.println(mockSaveModel.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));

//    mockSaveModel.savePortfolios();
  }

  @Test
  public void getPath() {

  }
}

//get next available date logic working