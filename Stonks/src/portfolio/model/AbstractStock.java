package portfolio.model;

import java.net.URL;

public abstract class AbstractStock implements IStock {
  String apiKey = "W0M1JOKC82EZEQA8";
  String stockSymbol = "GOOG"; //ticker symbol for Google
  URL url = null;

  protected String tickerSymbol ;
  protected int stockQuantity ;

  public AbstractStock(String tickerSymbol, int stockQuantity) {
    this.tickerSymbol = tickerSymbol ;
    this.stockQuantity = stockQuantity ;
  }
}
