package portfolio.model;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class MonthsPerformanceVisualizer extends AbstractPerformanceVisualizer {

  MonthsPerformanceVisualizer(IPortfolio portfolio){
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    while (tempDate.isBefore(end) || tempDate.getMonth().equals(end.getMonth())) {
      LocalDate monthEndDate = tempDate.withDayOfMonth(
          tempDate.getMonth().length(tempDate.isLeapYear()));
      double value = portfolio.getPortfolioValue(monthEndDate);

      dateValue.put(monthEndDate, value);
      tempDate = tempDate.plusMonths(timeSpan);
    }
  }

  String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      sb.append(mapEntry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()))
          .append(mapEntry.getKey().getYear()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("Scale: * = ").append("$" + scale).append("\n");
    return sb.toString();
  }
}
