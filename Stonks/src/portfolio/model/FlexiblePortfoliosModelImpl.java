package portfolio.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlexiblePortfoliosModelImpl extends AbstractPortfolioModel {

  public FlexiblePortfoliosModelImpl() {
    apiOptimizer = StockCache.getInstance();
    portfolioMap = new LinkedHashMap<>();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioMap.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.addStocksToPortfolio(stock, quantity);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioMap.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
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

  protected AbstractPortfolio createPortfolio(Map<IStock, Long> stockQty) {
    return new FlexiblePortfolioImpl(this.stockService, stockQty);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}