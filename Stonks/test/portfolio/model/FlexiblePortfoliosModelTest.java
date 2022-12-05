package portfolio.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.xml.sax.SAXException;

/**
 * JUnit test class for {@link FlexiblePortfoliosModel}.
 */
public class FlexiblePortfoliosModelTest {

  private AbstractPortfolioModel portfolios;
  private AbstractPortfolioModel portfolioNew;

  @Before
  public void setup() throws IllegalAccessException, NoSuchFieldException {
    IStockService mockExtensive = new MockStockService("/test/testExtensiveData.txt");

    portfolios = new FlexiblePortfoliosModel();
    portfolioNew = new FlexiblePortfoliosModel();

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolios, mockExtensive);

    stockService.set(portfolioNew, mockExtensive);
  }

  @Test
  public void testCreatePortfolio() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 3.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));

    map = new HashMap<>();
    map.put("AAPL", 7d);
    map.put("OCL", 9d);

    portfolios.createNewPortfolio(map);

    result = portfolios.getPortfolioComposition(2);

    assertTrue(result.contains("OCL -> 9.0\n"));
    assertTrue(result.contains("AAPL -> 7.0\n"));
  }


  @Test
  public void testCreatePortfolioOnADate() throws ParserConfigurationException {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolioOnADate(map, LocalDate.of(2020,10,10));

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    portfolios.addStocksToPortfolio("GOOG", 1D, 1,
        LocalDate.of(2021,10,9));
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));

    portfolios.sellStockFromPortfolio("MSFT", 1D, 1,
        LocalDate.of(2022,10,9));

    result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 0.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));

    map = new HashMap<>();
    map.put("AAPL", 7d);
    map.put("OCL", 9d);

    portfolios.createNewPortfolio(map);

    result = portfolios.getPortfolioComposition(2);

    assertTrue(result.contains("OCL -> 9.0\n"));
    assertTrue(result.contains("AAPL -> 7.0\n"));
  }

  @Test
  public void testAddStocksToPortfolio() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1d, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidDate() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1d, 1, LocalDate.of(2022, 10, 10));
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test
  public void testSellStockFromPortfolio() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1D, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 2.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockFromPortfolioInvalidDate() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1D, 1, LocalDate.of(2022, 10, 10));
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 2\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidPortfolio() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1d, 2, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(2);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioInvalidPortfolio() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", 1D, 2, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(2);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStocksFromPortfolioInvalidQty() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3D);
    map.put("PUBM", 2D);
    map.put("MSFT", 1D);
    map.put("MUN", 12D);

    portfolios.createNewPortfolio(map);

    portfolios.sellStockFromPortfolio("GOOG", -1.0, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStocksToPortfolioInvalidQty() {

    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", -1.0, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4\n"));
    assertTrue(result.contains("PUBM -> 2\n"));
    assertTrue(result.contains("MSFT -> 1\n"));
    assertTrue(result.contains("MUN -> 12\n"));
  }

  @Test
  public void testGetCostBasis() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1818.75, portfolios.getCostBasis(LocalDate.now(), 1), 0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisInvalidDate() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.of(2022, 10, 10), 1), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisInvalidPortfolio() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.now(), 2), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostBasisNegativePortfolio() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(1735.06, portfolios.getCostBasis(LocalDate.now(), -1), 0.0);
  }

  @Test
  public void testSetTransactionFeeAndGetCostBasis() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3d);
    map.put("PUBM", 2d);
    map.put("MSFT", 1d);
    map.put("MUN", 12d);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    portfolios.addStocksToPortfolio("GOOG", 1d, 1, LocalDate.now());
    portfolios.getPortfolioComposition(1);
    assertEquals(1877.58, portfolios.getPortfolioValue(LocalDate.now(), 1), 0.1);
    assertEquals(1927.57, portfolios.getCostBasis(LocalDate.now(), 1), 0.1);

    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));
  }

  @Test
  public void testSetCommissionFee() {
    Map<String, Double> map = new HashMap<>();
    map.put("GOOG", 3D);

    portfolios.setCommissionFee(10);
    portfolios.createNewPortfolio(map);

    assertEquals(306.46, portfolios.getCostBasis(LocalDate.now(), 1), 0.0);
    portfolios.sellStockFromPortfolio("GOOG", 1D, 1, LocalDate.now());
    assertEquals(316.46, portfolios.getCostBasis(LocalDate.now(), 1), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionNegativeId()
      throws IOException, ParserConfigurationException, SAXException {
    portfolios.getPortfolioComposition(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionInvalidId()
      throws IOException, ParserConfigurationException, SAXException {
    portfolios.getPortfolioComposition(2);
  }

  @Test
  public void testGetPath() {
    assertEquals("flexiblePortfolio/", new FlexiblePortfoliosModel().getPath());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCommissionFee() {
    AbstractPortfolioModel portfolioModel = new FlexiblePortfoliosModel();
    portfolioModel.setCommissionFee(-10);
  }
}