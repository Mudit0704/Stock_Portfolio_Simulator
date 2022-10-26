package portfolio.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class StockService implements IAPIStockService {

  private static final String APIKEY = "W0M1JOKC82EZEQA8";
  private URL stockServiceURL = null;
  private static Map<LocalDate, Double> dateClosingPriceMap = new HashMap();

  @Override
  public InputStream getStockPrices(String tickerSymbol) {
    InputStream in = null;
    String queryString = "https://www.alphavantage"
      + ".co/query?function=TIME_SERIES_DAILY"
      + "&outputsize=full"
      + "&symbol"
      + "=" + tickerSymbol + "&apikey=" + APIKEY + "&datatype=csv";

    try {
      in = this.queryAPI(queryString);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + tickerSymbol);
    }
    return in;
   }

  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    try {
      this.stockServiceURL = new URL(queryString);
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
        + "no longer works");
    }
    return stockServiceURL.openStream();
  }
}