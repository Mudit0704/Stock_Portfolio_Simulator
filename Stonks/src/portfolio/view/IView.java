package portfolio.view;

import java.io.IOException;

/**
 * Represents the view of the application and its set of related operations.
 */
public interface IView {

  /**
   * Displays the message for exiting an operation.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayEscapeFromOperation() throws IOException;

  /**
   * Displays the main menu of the application.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayMenu() throws IOException;

  /**
   * Displays a custom message required for the application.
   *
   * @param customText the message to be displayed.
   * @throws IOException if an I/O error occurs.
   */
  void displayCustomText(String customText) throws IOException;

  /**
   * Displays the message for asking for input.
   *
   * @throws IOException if an I/O error occurs.
   */
  void askForInput() throws IOException;

  /**
   * Displays the informative message when an invalid input is given.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayInvalidInput() throws IOException;
}
