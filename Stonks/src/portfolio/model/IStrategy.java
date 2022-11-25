package portfolio.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public interface IStrategy {
  Map<LocalDate, Map<IStock, Double>> applyStrategy(Map<IStock, Double> stockQtyRatio);

  double getStrategyInvestment();
}
