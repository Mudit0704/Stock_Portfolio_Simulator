package portfolio.model;

import java.time.LocalDate;

public class Stock extends AbstractStock {

  public Stock(String tickerSymbol, int numShares) {
    super(tickerSymbol, numShares);
  }

  @Override
  public double getValue(LocalDate date) {
    return this.getPriceByDate(date);
  }
}
