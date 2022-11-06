package portfolio.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public abstract class AbstractPortfolio implements IPortfolio {
  protected Map<IStock, Long> stockQuantityMap;
  IStockService stockService;
  protected LocalDate creationDate;


  AbstractPortfolio(IStockService stockService, Map<IStock, Long> stocks) {
    this.stockService = stockService;
    this.stockQuantityMap = stocks;
    this.creationDate = LocalDate.now();
  }

  @Override
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    if(date.isBefore(this.creationDate)) {
      return 0.0;
    }

    double portfolioValue = 0;

    for (Map.Entry<IStock, Long> stock : stockQuantityMap.entrySet()) {
      try {
        portfolioValue += stock.getKey().getValue(date) * stock.getValue();
      } catch (DateTimeParseException e) {
        throw new RuntimeException("API failure...\n");
      }
    }
    return portfolioValue;
  }

  protected abstract AbstractPortfolio createPortfolio(Map<IStock, Long> stockQty);

  public void addStocksToPortfolio(IStock stock, Long quantity) {
    throw new IllegalArgumentException("Cannot add stocks to this portfolio.");
  }

  public void sellStocksFromPortfolio(IStock stock, Long quantity) {
    throw new IllegalArgumentException("Cannot remove stocks from this portfolio.");
  }
}