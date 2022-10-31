package portfolio.model;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StockCacheTest {
  IStockAPIOptimizer apiOptimizer = StockCache.getInstance();

  @Test
  public void testCache() {
    for(int i=0; i<100000; i++) {
      StringBuilder stock = new StringBuilder("Stock" + i);
      apiOptimizer.cacheSetObj(stock.toString(), new Stock(stock.toString(), new MockStockService("test/testData.txt")));
    }
    for(int i=0; i<100000; i++) {
      StringBuilder stock = new StringBuilder("Stock" + i);
      assertTrue(null != apiOptimizer.cacheGetObj(stock.toString()));
    }
    for(int i=100000; i<200000; i++) {
      StringBuilder stock = new StringBuilder("Stock" + i);
      assertTrue(null == apiOptimizer.cacheGetObj(stock.toString()));
    }
  }
}