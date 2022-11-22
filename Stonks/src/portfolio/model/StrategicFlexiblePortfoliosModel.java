package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StrategicFlexiblePortfoliosModel extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
    int portfolioId, IStrategy strategy) {

    Map<IStock, Double> stockQty = new HashMap<>();

    for (Map.Entry<String, Double> entry : stockProportions.entrySet()) {
      IStock stock = new Stock(entry.getKey(), this.stockService);
      apiOptimizer.cacheSetObj(entry.getKey(), stock);
      stockQty.put(stock, entry.getValue());
    }

    AbstractPortfolio portfolio = super.getPortfolioFromMap(portfolioId).getValue();
    portfolio.investStocksIntoStrategicPortfolio(stockQty, totalAmount, strategy, transactionFee);
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Double> stockQty, LocalDate date) {
    return new StrategicPortfolio(this.stockService, stockQty, this.transactionFee, date);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}
