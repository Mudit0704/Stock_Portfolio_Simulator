package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface is the model interface for the Stocks application. This has various function
 * descriptions for the user inputted stock values. These methods call various other methods
 * to carry out the tasks as requested by the controller. It has methods which handle
 * validations, writing, composition, total etc.
 * m
 */
public interface Model {

  /**
   * This function writes the user inputted portfolio name into the XML file. This method in the
   * model will receive the portfolio name and the file path where this name has to be saved from
   * the controller.
   *
   * @param pfName   Portfolio Name(String), that was entered by the user in the text-based UI
   * @param filePath path of the XML file where the Portfolio Name will be placed. This is an
   *                 absolute path.
   */
  void writePortfolioName(String pfName, String filePath);

  /**
   * This function writes the user inputted portfolio name into the XML file. This method in the
   * model will receive the portfolio name and the file path where this name has to be saved from
   * the controller.
   *
   * @param pfName       Portfolio Name(String), that was entered by the user in the text-based UI
   * @param tickerSymbol ticker symbol (String) that represents the company for which the user is
   *                     interested in investing.
   * @param numShares    number of shares (int) that the user invested for a given tickersymbol
   * @param filePath     path of the XML file where the Portfolio Name will be placed. This is an
   *                     absolute path.
   * @param date         (LocalDate) date that is denoted in the LocalDate format
   * @return true if the user can sell the stocks
   */
  boolean checkIfCanSell(String pfName, String tickerSymbol, double numShares,
                         String filePath, LocalDate date);

  /**
   * This function writes the user inputted stock details into the XML file. This method in the
   * model will receive the portfolio name to which the ticker symbol(s) and the number of shares
   * will be bounded and saved in the xml at the file path.
   *
   * @param pfName       Portfolio Name(String), that was entered by the user in the text-based UI
   * @param tickerSymbol ticker symbol (String) that represents the company for which the user is
   *                     interested in investing.
   * @param numShares    number of shares (int) that the user invested for a given tickersymbol
   * @param filePath     path of the XML file where the stock details will be placed. This is an
   *                     absolute path.
   * @param date         (LocalDate) date that is denoted in the LocalDate format
   */
  void writeStockDetails(String pfName, String tickerSymbol, double numShares, double commFee,
                         String filePath, LocalDate date);

  /**
   * This function gets the portfolio composition from the XML. To get the portfolio, this method
   * should receive the portfolio name and the path of the file where that particular portfolio was
   * saved.
   *
   * @param pfNameComp Composition for the portfolio to be retrieved (String)
   * @param filePath   path of the XML file where the stock details will be available. This is an
   *                   absolute path.
   * @param date       (LocalDate) date that is denoted in the LocalDate format
   * @return a string which contains all the portfolio details (name, stocks, number of shares)
   */
  String getComposition(String pfNameComp, String filePath, LocalDate date);

  /**
   * This function gets the total of shares for a given portfolio from an XML file. To calculate the
   * total of a portfolio this model should receive the name of the portfolio, a hashmap which
   * contains a key value pair of stocks and their prices on a specific date (date entered by the
   * user) and the file path from where it will read the number of shares.
   *
   * @param pfNameTotal Total for the portfolio to be retrieved (String)
   * @param filePath    path of the XML file where the stock details will be available. This is an
   *                    absolute path.
   * @param date        (LocalDate) date that is denoted in the LocalDate format
   * @return a double value which contains the total value of the portfolio
   */
  double getTotal(String pfNameTotal, String filePath, LocalDate date, String type);

  /**
   * This function checks if the given portfolio name is unique, if not it will return false to the
   * controller.
   *
   * @param pfName   Portfolio Name(String), that was entered by the user in the text-based UI
   * @param filePath path of the XML file where the stock details will be available. This is an
   *                 absolute path.
   * @return true if portfolio name is unique (and hence valid) else will return false
   */
  boolean isPfNameUnique(String pfName, String filePath);

