package portfolio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StrategicFlexiblePortfoliosModel extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  private IStrategy strategy;

  @Override
  public void setStrategy(StrategyType strategy, LocalDate startDate,
      LocalDate endDate, int timeFrame, double investmentAmount) {
    startDate = super.getNextTransactionDate(startDate);
    this.strategy = AbstractStrategyCreator.strategyCreator(strategy,
          startDate, endDate, timeFrame, investmentAmount);
  }

  @Override
  public void createStrategicPortfolio(Map<String, Double> stockProportions, LocalDate date) {
    Map<IStock, Double> stockQty = getStockQuantitiesFromTickerSymbol(stockProportions);

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockQty);

    Map<IStock, Double> stocks = updatedFractionalQty.get(date);
    if(stocks == null) {
        stocks = new HashMap<>();
    }
    AbstractPortfolio portfolio = createPortfolio(stocks, date);

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate: updatedFractionalQty.entrySet()) {
      LocalDate dateEntry = stocksOnDate.getKey();
      if(dateEntry.isEqual(date)) {
        continue;
      }
      dateEntry = super.getNextTransactionDate(dateEntry);
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      portfolio.investStocksIntoStrategicPortfolio(proportions, dateEntry, transactionFee);
    }

    portfolioMap.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
      portfolio);
  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, int portfolioId) {

    Map<IStock, Double> stockQty = getStockQuantitiesFromTickerSymbol(stockProportions);

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockQty);

    AbstractPortfolio portfolio = super.getPortfolioFromMap(portfolioId).getValue();

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate: updatedFractionalQty.entrySet()) {
      LocalDate date = stocksOnDate.getKey();
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      portfolio.investStocksIntoStrategicPortfolio(proportions, date, transactionFee);
    }
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Double> stockQty, LocalDate date) {
    return new StrategicPortfolio(stockService, stockQty, this.transactionFee, date);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}