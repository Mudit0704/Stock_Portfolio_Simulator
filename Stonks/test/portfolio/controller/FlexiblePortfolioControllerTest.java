package portfolio.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.MockFlexiblePortfolioModel;
import portfolio.model.MockModel;
import portfolio.view.IView;
import portfolio.view.MockView;

/**
 * A JUnit test class for {@link FlexiblePortfolioController} class.
 */
public class FlexiblePortfolioControllerTest {

  private StringBuffer out;
  private IView mockView;
  private StringBuilder log;

  private IFlexiblePortfolioController controller;

  @Before
  public void setup() {
    out = new StringBuffer();
    mockView = new MockView(out);
    log = new StringBuilder();
  }

  @Test
  public void testRunInvalidInputs() throws IOException {
    Reader in = new StringReader("+\n +\n +\n E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testFlexibleOperationInvalidInputs() throws IOException {
    Reader in = new StringReader("2\n +\n +\n E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Invalid_Input Menu Ask_For_Input Invalid_Input Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenExitOperationInvalidInput()
      throws IOException {
    Reader in = new StringReader("2\n 8 8 \n D E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGE8.0", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Please enter the fee value: \n"
        + "Ask_For_Input Fee updated.\n"
        + "Escape Invalid_Input Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption1AddingOneStock() throws IOException {
    Reader in = new StringReader("2 1 2019-10-25 1\nTICKER_SYMBOL\n 12 E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGETICKER_SYMBOL {TICKER_SYMBOL=12} 2019-10-25", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1AddingOneStockWithInvalidInput() throws IOException {
    Reader in = new StringReader("2 1 2019-10-25 3\n 1\nTICKER_SYMBOL\n 12 E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGETICKER_SYMBOL {TICKER_SYMBOL=12} 2019-10-25", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption2WithValidValues() throws IOException {
    Reader in = new StringReader("2 2\n 1\n 2019-10-25\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios 1 ", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): Composition Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption2WhenNoPortfolios() throws IOException {
    Reader in = new StringReader("2 2\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, false));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "No portfolios\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5ExitWithoutPerformingTransaction() throws IOException {
    Reader in = new StringReader("2 5\n 1\nTICKER_SYMBOL\n 2 2019-10-25 E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios TICKER_SYMBOL ", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Purchase a new stock \n"
        + " 2 -> Sell a stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5InvalidInput()
      throws IOException {
    Reader in = new StringReader(
        "2 5\n 1\nTICKER_SYMBOL\n 2 2019-10-25 Invalid_Input\n\n 1\nTICKER_SYMBOL\n 2 2019-10-25 E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals(
        "ALPHAVANTAGEAvailable_Portfolios TICKER_SYMBOL Available_Portfolios TICKER_SYMBOL ",
        log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Purchase a new stock \n"
        + " 2 -> Sell a stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Invalid_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Purchase a new stock \n"
        + " 2 -> Sell a stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5BuyingOneStock() throws IOException {
    Reader in = new StringReader("2 5\n 1\nTICKER_SYMBOL\n 2 2019-10-25 1\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios TICKER_SYMBOL TICKER_SYMBOL_2_1_2019-10-25",
        log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Purchase a new stock \n"
        + " 2 -> Sell a stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Purchased\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5SellingOneStock() throws IOException {
    Reader in = new StringReader("2 5\n 1\nTICKER_SYMBOL\n 2 2019-10-25 2\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios TICKER_SYMBOL TICKER_SYMBOL_2_1_2019-10-25",
        log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Choose from the below menu: \n"
        + " 1 -> Purchase a new stock \n"
        + " 2 -> Sell a stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Sold\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5InvalidDate() throws IOException {
    Reader in = new StringReader(
        "2 5\n 1\nTICKER_SYMBOL\n 2 3019-10-25\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios TICKER_SYMBOL ", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide stock details for the transaction: \n"
        + "Stock Symbol: Stock Quantity: Please enter date for the transaction: \n"
        + "Please enter the date (yyyy-mm-dd): Invalid date for transaction.\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption5WhenNoPortfolioPresent()
      throws IOException {
    Reader in = new StringReader("2 5\n 1\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, false));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "No portfolios\n"
        + "Escape Invalid_Input Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption6WhenNoPortfolioPresent()
      throws IOException {
    Reader in = new StringReader("2 6\n 1\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, false));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "No portfolios\n"
        + "Escape Invalid_Input Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption6GetCostBasis() throws IOException {
    Reader in = new StringReader("2 6\n 1\n 2019-10-25\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios 2019-10-25_1", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): 1.0Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption7WhenNoPortfolioPresent()
      throws IOException {
    Reader in = new StringReader("2 7\n 1\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, false));

    assertEquals("ALPHAVANTAGE", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "No portfolios\n"
        + "Escape Invalid_Input Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption7GetPortfolioPerformance()
      throws IOException {
    Reader in = new StringReader("2 7\n 1\n 2019-10-25 2019-11-23\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGEAvailable_Portfolios 1_2019-10-25_2019-11-23", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please provide start date:\n"
        + "Please enter the date (yyyy-mm-dd): Please provide end date:\n"
        + "Please enter the date (yyyy-mm-dd): PerformanceEscape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption8InvalidInput()
      throws IOException {
    Reader in = new StringReader("2 8\n Invalid_Input\n 2.0\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGE2.0", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Please enter the fee value: \n"
        + "Ask_For_Input Invalid_Input Please enter the fee value: \n"
        + "Ask_For_Input Fee updated.\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption8SetCommissionFee()
      throws IOException {
    Reader in = new StringReader("2 8\n 2.0\n E E E");
    controller = new FlexiblePortfolioController(in, mockView);

    controller.run(new MockFlexiblePortfolioModel(log, true));

    assertEquals("ALPHAVANTAGE2.0", log.toString());
    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input Menu Ask_For_Input Please enter the fee value: \n"
        + "Ask_For_Input Fee updated.\n"
        + "Escape Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Create a static portfolio \n"
        + " 2 -> Create a flexible portfolio \n"
        + " E -> Exit from the application \n"
        + "Ask_For_Input ", out.toString());
  }
}
