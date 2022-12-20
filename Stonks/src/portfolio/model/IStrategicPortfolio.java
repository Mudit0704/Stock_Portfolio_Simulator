package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a Strategic portfolio and offers methods to invest into strategic portfolio.
 */
public interface IStrategicPortfolio extends IFlexiblePortfolio {

  /**
   * Invests specified stocks as per their quantities.
   *
   * @param stockProportions the stock quantities to be invested as per a strategy applied.
   * @param transactionFee   the transaction fee to be applied if any.
   */
  void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee);

  /**
   * Returns the data points for visualizing the performance of this portfolio within the specified
   * dates.
   *
   * @param start the start date from which the data points are to be collected.
   * @param end   the end date upto which the data points are to be collected.
   * @return the portfolio performance data points from the specified start and end dates.
   */
  Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end);
}
