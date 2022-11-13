package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Class for visualizing the performance of a portfolio in terms of days and contains the
 * implementation of necessary operations required for it.
 */
public class DaysPerformanceVisualizer extends AbstractPerformanceVisualizer {

  /**
   * Constructs an object of {@link DaysPerformanceVisualizer} and initializes its members.
   *
   * @param portfolio portfolio object for which the performance has to be visualized
   */
  DaysPerformanceVisualizer(IPortfolio portfolio) {
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    this.timeSpan = timeSpan;
    while (tempDate.isBefore(end) || tempDate.isEqual(end)) {
      tempDate = tempDate.getMonth().equals(end.getMonth()) ? end : tempDate;
      double value = portfolio.getPortfolioValue(tempDate);

      dateValue.put(tempDate, value);
      tempDate = tempDate.plusDays(this.timeSpan);
    }
  }

  @Override
  public String populateString(Map<LocalDate, Double> dateValue, Double minValue, int scale) {
    StringBuilder sb = new StringBuilder();
    sb.append("\nVisualizing using the period of days\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      if(timeSpan != 1) {
        sb.append(mapEntry.getKey().minusDays(timeSpan)).append(" -> ");
      }
      sb.append(mapEntry.getKey().toString()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("\nBase: ").append(String.format("%,.2f", minValue)).append("\n");
    sb.append("A line without asterisk means the performance during that timespan was"
        + " less than or equal to the base given above").append("\n");
    sb.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");
    return sb.toString();
  }
}
