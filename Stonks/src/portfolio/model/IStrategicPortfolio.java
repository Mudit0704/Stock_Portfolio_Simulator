package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IStrategicPortfolio extends IFlexiblePortfolio {

  /**
   *
   * @param stockProportions
   * @param totalAmount
   * @param portfolioId
   * @param date
   */
  void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions, Double totalAmount,
    int portfolioId, LocalDate date);
}
