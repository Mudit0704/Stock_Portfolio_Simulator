package portfolio.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public abstract class AbstractPortfolio implements IFlexiblePortfolio {
  protected Map<IStock, Long> stockQuantityMap;
  IStockService stockService;
  protected LocalDate creationDate;
  protected IStockAPIOptimizer apiOptimizer;

  AbstractPortfolio(IStockService stockService, Map<IStock, Long> stocks) {
    this.stockService = stockService;
    this.stockQuantityMap = stocks;
    this.creationDate = LocalDate.now();
    apiOptimizer = StockCache.getInstance();
  }

}