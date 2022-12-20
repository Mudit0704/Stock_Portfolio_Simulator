package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a dummy model to be used for testing GUI controller.
 */
public class MockStrategicPortfolioModel extends MockFlexiblePortfolioModel implements
    IStrategicFlexiblePortfolioModel {

  /**
   * Constructor for this dummy model class which takes in logging params to use for testing
   * flexible controller.
   *
   * @param log         to log the string input so far by the controller to the model.
   * @param dataPresent to represent if the data is input.
   */
  public MockStrategicPortfolioModel(StringBuilder log, boolean dataPresent) {
    super(log, dataPresent);
  }

  @Override
  public void setStrategy(StrategyType strategy, LocalDate startDate, LocalDate endDate,
      int timeFrame, double investmentAmount) {
    log.append(strategy).append(startDate).append(endDate).append(timeFrame)
        .append(investmentAmount);
  }

  @Override
  public void createStrategicPortfolio(Map<String, Double> stockProportions, LocalDate date) {
    log.append(stockProportions).append(date);
  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, int portfolioId) {
    log.append(stockProportions).append(portfolioId);
  }

  @Override
  public Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end,
      int portfolioId) {
    log.append(start).append(end).append(portfolioId);
    return new HashMap<>();
  }
}
