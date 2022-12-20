package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * This class has the implementation to switch between various data sources (CSV or API).
 */
public class DataSourceAPI implements DataSource {

  private final String tickerSymbol;
  private final LocalDate date;

  public DataSourceAPI(String tickerSymbol, LocalDate date) {
    this.tickerSymbol = tickerSymbol;
    this.date = date;
  }

  @Override
  public double stockPriceOnParticularDate() {
    try {
      URL oracle = new URL(
              "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize="
                  + "full&symbol=" + this.tickerSymbol + "&apikey=W0M1JOKC82EZEQA8&datatype=csv");
      BufferedReader in = new BufferedReader(
              new InputStreamReader(oracle.openStream()));
      String line;
      while ((line = in.readLine()) != null) {
        String[] str = line.split(",");
        String key = str[0];
        if (Objects.equals(key, "timestamp")) {
          continue;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFromCSV = LocalDate.parse(key, formatter);

        double value = Double.parseDouble(str[4]);

        if (this.date.compareTo(dateFromCSV) == 0) {
          return value;
        } else if (this.date.compareTo(dateFromCSV) > 0) {
          return -1;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public HashMap<LocalDate, Double> allStockPricesFromAPI(ArrayList<LocalDate> timestampsList) {
    HashMap<LocalDate, Double> stockDetailsFromAPI = new LinkedHashMap<>();
    try {
      URL oracle = new URL(
              "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize="
                      + "full&symbol=" + this.tickerSymbol
                      + "&apikey=W0M1JOKC82EZEQA8&datatype=csv");
      BufferedReader in = new BufferedReader(
              new InputStreamReader(oracle.openStream()));
      String line;
      while ((line = in.readLine()) != null) {
        String[] str = line.split(",");
        String key = str[0];
        if (Objects.equals(key, "timestamp")) {
          continue;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFromCSV = LocalDate.parse(key, formatter);
        double value = Double.parseDouble(str[4]);

        if (timestampsList.contains(dateFromCSV)) {
          stockDetailsFromAPI.put(dateFromCSV, value);
        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stockDetailsFromAPI;
  }

}
