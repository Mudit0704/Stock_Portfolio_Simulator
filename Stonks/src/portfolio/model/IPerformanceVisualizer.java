package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents the operations required for visualizing performance of a portfolio.
 */
public interface IPerformanceVisualizer {

  /**
   * Visualizes the portfolio performance.
   * @param start start date for the performance calculation
   * @param end end date for the performance calculation
   * @param timeSpan time span for which performance will be calculated
   * @param dateValue map to store the portfolio performance calculated for specific dates
   * @return
   */
  String visualize(LocalDate start, LocalDate end, int timeSpan, Map<LocalDate, Double> dateValue);
}
