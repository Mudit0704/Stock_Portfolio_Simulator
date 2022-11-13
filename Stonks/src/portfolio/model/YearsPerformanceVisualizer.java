package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Class for visualizing the performance of a portfolio in terms of years and contains the
 * implementation of necessary operations required for it.
 */
public class YearsPerformanceVisualizer extends AbstractPerformanceVisualizer {

  /**
   * Constructs an object of {@link YearsPerformanceVisualizer} and initializes its members.
   *
   * @param portfolio portfolio object for which the performance has to be visualized
   */
  YearsPerformanceVisualizer(IPortfolio portfolio) {
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    while (tempDate.isBefore(end) || tempDate.getYear() == end.getYear()) {
      LocalDate yearEndDate = tempDate.withDayOfYear(tempDate.lengthOfYear());
      yearEndDate = tempDate.getYear() == end.getYear() ? end : yearEndDate;
      double value = portfolio.getPortfolioValue(yearEndDate);

      dateValue.put(yearEndDate, value);
      tempDate = tempDate.plusYears(timeSpan);
    }
  }

  @Override
  String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale) {
    StringBuilder sb = new StringBuilder();
    sb.append("\nVisualizing using the period of years\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      sb.append(mapEntry.getKey().getYear()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("\nBase: ").append(String.format("%,.2f", minValue)).append("\n");
    sb.append("A line without asterisk means the performance during that timespan was"
        + " less than or equal to the base given above").append("\n");
    sb.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");
    return sb.toString();
  }
}
