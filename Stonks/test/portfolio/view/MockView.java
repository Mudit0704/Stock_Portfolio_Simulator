package portfolio.view;

import java.io.IOException;

/**
 * A Mock view class that display dummy outputs to help facilitate testing the controller.
 */
public class MockView implements IView {

  Appendable out;

  /**
   * Constructor to initialize the MockView class with an Appendable object.
   * @param out the appendable object to be set for testing.
   */
  public MockView(Appendable out) {
    this.out = out;
  }

  @Override
  public void displayEscapeFromOperation() throws IOException {
    this.out.append("Escape ");
  }

  @Override
  public void displayMenu() throws IOException {
    this.out.append("Menu ");
  }

  @Override
  public void displayCustomText(String customText) throws IOException {
    this.out.append(customText);
  }

  @Override
  public void askForInput() throws IOException {
    this.out.append("Ask_For_Input ");
  }

  @Override
  public void displayInvalidInput() throws IOException {
    this.out.append("Invalid_Input ");
  }

  @Override
  public void clearScreen() throws IOException {
    //do nothing
  }
}
