package portfolio.model;

import java.util.Map;
import java.util.Set;

/**
 * Represents the operations for any service which makes API calls for getting stock data.
 */
public interface IStockService extends IService {

  /**
   * Retrieves a set of all the actively traded stocks on the current date.
   *
   * @return a set of all the actively traded stocks.
   */
  Set<String> getValidStockSymbols();

  Map getStockPrices(String tickerSymbol);
}