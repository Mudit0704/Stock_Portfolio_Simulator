package portfolio.model;

import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Abstract class for visualizing the performance of a portfolio and contains the necessary
 * operations required for it.
 */
abstract class AbstractPerformanceVisualizer implements IPerformanceVisualizer {

  IPortfolio portfolio;
  int timeSpan;

  /**
   * Initializes the portfolio object of this class required for the operations.
   *
   * @param portfolio portfolio object for which performance has to be determined.
   */
  AbstractPerformanceVisualizer(IPortfolio portfolio) {
    this.portfolio = portfolio;
  }

  /**
   * Populates the performance values against the respective calculation dates.
   *
   * @param tempDate  Start date from which the performance has to be determined
   * @param end       End date till which the performance has to be determined
   * @param timeSpan  Time jumps between dates
   * @param dateValue Map storing the performance data and its respective date
   */
  abstract void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue);

  /**
   * Populates the performance string to display for a portfolio.
   *
   * @param dateValue Map storing the performance data and its respective date
   * @param minValue  Base value to be used for plotting the performance chart
   * @param scale     Scale to be used for plotting the performance chart
   * @return String representing the performance
   */
  abstract String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale);

  @Override
  public String visualize(LocalDate start, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    populatePortfolioValues(start, end, timeSpan, dateValue);
    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);

    int scale = getScale(minValue.get(), maxValue.get(), 1);

    return populateString(dateValue, minValue.get(), scale);
  }

  static int getScale(Double minValue, Double maxValue, int scale) {
    double diff = maxValue - minValue;
    int quotient = (int) (diff / scale);
    while (quotient >= 50) {
      scale++;
      quotient = (int) (diff / scale);
    }
    return scale;
  }

  static void populateBar(Double minValue, int scale, StringBuilder sb,
      Entry<LocalDate, Double> mapEntry) {
    int portfolioValInt = mapEntry.getValue().intValue();
    int minValInt = minValue.intValue();
    while (portfolioValInt > minValInt) {
      sb.append("*");
      portfolioValInt -= scale;
    }
    sb.append("\n");
  }
}