  /**
   * This function gets the cost basis for a given portfolio. To calculate the cost basis of a
   * portfolio, this model should receive the name of the portfolio, a hashmap which contains a key
   * value pair of stocks and their prices on a specific date (date entered by the user) and the
   * file path from where it will read the number of shares.
   *
   * @param pfNameTotal Total for the portfolio to be retrieved (String)
   * @param filePath    path of the XML file where the stock details will be available. This is an
   *                    absolute path.
   * @param date        (LocalDate) date that is denoted in the LocalDate format
   * @return a double value which contains the total value of the portfolio
   */
  double getCostBasis(String pfNameTotal, String filePath, LocalDate date);

  /**
   * This function checks if the ticker symbol is a valid symbol. A valid ticker symbol is the one
   * which is available in the master list of the stock names.
   *
   * @param tickerSymbol ticker symbol (String) that represents the company for which the user is
   *                     interested in investing.
   * @return true if ticker symbol is available in the master list, else return false
   */
  boolean isTickerSymbolValid(String tickerSymbol);

  /**
   * This function checks if the given file exists and the file path is valid or not.
   *
   * @param filePath path of the XML file that the user wants to upload. This is an absolute path.
   * @return false if the file path is valid, else will return true
   */
  boolean checkValidFilePath(String filePath);

  /**
   * This method checks if the number of shares entered by the user are fractional or not. It should
   * return false if fractional shares are entered.
   *
   * @param num is the number of shares
   * @return true if it is integer, else will return false
   */
  boolean isNumberOfSharesValid(String num);

  /**
   * This method checks if the date entered by the user are valid or not. It should return false if
   * invalid dates are entered.
   *
   * @param date (String) date formatted as a String.
   * @return true if it is integer, else will return false.
   */
  boolean isDateValid(String date, String purpose, ArrayList<LocalDate> datesFromAPI);

  /**
   * Checks whether the dats is valid or not. A valid date for creation and modification
   * is a date which is in yyyy-MM-dd format and the stock market is open on that day.
   *
   * @param pfNameGraph portfolio name.
   * @param filePath path of the file where portfolio is saved
   * @param date1 start date of the graph.
   * @param date2 end date of the graph.
   * @param validDatesInAPI dates on which market is opened.
   * @return a stringbuilder which has the performance graph for the text based UI.
   */
  StringBuilder getPerformanceGraph(String pfNameGraph, String filePath, LocalDate date1,
      LocalDate date2,  ArrayList<LocalDate> validDatesInAPI);

  /**
   * Checks whether the investment amount is in valid format.
   *
   * @param invtAmt investment amount entered by the user.
   * @return true if investment amount in valid.
   */
  boolean isInvestmentAmtValid(String invtAmt);

  /**
   * Checks whether the commission fee entered by the user is in valid format.
   *
   * @param commFee commission fee for that particular transaction.
   * @return returns true if commission fee is of valid format.
   */
  boolean isCommFeeValid(String commFee);

  /**
   * Returns the investment amount in double.
   *
   * @param invtAmt investment amount entered by the user.
   * @return investment amount (double).
   */
  double getInvestmentAmount(String invtAmt);

  /**
   * Returns the commission fee in double.
   *
   * @param commFee commission fee for the particular transaction.
   * @return the commission fee (double)
   */
  double getCommFee(String commFee);

  /**
   * Checks whether the percentage entered by the user is in valid format. An invalid percentage is
   * a non positive number.
   *
   * @param perc percentage of investment amount to be put for a particular stock.
   * @return true if percentage is valid.
   */
  boolean isPercentageValid(String perc);

  /**
   * Returns the percentage from string to double format.
   *
   * @param perc percentage of investment amount to be put for a particular stock.
   * @return percentage (double)
   */
  double getPercentage(String perc);

  /**
   * Checks whether the interval entered by the user is in valid format. An invalid interval is
   * a non positive integer.
   *
   * @param interval interval in which user wants investments to happen in dollar cost average.
   * @return returns true if interval is of valid format.
   */
  boolean isIntervalValid(String interval);

  /**
   * Returns the interval for DCA in int format.
   * @param interval The interval in which the user wants investments to happen in case of dollar
   *                 cost averaging.
   * @return converts the intervals from string to int format.
   */
  int getInterval(String interval);

