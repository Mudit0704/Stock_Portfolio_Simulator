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

    IFlexiblePortfolio portfolio = portfolioList.get(portfolioId);
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

    IFlexiblePortfolio portfolio = this.portfolioList.get(portfolioId);
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
}
