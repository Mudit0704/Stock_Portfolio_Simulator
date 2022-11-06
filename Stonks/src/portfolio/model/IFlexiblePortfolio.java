package portfolio.model;

import java.time.LocalDate;

public interface IFlexiblePortfolio extends IPortfolio {

  double COMMISSION_FEE_PERCENT = 0.20d;

  /**
   * Adds a given set of stocks to the portfolio.
   *
   * @param stocks
   * @param quantity
   */
  void addStocksToPortfolio(IStock stocks, Long quantity);

  /**
   * Sells the given set of stocks from this portfolio.
   *
   * @param tickerSymbol
   * @param quantity
   * @throws IllegalArgumentException
   */
  void sellStocksFromPortfolio(IStock tickerSymbol, Long quantity) throws IllegalArgumentException;

  /**
   * Gets the cost basis of the given portfolio by the date.
   *
   * @param date
   * @return
   */
  double getPortfolioCostBasisByDate(LocalDate date);
}
