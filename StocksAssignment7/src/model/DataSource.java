package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface is responsible for fetching the stock prices from a data source (API) on a date.
 */
public interface DataSource {
  double stockPriceOnParticularDate();

  HashMap<LocalDate, Double> allStockPricesFromAPI(ArrayList<LocalDate> timestampsList);
}
