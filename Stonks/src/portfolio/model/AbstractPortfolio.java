package portfolio.model;

import java.util.Map;

public abstract class AbstractPortfolio implements IFlexiblePortfolio {
  Map<IStock, Long> stockQuantityMap;
  IStockService stockService;

  AbstractPortfolio(IStockService stockService, Map<IStock, Long> stocks) {
    this.stockService = stockService;
    this.stockQuantityMap = stocks;
  }
}
