package portfolio.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * This interface represents a Strategy. It offers methods to apply strategy to any given set of
 * stocks and get the information about the strategy.
 */
public interface IStrategy {

  /**
   * Applies a strategy based on the stocks and their percentages.
   *
   * @param stockQtyRatio represents the ratio of stock quantities to be invested by applying
   *                      strategy.
   * @return the quantities of stocks based on strategy application on different dates.
   */
  Map<LocalDate, Map<IStock, Double>> applyStrategy(Map<IStock, Double> stockQtyRatio);

  /**
   * Returns the amount invested in any strategy.
   *
   * @return the amount invested through this strategy.
   */
  double getStrategyInvestment();
}
