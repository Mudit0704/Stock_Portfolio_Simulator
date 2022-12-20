package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class makes call to the Alpha-vantage API and retrieves valid dates
 * (market open and closed dates).
 */
public class APICaller {

  /**
   * This method calls the Alpha vantage and returns the valid dates (market opened).
   *
   * @return market open dates (ArrayList) in LocalDate format (yyyy-MM-dd)
   */
  public ArrayList<LocalDate> callAPI() {
    ArrayList<LocalDate> validDates = new ArrayList<>();
    try {
      URL oracle = new URL(
              "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize="
                      + "full&symbol=AAPL&apikey=W0M1JOKC82EZEQA8&datatype=csv");
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
        validDates.add(dateFromCSV);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return validDates;
  }
}
