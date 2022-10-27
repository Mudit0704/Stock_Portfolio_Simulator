package portfolio.model;

import java.util.Map;

/**
 * Represents the operations for any service making API calls.
 */
public interface IAPIStockService extends IAPIService {

  Map getStockPrices(String tickerSymbol);
}