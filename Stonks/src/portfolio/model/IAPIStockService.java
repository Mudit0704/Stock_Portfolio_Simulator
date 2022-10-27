package portfolio.model;

import java.util.Map;

/**
 * Represents the operations for any service which makes API calls for getting stock data.
 */
public interface IAPIStockService extends IAPIService {

  Map getStockPrices(String tickerSymbol);
}