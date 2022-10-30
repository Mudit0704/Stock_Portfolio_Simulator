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
  public void testRunInvalidInputs() throws IOException, InterruptedException {
    Reader in = new StringReader("+ + + E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Invalid_Input Menu Ask_For_Input Invalid_Input Menu Ask_For_"
        + "Input Invalid_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1ExitWithoutAddingStocks() throws IOException, InterruptedException {
    Reader in = new StringReader("1 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

    assertEquals("", log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n"
        + " E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidInput()
      throws IOException, InterruptedException {
    Reader in = new StringReader("1 INVALID E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

    assertEquals("",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Invalid_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1AddingOneStock() throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

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
  public void testRunWhenOption1AddingMultipleStock() throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n 12 1\n TICKER_SYMBOL2\n 5 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

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
  public void testRunWhenOption1InvalidTickerSymbol() throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL33\n TICKER_SYMBOL2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

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
      throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL33\n TICKER_SYMBOL3\n TICKER_SYMBOL2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

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
  public void testRunWhenOption1InvalidStockQuantity() throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n quantity\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: "
        + "Choose from the below menu: \n 1 -> Add a new stock \n"
        + " E -> Exit from the operation \nAsk_For_Input Menu Ask_For_Input ", out.toString());
  }

  @Test
  public void testRunWhenOption1InvalidStockQuantityMultipleTimes()
      throws IOException, InterruptedException {
    Reader in = new StringReader("1 1\nTICKER_SYMBOL\n quantity\n quantity2\n 12 E E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

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
      throws IOException, InterruptedException {
    Reader in = new StringReader("2 E");
    IPortfolioController controller = new PortfolioController(in, mockView);

    controller.run(new MockModel(log));

    assertEquals("TICKER_SYMBOL {TICKER_SYMBOL=12} ",
        log.toString());
    assertEquals("Menu Ask_For_Input Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: Invalid_Input "
        + "Stock Quantity: Choose from the below menu: \n"
        + " 1 -> Add a new stock \n E -> Exit from the operation \n"
        + "Ask_For_Input Menu Ask_For_Input ", out.toString());
  }
}