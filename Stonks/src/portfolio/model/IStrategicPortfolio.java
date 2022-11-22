package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public interface IStrategicPortfolio extends IFlexiblePortfolio {

  void createFractionalStrategicPortfolio(Map<IStock, Double> stockProportions,
      Double totalAmount, LocalDate date, double transactionFee);
  /**
   *
   * @param stockProportions
   * @param totalAmount
   * @param date
   * @param transactionFee
   */
  void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      Double totalAmount, LocalDate date, double transactionFee);
}
