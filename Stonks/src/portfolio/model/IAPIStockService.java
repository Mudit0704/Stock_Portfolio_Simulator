package portfolio.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the operations for any service making API calls.
 */
public interface IAPIStockService extends IAPIService {

  InputStream getStockPrices(String tickerSymbol);
}