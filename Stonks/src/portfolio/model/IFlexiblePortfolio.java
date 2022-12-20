package portfolio.model;

import java.time.LocalDate;

/**
 * This interface represents a single flexible portfolio of stocks and its set of related
 * operations.
 */
public interface IFlexiblePortfolio extends IPortfolio {

  /**
   * Adds a given set of stocks to the portfolio.
   *
   * @param stocks   Stock object to be added to the portfolio
   * @param quantity Quantity of the stock to be added
   */
  void addStocksToPortfolio(IStock stocks, Long quantity, LocalDate date, double transactionFee);

  /**
   * Sells the given set of stocks from this portfolio.
   *
   * @param stock    stock object to be sold from the portfolio
   * @param quantity quantity of stock to be sold
   * @throws IllegalArgumentException if provided invalid inputs
   */
  void sellStocksFromPortfolio(IStock stock, Long quantity,
      LocalDate date, double transactionFee)
      throws IllegalArgumentException;

  /**
   * Gets the cost basis of the given portfolio by the date.
   *
   * @param date the date at which cost basis of the portfolio is required
   * @return the cost basis at the given date
   */
  double getPortfolioCostBasisByDate(LocalDate date);

  /**
   * Gets the composition of the given portfolio by the date.
   *
   * @param date the date at which composition of the portfolio is required
   * @return the composition at the given date
   */
  String getPortfolioCompositionOnADate(LocalDate date);

  /**
   * Gets the portfolio performance within a date range.
   *
   * @param start start date of the date range for which performance has to be determined
   * @param end   end date of the date range for which performance has to be determined
   * @return String representing the portfolio performance
   */
  String getPortfolioPerformance(LocalDate start, LocalDate end);
}
