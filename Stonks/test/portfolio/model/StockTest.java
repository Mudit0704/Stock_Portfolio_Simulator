package portfolio.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import org.junit.Test;

public class StockTest {

  @Test
  public void getValue() throws NoSuchFieldException, IllegalAccessException {
    IStock stock = new Stock("GOOG", new MockStockService());
    Class obj = stock.getClass();
    Field field = obj.getDeclaredField("stockService");
    field.setAccessible(true);

    field.set(stock,new MockStockService());
    System.out.println(stock.getValue(LocalDate.now()));
  }

  @Test
  public void getStockTicker() {
    IStock stock = new Stock("GOOG", new MockStockService());
    assertEquals("GOOG", stock.getStockTicker());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerEmpty() {
    IStock stock = new Stock("", new MockStockService());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerInvalid() {
    String obj = null;
    IStock stock = new Stock(obj, new MockStockService());
  }
}