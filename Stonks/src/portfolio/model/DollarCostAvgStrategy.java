package portfolio.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DollarCostAvgStrategy implements IStrategy {

  private final LocalDate startDate;
  private final LocalDate endDate;
  protected final Double totalAmount;
  protected int timeFrame;
  protected DollarCostAvgStrategy(LocalDate startDate, LocalDate endDate, Double totalAmount,
      int timeFrame) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalAmount = totalAmount;
    this.timeFrame = timeFrame;
  }

  @Override
  public Map<LocalDate, Map<IStock, Double>> applyStrategy(Map<IStock, Double> stockQtyRatio) {
    Map<LocalDate, Map<IStock, Double>> stockQtyBasedOnStrategy = new LinkedHashMap<>();
    LocalDate tempDate = startDate;

    while(tempDate.isBefore(endDate) && !tempDate.isAfter(LocalDate.now())) {
      Map<IStock, Double> stockQtyMap = new LinkedHashMap<>();

      for(Map.Entry<IStock, Double> stockQty:stockQtyRatio.entrySet()) {
        Double proportion = totalAmount * stockQty.getValue() / 100.0;
        //TODO: Find a cleaner way to do this...
        Double qty = proportion / stockQty.getKey().getValue(tempDate);
        stockQtyMap.put(stockQty.getKey(), qty);
      }
      stockQtyBasedOnStrategy.put(tempDate, stockQtyMap);
      tempDate = tempDate.plusDays(this.timeFrame);
    }

    while(tempDate.isBefore(endDate)) {
      stockQtyBasedOnStrategy.put(tempDate, null);
      tempDate = tempDate.plusDays(this.timeFrame);
    }

    return stockQtyBasedOnStrategy;
  }

  @Override
  public double getStrategyInvestment() {
    return this.totalAmount;
  }

  public static class DollarCostAvgStrategyBuilder extends StrategyBuilder<DollarCostAvgStrategyBuilder> {
    private int strategyTimeFrame;
    private LocalDate strategyEndDate = LocalDate.of(2100, 12,31);
    private LocalDate strategyStartDate;
    private Double totalAmount;

    protected DollarCostAvgStrategyBuilder setStrategyTimeFrame(int timeFrame) {
      this.strategyTimeFrame = timeFrame;
      return returnBuilder();
    }

    protected DollarCostAvgStrategyBuilder setStrategyStartDate(LocalDate startDate) {
      this.strategyStartDate = startDate;
      return returnBuilder();
    }

    protected DollarCostAvgStrategyBuilder setStrategyEndDate(LocalDate endDate) {
      this.strategyEndDate = endDate;
      return returnBuilder();
    }

    @Override
    protected DollarCostAvgStrategyBuilder setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
      return returnBuilder();
    }

    @Override
    protected IStrategy build() {
      return new DollarCostAvgStrategy(this.strategyStartDate, this.strategyEndDate,
          this.totalAmount, this.strategyTimeFrame);
    }

    @Override
    protected DollarCostAvgStrategyBuilder returnBuilder() {
      return this;
    }
  }
}
