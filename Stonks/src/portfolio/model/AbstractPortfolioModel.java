package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPortfolioModel implements IPortfoliosModel {

  List<AbstractPortfolio> portfolioList;
  IStockService stockService;
  IStockAPIOptimizer apiOptimizer;

  public void setServiceType(ServiceType serviceType) {
    stockService = AbstractServiceCreator.serviceCreator(serviceType);
  }

  @Override
  public Double getPortfolioValue(LocalDate date, int portfolioId) throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now()) || portfolioId > portfolioList.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }

    return portfolioList.get(portfolioId - 1).getPortfolioValue(date);
  }

  @Override
  public String getAvailablePortfolios() throws RuntimeException {
    int portfolioNo = 0;
    StringBuilder composition = new StringBuilder();

    if (portfolioList.size() > 0) {
      while (portfolioNo < portfolioList.size()) {
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

  @Override
  public String getPortfolioComposition(int portfolioId) {
    if (portfolioId > portfolioList.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder composition = new StringBuilder();
    composition.append("Portfolio").append(portfolioId).append("\n")
        .append(portfolioList.get(portfolioId - 1).getPortfolioComposition()).append("\n");
    return composition.toString();
  }
}