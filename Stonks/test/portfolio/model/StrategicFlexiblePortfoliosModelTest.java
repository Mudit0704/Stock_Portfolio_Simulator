package portfolio.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.DollarCostAvgStrategy.DollarCostAvgStrategyBuilder;

public class StrategicFlexiblePortfoliosModelTest {
  IStrategicFlexiblePortfolioModel portfolio;
  private IStockService mockStockService;

  @Before
  public void setup() {
    portfolio = new StrategicFlexiblePortfoliosModel();
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
  }

  @Test
  public void setStrategy() {
  }

  @Test
  public void testCreateNewPortfolioOnADate() throws NoSuchFieldException, IllegalAccessException {
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
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2015, 11, 30)));

    map = new HashMap<>();
    map.put("GOOG", 50d);
    map.put("PUBM", 20d);
    map.put("MSFT", 30d);

    portfolio.investStrategicPortfolio(map, 1);
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.of(2017, 10, 30)));
    portfolio.addStocksToPortfolio("GOOG", 2d, 1, LocalDate.of(2016,10,30));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
  }

  @Test
  public void getPath() {
  }
}