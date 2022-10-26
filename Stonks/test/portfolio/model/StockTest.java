package portfolio.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class StockTest {

  @Test
  public void getValue() {

  }

  @Test
  public void getStockTicker() {
    IStock stock = new Stock("GOOG");
    assertEquals("GOOG", stock.getStockTicker());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerEmpty() {
    IStock stock = new Stock("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerInvalid() {
    String obj = null;
    IStock stock = new Stock(obj);
  }
}