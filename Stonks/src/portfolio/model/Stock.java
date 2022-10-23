package portfolio.model;

import java.util.Date;

public class Stock extends AbstractStock {

  public Stock(String tickerSymbol, int numShares) {
    super(tickerSymbol, numShares);
  }

  @Override
  public double getValue(Date date) {
    return this.getPriceByDate(date);
  }
}
