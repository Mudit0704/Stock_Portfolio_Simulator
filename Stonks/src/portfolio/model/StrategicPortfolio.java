package portfolio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

  private void performCascadingUpdateForRetrospectiveBuy(Map<LocalDate, Double> historicQty, double quantity,
      LocalDate dateAfterPurchaseToUpdate, double costBasisUpdateFactor) {
    for(Map.Entry<LocalDate, Double> qtyOnDate:historicQty.entrySet()) {
      if (qtyOnDate.getKey().isAfter(dateAfterPurchaseToUpdate)) {
        Double qtyToUpdate = qtyOnDate.getValue();
        qtyToUpdate += quantity;
        historicQty.put(qtyOnDate.getKey(), qtyToUpdate);

        costBasisHistory.put(qtyOnDate.getKey(), costBasisHistory.get(qtyOnDate.getKey())
            + costBasisUpdateFactor);
      }
    }
  }

  @Override
  public void addStocksToPortfolio(IStock stock, Double quantity,
    LocalDate date, double transactionFee) {
    double stockQty = 0;

    if (isTransactionSequenceInvalid(stock, date, TransactionType.BUY)) {
      throw new IllegalArgumentException("Date given is not chronological based on previous "
        + "transaction dates\n");
    }

    Map<LocalDate, Double> historicQty = stockHistoryQty.getOrDefault(stock, new LinkedHashMap<>());
    List<LocalDate> listOfDates = new ArrayList<>(historicQty.keySet());
    LocalDate recentTxnDateForCurrent = getClosestDate(date, listOfDates);

    if (recentTxnDateForCurrent != null) {
      stockQty = historicQty.get(recentTxnDateForCurrent);
    }

    double updatedQty = stockQty + quantity;
    updateHistoricHoldings(stock, date, updatedQty);

    stockQuantityMap.put(stock, stockQuantityMap.get(stock) + quantity);

    double prevCostBasis = this.getPortfolioCostBasisByDate(date);
    double updateCostBasisBy = stock.getValue(date) * quantity + transactionFee;
    costBasisHistory.put(date, prevCostBasis + updateCostBasisBy);

    performCascadingUpdateForRetrospectiveBuy(historicQty, quantity, date, updateCostBasisBy);
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
      LocalDate date, double transactionFee) {

    for(Map.Entry<IStock, Double> proportion:stockProportions.entrySet()) {
      addStocksToPortfolio(proportion.getKey(), proportion.getValue(), date, transactionFee);
    }
  }

  @Override
  protected boolean isTransactionSequenceInvalid(IStock stock, LocalDate date,
    TransactionType transactionType) {
    if (transactionType == TransactionType.BUY) {
      return false;
    } else {
      return super.isTransactionSequenceInvalid(stock, date, transactionType);
    }
  }
}