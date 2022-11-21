package portfolio.view;

import java.io.IOException;

public interface ICommonView {

  /**
   * Displays the main menu of the application.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayMenu() throws IOException;

  /**
   * Displays the informative message when an invalid input is given.
   *
   * @throws IOException if an I/O error occurs.
   */
  void displayInvalidInput() throws IOException;

  /**
   * Displays a custom message required for the application.
   *
   * @param customText the message to be displayed.
   * @throws IOException if an I/O error occurs.
   */
  void displayCustomText(String customText) throws IOException;
}
