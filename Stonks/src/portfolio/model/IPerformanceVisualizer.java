package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IPerformanceVisualizer {

  String visualize(LocalDate start, LocalDate end, int timeSpan, Map<LocalDate, Double> dateValue);
}
