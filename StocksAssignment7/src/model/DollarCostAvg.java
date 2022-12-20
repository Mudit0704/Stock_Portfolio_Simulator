package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class has methods which will calculate the amount of money to be invested in a particular
 * stock depending on the percentage of money the user has chosen to invest their money into.
 */
public class DollarCostAvg {

  /**
   * This method takes the investment amount and a hashmap which has
   * {ticker symbol - investment percentages} as key-value pair.
   * These percentages are converted to the equivalent prices and returns a new hashmap which has
   * {ticker symbol - price} as key-value pairs.
   *
   * <p>Ex. Invt amt = $2000
   * {AAPL : 60, TSLA : 40} will be converted to {AAPL : 1200, AMZN : 800}
   *
   * @param mapForTsPerc a hashmap with TS-Percentage as key-value pair
   * @param invtAmt      the total amount user wants to invest
   * @return a hashmap with TS-Amount as key-value pair
   */
  public HashMap<String, Double> getValueForPerc(HashMap<String, Double> mapForTsPerc,
                                                 double invtAmt) {

    HashMap<String, Double> mapForTsAmt = new LinkedHashMap<>();
    // {TS : Investment amount calculated from percentage}
    double amount = 0.0;
    for (Map.Entry<String, Double> set : mapForTsPerc.entrySet()) {   //Calculate Total
      amount = invtAmt * (set.getValue() / 100);
      mapForTsAmt.put(set.getKey(), amount);
    }
    return mapForTsAmt;
  }

  /**
   * Calculates the price of the particular stock on a range of dates.
   *
   * @param tickerSymbol the ticker symbol for which the user wants to invest their money.
   * @param timestampsList the timestamps on which users money will be invested.
   * @return a hashmap with date and price as key-value pair.
   */
  public HashMap<LocalDate, Double> getPriceOnTimestamps(String tickerSymbol,
                                                         ArrayList<LocalDate> timestampsList) {
    DataSourceAPI getPrice = new DataSourceAPI(tickerSymbol, null);
    return (getPrice.allStockPricesFromAPI(timestampsList));
  }

}
