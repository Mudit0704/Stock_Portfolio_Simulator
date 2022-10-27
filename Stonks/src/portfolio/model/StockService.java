package portfolio.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class StockService implements IAPIStockService {

  private static final String APIKEY = "W0M1JOKC82EZEQA8";
  private URL stockServiceURL = null;

  @Override
  public Map getStockPrices(String tickerSymbol) {
    String queryString = "https://www.alphavantage"
      + ".co/query?function=TIME_SERIES_DAILY"
      + "&outputsize=full"
      + "&symbol"
      + "=" + tickerSymbol + "&apikey=" + APIKEY + "&datatype=csv";
    InputStream in ;

    try {
      in = this.queryAPI(queryString);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + tickerSymbol);
    }
    try {
      return populateStockData(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map populateStockData(InputStream in) throws IOException {
    StringBuilder dataRead = new StringBuilder();
    Map<LocalDate, Double> dateClosingPriceMap = new HashMap<>();

    for (int ch; (ch = in.read()) != -1; ) {
      dataRead.append((char) ch);
    }
    String ds = dataRead.toString();
    String[] resultArr = ds.split("\n");

    int i=0;
    for(String str:resultArr){
      if(i==0) {
        i++;
        continue;
      }
      String[] arr = str.split(",");
      dateClosingPriceMap.put(LocalDate.parse(arr[0]), Double.valueOf(arr[4]));
    }
    return dateClosingPriceMap;
  }

  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    try {
      this.stockServiceURL = new URL(queryString);
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
        + "no longer works");
    }
    InputStream in = stockServiceURL.openStream();
    return in;
  }
}