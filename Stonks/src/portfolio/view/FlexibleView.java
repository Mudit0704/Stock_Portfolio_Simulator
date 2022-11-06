package portfolio.view;

import java.io.IOException;

public class FlexibleView extends View implements IView {

  private final Appendable out;

  /**
   * Constructs an object of View and initializes its members.
   *
   * @param out an {@link Appendable} to display output in the application.
   */
  public FlexibleView(Appendable out) {
    super(out);
    this.out = out;
  }

  @Override
  public void displayMenu() throws IOException {
    super.displayMenu();
    this.out.append(" 5 -> Purchase a new stock for an existing portfolio \n"
        + " 6 -> Sell a stock from an existing portfolio \n"
        + " 7 -> Determine the cost basis of an existing portfolio at a given date \n"
        + " 8 -> Display portfolio performance \n");
  }
}
