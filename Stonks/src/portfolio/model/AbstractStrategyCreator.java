package portfolio.model;

import java.time.LocalDate;

/**
 * Abstract class handling the creation of strategy objects.
 */
public abstract class AbstractStrategyCreator {

  /**
   * Creates an object of the strategyType passed as parameter.
   *
   * @param strategyType     type of strategy for which the object has to be created
   * @param startDate        the start date of any strategy.
   * @param endDate          the end date of any strategy.
   * @param timeFrame        the time frame of any strategy.
   * @param investmentAmount the amount to be invested in the strategy.
   * @return the requested strategy type object.
   */
  public static IStrategy strategyCreator(StrategyType strategyType, LocalDate startDate,
    LocalDate endDate, int timeFrame, double investmentAmount) {

    if (strategyType == StrategyType.DOLLARCOSTAVERAGING) {
      return new DollarCostAvgStrategy.DollarCostAvgStrategyBuilder()
        .setTotalAmount(investmentAmount)
        .setStrategyStartDate(startDate)
        .setStrategyEndDate(endDate)
        .setStrategyTimeFrame(timeFrame)
        .build();
    } else {
      return new NormalStrategy.NormalStrategyBuilder()
        .setTotalAmount(investmentAmount)
        .setDate(startDate)
        .build();
    }
  }
}
