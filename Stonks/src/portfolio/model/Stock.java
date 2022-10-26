package portfolio.model;

import java.time.LocalDate;

public class Stock implements IStock {

  private final String tickerSymbol;

  public Stock(String tickerSymbol) throws IllegalArgumentException {
    if(tickerSymbol == null) {
      throw new IllegalArgumentException();
    }
    this.tickerSymbol = tickerSymbol;
  }

  @Override
  public double getValue(LocalDate date) {
    return StockService.getPriceByDate(this.tickerSymbol, date);
  }

  @Override
  public String getStockTicker() {
    return this.tickerSymbol;
  }
}