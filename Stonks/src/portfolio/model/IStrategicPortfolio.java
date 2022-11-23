package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IStrategicPortfolio extends IFlexiblePortfolio {
  /**
   * @param stockProportions
   * @param transactionFee
   */
  void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee);
}
