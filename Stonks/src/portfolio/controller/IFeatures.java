package portfolio.controller;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents the GUI controller and its related operations required to implement portfolios.
 */
public interface IFeatures {

  /**
   * Creates a new portfolio with the given creation date containing the stocks passed.
   *
   * @param stocks stocks to be added in the new portfolio
   * @param date   date for which the portfolio has to be created
   * @return the status of portfolio creation represented by a string
   */
  String createFlexiblePortfolio(Map<String, Double> stocks, String date);

  /**
   * Sells stocks from an existing portfolio.
   *
   * @param tickerSymbol   of the stock to be sold
   * @param quantity       quantity of the stock to be sold
   * @param portfolioId    id of the portfolio from which stock has to be sold
   * @param date           date at which the stock has to be sold
   * @param transactionFee transaction fee to be charged for selling the stock
   * @return the status of stock selling represented by a string
   */
  String sellPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee);

  /**
   * Buy stocks for an existing portfolio.
   *
   * @param tickerSymbol   of the stock to be bought
   * @param quantity       quantity of the stock to be bought
   * @param portfolioId    id of the portfolio from which stock has to be bought
   * @param date           date at which the stock has to be bought
   * @param transactionFee transaction fee to be charged for buying the stock
   * @return the status of stock buying represented by a string
   */
  String buyPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee);

  /**
   * Get the cost basis of a specified portfolio.
   *
   * @param date        the date at which cost basis of the portfolio is required.
   * @param portfolioId id of the portfolio for which cost basis is required.
   * @return the portfolio's cost basis.
   */
  double getCostBasis(String date, String portfolioId);

  /**
   * Get the value of an existing portfolio at a given date.
   *
   * @param date        the date at which value of the portfolio is required.
   * @param portfolioId id of the portfolio for which value is required.
   * @return the portfolio's value.
   */
  double getPortfolioValue(String date, String portfolioId);

  /**
   * Saves all the portfolios.
   *
   * @return the status of portfolio saving represented by a string
   */
  String savePortfolio();

  /**
   * Retrieves all the portfolios from /flexiblePortfolios path.
   *
   * @return the status of portfolio retrieval represented by a string
   */
  String retrievePortfolio();

  /**
   * Get all the available portfolios.
   *
   * @return a string of available portfolios
   */
  String getAvailablePortfolios();

  /**
   * Performs fractional investment on an existing portfolio for a given date.
   *
   * @param stockProportions proportions for different stocks to be invested
   * @param totalAmount      total amount to be invested
   * @param portfolioId      portfolio for which the investment has to be done
   * @param date             date at which the investment has to be done
   * @return the status of investment represented by a string
   */
  String performFractionalInvestmentOnAGivenDate(Map<String, Double> stockProportions,
      String totalAmount, String portfolioId, String date);

  /**
   * Creates a new portfolio using the dollar cost average strategy.
   *
   * @param stockProportions proportions for different stocks to be invested
   * @param totalAmount      total amount to be invested
   * @param startDate        start date at which the investment strategy has to start
   * @param endDate          end date at which the investment strategy has to end
   * @param timeFrame        time spans between each investment
   * @return the status of portfolio creation represented by a string
   */
  String createDollarCostAveragePortfolio(Map<String, Double> stockProportions, String totalAmount,
      String startDate, String endDate, String timeFrame);

  /**
   * Apply dollar cost average strategy on an existing portfolio.
   *
   * @param stockProportions proportions for different stocks to be invested
   * @param totalAmount      total amount to be invested
   * @param startDate        start date at which the investment strategy has to start
   * @param endDate          end date at which the investment strategy has to end
   * @param timeFrame        time spans between each investment
   * @param portfolioId      portfolio for which the investment has to be done
   * @return the status of portfolio creation represented by a string
   */
  String applyDollarCostAveragePortfolio(Map<String, Double> stockProportions, String totalAmount,
      String startDate, String endDate, String timeFrame, String portfolioId);

  /**
   * Checks whether the given ticker symbol is a valid one.
   *
   * @param tickerSymbol stock ticker symbol to validated
   * @return <code>true</code> if the symbol is valid; otherwise <code>false</code>
   */
  boolean isTickerSymbolValid(String tickerSymbol);

  /**
   * Get the performance of an existing portfolio between given dates.
   *
   * @param startDate   start date for the portfolio performance calculation
   * @param endDate     end date for the portfolio performance calculation
   * @param portfolioId id of the portfolio for which the performance has to be determined
   * @return a map containing the portfolio values at certain dates
   */
  Map<LocalDate, Double> getPortfolioPerformance(String startDate, String endDate,
      String portfolioId);
}
