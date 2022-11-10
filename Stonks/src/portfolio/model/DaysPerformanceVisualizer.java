package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class DaysPerformanceVisualizer extends AbstractPerformanceVisualizer{

  DaysPerformanceVisualizer(IPortfolio portfolio){
    super(portfolio);
  }

  @Override
  public void populatePortfolioValues(LocalDate tempDate, LocalDate end, int timeSpan,
      Map<LocalDate, Double> dateValue) {
    while (tempDate.isBefore(end)) {
      double value = portfolio.getPortfolioValue(tempDate);

      dateValue.put(tempDate, value);
      tempDate = tempDate.plusDays(timeSpan);
    }
  }


  public String populateString(Map<LocalDate, Double> dateValue, Double minValue, int scale) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      sb.append(mapEntry.getKey().toString()).append(": ");
      populateBar(minValue, scale, sb, mapEntry);
    }
    sb.append("Scale: ").append(scale).append("\n");
    return sb.toString();
  }
}
