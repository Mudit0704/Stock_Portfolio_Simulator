package portfolio.model;

import java.net.URL;

public class Stock extends AbstractStock {

  public Stock(String tickerSymbol, int numShares) {
    super(tickerSymbol, numShares);
  }

  @Override
  public double getValue() {

    return 0;
  }
}