  /**
   * Returns the ticker symbol.
   *
   * @param ts ticker symbol entered by the user
   * @return returns the ticker symbol
   */
  String getTickerSymbol(String ts);

  /**
   * Checks whether the start date for dollar cost averaging is valid or not.
   * A valid start date is a date which is in yyyy-MM-dd format.
   *
   * @param strDate date in string format
   * @return true if date is valid
   */
  boolean isStartDateValid(String strDate);

  /**
   * Checks whether the end date for dollar cost averaging is valid or not.
   * A valid end date is a date which is in yyyy-MM-dd format and greater than the start date.
   *
   * @param strDate date in string format
   * @param startDate start date entered by the user for graph or DCA.
   * @return true if date is valid.
   */
  boolean isEndDateValid(String strDate, LocalDate startDate);

  /**
   * Returns the date in the LocalDate format.
   *
   * @param strDate date in string format.
   * @return converts string to LocalDate.
   */
  LocalDate getDate(String strDate);

  /**
   * Adds the stocks and the percentage of the investment amount to a map in cases where the
   * user wants to create portfolio by investing money instead of specifying exact number of
   * shares.
   * @param tickerSymbol stock to be invested into.
   * @param percentage percentage of money to be invested in the said stock.
   */
  void saveTsAndPerc(String tickerSymbol, double percentage);

  /**
   * Resets the map of stocks and the percentage of the investment amount in case the
   * user wants to create portfolio by investing money instead of specifying exact number of
   * shares.
   */
  void resetTsAndPerc();

  /**
   * Incorporates the dollar cost averaeg strategy.
   * Calculates the timestamps and the number of stock to be registered in a portfolio
   * depending on the investment amount and the percentage split up of this amount
   * into various stocks. Saves the investment strategy in a portfolio on each interval between
   * the start and end date (as provided by the user).
   *
   * @param pfName portfolio name of the portfolio to be created/ modified.
   * @param invtAmt investment amount provided by the user
   * @param commFee commission fee per transaction
   * @param startDate start date of the investment strategy
   * @param endDate end date of the investment strategy
   * @param intervals intervals in which the user wants to automatically buy stocks.
   * @param filePath path of the file where portfolio details will be saved.
   * @param validDates dats on which the market is open for the buying to take place.
   * @param purpose whether portfolio is being created or modified using this strategy.
   */
  void dollarCostAveraging(String pfName, double invtAmt, double commFee, LocalDate startDate,
                           LocalDate endDate, int intervals, String filePath,
                           ArrayList<LocalDate> validDates, String purpose);

  /**
   * Converts the number of shares from string format to integer format.
   *
   * @param numShares number of shares user wants to buy
   * @return the number of shares as an integer
   */
  double getNumberOfShares(String numShares);

  /**
   * Checks whether the user has invested 100% of his money.
   *
   * @return true if user has invested 100% of his money.
   */
  boolean isPercentagesSum100();

  /**
   * Checks whether the percentage of money to be invested has crossed 100 or not.
   *
   * @param percentage percentage of money to be put in a stock
   * @return true if user is trying to invest more than 100% of his investment amount.
   */
  boolean percentageSumMoreThan100(double percentage);

  /**
   * Gets a list of all the stocks supported by this system.
   *
   * @return an arraylist of all the supported stocks.
   */
  ArrayList<String> getStockList();

  /**
   * Calculates the timestamps and the total value on that timestamp for plotting the graph.
   * Returns a hashmap whose key value pair acts as x-y axis on the graph on GUI.
   *
   * @param pfNameGraph portfolio name whose graph is to plotted.
   * @param filePath path of the file where portfolio details are available.
   * @param date1 start date of the graph.
   * @param date2 end date of the graph.
   * @param validDatesInAPI valid dates from api.
   * @return a hashmap with timestamp and value as a key value pair.
   */
  HashMap<String,Integer> getAxisForGraph(String pfNameGraph, String filePath, LocalDate date1,
                             LocalDate date2, ArrayList<LocalDate> validDatesInAPI);

  void rebalanceExistingPortfolio(String pfName, String filePath, LocalDate date,
      ArrayList<LocalDate> validDatesInAPI, String type);
}
