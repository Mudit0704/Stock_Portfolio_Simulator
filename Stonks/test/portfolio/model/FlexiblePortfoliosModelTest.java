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
  private AbstractPortfolioModel portfolioMockModel = new MockModelForFlexiPortfolio();
  private AbstractPortfolioModel portfolioNew;

  @Before
  public void setup() throws IllegalAccessException, NoSuchFieldException {
    IStockService mockExtensive = new MockStockService("/test/testExtensiveData.txt");

    portfolios = new FlexiblePortfoliosModel();
    portfolioMockModel = new MockModelForFlexiPortfolio();
    portfolioNew = new FlexiblePortfoliosModel();

    Field stockService = AbstractPortfolioModel.class.getDeclaredField("stockService");

    stockService.set(portfolios, mockExtensive);

    stockService.set(portfolioMockModel, mockExtensive);

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

    assertEquals(1810.76, portfolios.getCostBasis(LocalDate.now(), 1), 0.1);
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
    assertEquals(1869.58, portfolios.getPortfolioValue(LocalDate.now(), 1), 0.1);
    assertEquals(1919.58, portfolios.getCostBasis(LocalDate.now(), 1), 0.1);

    String result = portfolios.getPortfolioComposition(1);
    assertTrue(result.contains("GOOG -> 4.0\n"));
    assertTrue(result.contains("PUBM -> 2.0\n"));
    assertTrue(result.contains("MSFT -> 1.0\n"));
    assertTrue(result.contains("MUN -> 12.0\n"));
  }

  @Test
  public void testGetPortfolioPerformance()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.retrievePortfolios();

    LocalDate startDate = LocalDate.of(2019, 10, 24);
    LocalDate endDate = LocalDate.of(2019, 11, 30);

    String expectedString = "\n"
        + "Performance of Portfolio1 from 2019-10-24 to 2019-11-30\n"
        + "\n"
        + "Visualizing using the period of days\n"
        + "2019-10-24 -> 2019-10-30: \n"
        + "2019-10-31 -> 2019-11-06: ****\n"
        + "2019-11-07 -> 2019-11-13: ****\n"
        + "2019-11-14 -> 2019-11-20: **************************************************\n"
        + "2019-11-21 -> 2019-11-30: **************************************************\n"
        + "\n"
        + "Base: 7,567.74\n"
        + "A line without asterisk means the performance during that timespan was equal"
        + " to the base given above\n"
        + "Scale: * = Base+$58\n";

    assertEquals(expectedString, portfolioMockModel.getPortfolioPerformance(1, startDate, endDate));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioPerformanceInvalidId()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.retrievePortfolios();

    LocalDate startDate = LocalDate.of(2019, 10, 24);
    LocalDate endDate = LocalDate.of(2019, 11, 30);

    String expectedString = "Performance of portfolio XXX from 2019-10-24 to 2019-11-30\n"
        + "2019-10-31: \n"
        + "2019-11-07: *****\n"
        + "2019-11-14: ******\n"
        + "2019-11-21: *************************************************\n"
        + "2019-11-28: **************************************************\n"
        + "Scale: * = $59\n";

    assertEquals(expectedString, portfolioMockModel.getPortfolioPerformance(2, startDate, endDate));
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

  @Test
  public void testGetPortfolioCompositionOnAGivenDate()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.setCommissionFee(10);
    portfolioMockModel.getPortfolioComposition(1);

    String result = portfolioMockModel.getPortfolioCompositionOnADate(1,
        LocalDate.of(2019, 11, 11));

    assertTrue(result.contains("AAPL -> 2.0\n"));
    assertTrue(result.contains("GOOG -> 2.0\n"));
    assertTrue(result.contains("A -> 2.0\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioCompositionOnInvalidGivenDate()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.setCommissionFee(10);
    portfolioMockModel.getPortfolioComposition(1);

    String result = portfolioMockModel.getPortfolioCompositionOnADate(1,
        LocalDate.of(2015, 11, 11));

    assertTrue(result.contains("AAPL -> 2\n"));
    assertTrue(result.contains("GOOG -> 2\n"));
    assertTrue(result.contains("A -> 2\n"));
  }

  @Test
  public void testGetAvailablePortfolios()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.setCommissionFee(10);
    portfolioMockModel.getPortfolioComposition(1);

    String result = portfolioMockModel.getAvailablePortfolios();
    assertTrue(result.equals("Portfolio1(Creation datetime: test_any_date)\n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetAvailablePortfoliosNothing()
      throws IOException, ParserConfigurationException, SAXException {

    portfolioMockModel.setCommissionFee(10);
    portfolioMockModel.getPortfolioComposition(1);

    String result = portfolioMockModel.getAvailablePortfolios();
    assertTrue(result.equals("Portfolio1 -> test_any_date\n"));
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

  @Test(expected = RuntimeException.class)
  public void testMultipleRetrieve()
      throws IOException, ParserConfigurationException, SAXException {
    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.retrievePortfolios();
  }

  @Test(expected = None.class)
  public void testSaveRetrieve()
      throws IOException, ParserConfigurationException, SAXException {
    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.savePortfolios();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCompositionOnADateInvalidPortfolioId()
      throws IOException, ParserConfigurationException, SAXException {
    portfolioMockModel.retrievePortfolios();
    portfolioMockModel.getPortfolioCompositionOnADate(2, LocalDate.now());
  }

  @Test
  public void testGetPath() {
    assertEquals("flexiblePortfolio/", new FlexiblePortfoliosModel().getPath());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveNothing() throws ParserConfigurationException {
    portfolioMockModel.setCommissionFee(10);
    portfolioMockModel.getPortfolioComposition(1);

    portfolioMockModel.savePortfolios();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCommissionFee() {
    AbstractPortfolioModel portfolioModel = new FlexiblePortfoliosModel();
    portfolioModel.setCommissionFee(-10);
  }

  @Test(expected = None.class)
  public void testTransactionCache()
      throws ParserConfigurationException, IOException, SAXException {
    portfolioNew.setCommissionFee(10);
    portfolioNew.retrievePortfolios();
    portfolioNew.addStocksToPortfolio("GGO", 1d, 1, LocalDate.now());
    portfolioNew.getPortfolioCompositionOnADate(1, LocalDate.now());
  }
}