package portfolio.model;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Stock implements IStock {

  private final String tickerSymbol;
  private IAPIStockService stockService;

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
      dateClosingPriceMap = stockService.getStockPrices(this.tickerSymbol);
    }

    return (dateClosingPriceMap.containsKey(date)) ? dateClosingPriceMap.get(date)
      : dateClosingPriceMap.get(getClosestDate(date));
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