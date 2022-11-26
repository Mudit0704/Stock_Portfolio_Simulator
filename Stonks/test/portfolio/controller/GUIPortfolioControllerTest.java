package portfolio.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UnsupportedLookAndFeelException;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.MockStrategicPortfolioModel;
import portfolio.model.StrategicFlexiblePortfoliosModel;
import portfolio.view.IView;
import portfolio.view.MockGUIView;
import portfolio.view.MockView;

/**
 * A JUnit test class for {@link GUIPortfolioController} class.
 */
public class GUIPortfolioControllerTest {

  private StringBuffer out;
  private IView mockView;
  private StringBuilder log;
  private Features controller;
  private Reader in;

  @Before
  public void setup() throws UnsupportedLookAndFeelException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, IOException {
    out = new StringBuffer();
    mockView = new MockView(out);
    log = new StringBuilder();
    in = new StringReader("2");
    controller = new GUIPortfolioController(new MockStrategicPortfolioModel(log, true),
        in, mockView, new MockGUIView("Stonks"));
  }

  @Test
  public void testRunInvalidInputs() throws IOException, UnsupportedLookAndFeelException,
      ClassNotFoundException, InstantiationException, IllegalAccessException {
    Reader in = new StringReader("+\n +\n +\n E");
    controller = new GUIPortfolioController(new StrategicFlexiblePortfoliosModel(),
        in, mockView, new MockGUIView("Stonks"));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Use text-based UI \n"
        + " 2 -> Use graphical user interface \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Use text-based UI \n"
        + " 2 -> Use graphical user interface \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Use text-based UI \n"
        + " 2 -> Use graphical user interface \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Use text-based UI \n"
        + " 2 -> Use graphical user interface \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Use text-based UI \n"
        + " 2 -> Use graphical user interface \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void createFlexiblePortfolio() {
    Map<String, Double> stocks = new HashMap<>();
    stocks.put("GOOG", 10D);

    assertEquals("Created",
        controller.createFlexiblePortfolio(stocks, String.valueOf(LocalDate.now())));
    assertEquals("ALPHAVANTAGE{GOOG=10.0} 2022-11-26", log.toString());
  }

  @Test
  public void sellPortfolioStocks() {
    assertEquals("Sold", controller.sellPortfolioStocks("GOOG", "1",
        "1", "2019-10-25", "1"));
    assertEquals("ALPHAVANTAGE1.0GOOG_1.0_1_2019-10-25", log.toString());
  }

  @Test
  public void buyPortfolioStocks() {
    assertEquals("Bought", controller.buyPortfolioStocks("GOOG", "1",
        "1", "2019-10-25", "1"));
    assertEquals("ALPHAVANTAGE1.0GOOG_1.0_1_2019-10-25", log.toString());
  }

  @Test
  public void getCostBasis() {
    assertEquals(1.0, controller.getCostBasis("2019-10-25", "1"), 0.0);
    assertEquals("ALPHAVANTAGE2019-10-25_1", log.toString());
  }

  @Test
  public void getPortfolioValue() {
    assertEquals(2.0, controller.getPortfolioValue("2019-10-25", "1"), 0.0);
    assertEquals("ALPHAVANTAGE2019-10-25 1", log.toString());
  }

  @Test
  public void savePortfolio() {
    assertEquals("Saved", controller.savePortfolio());
    assertEquals("ALPHAVANTAGE", log.toString());
  }

  @Test
  public void savePortfolioNoDataPresent() throws UnsupportedLookAndFeelException,
      ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
    in = new StringReader("2");
    controller = new GUIPortfolioController(new MockStrategicPortfolioModel(log, false),
        in, mockView, new MockGUIView("Stonks"));
    assertEquals("No portfolios to save\n", controller.savePortfolio());
    assertEquals("ALPHAVANTAGEALPHAVANTAGE", log.toString());
  }

  @Test
  public void retrievePortfolio() throws UnsupportedLookAndFeelException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, IOException {
    in = new StringReader("2");
    controller = new GUIPortfolioController(new MockStrategicPortfolioModel(log, false),
        in, mockView, new MockGUIView("Stonks"));
    assertEquals("Retrieved", controller.retrievePortfolio());
    assertEquals("ALPHAVANTAGEALPHAVANTAGE", log.toString());
  }

  @Test
  public void retrievePortfolioDataAlreadyPresent() {
    assertEquals("Portfolios already populated\n", controller.retrievePortfolio());
    assertEquals("ALPHAVANTAGE", log.toString());
  }

  @Test
  public void getAvailablePortfolios() {
    assertEquals("Available_Portfolios ", controller.getAvailablePortfolios());
    assertEquals("ALPHAVANTAGEAvailable_Portfolios ", log.toString());
  }

  @Test
  public void fractionalInvestmentOnAGivenDate() {
    Map<String, Double> stockProportions = new HashMap<>();
    stockProportions.put("GOOG", 10D);
    stockProportions.put("MSFT", 90D);

    assertEquals("Invested", controller.performFractionalInvestmentOnAGivenDate
        (stockProportions, "1000", "1", "2019-10-25"));
    assertEquals("ALPHAVANTAGENORMAL2019-10-252019-10-2501000.0{MSFT=90.0, GOOG=10.0}1",
        log.toString());
  }

  @Test
  public void createDollarCostAveragePortfolio() {
    Map<String, Double> stockProportions = new HashMap<>();
    stockProportions.put("GOOG", 10D);
    stockProportions.put("MSFT", 90D);

    assertEquals("Created", controller.createDollarCostAveragePortfolio
        (stockProportions, "1000", "2016-10-25", "2019-10-25", "1"));
    assertEquals("ALPHAVANTAGEDOLLARCOSTAVERAGING2016-10-252019-10-2511000.0"
        + "{MSFT=90.0, GOOG=10.0}2016-10-25", log.toString());
  }

  @Test
  public void isTickerSymbolValid() {
    assertTrue(controller.isTickerSymbolValid("TICKER_SYMBOL"));
    assertEquals("ALPHAVANTAGETICKER_SYMBOL ", log.toString());
  }

  @Test
  public void getPortfolioPerformance() {
    controller.getPortfolioPerformance("2019-10-25", "2022-10-25", "1");
    assertEquals("ALPHAVANTAGE2019-10-252022-10-251", log.toString());
  }
}