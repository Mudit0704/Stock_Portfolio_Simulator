package portfolio.model;

import java.time.LocalDate;

public interface IFlexiblePortfolio extends IPortfolio {

  /**
   * Adds a given set of stocks to the portfolio.
   *
   * @param stocks
   * @param quantity
   */
  void addStocksToPortfolio(IStock stocks, Long quantity, LocalDate date, double transactionFee);

  /**
   * Sells the given set of stocks from this portfolio.
   *
   * @param tickerSymbol
   * @param quantity
   * @throws IllegalArgumentException
   */
  void sellStocksFromPortfolio(IStock tickerSymbol, Long quantity,
      LocalDate date, double transactionFee)
      throws IllegalArgumentException;

  /**
   * Gets the cost basis of the given portfolio by the date.
   *
   * @param date
   * @return
   */
  double getPortfolioCostBasisByDate(LocalDate date);

  String getPortfolioCompositionOnADate(LocalDate date);

  String getPortfolioPerformance(LocalDate start, LocalDate end);
}
