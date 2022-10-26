package portfolio.model;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Stock implements IStock {

  private final String tickerSymbol;
  IAPIStockService stockService;

  private Map<LocalDate, Double> dateClosingPriceMap;

  public Stock(String tickerSymbol) throws IllegalArgumentException {
    if(tickerSymbol == null || tickerSymbol.length() == 0) {
      throw new IllegalArgumentException();
    }
    stockService = new StockService();
    this.tickerSymbol = tickerSymbol;
    this.dateClosingPriceMap = new HashMap<>();
  }

  @Override
  public double getValue(LocalDate date) {
    if(dateClosingPriceMap.size() == 0) {
      populateStockData();
    }

    return (dateClosingPriceMap.containsKey(date)) ? dateClosingPriceMap.get(date)
      : dateClosingPriceMap.get(getClosestDate(date));
  }

  private void populateStockData() {
    InputStream in = stockService.getStockPrices(this.tickerSymbol);
    Scanner sc = new Scanner(in).useDelimiter("\n");

    sc.next();

    while (sc.hasNext()) {
      String result = sc.next();
      String[] arr = result.split(",");

      dateClosingPriceMap.put(LocalDate.parse(arr[0]), Double.valueOf(arr[4]));
    }
  }

  @Override
  public String getStockTicker() {
    return this.tickerSymbol;
  }

  private LocalDate getClosestDate(LocalDate date) {
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