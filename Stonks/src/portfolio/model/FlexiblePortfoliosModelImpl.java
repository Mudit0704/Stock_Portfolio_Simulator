package portfolio.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlexiblePortfoliosModelImpl extends AbstractPortfolioModel {

  private double transactionFee;

  public FlexiblePortfoliosModelImpl() {
    apiOptimizer = StockCache.getInstance();
    portfolioMap = new LinkedHashMap<>();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId, LocalDate date) {
    if (portfolioId < 0 || portfolioId > portfolioMap.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.addStocksToPortfolio(stock, quantity, date, transactionFee);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId, LocalDate date) {
    if (portfolioId < 0 || portfolioId > portfolioMap.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.sellStocksFromPortfolio(stock, quantity, date, transactionFee);
  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    //TODO add validations here.
    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    return portfolio.getPortfolioCostBasisByDate(date);
  }

  @Override
  public String getPortfolioPerformance(int portfolioId, LocalDate rangeStart, LocalDate rangeEnd) {
    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    return portfolio.getPortfolioPerformance(rangeStart, rangeEnd);
  }

  @Override
  public String getPortfolioCompositionOnADate(int portfolioId, LocalDate date) {
//    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
//    return portfolio.getPortfolioPerformance(rangeStart, rangeEnd);
//    return null;
//

    if (portfolioId > portfolioMap.size() || portfolioId <= 0) {
      throw new IllegalArgumentException();
    }

    StringBuilder composition = new StringBuilder();
    composition.append("Portfolio").append(portfolioId).append("\n")
      .append(getPortfolioFromMap(portfolioId).getValue().getPortfolioCompositionOnADate(date)).append("\n");
    return composition.toString();

  }

  @Override
  public void setCommissionFee(double commissionFee) {
    this.transactionFee = commissionFee;
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Long> stockQty) {
    return new FlexiblePortfolioImpl(this.stockService, stockQty, this.transactionFee);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}