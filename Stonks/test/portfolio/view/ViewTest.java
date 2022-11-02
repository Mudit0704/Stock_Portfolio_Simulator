package portfolio.view;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class to test {@link View} class.
 */
public class ViewTest {

  private IView view;
  private StringBuilder viewOutput;

  @Before
  public void setUp() {
    viewOutput = new StringBuilder();
    view = new View(viewOutput);
  }

  @Test
  public void displayEscapeFromOperation() throws IOException {
    view.displayEscapeFromOperation();

    assertEquals("Press E to exit from the operation\n", viewOutput.toString());
  }

  @Test
  public void displayMenu() throws IOException {
    view.displayMenu();

    assertEquals("Choose from the below menu: \n 1 -> Create a new portfolio \n"
        + " 2 -> Get portfolio composition (Stock Symbol -> Stock Quantity)"
        + "\n 3 -> Get a portfolio value at a given date \n 4 -> Save/Retrieve portfolio at a "
        + "given path"
        + "\n E -> Exit from the application \n", viewOutput.toString());
  }

  @Test
  public void displayCustomText() throws IOException {
    view.displayCustomText("TestText");

    assertEquals("TestText", viewOutput.toString());
  }

  @Test
  public void askForInput() throws IOException {
    view.askForInput();

    assertEquals("Your input: ", viewOutput.toString());
  }

  @Test
  public void displayInvalidInput() throws IOException {
    view.displayInvalidInput();

    assertEquals("Invalid input\n", viewOutput.toString());
  }
}