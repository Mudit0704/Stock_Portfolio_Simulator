package portfolio.model;

/**
 * This interface represents a caching layer to avoid making additional API calls.
 */
public interface IStockAPIOptimizer {

  /**
   * Gets the cached object for the specified key.
   *
   * @param tickerSymbol the key against which the object is to be fetched.
   * @return the Stock object for the specified ticker symbol.
   */
  IStock cacheGetObj(String tickerSymbol);

  /**
   * Sets the cached object for the specified key.
   *
   * @param tickerSymbol the key whose stock object is to be stored.
   * @param stockObj the stock object to be stored.
   */
  void cacheSetObj(String tickerSymbol, IStock stockObj);
}
