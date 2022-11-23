package portfolio.model;

import static junit.framework.TestCase.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.DollarCostAvgStrategy.DollarCostAvgStrategyBuilder;

public class StrategicPortfolioTest {
  AbstractPortfolio portfolio;
  private IStockService mockStockService;

  @Before
  public void setup() {
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
  }

  @Test
  public void testStrategicPortfolioCreation() {
//    Map<IStock, Double> map = new HashMap<>();
//    map.put(new Stock("GOOG", mockStockService), 3d);
//    map.put(new Stock("PUBM", mockStockService), 1d);
//    map.put(new Stock("MSFT", mockStockService), 2d);
//
//    IStrategy normalStrategy = new NormalStrategy.NormalStrategyBuilder()
//        .setDate(LocalDate.of(2015,10,25)).build();
//
//    portfolio = new StrategicPortfolio(mockStockService, map, 0.0,
//        LocalDate.of(2015,10,25));
//
//    String result = portfolio.getPortfolioComposition();
//    assertTrue(result.contains("GOOG -> 3.0\n"));
//    assertTrue(result.contains("MSFT -> 2.0\n"));
//    assertTrue(result.contains("PUBM -> 1.0\n"));
  }

  @Test
  public void testInvestStocksIntoStrategicPortfolio() {
    Map<IStock, Double> map = new HashMap<>();
    map.put(new Stock("GOOG", mockStockService), 50d);
    map.put(new Stock("PUBM", mockStockService), 30d);
    map.put(new Stock("MSFT", mockStockService), 20d);

    double totalAmount = 5000d;
    IStrategy strategy = new DollarCostAvgStrategyBuilder()
      .setStrategyStartDate(LocalDate.of(2015,10,25))
      .setStrategyEndDate(LocalDate.of(2016,10,25))
      .setStrategyTimeFrame(30)
      .setTotalAmount(totalAmount)
      .build();

    portfolio = new StrategicPortfolio(mockStockService, map, 0.0, LocalDate.of(2015,10,25));

    portfolio.investStocksIntoStrategicPortfolio(map, LocalDate.of(2015, 10,25), 0.0);
    String result = portfolio.getPortfolioComposition();
    System.out.println(result);
    result = portfolio.getPortfolioCompositionOnADate(LocalDate.of(2015,10,25));
    System.out.println(result);
    result = portfolio.getPortfolioCompositionOnADate(LocalDate.of(2015,11,25));
    System.out.println(result);
    result = portfolio.getPortfolioCompositionOnADate(LocalDate.of(2015,12,25));
    System.out.println(result);

    String path = System.getProperty("user.dir") + "/test/test_strategic.xml";
    try {
      portfolio.savePortfolio(path);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

}