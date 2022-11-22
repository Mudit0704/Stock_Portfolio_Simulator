package portfolio.model;

import java.util.Map;

public interface IStrategicPortfolio extends IFlexiblePortfolio {
  /**
   * @param stockProportions
   * @param totalAmount
   * @param transactionFee
   */
  void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      Double totalAmount, IStrategy strategy, double transactionFee);
}
