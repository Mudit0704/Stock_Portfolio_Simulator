package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents a strategic portfolio giving the ability to add fractional shares.
 */
public interface IStrategicFlexiblePortfolioModel extends IFlexiblePortfoliosModel {

  public void setStrategy(StrategyType strategy);
  /**
   * Creates a strategic portfolio based on the total amount and proportion of each share.
   *
   * @param stockProportions
   */
  void investStrategicPortfolio(Map<String, Double> stockProportions,
    int portfolioId, IStrategy strategy);
}
