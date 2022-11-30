package portfolio.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * Abstract class to store common logic for implementing the operations for a portfolio.
 */
public abstract class AbstractPortfolio implements IStrategicPortfolio {

  protected Map<IStock, Double> stockQuantityMap;
  IStockService stockService;
  protected IStockAPIOptimizer apiOptimizer;
  protected Map<IStock, Map<LocalDate, Double>> stockHistoryQty;
  protected Map<LocalDate, Double> costBasisHistory;

  /**
   * Enum representing the type of transaction being performed.
   */
  protected enum TransactionType {
    SELL,
    BUY
  }

  /**
   * Constructor to initialize the class members.
   *
   * @param stockService service object to fetch stock data from API
   * @param stocks       map containing stocks and their quantities for a portfolio
   */
  AbstractPortfolio(IStockService stockService, Map<IStock, Double> stocks) {
    this.stockService = stockService;
    this.stockQuantityMap = stocks;
    apiOptimizer = StockCache.getInstance();
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee) {
    return;
  }

  @Override
  public Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end) {
    return null;
  }

  protected abstract boolean isTransactionSequenceInvalid(IStock stock, LocalDate date,
      TransactionType transactionType);

  //This function has been moved up in this abstract class, to help facilitate its reuse in other
  // child classes.
  protected LocalDate getClosestDate(LocalDate date, List<LocalDate> qtyHistory) {
    long minDiff = Long.MAX_VALUE;
    LocalDate result = null;

    for (LocalDate currDate : qtyHistory) {
      long diff = ChronoUnit.DAYS.between(currDate, date);

      if (currDate.isBefore(date) && minDiff > diff) {
        minDiff = diff;
        result = currDate;
      }
    }
    return result;
  }

  protected void scheduleInvestment(LocalDate date, double amount, double transactionFee,
      Map<IStock, Double> stocks) {
    return;
  }
}