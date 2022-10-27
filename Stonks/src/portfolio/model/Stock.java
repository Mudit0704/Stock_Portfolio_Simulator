package portfolio.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Stock implements IStock {

  private final String tickerSymbol;
  private final IAPIStockService stockService;

  private Map<LocalDate, Double> dateClosingPriceMap;

  public Stock(String tickerSymbol, IAPIStockService stockService) throws IllegalArgumentException {
    if(tickerSymbol == null || tickerSymbol.length() == 0) {
      throw new IllegalArgumentException();
    }
    this.stockService = stockService;
    this.tickerSymbol = tickerSymbol;
    this.dateClosingPriceMap = new HashMap<>();
  }

  @Override
  public double getValue(LocalDate date) {
    if(dateClosingPriceMap.size() == 0) {
      dateClosingPriceMap = stockService.getStockPrices(this.tickerSymbol);
    }

    if (dateClosingPriceMap.containsKey(date)) {
      return dateClosingPriceMap.get(date);
    }
    else {
      LocalDate latestDate = getClosestDate(date);

      if(latestDate == null) {
        throw new IllegalArgumentException("Cannot find stock price for the given date.");
      }
      else {
        return dateClosingPriceMap.get(latestDate);
      }
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