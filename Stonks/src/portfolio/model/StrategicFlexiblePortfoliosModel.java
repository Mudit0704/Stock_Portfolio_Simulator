package portfolio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Strategic portfolio that can be used to apply various time based
 * investment strategies. This class implements the IStrategicFlexiblePortfolioModel interface and
 * is a class adapter over FlexiblePortfoliosModel.
 */
public class StrategicFlexiblePortfoliosModel extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  private IStrategy strategy;

  @Override
  public void setStrategy(StrategyType strategy, LocalDate startDate,
      LocalDate endDate, int timeFrame, double investmentAmount) {
    if(investmentAmount <= 0) {
      throw new IllegalArgumentException("Invalid amount to invest.");
    }
    startDate = dateNavigator.getNextAvailableDate(startDate);
    this.strategy = AbstractStrategyCreator.strategyCreator(strategy,
          startDate, endDate, timeFrame, investmentAmount);
  }

  @Override
  public void createStrategicPortfolio(Map<String, Double> stockProportions, LocalDate date) {
    Map<IStock, Double> stockQty = getStockQuantitiesFromTickerSymbol(stockProportions);

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockQty);

    Map<IStock, Double> stocks = updatedFractionalQty.getOrDefault(date, new HashMap<>());

    AbstractPortfolio portfolio = createPortfolio(stocks, date);

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate:updatedFractionalQty.entrySet()) {
      LocalDate dateEntry = stocksOnDate.getKey();

      if(dateEntry.isEqual(date)) {
        continue;
      }
      if (stocksOnDate.getValue() == null) {
        portfolio.scheduleInvestment(stocksOnDate.getKey(), strategy.getStrategyInvestment(),
            this.transactionFee, stockQty);
        continue;
      }
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      portfolio.investStocksIntoStrategicPortfolio(proportions, dateEntry, transactionFee);
    }

    portfolioMap.put(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")),
      portfolio);
  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, int portfolioId) {

    Map<IStock, Double> stockQty = getStockQuantitiesFromTickerSymbol(stockProportions);

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockQty);

    AbstractPortfolio portfolio = super.getPortfolioFromMap(portfolioId).getValue();

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate: updatedFractionalQty.entrySet()) {
      if (stocksOnDate.getValue() == null) {
        portfolio.scheduleInvestment(stocksOnDate.getKey(), strategy.getStrategyInvestment(),
            this.transactionFee, stockQty);
        continue;
      }
      LocalDate date = stocksOnDate.getKey();
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      portfolio.investStocksIntoStrategicPortfolio(proportions, date, transactionFee);
    }
  }

  @Override
  public Map<LocalDate, Double> lineChartPerformanceAnalysis(LocalDate start, LocalDate end,
      int portfolioId) {
    if(portfolioId > super.portfolioMap.size()) {
      throw new IllegalArgumentException("Invalid portfolio id.");
    }
    AbstractPortfolio portfolio = super.getPortfolioFromMap(portfolioId).getValue();
    return portfolio.lineChartPerformanceAnalysis(start, end);
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Double> stockQty, LocalDate date) {
    return new StrategicPortfolio(stockService, stockQty, this.transactionFee, date);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}