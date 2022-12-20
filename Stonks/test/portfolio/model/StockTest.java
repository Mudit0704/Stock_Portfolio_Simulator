package portfolio.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.Test;

/**
 * A JUnit test class to test the Stock class.
 */
public class StockTest {

  @Test
  public void getValueOnNormalTradingDate() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testExtensiveData.txt"));
    assertEquals(104.93, stock.getValue(LocalDate.of(2022,10,25)), 0.0);
  }

  @Test
  public void getValueOnNonTradingDay() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testExtensiveData.txt"));
    assertEquals(101.48, stock.getValue(LocalDate.of(2022,10,22)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueOnInvalidDate() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testExtensiveData.txt"));
    assertEquals(101.48, stock.getValue(LocalDate.of(2024,10,22)), 0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getValueBeyondAvailableDates() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testExtensiveData.txt"));
    stock.getValue(LocalDate.of(2010,10,22));
  }

  @Test
  public void getStockTicker() {
    IStock stock = new Stock("GOOG", new MockStockService("/test/testExtensiveData.txt"));
    assertEquals("GOOG", stock.getStockTicker());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerEmpty() {
    new Stock("", new MockStockService("/test/testExtensiveData.txt"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getStockTickerInvalid() {
    String obj = null;
    new Stock(obj, new MockStockService("/test/testExtensiveData.txt"));
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