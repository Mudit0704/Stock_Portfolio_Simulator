package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PortfoliosTest {
  @Test
  public void testGetPortfolioValue() {
    IPortfolios portfolios = new MockPortfolios(new MockStockService("/test/testData.txt"));

    Map<String, Integer> map = new HashMap<>();
    map.put("GOOG", 3);
    map.put("PUBM", 2);
    map.put("MSFT", 1);

    portfolios.createNewPortfolio(map);

    System.out.println(portfolios.getPortfolioValue(LocalDate.of(2022,10,28), 1));
  }

  @Test
  public void testGetPortfolioValueForEmptyPortfolio() {

  }

  @Test
  public void testGetPortfolioValueForInvalidPortfolioId() {

  }

  @Test
  public void testGetPortfolioValueForInvalidDate() {

  }

  @Test
  public void testGetPortfolioValueForNullDate() {

  }

  @Test
  public void testSavePortfolio() {

  }

  @Test
  public void testSavePortfolioInvalidPath() {

  }

  @Test
  public void testRetrievePortfolio() {

  }

  @Test
  public void testRetrievePortfolioInvalidPath() {

  }

  @Test
  public void testRetrievePortfolioParsingError() {

  }

  @Test
  public void testRetrievePortfolioFileNotFoundError() {

  }

  @Test
  public void testSetStocksInPortfolio() {

  }

  @Test
  public void testSetStocksInPortfolioZeroStocks() {

  }
}