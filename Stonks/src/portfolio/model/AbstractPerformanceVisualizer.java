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

  /**
   * Initializes the portfolio object of this class required for the operations.
   * @param portfolio portfolio object for which performance has to be determined.
   */
  AbstractPerformanceVisualizer(IPortfolio portfolio){
    this.portfolio = portfolio;
  }

  abstract void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue);

  abstract String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale);

  @Override
  public String visualize(LocalDate start, LocalDate end, int timeSpan, Map<LocalDate, Double> dateValue) {
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
