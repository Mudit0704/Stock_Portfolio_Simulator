package portfolio.model;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class FlexiblePortfoliosModelImplTest {

  private IStockService mockExtensive;
  private AbstractPortfolioModel portfolios;
  @Before
  public void setup() throws IllegalAccessException, NoSuchFieldException {
    mockExtensive = new MockStockService("/test/testExtensiveData.txt");
    portfolios = new FlexiblePortfoliosModelImpl();
    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");
    stockService.setAccessible(true);
    stockService.set(portfolios, mockExtensive);
  }

  @Test
  public void testCreatePortfolio() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 3\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));


    map = new HashMap<>();
    map.put("AAPL", 7L);
    map.put("OCL", 9L);

    portfolios.createNewPortfolio(map);

    result = portfolios.getPortfolioComposition(2);

    assertTrue(result.contains("OCL -> 9\n"));
    assertTrue(result.contains("AAPL -> 7\n"));
  }

  @Test
  public void testAddStocksToPortfolio() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1L, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidDate() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1L, 1, LocalDate.of(2022,10,10));
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test
  public void testSellStockFromPortfolio() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1L, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 2\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockFromPortfolioInvalidDate() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1L, 1, LocalDate.of(2022,10,10));
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 2\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidPortfolio() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1L, 2, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(2);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioInvalidPortfolio() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1L, 2, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(2);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioInvalidQty() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", -1L, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidQty() {

    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", -1L, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test
  public void testGetCostBasis() {
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.now(), 1), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisInvalidDate() {
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.of(2022,10,10), 1), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisInvalidPortfolio() {
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.now(), 2), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisNegativePortfolio() {
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.now(), -1), 0.0);
  }

  @Test
  public void testSetTransactionFeeAndGetCostBasis() {
    Map<String, Long> map = new HashMap<>();
    map.put("GOOG", 3L);
    map.put("PUBM", 2L);
    map.put("MSFT", 1L);
    map.put("MUN", 12L);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1L, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    assertEquals(1789.23, portfolios.getPortfolioValue(LocalDate.now(), 1), 0.0);
    assertEquals(1839.23, portfolios.getCostBasis(LocalDate.now(), 1), 0.0);

    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));

  }

  @Test
  public void testGetPortfolioPerformance() {
  }

  @Test
  public void testSetCommissionFee() {
  }

  @Test
  public void testGetPath() {
  }
}