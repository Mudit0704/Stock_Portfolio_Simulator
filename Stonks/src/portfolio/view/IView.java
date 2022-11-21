package portfolio.view;

import java.io.IOException;

/**
 * Represents the view of the application and its set of related operations.
 */
public interface IView extends ICommonView {

  /**
   * Displays the message for exiting an operation.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayEscapeFromOperation() throws IOException;

  /**
   * Displays the message for asking for input.
   *
   * @throws IOException if an I/O error occurs.
   */
  void askForInput() throws IOException;
}
