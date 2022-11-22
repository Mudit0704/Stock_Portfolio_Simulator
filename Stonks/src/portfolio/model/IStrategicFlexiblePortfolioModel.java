package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * This interface represents a strategic portfolio giving the ability to add fractional shares.
 */
public interface IStrategicFlexiblePortfolioModel extends IFlexiblePortfoliosModel {

  /**
   * Creates a strategic portfolio based on the total amount and proportion of each share.
   *
   * @param stockProportions
   * @param totalAmount
   */
  void investStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
    int portfolioId, IStrategy strategy);
}
