package portfolio.model;

import java.time.LocalDate;
import java.util.Date;

public class Stock extends AbstractStock {

  public Stock(String tickerSymbol, int numShares) {
    super(tickerSymbol, numShares);
  }

  @Override
  public double getValue(LocalDate date) {
    return this.getPriceByDate(date);
  }
}
