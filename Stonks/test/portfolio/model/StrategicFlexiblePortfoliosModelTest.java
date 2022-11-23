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
    IStrategy strategy = new DollarCostAvgStrategyBuilder()
      .setStrategyStartDate(LocalDate.of(2015,10,25))
      .setStrategyEndDate(LocalDate.of(2016,10,25))
      .setStrategyTimeFrame(30)
      .setTotalAmount(totalAmount)
      .build();

    portfolio.setStrategy(StrategyType.DOLLARCOSTAVERAGING);
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolio, mockStockService);
    portfolio.createNewPortfolioOnADate(map, LocalDate.of(2015,10,25));
    System.out.println(portfolio.getPortfolioCompositionOnADate(1, LocalDate.now()));
  }

  @Test
  public void investStrategicPortfolio() {
  }

  @Test
  public void createPortfolio() {
  }

  @Test
  public void getPath() {
  }
}