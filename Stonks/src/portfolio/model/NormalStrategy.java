package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a normal strategy to invest specific stocks on a given specific date. This
 * class implements the IStrategy interface.
 */
public class NormalStrategy implements IStrategy {

  protected final double totalAmount;
  protected final LocalDate date;
  protected IDateNavigator dateNavigator;

  protected NormalStrategy(Double totalAmount, LocalDate date) {
    this.totalAmount = totalAmount;
    this.date = date;
    dateNavigator = DateNavigator.getInstance();
  }

  @Override
  public Map<LocalDate, Map<IStock, Double>> applyStrategy(Map<IStock, Double> stockQtyRatio) {
    Map<LocalDate, Map<IStock, Double>> stockQtyBasedOnStrategy = new HashMap<>();
    LocalDate tempDate = date;
    tempDate = dateNavigator.getNextAvailableDate(tempDate);

    Map<IStock, Double> stockQtyMap = new HashMap<>();

    if (tempDate.isAfter(LocalDate.now())) {
      stockQtyBasedOnStrategy.put(tempDate, null);
      return stockQtyBasedOnStrategy;
    }

    for (Map.Entry<IStock, Double> stockQty : stockQtyRatio.entrySet()) {
      Double proportion = totalAmount * stockQty.getValue() / 100.0;
      Double qty = proportion / stockQty.getKey().getValue(this.date);
      stockQtyMap.put(stockQty.getKey(), qty);
    }
    stockQtyBasedOnStrategy.put(tempDate, stockQtyMap);

    return stockQtyBasedOnStrategy;
  }

  @Override
  public double getStrategyInvestment() {
    return this.totalAmount;
  }

  /**
   * This class is a builder class for the NormalStrategy type instances. It extends the
   * StrategyBuilder abstract class. This class offers methods to collect the date for normal,
   * transaction execution, the total amount, and build the NormalStrategy type instances.
   */
  public static class NormalStrategyBuilder extends StrategyBuilder<NormalStrategyBuilder> {

    protected LocalDate date;
    protected Double totalAmount;

    protected NormalStrategyBuilder setDate(LocalDate date) {
      this.date = date;
      return returnBuilder();
    }

    @Override
    protected NormalStrategyBuilder setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
      return returnBuilder();
    }

    @Override
    protected IStrategy build() {
      return new NormalStrategy(totalAmount, date);
    }

    @Override
    protected NormalStrategyBuilder returnBuilder() {
      return this;
    }
  }
}
