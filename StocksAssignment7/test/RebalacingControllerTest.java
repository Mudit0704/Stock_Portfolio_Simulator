import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.StockController;
import controller.StockControllerGUIImpl;
import controller.StockControllerImpl;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import model.Model;
import org.junit.Before;
import org.junit.Test;
import view.StockViewImpl;
import view.ViewGUIImpl;

/**
 * JUnit class to test the rebalancing logic inside textbased and gui based controllers.
 */
public class RebalacingControllerTest {

  StringBuilder log;
  Model mockModel;
  PrintStream out;
  ByteArrayOutputStream bytes;


  @Before
  public void setup() {
    log = new StringBuilder();
    bytes = new ByteArrayOutputStream();
    out = new PrintStream(bytes);
    mockModel = new MockRebalancingModel(log, false, true);
  }

  @Test
  public void testTextBasedController_RebalancePortfolio() {
    Reader in = new StringReader("2 8 College 2022-02-03 MSFT 100 No 9 3");
    StockController controller = new StockControllerImpl(mockModel, new StockViewImpl(out), in);
    controller.selectOption();

    assertEquals("Welcome to the Stock Application\n"
        + "-----------------------\n"
        + "1. Create/ Fetch inflexible portfolio\n"
        + "2. Create/ Fetch flexible portfolio (Commission fee : $10 per transaction)\n"
        + "3. Quit\n"
        + "-----------------------\n"
        + "Enter Choice : ----------- FLEXIBLE PORTFOLIO ------------\n"
        + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
        + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
        + "3. Upload existing portfolio file\n"
        + "4. Examine current composition for a portfolio\n"
        + "5. Cost Basis\n"
        + "6. Get total value of the portfolio.\n"
        + "7. Get performance LineChartView.\n"
        + "8. Quit\n"
        + "-----------------------\n"
        + "Enter Choice : Enter the portfolio name whose rebalance you want : "
        + "Enter the date (yyyy-MM-dd) for rebalance: * Portfolio Name : College\n"
        + "* Ticker Symbol: MSFT, Number of Shares: 24.0\n"
        + "* Ticker Symbol: AAPL, Number of Shares: 87.41798054345067\n"
        + "* Ticker Symbol: HD, Number of Shares: 96.07696013987874\n"
        + "Enter Ticker Symbol : Enter the percent you want to invest in MSFT: "
        + "Do you want to view rebalance of other portfolios? (Y/N)"
        + " : ----------- FLEXIBLE PORTFOLIO ------------\n"
        + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
        + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
        + "3. Upload existing portfolio file\n"
        + "4. Examine current composition for a portfolio\n"
        + "5. Cost Basis\n"
        + "6. Get total value of the portfolio.\n"
        + "7. Get performance LineChartView.\n"
        + "8. Quit\n"
        + "-----------------------\n"
        + "Enter Choice : Welcome to the Stock Application\n"
        + "-----------------------\n"
        + "1. Create/ Fetch inflexible portfolio\n"
        + "2. Create/ Fetch flexible portfolio (Commission fee : $10 per transaction)\n"
        + "3. Quit\n"
        + "-----------------------\n"
        + "Enter Choice : Exiting application. Thank You", bytes.toString());
    assertEquals("CollegeC:\\Users\\Mudit Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group\\"
        + "PDP_A47\\StocksAssignment7\\portfoliosFlexible.xml100.0MSFT100.0CollegeC:\\Users\\"
        + "Mudit Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group\\PDP_A47\\StocksAssignment7\\"
        + "portfoliosFlexible.xml2022-02-03Flexible", log.toString());
  }

  @Test
  public void testTextBasedController_RebalancePortfolioStocksNotFound() {
    Reader in = new StringReader("2 8 NameNotFound 2022-02-03 9 3");
    StockController controller = new StockControllerImpl(mockModel, new StockViewImpl(out), in);
    controller.selectOption();

    assertTrue(bytes.toString()
        .contains("!!! ERROR : You're trying to get composition before purchasing a stock"));
    assertEquals("NameNotFoundC:\\Users\\Mudit Maheshwari\\Desktop\\NEU_StudyMat\\"
        + "PDP_Group\\PDP_A47\\StocksAssignment7\\portfoliosFlexible.xml", log.toString());
  }

  @Test
  public void testTextBasedController_RebalancePortfolioInvalidPercentage() {
    Reader in = new StringReader("2 8 College 2022-02-03 MSFT 110 No 9 3");
    StockController controller = new StockControllerImpl(mockModel, new StockViewImpl(out), in);
    controller.selectOption();

    assertTrue(bytes.toString().contains("!!! Sum of percentages exceed 100. Please enter last "
        + "details again."));
    assertEquals("CollegeC:\\Users\\Mudit Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group"
        + "\\PDP_A47\\StocksAssignment7\\portfoliosFlexible.xml110.0CollegeC:\\Users\\Mudit "
        + "Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group\\PDP_A47\\StocksAssignment7\\portfolios"
        + "Flexible.xml2022-02-03Flexible", log.toString());
  }

  @Test
  public void testGUIBasedController_RebalancePortfolio() {
    new StockControllerGUIImpl(mockModel, new ViewGUIImpl("test")).rebalancePortfolio("College",
        "2022-02-03");

    assertEquals("CollegeC:\\Users\\Mudit Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group"
        + "\\PDP_A47\\StocksAssignment7\\portfoliosFlexible.xml2022-02-03Flexible", log.toString());
  }

  @Test
  public void testGUIBasedController_addStockWeightsForRebalance() {
    mockModel = new MockRebalancingModel(log, false, false);
    new StockControllerGUIImpl(mockModel,
        new MockRebalancingView("test", out)).addStockWeightsForRebalance(
        "MSFT", "100");

    assertEquals("100.0MSFT100.0", log.toString());
    assertEquals("All stockes added", bytes.toString());
  }

  @Test
  public void testGUIBasedController_addStockWeightsForRebalance_PercentageMoreThan100() {
    mockModel = new MockRebalancingModel(log, true, false);
    new StockControllerGUIImpl(mockModel,
        new MockRebalancingView("test", out)).addStockWeightsForRebalance(
        "MSFT", "110");

    assertEquals("110.0", log.toString());
    assertEquals("Error", bytes.toString());
  }

  @Test
  public void testGUIBasedController_addStockWeightsForRebalance_PercentageEquals100() {
    mockModel = new MockRebalancingModel(log, false, true);
    new StockControllerGUIImpl(mockModel,
        new MockRebalancingView("test", out)).addStockWeightsForRebalance(
        "MSFT", "100");

    assertEquals("100.0MSFT100.0", log.toString());
    assertEquals("Reached 100", bytes.toString());
  }

  @Test
  public void testGUIBasedController_getPortfolioStocks() {
    List<String> result = new StockControllerGUIImpl(mockModel,
        new MockRebalancingView("test", out)).getPortfolioStocks(
        "MSFT", "2022-11-15");

    assertEquals("MSFTC:\\Users\\Mudit Maheshwari\\Desktop\\NEU_StudyMat\\PDP_Group"
        + "\\PDP_A47\\StocksAssignment7\\portfoliosFlexible.xml2022-11-15", log.toString());
    assertTrue(result.contains("MSFT"));
  }
}
