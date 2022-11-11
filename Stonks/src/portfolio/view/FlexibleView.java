package portfolio.view;

import java.io.IOException;

/**
 * Responsible for displaying output of the flexible portfolio application, implements
 * {@link IView} and extends {@link View}.
 */
public class FlexibleView extends View implements IView {

  private final Appendable out;

  /**
   * Constructs an object of Flexible View and initializes its members.
   *
   * @param out an {@link Appendable} to display output in the application.
   */
  public FlexibleView(Appendable out) {
    super(out);
    this.out = out;
  }

  @Override
  public void displayMenu() throws IOException {
    this.out.append("Choose from the below menu: \n 1 -> Create a new portfolio \n"
        + " 2 -> Get portfolio composition at a given date (Stock Symbol -> Stock Quantity) \n"
        + " 3 -> Get a portfolio value at a given date \n 4 -> Save/Retrieve portfolio at a "
        + "given path\n"
        + " 5 -> Perform a transaction on an existing portfolio \n"
        + " 6 -> Determine the cost basis of an existing portfolio at a given date \n"
        + " 7 -> Display portfolio performance\n"
        + " 8 -> Set the commission fee\n E -> Exit from the application \n");
  }
}
