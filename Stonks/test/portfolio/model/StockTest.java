package portfolio.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.Test;

public class StockTest {

  @Test
  public void getValueOnNormalTradingDate() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testData.txt"));
    assertEquals(104.93, stock.getValue(LocalDate.of(2022,10,25)), 0.0);
  }

  @Test
  public void getValueOnNonTradingDay() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testData.txt"));
    assertEquals(101.48, stock.getValue(LocalDate.of(2022,10,22)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueOnInvalidDate() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testData.txt"));
    assertEquals(101.48, stock.getValue(LocalDate.of(2024,10,22)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueBeyondAvailableDates() {
    IStock stock = new Stock("GOOG", new MockStockService());
    stock.getValue(LocalDate.of(2010,10,22));
  }

  @Test
  public void getStockTicker() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testData.txt"));
    assertEquals("GOOG", stock.getStockTicker());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerEmpty() {
    new Stock("", new MockStockService("/test/testData.txt"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerInvalid() {
    String obj = null;
    new Stock(obj, new MockStockService("/test/testData.txt"));
  }

  @Test
  public void getStockTickerSetCheck() {
    IStockService testSet = new MockStockService("/test/testStockSymbolValidation.txt");
    Set<String> set = testSet.getValidStockSymbols();
    assertTrue(set.contains("PUBM"));
    assertTrue(set.contains("GOOG"));
    assertFalse(set.contains("uwydshjbc"));
  }
}