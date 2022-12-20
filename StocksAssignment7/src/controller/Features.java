package controller;

import java.util.List;

/**
 * This interface contains all the features to be performed when working with the GUI.
 */
public interface Features {

  /**
   * Event method to display the text inputted while selecting the option from the main GUI menu.
   *
   * @param typed (String) input field
   */
  void echoTextTyped(String typed);

  /**
   * This method calls the view to open a new portfolio creation frame on the GUI.
   */
  void createBySelf();

  /**
   * This method calls the view to open a new portfolio creation frame for investment strategy.
   */
  void createByInvtOnce();

  /**
   * This method calls the view to open a new modification frame for investment strategy.
   */
  void modifyInvtOnce();

  /**
   * This method calls the view to open a new portfolio creation frame for dollar cost averaging
   * frame on the GUI.
   */
  void createByDca();

  /**
   * This method receives the values entered by the user while creating a portfolio by entering the
   * quantity themselves. It calls the model for validating the inputs and throws an error or
   * success message accordingly.
   *
   * @param pfName       portfolio name
   * @param tickerSymbol ticker symbol
   * @param numShares    number of shares
   * @param commFee      commission fee
   * @param date         date
   * @param flag         to check if pf name field is disabled.
   */
  void echoStockDetails(String pfName, String tickerSymbol, String numShares, String commFee,
      String date, boolean flag);

  /**
   * This method receives the values entered by the user for portfolio creation using Dollar Cost
   * Averaging. It calls the model for validating the inputs and throws an error or success message
   * accordingly.
   *
   * @param pfName        portfolio name
   * @param invtAmt       investment amount
   * @param commFee       commission fee
   * @param tickerSymbol  ticker symbol for investing
   * @param perc          percentage of the investment amount to be invested in a particular stock.
   * @param startDate     start date of the investment strategy.
   * @param endDate       end date of the investment strategy.
   * @param interval      intervals in which user wants to invest.
   * @param flagForPfName to check if portfolio name field is disabled or enabled.
   * @param flagForStocks to check if stock name field is disabled or enabled.
   */
  void createDcaPortfolio(String pfName, String invtAmt, String commFee, String tickerSymbol,
      String perc, String startDate, String endDate, String interval,
      boolean flagForPfName, boolean flagForStocks);

  /**
   * This method gets the portfolio name with date for composition or total or cost basis. It calls
   * the model for validating the inputs and throws an error or success message accordingly.
   *
   * @param pfName portfolio name.
   * @param date   date for composition/ total/ cost basis.
   */
  void echoPfNameWithDate(String pfName, String date);

  /**
   * This method loads the file path of the new portfolio chosen by the user to perform various
   * operations on. It throws an error if invalid file is loaded.
   *
   * @param filePath file path of the newly uploaded file
   */
  void getFilePath(String filePath);

  void modifyStockBuy();

  void modifyStockSell();

  /**
   * This method gets the details entered by the user for modifying (buy/ sell) a stock(s) to an
   * existing portfolio.
   *
   * @param pfName       portfolio name for modification.
   * @param tickerSymbol ticker symbol to be bought/ sold.
   * @param numShares    number of shares to be bought/ sold.
   * @param commFee      commission fee.
   * @param date         date on which buying/ selling will happen.
   */
  void buyOrSellStocks(String pfName, String tickerSymbol, String numShares, String commFee,
      String date);

  /**
   * This method gets the details entered by the user for investing an amount in several stocks on a
   * specific date.
   *
   * @param pfName        portfolio name for creation.
   * @param invtAmt       money to be invested.
   * @param commFee       commission fee.
   * @param tickerSymbol  ticker symbol to be bought.
   * @param perc          percentage split of how the money is to be invested.
   * @param date          date on which investment is to be done.
   * @param flagForPfName if portfolio name field is enabled or disabled.
   * @param flagForStocks if ticker symbol field is enabled or disabled.
   */
  void createInvtPortfolio(String pfName, String invtAmt, String commFee, String tickerSymbol,
      String perc, String date, boolean flagForPfName,
      boolean flagForStocks);

  void modifyStocksDca();

  /**
   * This method gets the details entered by the user to display a graph in a time range.
   *
   * @param inPFName  portfolio name
   * @param inDate    start date for graph
   * @param inEndDate end date for graph
   */
  void getGraph(String inPFName, String inDate, String inEndDate);

  /**
   * This method performs the rebalancing of a portfolio at a given date by calling the respective
   * model method.
   *
   * @param pfName name of the portfolio for which rebalancing has to be done
   * @param date   date at which rebalancing has to be done
   */
  void rebalancePortfolio(String pfName, String date);

  /**
   * Add weightage for stocks inside a portfolio for rebalancing.
   *
   * @param tickerSymbol stock symbol for which weightage is being set
   * @param percentage   weightage for the stock symbol
   */
  void addStockWeightsForRebalance(String tickerSymbol, String percentage);

  /**
   * Get a list of stocks in a portfolio at a given date.
   *
   * @param pfName name of the portfolio for which stocks have to be determined
   * @param date   date at which the stocks have to be determined
   * @return list of stocks inside the portfolio at a given date
   */
  List<String> getPortfolioStocks(String pfName, String date);
}
