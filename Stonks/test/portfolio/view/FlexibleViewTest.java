package portfolio.view;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for {@link FlexibleView}.
 */
public class FlexibleViewTest {
  private IView view;
  private StringBuilder viewOutput;

  @Before
  public void setUp() {
    viewOutput = new StringBuilder();
    view = new FlexibleView(viewOutput);
  }

  @Test
  public void displayMenu() throws IOException {
    view.displayMenu();

    assertEquals("Choose from the below menu: \n"
        + " 1 -> Create a new portfolio \n"
        + " 2 -> Get portfolio composition at a given date (Stock Symbol -> Stock Quantity) \n"
        + " 3 -> Get a portfolio value at a given date \n"
        + " 4 -> Save/Retrieve portfolio at a given path\n"
        + " 5 -> Perform a transaction on an existing portfolio \n"
        + " 6 -> Determine the cost basis of an existing portfolio at a given date \n"
        + " 7 -> Display portfolio performance\n"
        + " 8 -> Set the commission fee\n"
        + " E -> Exit from the application \n", viewOutput.toString());
  }
}