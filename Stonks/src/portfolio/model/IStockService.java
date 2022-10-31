package portfolio.model;

import java.util.Map;
import java.util.Set;

/**
 * Represents the operations for any service which makes API calls for getting stock data.
 */
interface IStockService extends IService {

  /**
   * Retrieves a set of all the actively traded stocks on the current date.
   *
   * @return a set of all the actively traded stocks.
   */
  Set<String> getValidStockSymbols();

  /**
   * Populates stock data after calling the API.
   *
   * @param tickerSymbol tickerSymbol of the stock for which value has to be populated
   * @return the historic stock data.
   */
  Map getStockPrices(String tickerSymbol);
}