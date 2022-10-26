package portfolio.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

class StockService {

  private static final String APIKEY = "W0M1JOKC82EZEQA8";
  private static URL url = null;
  private static Map<LocalDate, Double> dateClosingPriceMap = new HashMap();

  static double getPriceByDate(String tickerSymbol, LocalDate date) {
    try {
      url = new URL("https://www.alphavantage"
        + ".co/query?function=TIME_SERIES_DAILY"
        + "&outputsize=full"
        + "&symbol"
        + "=" + tickerSymbol + "&apikey=" + APIKEY + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
        + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      Scanner sc = new Scanner(in).useDelimiter("\n");

      sc.next();
      while (sc.hasNext()) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String result = sc.next();
        String[] arr = result.split(",");

        dateClosingPriceMap.put(LocalDate.parse(arr[0]), Double.valueOf(arr[4]));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + tickerSymbol);
    }
    return (dateClosingPriceMap.containsKey(date)) ? dateClosingPriceMap.get(date)
      : dateClosingPriceMap.get(getClosestDate(date));
  }

  private static LocalDate getClosestDate(LocalDate date) {
    long minDiff = Long.MAX_VALUE;
    LocalDate result = null;

    for (Map.Entry<LocalDate, Double> mapElement : dateClosingPriceMap.entrySet()) {
      LocalDate currDate = mapElement.getKey();
      long diff = ChronoUnit.DAYS.between(currDate, date);

      if (currDate.isBefore(date) && minDiff > diff) {
        minDiff = diff;
        result = currDate;
      }
    }
    return result;
  }
}