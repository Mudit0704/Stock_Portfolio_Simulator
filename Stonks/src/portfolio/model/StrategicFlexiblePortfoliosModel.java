package portfolio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class StrategicFlexiblePortfoliosModel extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  private IStrategy strategy;

  @Override
  public void setStrategy(StrategyType strategy, LocalDate startDate,
      LocalDate endDate, int timeFrame, double investmentAmount) {
    this.strategy = AbstractStrategyCreator.strategyCreator(strategy,
          startDate, endDate, timeFrame, investmentAmount);
  }

  @Override
  public void createNewPortfolioOnADate(Map<String, Double> stocks, LocalDate date) {
    Map<IStock, Double> stockQty = getStockQuantitiesFromTickerSymbol(stocks);

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockQty);

    AbstractPortfolio portfolio = createPortfolio(updatedFractionalQty.get(date), date);

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate: updatedFractionalQty.entrySet()) {
      LocalDate dateEntry = stocksOnDate.getKey();
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      portfolio.investStocksIntoStrategicPortfolio(proportions, dateEntry, transactionFee);
    }

    portfolioMap.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
      portfolio);
  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions,
      int portfolioId, IStrategy strategy) {

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