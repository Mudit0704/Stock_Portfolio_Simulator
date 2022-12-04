package controller;

/**
 * This interface is the controller interface for the Stocks application. It has a method
 * that displays the menu from view (Graphical UI or Text-based UI) and gives the user options
 * which is later processed by the model. This is the main entry point to the code.
 * In text based UI user can create both flexible and inflexible portfolios of stocks
 * whereas in graphical UI user can create only flexible portfolio.
 */
public interface StockController {

  /**
   * Calls the methods to perform operations based on the selected choice. Calls the view to
   * display the appropriate output statements.
   */
  void selectOption();

}
