package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents flexible portfolios allowing operations which weren't allowed in the
 * original portfolios model.
 */
public interface IFlexiblePortfoliosModel extends IPortfoliosModel {

  /**
   * Creates new portfolio on the specified date.
   *
   * @param stocks mapping of stock ticker symbols and quantities.
   * @param date   the date on which the portfolio is to be created.
   */
  void createNewPortfolioOnADate(Map<String, Long> stocks, LocalDate date);

  /**
   * Adds new stocks and their respective quantities to an existing portfolio.
   *
   * @param tickerSymbol ticker symbol of the stock to be added
   * @param quantity     quantity of the stock to be added
   * @param portfolioId  id of the portfolio for which stock has to be added
   */
  void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId, LocalDate date);

  /**
   * Sells current stocks and their respective quantities from the given portfolio.
   *
   * @param tickerSymbol ticker symbol of the stock to be sold
   * @param quantity     quantity of the stock to be sold
   * @param portfolioId  id of the portfolio for which stock has to be sold
   * @throws IllegalArgumentException if provided invalid inputs
   */
  void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId, LocalDate date)
      throws IllegalArgumentException;

  /**
   * Determines the cost basis of a specified portfolio.
   *
   * @param date        the date at which cost basis of the portfolio is required
   * @param portfolioId id of the portfolio for which cost basis is required
   * @return the portfolio's cost basis
   */
  double getCostBasis(LocalDate date, int portfolioId);

  /**
   * Gets the portfolio performance within a date range.
   *
   * @param portfolioId id of the portfolio for which performance is required
   * @param rangeStart  start date of the date range for which performance has to be determined
   * @param rangeEnd    end date of the date range for which performance has to be determined
   * @return String representing the portfolio performance
   */
  String getPortfolioPerformance(int portfolioId, LocalDate rangeStart, LocalDate rangeEnd);

  /**
   * Gets the composition of the given portfolio by the date.
   *
   * @param portfolioId id of the portfolio for which composition is required
   * @param date        the date at which composition of the portfolio is required
   * @return
   */
  String getPortfolioCompositionOnADate(int portfolioId, LocalDate date);

  /**
   * Sets the stock service type for the portfolio model
   *
   * @param serviceType type of service required. Represented by {@link ServiceType}.
   */
  void setServiceType(ServiceType serviceType);

  /**
   * Sets the commission fee for the portfolio model
   *
   * @param commissionFee value of commission fee to be set
   */
  void setCommissionFee(double commissionFee);

}
