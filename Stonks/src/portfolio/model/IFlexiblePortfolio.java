package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IFlexiblePortfolio extends IPortfolio {

  /**
   * Adds a given set of stocks to the portfolio.
   *
   * @param stocks
   */
  void addStocksToPortfolio(Map<String, Long> stocks);

  /**
   * Sells the given set of stocks from this portfolio.
   *
   * @param stocks
   * @throws IllegalArgumentException if the number of stocks specified is not in portfolio.
   */
  void sellStocksFromPortfolio(Map<String, Long> stocks) throws IllegalArgumentException;

  /**
   * Gets the cost basis of the given portfolio by the date.
   *
   * @param date
   * @return
   */
  double getPortfolioCostBasisByDate(LocalDate date);
}
