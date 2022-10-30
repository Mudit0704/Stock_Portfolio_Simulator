package portfolio.model;

import java.util.HashMap;
import java.util.Map;

public class APICache implements IStockAPIOptimizer {

  final Map<String, IStock> stockMap ;

  private APICache() {
    stockMap = new HashMap<>();
  }

  static IStockAPIOptimizer apiOptimizer;

  static IStockAPIOptimizer getInstance() {
    if(apiOptimizer == null) {
      apiOptimizer = new APICache();
    }
    return apiOptimizer;
  }

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
}
