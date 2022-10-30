package portfolio.model;

/**
 * This interface represents a caching layer to avoid making additional API calls.
 */
public interface IStockAPIOptimizer {
  IStock cacheGetObj(String tickerSymbol);
  void cacheSetObj(String tickerSymbol, IStock stockObj);
}
