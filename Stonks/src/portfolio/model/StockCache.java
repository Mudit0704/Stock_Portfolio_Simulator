package portfolio.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the caching of stock data from API.
 */
class StockCache implements IStockAPIOptimizer {

  final Map<String, IStock> stockMap ;

  private StockCache() {
    stockMap = new HashMap<>();
  }

  static IStockAPIOptimizer apiOptimizer;

  static IStockAPIOptimizer getInstance() {
    if(apiOptimizer == null) {
      apiOptimizer = new StockCache();
    }
    return apiOptimizer;
  }

  //region Public Methods
  @Override
  public IStock cacheGetObj(String tickerSymbol) {
    if(!this.stockMap.containsKey(tickerSymbol)) {
      return null;
    }
    return this.stockMap.get(tickerSymbol);
  }

  @Override
  public void cacheSetObj(String tickerSymbol, IStock stockObj) {
    this.stockMap.put(tickerSymbol, stockObj);
  }
  //endregion
}
