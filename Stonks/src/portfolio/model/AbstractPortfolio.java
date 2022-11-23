package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Abstract class to store common logic for implementing the operations for a portfolio.
 */
public abstract class AbstractPortfolio implements IStrategicPortfolio {

  protected Map<IStock, Double> stockQuantityMap;
  IStockService stockService;
  protected IStockAPIOptimizer apiOptimizer;

  protected enum TransactionType {
    SELL,
    BUY
  }

  /**
   * Constructor to initialize the class members.
   * @param stockService service object to fetch stock data from API
   * @param stocks map containing stocks and their quantities for a portfolio
   */
  AbstractPortfolio(IStockService stockService, Map<IStock, Double> stocks) {
    this.stockService = stockService;
    this.stockQuantityMap = stocks;
    apiOptimizer = StockCache.getInstance();
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee) {

  }

  protected abstract boolean isTransactionSequenceInvalid(IStock stock, LocalDate date,
    TransactionType transactionType);
}