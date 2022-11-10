package portfolio.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the functionality of a singular stock and the operations related to it.
 */
class Stock implements IStock {

  //region Class Members
  private final String tickerSymbol;
  private final IStockService stockService;

  private Map<LocalDate, Double> dateClosingPriceMap;
  //endregion

  /**
   * Construct an object of Stock and initializes its members.
   *
   * @param tickerSymbol tickerSymbol of the stock.
   * @param stockService stock service linked with the stock for populating its data.
   * @throws IllegalArgumentException if tickerSymbol is null or empty.
   */
  public Stock(String tickerSymbol, IStockService stockService) throws IllegalArgumentException {
    if (tickerSymbol == null || tickerSymbol.length() == 0) {
      throw new IllegalArgumentException();
    }
    this.stockService = stockService;
    this.tickerSymbol = tickerSymbol;
    this.dateClosingPriceMap = new HashMap<>();
  }

  //region Public Methods
  @Override
  public double getValue(LocalDate date) {
    if (dateClosingPriceMap.size() == 0) {
      try {
        dateClosingPriceMap = stockService.getStockPrices(this.tickerSymbol);
      }
      catch (DateTimeParseException e) {
        throw new RuntimeException("API Failure...\n");
      }
    }

    if (dateClosingPriceMap.containsKey(date)) { // TODO fix this shit
      return dateClosingPriceMap.get(date);
    } else {
      LocalDate latestDate = getClosestDate(date);

      if (date.isAfter(LocalDate.now()) || latestDate == null) {
        throw new IllegalArgumentException("Cannot find stock price for the given date.");
      } else {
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
  //endregion
}