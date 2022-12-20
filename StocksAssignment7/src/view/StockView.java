package view;

import java.time.LocalDate;


/**
 * This is the view interface for the Stocks application. This has various function descriptions to
 * display the menu and values for the user on a text-based interface (console).
 */
public interface StockView {

  /**
   * This function prints the menu on the text based interface that has the options for creating
   * flexible and inflexible portfolios.
   */
  void printMenu();

  /**
   * This function prints the menu on the text based interface that has the options for flexible
   * portfolios (total value, composition, cost basis, upload and retrieve).
   */
  void printSubMenu1();

  /**
   * This function prints the menu on the text based interface that has the options for inflexible
   * portfolios (total value, composition, cost basis, upload and retrieve).
   */
  void printSubMenu2();

  /**
   * This function prints the message for entering the portfolio name on the text-based interface.
   */
  void getPFName();

  /**
   * This function prints the message for entering the ticker symbol on the text-based interface.
   */
  void getTickerSymbol();

  /**
   * This function prints the message, asking if the user has to update more portfolios on the
   * text-based interface.
   */
  void morePortfolios();

  /**
   * This function prints the message asking if the user has to update more ticker symbols on the
   * text-based interface.
   */
  void moreTickerSymbols();

  /**
   * This function prints the message for entering the number of shares for a given ticker symbol on
   * the text-based interface.
   */
  void getNoOfShares();

  void askPfName(String purpose);

  void askMorePfName(String purpose);

  /**
   * This function returns the composition for a given portfolio name.
   *
   * @param fullPfDetails (String) output that prints the portfolio details in desired format
   */
  void printPfDetailsForComposition(String fullPfDetails);

  /**
   * This function returns the total shares for a given portfolio name.
   *
   * @param pfTotal (double) total value for the portfolio
   * @param pfName  (String) name of the portfolio
   */
  void printTotalPf(double pfTotal, String pfName);

  /**
   * This function returns the cost basis for a given portfolio name.
   *
   * @param pfTotal (double) total value for the portfolio
   * @param pfName  (String) name of the portfolio
   * @param date    (LocalDate) date that is denoted in the LocalDate format
   */
  void printCostBasis(double pfTotal, String pfName, LocalDate date);

  /**
   * This function prints the message if the user has selected the quit option.
   */
  void exitMsg();

  /**
   * This function prints the message if the user has selected an invalid option.
   */
  void errorMsg1();

  void errorMsg2();

  void errorMsg3();

  void errorMsg4();

  void errorMsg5();

  void errorMsg6();

  void errorMsg7();

  void errorMsg8();

  void errorMsg9();

  void errorMsg10();

  void errorMsg11();

  void errorMsg12();

  void errorMsg13();

  /**
   * This function prints the message if the market was closed for a particular date when used to
   * retrieve total, cost basis.
   *
   * @param date (LocalDate) date that is denoted in the LocalDate format
   */
  void marketClosed(LocalDate date);

  /**
   * This function prints the message for the user asking for file path for upload.
   */
  void getFileURL();

  /**
   * This function prints the success message if file upload is successful.
   */
  void successFileURL();

  /**
   * This function prints the message asking the user for buying or selling stocks.
   */
  void modifyPurchaseOrSell();

  /**
   * This function prints the message, asking the user for portfolio name to modify the portfolio.
   */
  void modifyGetPfName();

  /**
   * This function prints the message, asking the user for more modifications.
   */
  void moreModifications();

  /**
   * This function prints the message, asking the user for more modification to stock values.
   */
  void modifyMoreStocks();

  void printGraph(StringBuilder pfGraph, String pfNameGraph, LocalDate date1, LocalDate date2);


  /**
   * This function prints the message, asking the user for date for retrieving cost basis, total
   * values and composition.
   *
   * @param purpose (String) option to either or buy or sell the stocks
   */
  void askDate(String purpose);

  /**
   * This function prints the message, asking the user for start and end dates to calculate
   * portfolio performance.
   *
   * @param purpose (String) option to calculate performance of a portfolio
   */
  void askPerformanceDate(String purpose);

  void errorMsg14();

  void errorMsg15();

  void errorMsg16();

  void errorMsg17();

  void errorMsg18();

  void errorMsg19();

  void errorMsg20();

  void getInvestmentAmount();

  void getCommFee();

  void getNumOfSharesForDCA();

  void getPercentageForDCA(String ts);

  void getTSNameForDCA(int i);

  void getIntervalForDCA();

  void getStartDateForDCA();

  void getEndDateForDCA();

  void successDCAPortfolios();
}
