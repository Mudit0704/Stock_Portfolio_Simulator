package portfolio.controller;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import portfolio.model.MockModel;
import portfolio.view.IView;
import portfolio.view.MockView;

public class PortfolioControllerTest {

  private final static String CREATE_PORTFOLIO_SUB_MENU =
      "Choose from the below menu: \n 1 -> Add a new stock "
          + "\n E -> Exit from the operation \n";
  private final static String SAVE_RETRIEVE_PORTFOLIO_MENU =
      "Choose from the below menu: \n 1 -> Save portfolio "
          + "\n 2 -> Retrieve portfolio \n E -> Exit from the operation \n";
  private static final String CHOOSE_FROM_AVAILABLE_PORTFOLIOS = "Choose from available portfolios "
      + "(eg: Portfolio1 -> give 1):\n";

  @Test
  public void testRunInvalidInputs() throws Exception {
    StringBuffer out = new StringBuffer();
    IView mockView = new MockView(out);
    Reader in = new StringReader("+ + + E");
    IPortfolioController controller = new PortfolioController(in, mockView);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("", log.toString());
    assertEquals("MenuAsk For InputInvalid InputMenuAsk For InputInvalid InputMenuAsk For "
            + "InputInvalid InputMenuAsk For Input", out.toString());
  }

  @Test
  public void testRunInvalidInputsd() throws Exception {
    StringBuffer out = new StringBuffer();
    IView mockView = new MockView(out);
    Reader in = new StringReader("+ + + E");
    IPortfolioController controller = new PortfolioController(in, mockView);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("", log.toString());
    assertEquals("MenuAsk For InputInvalid InputMenuAsk For InputInvalid InputMenuAsk For "
        + "InputInvalid InputMenuAsk For Input", out.toString());
  }

  @Test
  public void testPortfolioControllerConstructor() {
  }
}