package portfolio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexiblePortfoliosModelImpl extends PortfoliosModel
    implements IFlexiblePortfoliosModel {

  private List<IFlexiblePortfolio> portfolioList;
  private IStockService stockService;
  private IStockAPIOptimizer apiOptimizer;

  public FlexiblePortfoliosModelImpl() {
    apiOptimizer = StockCache.getInstance();
    portfolioList = new ArrayList<>();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if(portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio ID");
    }

    IFlexiblePortfolio portfolio = portfolioList.get(portfolioId - 1);
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.addStocksToPortfolio(stock, quantity);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if(portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = this.portfolioList.get(portfolioId - 1);
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.sellStocksFromPortfolio(stock, quantity);
  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    return 0;
  }

  @Override
  public void setServiceType(ServiceType serviceType) {
    stockService = AbstractServiceCreator.serviceCreator(serviceType);
  }

  @Override
  public void createNewPortfolio(Map<String, Long> stocks) {
    IFlexiblePortfolio portfolio = new FlexiblePortfolioImpl(stockService, null);
    portfolioList.add(portfolio);

    for (Map.Entry<String, Long> entry : stocks.entrySet()) {
      this.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioList.size() - 1);
    }
  }

  @Override
  public Double getPortfolioValue(LocalDate date, int portfolioId) throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now()) || portfolioId > portfolioList.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }

    return portfolioList.get(portfolioId - 1).getPortfolioValue(date);
  }
}