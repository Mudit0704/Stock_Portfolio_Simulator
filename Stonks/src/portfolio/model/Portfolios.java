package portfolio.model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Portfolios implements IPortfolios {

  private final List<IPortfolio> portfolios = new ArrayList<>();
  private final IStockService stockService;
  private final IStockAPIOptimizer apiOptimizer;

  public Portfolios() {
    this(new StockService());
  }

  Portfolios(IStockService stockService) {
    this.stockService = stockService;
    apiOptimizer = APICache.getInstance();
  }

  @Override
  public String getPortfolioComposition(int portfolioId) throws IllegalArgumentException {
    if (portfolioId > portfolios.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder composition = new StringBuilder();
    composition.append("Portfolio").append(portfolioId).append("\n")
        .append(portfolios.get(portfolioId - 1).getPortfolioComposition()).append("\n");
    return composition.toString();
  }

  @Override
  public Double getPortfolioValue(LocalDate date, int portfolioId) {
    if (date.isAfter(LocalDate.now()) || portfolioId > portfolios.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }
    return portfolios.get(portfolioId - 1).getPortfolioValue(date);
  }

  @Override
  public boolean savePortfolios() throws IllegalArgumentException {
    if (portfolios.size() == 0) {
      return false;
    }
    int portfolioNo = 0;
    String userDirectory = System.getProperty("user.dir");
    for (IPortfolio portfolio : portfolios) {
      portfolioNo++;
      portfolio.savePortfolio(userDirectory + "/portfolio" + portfolioNo + ".xml");
    }

    return true;
  }

  @Override
  public boolean retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException {
    boolean result = false;
    String userDirectory = System.getProperty("user.dir") + "/";
    File dir = new File(userDirectory);
    File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));

    if (files != null && files.length != 0) {
      for (File file : files) {
        IPortfolio portfolio = new Portfolio(stockService);
        if (!portfolio.retrievePortfolio(userDirectory + file.getName())) {
          return false;
        }
        portfolios.add(portfolio);
      }
      result = true;
    }

    return result;
  }

  @Override
  public void createNewPortfolio(Map<String, Integer> stocks) {
    if (stocks.size() == 0) {
      return;
    }
    IPortfolio portfolio = new Portfolio(stockService);

    Map<IStock, Integer> stockList = new HashMap<>();

    for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
      IStock newStock = apiOptimizer.cacheGetObj(entry.getKey());
      if (newStock == null) {
        newStock = new Stock(entry.getKey(), this.stockService);
        apiOptimizer.cacheSetObj(entry.getKey(), newStock);
      }
      stockList.put(newStock, entry.getValue());
    }

    portfolio.setPortfolioStocks(stockList);
    portfolios.add(portfolio);
  }

  @Override
  public String getAvailablePortfolios() throws IllegalArgumentException {
    int portfolioNo = 0;
    StringBuilder composition = new StringBuilder();

    if (portfolios.size() > 0) {
      while (portfolioNo < portfolios.size()) {
        composition.append("Portfolio").append(portfolioNo + 1).append("\n");
        portfolioNo++;
      }
    } else {
      throw new IllegalArgumentException("No portfolios");
    }
    return composition.toString();
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    return this.stockService.getValidStockSymbols().contains(tickerSymbol);
  }


}