package portfolio.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Portfolio implements IPortfolio{
  private final List<AbstractStock> stocks = new ArrayList<>();

  public Portfolio setPortfolioStocks(Map<String, Integer> stocks) {
    for(Map.Entry<String, Integer> stock : stocks.entrySet()) {
      this.stocks.add(new Stock(stock.getKey(), stock.getValue()));
    }

    return this;
  }

  @Override
  public String getPortfolioComposition() {
    return null;
  }

  @Override
  public double getPortfolioValue(Date date) {
    return 0;
  }

  @Override
  public void savePortfolio(String path) throws IllegalArgumentException {

  }

  @Override
  public void retrievePortfolio(String path) throws FileNotFoundException {

  }
}
