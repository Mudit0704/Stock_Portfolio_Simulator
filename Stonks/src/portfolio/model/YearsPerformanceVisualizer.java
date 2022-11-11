package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class YearsPerformanceVisualizer extends AbstractPerformanceVisualizer {

  YearsPerformanceVisualizer(IPortfolio portfolio){
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    while (tempDate.isBefore(end) || tempDate.getYear() == end.getYear()) {
      LocalDate yearEndDate = tempDate.withDayOfYear(tempDate.lengthOfYear());
      double value = portfolio.getPortfolioValue(yearEndDate);

      dateValue.put(yearEndDate, value);
      tempDate = tempDate.plusYears(timeSpan);
    }
  }

  String populateString(Map<LocalDate, Double> dateValue, Double minValue,
      int scale) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      sb.append(mapEntry.getKey().getYear()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("Scale: ").append(scale).append("\n");
    return sb.toString();
  }
}
