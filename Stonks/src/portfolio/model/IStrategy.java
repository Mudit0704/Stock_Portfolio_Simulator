package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IStrategy {
  Map getPortfolioStocksPerStrategy(Map<IStock, Double> stockQtyRatio, LocalDate date);
}
