package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicPortfolio extends FlexiblePortfolio implements IStrategicPortfolio {

  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee
   * @param date           date on which this portfolio is created.
   */
  protected StrategicPortfolio(IStockService stockService, Map<IStock, Double> stocks,
    double transactionFee, LocalDate date) {
    super(stockService, stocks, transactionFee, date);
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee) {

    for(Map.Entry<IStock, Double> proportion:stockProportions.entrySet()) {
      super.addStocksToPortfolio(proportion.getKey(), proportion.getValue(), date, transactionFee);
    }
  }

  @Override
  protected boolean isTransactionSequenceInvalid(IStock stock, LocalDate date) {
    return false;
  }
}