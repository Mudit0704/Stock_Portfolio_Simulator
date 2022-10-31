package portfolio.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This class represents the entry point of the Model of this application. This implements the
 * IPortfolios interface and represents a list of Portfolios and the applicable operations on them
 * such as save, retrieve, get value on specific date, get their composition.
 */
public class Portfolios implements IPortfolios {

  private final List<IPortfolio> portfolios = new ArrayList<>();
  private final IStockService stockService;
  private final IStockAPIOptimizer apiOptimizer;

  public Portfolios() {
    this(new StockService());
  }

  Portfolios(IStockService stockService) {
    this.stockService = stockService;
    apiOptimizer = StockCache.getInstance();
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
  public Double getPortfolioValue(LocalDate date, int portfolioId) throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now()) || portfolioId > portfolios.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }
    return portfolios.get(portfolioId - 1).getPortfolioValue(date);
  }

  @Override
  public void savePortfolios() throws RuntimeException, ParserConfigurationException {
    if (portfolios.size() == 0) {
      throw new RuntimeException("No portfolios to save\n");
    }
    int portfolioNo = 0;
    String userDirectory = System.getProperty("user.dir");
    for (IPortfolio portfolio : portfolios) {
      portfolioNo++;
      portfolio.savePortfolio(userDirectory + "/portfolio" + portfolioNo + ".xml");
    }
  }

  @Override
  public void retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException {
    if (portfolios.size() > 0) {
      throw new RuntimeException("Portfolios already populated\n");
    }

    String userDirectory = System.getProperty("user.dir") + "/";
    File dir = new File(userDirectory);
    File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));

    if (files != null && files.length != 0) {
      for (File file : files) {
        IPortfolio portfolio = new Portfolio(stockService);
        portfolio.retrievePortfolio(userDirectory + file.getName());
        portfolios.add(portfolio);
      }
    } else {
      throw new FileNotFoundException("No portfolios found to retrieve");
    }
  }

  @Override
  public void createNewPortfolio(Map<String, Long> stocks) {
    if (stocks.size() == 0) {
      return;
    }
    IPortfolio portfolio = new Portfolio(stockService);

    Map<IStock, Long> stockList = new HashMap<>();

    for (Map.Entry<String, Long> entry : stocks.entrySet()) {
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
  public String getAvailablePortfolios() throws RuntimeException {
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