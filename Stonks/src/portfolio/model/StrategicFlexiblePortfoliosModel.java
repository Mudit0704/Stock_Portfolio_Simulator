package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicFlexiblePortfoliosModel extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  @Override
  public void createStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate date, LocalDate endDate) {
    super.createNewPortfolioOnADate(stockProportions, date);
  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
    int portfolioId, LocalDate date) {

    for(Map.Entry<String, Double> stock:stockProportions.entrySet()) {
      super.addStocksToPortfolio(stock.getKey(), stock.getValue(), portfolioId, date);
    }
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Double> stockQty, LocalDate date) {
    return new StrategicPortfolio(this.stockService, stockQty, this.transactionFee, date);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}
