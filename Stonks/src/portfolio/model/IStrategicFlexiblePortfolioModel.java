package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents a strategic portfolio giving the ability to add fractional shares and
 * invest using time based investment strategies.
 */
public interface IStrategicFlexiblePortfolioModel extends IFlexiblePortfoliosModel {

  /**
   * Sets the strategy that this portfolio is to be associated with.
   *
   * @param strategy         the strategy type to be applied.
   * @param startDate        the start date if any for the strategy.
   * @param endDate          the end date of the strategy if any.
   * @param timeFrame        the time frame applicable for the transaction if applicable.
   * @param investmentAmount the amount to be invested in the strategy.
   */
  void setStrategy(StrategyType strategy, LocalDate startDate,
      LocalDate endDate, int timeFrame, double investmentAmount);

  /**
   * Creates a strategic portfolio based on the total amount and proportion of each share on the
   * specified date.
   *
   * @param stockProportions the map of stock ticker symbols and the percentages of their shares in
   *                         the total amount to be invested.
   * @param date             the date on which the strategic portfolio is to be created.
   */
  void createStrategicPortfolio(Map<String, Double> stockProportions, LocalDate date);

  /**
   * Invests amount into the specified strategic portfolio using a specified strategy.
   *
   * @param stockProportions the map of stock ticker symbols and the percentages of their shares in
   *                         the total amount to be invested.
   * @param portfolioId      the id of the specified portfolio.
   */
  void investStrategicPortfolio(Map<String, Double> stockProportions, int portfolioId);

  /**
   * Offers the data points to visualize the performance of the specified portfolio from a specified
   * date range.
   *
   * @param start       the start date from which the portfolio performance is to be visualized.
   * @param end         the end date upto which the portfolio performance is to be visualized.
   * @param portfolioId the specified portfolio id.
   * @return the data points to be used to visualize the portfolio performance.
   */
  Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end,
      int portfolioId);
}
