package portfolio.view;

import java.io.IOException;

/**
 * Responsible for displaying output of the application and implements {@link IView}.
 */
public class View implements IView {

  private final Appendable out;

  /**
   * Constructs an object of View and initializes its members.
   *
   * @param out an {@link Appendable} to display output in the application.
   */
  public View(Appendable out) {
    this.out = out;
  }

  //region Public Methods
  @Override
  public void displayEscapeFromOperation() throws IOException {
    this.out.append("Press E to exit from the operation\n");
  }

  @Override
  public void displayMenu() throws IOException {
    this.out.append("Choose from the below menu: \n 1 -> Create a new portfolio \n"
        + " 2 -> Get portfolio composition (Stock Symbol -> Stock Quantity)"
        + "\n 3 -> Get a portfolio value at a given date \n 4 -> Save/Retrieve portfolio at a "
        + "given path \n");
  }

  @Override
  public void displayCustomText(String customText) throws IOException {
    this.out.append(customText);
  }

  @Override
  public void askForInput() throws IOException {
    this.out.append("Your input: ");
  }

  @Override
  public void displayInvalidInput() throws IOException {
    this.out.append("Invalid input\n");
  }

  //endregion
}
