package view;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Features;

/**
 * This is the view GUI interface for the Stocks application. This has various function
 * descriptions to display the menu and values for the user on a Graphical user interface.
 */

public interface ViewGUI {

  /**
   * This method prints the main menu on the GUI.
   */
  void getFlexibleMenu();

  /**
   * This feeature initialises an object of the Features interface to interact with the controller
   * implementation class.
   * @param features object of the Features interface.
   */
  void addFeatures(Features features);

  /**
   * This method prints the options for the flexible portfolio creation menu on the GUI.
   */
  void createOptions();

  /**
   * This method displays the frame where the user can enter the new portfolio details.
   * @param tickerSymbolList List of valid ticker symbols.
   */
  void getCreatePage(ArrayList<String> tickerSymbolList);

  /**
   * This method displays an error pop up if user enters invalid investment amount. An invalid
   * portfolio name here is a portfolio which user is trying to create but already exists. For
   * creating the user has to enter a unique portfolio name everytime.
   */
  void errorPfName();

  /**
   * This method displays an error pop up if user enters invalid portfolio name. An invalid
   * portfolio name here is a portfolio which user is trying to access for composition,
   * modification, total etc which is never created (not present in xml).
   */
  void errorPfName2();

  /**
   * This method displays an error pop up if user enters invalid commission fee. An invalid
   * commission fee is a non-positive number.
   */
  void errorCommFee();

  /**
   * This method displays an error pop up if user enters invalid investment amount. An invalid
   * investment amount is a non-positive amount.
   */
  void errorInvtAmt();

  /**
   * This method displays an error pop up if user enters invalid date. Invalid date is a date
   * which is not in yyyy-MM-dd format.
   */
  void errorDate();

  /**
   * This method displays an error pop up if user enters invalid number of shares.
   */
  void errorNs();

  /**
   * This method displays a success pop up if portfolio is modified properly.
   */
  void stockModifiedSuccess();

  /**
   * This method displays an error pop up if user tries to buy on a holiday
   * (i.e. when market is closed).
   */
  void errorDateMarketClosed();

  /**
   * This method displays the frame for getting inputs from user for viewing performance graph
   * of a portfolio.
   */
  void getGraphPage();

  /**
   * This method displays the frame for creating portfolio by investing money using dollar
   * cost averaging method.
   * @param tickerSymbolList list of valid ticker symbols for combo box.
   */
  void getDcaPage(ArrayList<String> tickerSymbolList);

  /**
   * This method throws a success pop up if stock is added successfully while investing money via
   * strategies.
   */
  void dcaStockAdded();

  /**
   * This method throws a success pop up if the sum of percentages of money invested in stocks
   * reaches 100%. It informs the user that they cannot invest in more stocks in this transaction.
   */
  void dcaInvested100();

  /**
   * This method displays an error pop up if user enters incorrect percentage for splitting their
   * invested amount.
   */
  void errorPerc();

  /**
   * This method displays a frame which takes the portfolio name and date. Its a common frame which
   * is used for composition, total or cost basis.
   */
  void getPfNameAndDate();

  /**
   * This method prints the frame for total or cost basis or composition of the GUI.
   * @param output amount or composition (string)
   */
  void outputPage(String output);

  /**
   * This method displays an error pop up if user enters invalid interval during DCA type of
   * investment.
   */
  void errorInterval();

  /**
   * This method displays a success pop up if portfolio is created successfully.
   * @param purpose text for displaying whether portfolio was 'created' or 'modified'.
   */
  void portfolioSuccess(String purpose);

  /**
   * This method displays a frame allowing the user to load an existing portfolio.
   */
  void uploadFile();

  /**
   * This method displays a success pop up if user uploads a valid xml file.
   */
  void fileUploadSuccess();

  /**
   * This method displays an error pop up if user uploads an invalid xml file.
   */
  void errorFileUpload();

  /**
   * This method displays a frame which takes the inputs from user for modifying a portfolio.
   */
  void getModifyPage();

  /**
   * This method displays the frame for displaying the ways in which user can modify a portfolio.
   * @param tickerSymbolList list of valid ticker symbols for combo box.
  */
  void modifyStocks(ArrayList<String> tickerSymbolList);

  /**
   * This method displays an error pop up if user tries to sell more number of shares than he owns.
   */
  void errorSell();

  /**
   * This method displays the frame for creating portfolio by investing money on a specific
   * date on the GUI.
   * @param tickerSymbolList list of valid ticker symbols for combo box.
   */
  void getCreateByInvtOncePage(ArrayList<String> tickerSymbolList);

  /**
   * This method is responsible for drawing the performance graph (a line chart).
   * @param axisForGraph key value pair for x-y axis values.
   */
  void drawGraph(HashMap<String, Integer> axisForGraph);

  void rebalancePortfolio(String pfName, String date);

  void rebalanceDone100();

  void rebalanceStockAdded();
}
