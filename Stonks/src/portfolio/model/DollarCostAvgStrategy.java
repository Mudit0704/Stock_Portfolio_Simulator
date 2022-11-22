package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DollarCostAvgStrategy implements IStrategy<Map<IStock, Double>> {

  protected LocalDate startDate;
  protected LocalDate endDate;
  protected Double totalAmount;
  protected DollarCostAvgStrategy(LocalDate startDate, LocalDate endDate, Double totalAmount) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalAmount = totalAmount;
  }

  @Override
  public Map getPortfolioStocksPerStrategy(Map<IStock, Double> stockQtyRatio) {
    Map<IStock, Double> stockQtyBasedOnStrategy = new HashMap<>();

    for(Map.Entry<IStock, Double> stockQty:stockQtyRatio.entrySet()) {
      Double proportion = totalAmount * stockQty.getValue() / 100.0;
      Double qty = proportion / stockQty.getKey().getValue(this.startDate);
      stockQtyBasedOnStrategy.put(stockQty.getKey(), qty);
    }

    return stockQtyBasedOnStrategy;
  }

  public static class StrategicPortfolioBuilder extends StrategyBuilder<StrategicPortfolioBuilder> {
    protected LocalDate strategyStartDate;
    protected LocalDate strategyEndDate;
    protected Double totalAmount;

    @Override
    protected StrategicPortfolioBuilder setStrategyStartDate(LocalDate startDate) {
      this.strategyStartDate = startDate;
      return returnBuilder();
    }

    @Override
    protected StrategicPortfolioBuilder setStrategyEndDate(LocalDate startDate) {
      this.strategyStartDate = startDate;
      return returnBuilder();
    }

    @Override
    protected StrategicPortfolioBuilder setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
      return returnBuilder();
    }

    @Override
    protected IStrategy<Map<IStock, Double>> build() {
      return new DollarCostAvgStrategy(this.strategyStartDate, this.strategyEndDate,
          this.totalAmount);
    }

    @Override
    protected StrategicPortfolioBuilder returnBuilder() {
      return this;
    }
  }
}
