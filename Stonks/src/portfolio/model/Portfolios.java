package portfolio.model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Portfolios implements IPortfolios {
  private final List<IPortfolio> portfolios = new ArrayList<>();
  private IStockService stockService;

  @Override
  public String getPortfolioComposition(int portfolioId) {
    if(portfolioId > portfolios.size()) {
      return "Invalid portfolioId\n";
    }
    int portfolioNo = 0;
    StringBuilder composition = new StringBuilder("No portfolios\n");
    if (portfolios.size() > 0) {
      composition = new StringBuilder();
      portfolioNo++;
      composition.append("Portfolio").append(portfolioNo).append("\n")
          .append(portfolios.get(portfolioId-1).getPortfolioComposition()).append("\n");
    }
    return composition.toString();
  }

  @Override
  public String getPortfolioValue(LocalDate date, int portfolioId) {
    StringBuilder portfolioValues = new StringBuilder("No Portfolios\n");
    if(portfolioId > portfolios.size()) {
      return "Invalid portfolioId\n";
    }
    if (portfolios.size() > 0) {
      portfolioValues = new StringBuilder();
      portfolioValues.append("Portfolio").append(portfolioId).append("\n")
          .append(portfolios.get(portfolioId-1).getPortfolioValue(date)).append("\n");
    }
    return portfolioValues.toString();
  }

  @Override
  public boolean savePortfolio(String path) throws IllegalArgumentException {
    if(portfolios.size() == 0) {
      return false;
    }
    int portfolioNo = 0;
    for(IPortfolio portfolio : portfolios) {
      portfolioNo++;
      portfolio.savePortfolio(path+"portfolio"+portfolioNo+".xml");
    }

    return true;
  }

  @Override
  public boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException {
    boolean result = false;

    File dir = new File(path);
    File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));

    if (files != null && files.length != 0) {
      for (File file : files) {
        IPortfolio portfolio = new Portfolio(stockService);
        portfolio.retrievePortfolio(path + file.getName());
        portfolios.add(portfolio);
      }
      result = true;
    }

    return result;
  }

  @Override
  public void createNewPortfolio(Map<String, Integer> stocks) {
    IPortfolio portfolio = new Portfolio(stockService);
    portfolio.setPortfolioStocks(stocks);
    portfolios.add(portfolio);
  }

  @Override
  public String getAvailablePortfolios() {
    int portfolioNo = 0;
    StringBuilder composition = new StringBuilder("No portfolios\n");
    if (portfolios.size() > 0) {
      composition = new StringBuilder();
      while(portfolioNo < portfolios.size()) {
        composition.append("Portfolio").append(portfolioNo+1).append("\n");
        portfolioNo++;
      }
    }
    return composition.toString();
  }

  @Override
  public void setPortfolioServiceType(ServiceType serviceType) {
    if (serviceType == ServiceType.STOCK) {
      this.stockService = new StockService();
    }
  }

  public boolean isTickerSymbolValid(String tickerSymbol) {
    return this.stockService.getValidStockSymbols().contains(tickerSymbol);
  }


}