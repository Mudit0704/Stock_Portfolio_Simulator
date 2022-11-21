package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicPortfolio extends FlexiblePortfolio implements IStrategicPortfolio {

  protected IStrategy strategy;
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
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
    Double totalAmount, int portfolioId, LocalDate date) {

  }
}
