package portfolio.model;

import java.util.Map;

public interface IStrategy<T> {
  T getPortfolioStocksPerStrategy(Map<IStock, Double> stockQtyRatio);
}
