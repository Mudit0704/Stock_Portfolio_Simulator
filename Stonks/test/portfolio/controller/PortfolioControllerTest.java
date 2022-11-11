package portfolio.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.MockModel;
import portfolio.view.IView;
import portfolio.view.MockView;

/**
 * A JUnit test class to test the Portfolio class.
 */
public class PortfolioControllerTest {

  private StringBuffer out;
  private IView mockView;
  private StringBuilder log;

  @Before
  public void setup() {
    out = new StringBuffer();
    mockView = new MockView(out);
    log = new StringBuilder();
  }

  @Test
  public void testRunInvalidInputs() throws IOException {
    Reader in = new StringReader("+\n +\n +\n E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Invalid_Input Menu Ask_For_Input "
        + "Invalid_Input Menu Ask_For_"
        + "Input Invalid_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOptionExitOperationInvalidInput()
      throws IOException {
    Reader in = new StringReader("2\n 2 D E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios 2 ", log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
        + "(eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Composition Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption1ExitWithoutAddingStocks() throws IOException {
    Reader in = new StringReader("1 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidInput()
      throws IOException {
    Reader in = new StringReader("1 INVALID\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1AddingOneStock() throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1AddingMultipleStock() throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n 12 1\n TICKER_SYMBOL2\n 5 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL  TICKER_SYMBOL2 { TICKER_SYMBOL2=5, TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidTickerSymbol() throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL33\n TICKER_SYMBOL2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL33  TICKER_SYMBOL2 { TICKER_SYMBOL2=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Invalid Ticker Symbol\n"
        + "Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidTickerSymbolMultipleTimes()
      throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL33\n TICKER_SYMBOL3\n "
        + "TICKER_SYMBOL2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL33  TICKER_SYMBOL3  TICKER_SYMBOL2 { TICKER_SYMBOL2=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Invalid Ticker Symbol\n"
        + "Stock Symbol: Invalid Ticker Symbol\n"
        + "Stock Symbol: Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidStockQuantity() throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n quantity\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: "
        + "Choose from the below menu: \n 1 -> Add a new stock \n"
        + " E -> Exit from the operation \nAsk_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1ZeroNegativeStockQuantity()
      throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n 0\n -1\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: "
        + "Invalid_Input Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidStockQuantityMultipleTimes()
      throws IOException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n quantity\n quantity2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: Invalid_Input "
        + "Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption2()
      throws IOException {
    Reader in = new StringReader("2\n 2 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios 2 ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
            + "(eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Composition Escape \n"
            + "----Exiting----\n",
        out.toString());
  }

  @Test
  public void testRunWhenOption2InvalidPortfolioId()
      throws IOException {
    Reader in = new StringReader("2\n Id\n 2 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios Available_Portfolios 2 ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios"
            + " (eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available "
            + "portfolios (eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Composition Escape \n"
            + "----Exiting----\n",
        out.toString());
  }

  @Test
  public void testRunWhenOption2InvalidPortfolioIdNegativeZero()
      throws IOException {
    Reader in = new StringReader("2\n -1\n 0\n 2 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios -1 Available_Portfolios 0 Available_Portfolios 2 ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
            + "(eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available"
            + " portfolios (eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available"
            + " portfolios (eg: Portfolio1 -> give 1):\n"
            + "Available_Portfolios Ask_For_Input Composition Escape \n"
            + "----Exiting----\n",
        out.toString());
  }

  @Test
  public void testRunWhenOption2NoDataFound()
      throws IOException {
    Reader in = new StringReader("2\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, false));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
        + "(eg: Portfolio1 -> give 1):\nNo portfolios\nEscape Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption3()
      throws IOException {
    Reader in = new StringReader("3\n 2 2022-10-25 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios 2022-10-25 2",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
        + "(eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): Portfolio2\n"
        + "2.00\n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption3InvalidPortfolioId()
      throws IOException {
    Reader in = new StringReader("3\n Id\n 2 2022-10-25 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios Available_Portfolios 2022-10-25 2",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios"
        + " (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available "
        + "portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): Portfolio2\n"
        + "2.00\n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption3InvalidPortfolioIdNegativeZero()
      throws IOException {
    Reader in = new StringReader("3\n -1\n 0\n 2 2022-10-25 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios Available_Portfolios Available_Portfolios 2022-10-25 2",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
        + "(eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available "
        + "portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available "
        + "portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): Portfolio2\n"
        + "2.00\n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption3InvalidDate()
      throws IOException {
    Reader in = new StringReader("3\n 2\n 2022A10-25\n 2022-10-25\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("Available_Portfolios 2022-10-25 2",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios (eg: Portfolio1 -> "
        + "give 1):\n"
        + "Available_Portfolios Ask_For_Input Please enter the date (yyyy-mm-dd): Invalid_Input "
        + "Please enter the date (yyyy-mm-dd): Portfolio2\n"
        + "2.00\nEscape Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption3NoDataPresent()
      throws IOException {
    Reader in = new StringReader("3\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, false));

    assertEquals("",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from available portfolios "
            + "(eg: Portfolio1 -> give 1):\n"
            + "No portfolios\n"
            + "Escape Menu Ask_For_Input ",
        out.toString());
  }

  @Test
  public void testRunWhenOption4ExitWithoutOperation()
      throws IOException {
    Reader in = new StringReader("4\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
            + " 1 -> Save portfolio \n 2 -> Retrieve portfolio \n"
            + " E -> Exit from the operation \nAsk_For_Input Menu Ask_For_Input ",
        out.toString());
  }

  @Test
  public void testRunWhenOption4InvalidInput()
      throws IOException {
    Reader in = new StringReader("4\n Invalid\n E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
            + " 1 -> Save portfolio \n 2 -> Retrieve portfolio \n"
            + " E -> Exit from the operation \n"
            + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
            + " 1 -> Save portfolio \n 2 -> Retrieve portfolio \n"
            + " E -> Exit from the operation \nAsk_For_Input Menu Ask_For_Input ",
        out.toString());
  }

  @Test
  public void testRunWhenOption4SaveNoDataPresent()
      throws IOException {
    Reader in = new StringReader("4\n 1 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, false));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Save portfolio \n"
        + " 2 -> Retrieve portfolio \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input No portfolios to save\n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption4RetrieveDataPresent()
      throws IOException {
    Reader in = new StringReader("4\n 2 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Save portfolio \n"
        + " 2 -> Retrieve portfolio \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Portfolios already populated\n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }

  @Test
  public void testRunWhenOption4Save()
      throws IOException {
    Reader in = new StringReader("4\n 1 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, true));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
            + " 1 -> Save portfolio \n"
            + " 2 -> Retrieve portfolio \n"
            + " E -> Exit from the operation \n"
            + "Ask_For_Input Saved\n"
            + "Escape Encountered a problem while saving or retrieving \n"
            + "Escape \n"
            + "----Exiting----\n",
        out.toString());
  }

  @Test
  public void testRunWhenOption4Retrieve()
      throws IOException {
    Reader in = new StringReader("4\n 2 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log, false));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Save portfolio \n"
        + " 2 -> Retrieve portfolio \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Retrieved\n"
        + "Escape Encountered a problem while saving or retrieving \n"
        + "Escape \n"
        + "----Exiting----\n", out.toString());
  }
}