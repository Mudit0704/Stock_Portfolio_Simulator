package portfolio.model;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

/**
 * Class for visualizing the performance of a portfolio in terms of months and contains the
 * implementation of necessary operations required for it.
 */
public class MonthsPerformanceVisualizer extends AbstractPerformanceVisualizer {

  /**
   * Constructs an object of {@link MonthsPerformanceVisualizer} and initializes its members.
   *
   * @param portfolio portfolio object for which the performance has to be visualized.
   */
  MonthsPerformanceVisualizer(IPortfolio portfolio) {
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    this.timeSpan = timeSpan;
    while (tempDate.isBefore(end) || tempDate.getMonth().equals(end.getMonth())
        || tempDate.getMonth().equals(end.getMonth().plus(1))) {
      LocalDate monthEndDate = tempDate.withDayOfMonth(
          tempDate.getMonth().length(tempDate.isLeapYear()));
      if (tempDate.getMonth().equals(end.getMonth().plus(1))
          && tempDate.getYear() == end.getYear()) {
        if (timeSpan == 1) {
          break;
        }
        monthEndDate = end;
      }

      double value = portfolio.getPortfolioValue(monthEndDate);

      dateValue.put(monthEndDate, value);
      tempDate = tempDate.plusMonths(this.timeSpan);
    }
  }

  @Override
  String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale) {
    StringBuilder sb = new StringBuilder();
    sb.append("\nVisualizing using the period of ").append(this.timeSpan).append("month(s)\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      sb.append(mapEntry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.US))
          .append(mapEntry.getKey().getYear()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("\nBase: ").append(String.format("%,.2f", minValue)).append("\n");
    sb.append("A line without asterisk means the performance during that timespan was"
        + " equal to the base given above").append("\n");
    sb.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");
    return sb.toString();
  }
}
