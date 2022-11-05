package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents flexible portfolios allowing operations which weren't allowed in the
 * original portfolios model.
 */
public interface IFlexiblePortfoliosModel extends IPortfoliosModel {

  /**
   * Adds new stocks and their respective quantities to an existing portfolio.
   *
   * @param stocks
   * @param portfolioId
   */
  void addStocksToPortfolio(Map<String, Long> stocks, int portfolioId);

  /**
   * Sells current stocks and their respective quantities from the given portfolio
   *
   * @param stocks
   * @param portfolioId
   * @throws IllegalArgumentException if the number of stocks specified is not in portfolio.
   */
  void sellStockFromPortfolio(Map<String, Long> stocks, int portfolioId)
      throws IllegalArgumentException;

  /**
   * Determines the cost basis of a specified portfolio.
   *
   * @param date
   * @param portfolioId
   * @return
   */
  double getCostBasis(LocalDate date, int portfolioId);

}
