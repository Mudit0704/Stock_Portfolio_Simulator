package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents a strategic portfolio giving the ability to add fractional shares.
 */
public interface IStrategicFlexiblePortfolioModel extends IFlexiblePortfoliosModel {

  void setStrategy(StrategyType strategy, LocalDate startDate,
    LocalDate endDate, int timeFrame, double investmentAmount);

  void createStrategicPortfolio(Map<String, Double> stockProportions, LocalDate date);
  /**
   * Creates a strategic portfolio based on the total amount and proportion of each share.
   *
   * @param stockProportions
   */
  void investStrategicPortfolio(Map<String, Double> stockProportions, int portfolioId);

  Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end, int portfolioId);
}
