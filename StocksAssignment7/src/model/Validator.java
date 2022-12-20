package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface has the implementations to check various fields such as stocks, dates, portfolio
 * names and ticker symbols.
 */
public interface Validator {

  /**
   * This function checks if the user can sell the stock.
   *
   * @param pfName       (String) the portfolio name
   * @param tickerSymbol (String) the ticker symbol
   * @param numShares    (int) the number of shares
   * @param filePath     (String) path of the stock csv files/API
   * @param date         (LocalDate) date to be inputted by the user
   * @return true if stocks can be sold
   */
  boolean checkIfCanSell(String pfName, String tickerSymbol, double numShares,
                         String filePath, LocalDate date);

  /**
   * This function checks if the pfName is unique.
   *
   * @param pfName   (String) the portfolio name
   * @param filePath (String) path of the stock csv files/API
   * @return true if portfolio name is unique
   */
  boolean isPfNameUnique(String pfName, String filePath);

  /**
   * This function checks if the date format is valid.
   *
   * @param strDate (String) date in  string format
   * @return true if date is valid
   */
  boolean isDateValid(String strDate, String purpose, ArrayList<LocalDate> datesFromAPI);

  /**
   * This function checks if the file path is valid.
   *
   * @param path (String) path of the stock csv files/API
   * @return true if path is correct.
   */
  boolean checkValidFilePath(String path);

  /**
   * This function checks if the file path is valid.
   *
   * @param tickerSymbol (String) ticker symbol
   * @return true if ticker symbol is valid
   */
  boolean isTickerSymbolValid(String tickerSymbol);

  boolean isInvestmentAmtValid(String invtAmt);

  boolean isCommFeeValid(String invtAmt);

  boolean isNumOfStocksValid(String numOfStocks);

  boolean isPercentageValid(String perc, HashMap<String, Double> mapForTsPerc);

  boolean isIntervalValid(String interval);

  boolean isStartDateValid(String strDate);

  boolean isEndDateValid(String strDate, LocalDate startDate);

  boolean isNumberOfSharesValid(String num);

  boolean doPercentagesSumTo100(HashMap<String, Double> mapForTsPerc);
}
