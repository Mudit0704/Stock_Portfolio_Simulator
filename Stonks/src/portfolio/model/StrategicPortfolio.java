package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicPortfolio extends FlexiblePortfolio implements IStrategicPortfolio {

  protected IStrategy<Map<IStock, Double>> strategy;
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee
   * @param date           date on which this portfolio is created.
   */
  public StrategicPortfolio(IStockService stockService, Map<IStock, Double> stocks,
    double transactionFee, LocalDate date) {
    super(stockService, stocks, transactionFee, date);
  }

  @Override
  public void createFractionalStrategicPortfolio(Map<IStock, Double> stockProportions,
    Double totalAmount, LocalDate date, double transactionFee) {

  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
    Double totalAmount, LocalDate date, double transactionFee) {

    strategy = new DollarCostAvgStrategy.StrategicPortfolioBuilder()
        .setTotalAmount(totalAmount)
        .setStrategyStartDate(date)
        .build();

    Map<IStock, Double> updatedFractionalQty = strategy.getPortfolioStocksPerStrategy(stockProportions);
    for(Map.Entry<IStock, Double> stockQty: updatedFractionalQty.entrySet()) {
      super.addStocksToPortfolio(stockQty.getKey(), stockQty.getValue(), date, transactionFee);
    }
  }
}

// Abstract the validation method in the FlexiblePortfolio class and implement the method appropriately for this class
// remove the createFractionalStrategicPortfolio() from this class
// use only the investStocksIntoStrategicPortfolio() method from the upper layer
