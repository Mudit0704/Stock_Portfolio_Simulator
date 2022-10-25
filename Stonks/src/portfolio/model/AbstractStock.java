package portfolio.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public abstract class AbstractStock implements IStock {

  private static final String APIKEY = "W0M1JOKC82EZEQA8";
  private URL url = null;
  private Map<Date, Double> dateClosingPriceMap = new HashMap();

  protected String tickerSymbol;
  protected int stockQuantity;

  public AbstractStock(String tickerSymbol, int stockQuantity) {
    this.tickerSymbol = tickerSymbol;
    this.stockQuantity = stockQuantity;
  }

  protected double getPriceByDate(Date date) {
    try {
      url = new URL("https://www.alphavantage"
        + ".co/query?function=TIME_SERIES_DAILY"
        + "&outputsize=compact"
        + "&symbol"
        + "=" + this.tickerSymbol + "&apikey=" + APIKEY + "&datatype=json");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
        + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();
    Map<Date, Double> dateSet = new HashMap();

    try {
      in = url.openStream();
      int b;

      Scanner sc = new Scanner(in).useDelimiter("\n");

      sc.next();
      while (sc.hasNext()) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String result = sc.next();
        String[] arr = result.split(",");

        try {
          dateSet.put(formatter.parse(arr[0]), Double.valueOf(arr[4]));
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + this.tickerSymbol);
    }

    return (dateClosingPriceMap.containsKey(date)) ? dateClosingPriceMap.get(date)
      : dateClosingPriceMap.get(getClosestDate(date));
  }

  private Date getClosestDate(Date date) {
    long minDiff = Long.MAX_VALUE;
    Date result = null;

    for (Map.Entry<Date, Double> mapElement : dateClosingPriceMap.entrySet()) {
      Date currDate = mapElement.getKey();
      long timeDiff = Math.abs(date.getTime() - currDate.getTime());
      long diff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

      if (currDate.before(date) && minDiff > diff) {
        minDiff = diff;
        result = currDate;
      }
    }
    return result;
  }
}