import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AlphaVantageDemo {
  static Map<Date, Double> dateSet = new HashMap();

  public static void main(String []args) throws ParseException {
    //the API key needed to use this web service.
    //Please get your own free API key here: https://www.alphavantage.co/
    //Please look at documentation here: https://www.alphavantage.co/documentation/
    String apiKey = "3DEFVVFI8JLKXZCN";
    String stockSymbol = "GOOG"; //ticker symbol for Google
    URL url = null;

    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
        + ".co/query?function=TIME_SERIES_DAILY"
        + "&outputsize=full"
        + "&symbol"
        + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
        + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      StringBuilder sb = new StringBuilder();
      for (int ch; (ch = in.read()) != -1; ) {
        sb.append((char) ch);
      }
      String ds = sb.toString();
      String[] resultArr = ds.split("\n");
      int i=0;
      for(String str:resultArr){
        if(i==0) {
          i++;
          continue;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String[] arr = str.split(",");

        try {
  //          System.out.println(arr[0] + " " + formatter.parse(arr[0]));
          dateSet.put(formatter.parse(arr[0]), Double.valueOf(arr[4]));
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    try {
      System.out.println(dateSet.get(formatter.parse("2022-10-21")));
      System.out.println(getClosestDate(formatter.parse("2022-10-02")) + " " + dateSet.get(formatter.parse("2022-10-02")));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private static Date getClosestDate(Date date) {
    long minDiff = Long.MAX_VALUE;
    Date result = null;

    for (Map.Entry<Date, Double> mapElement : dateSet.entrySet()) {
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
