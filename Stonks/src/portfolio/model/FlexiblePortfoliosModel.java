package portfolio.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents the entry point of the Model of this enhanced application. This extends the
 * {@link AbstractPortfolioModel} interface and represents a list of Portfolios and the applicable
 * operations on them.
 */
public class FlexiblePortfoliosModel extends AbstractPortfolioModel {

  /**
   * Constructs an object of {@link FlexiblePortfoliosModel} and initializes its members.
   */
  public FlexiblePortfoliosModel() {
    super();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Double quantity,
      int portfolioId, LocalDate date) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Invalid quantity");
    }

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
  public void sellStockFromPortfolio(String tickerSymbol, Double quantity, int portfolioId,
      LocalDate date) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Invalid quantity");
    }

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
    if (portfolioId > portfolioMap.size() || portfolioId <= 0) {
      throw new IllegalArgumentException("Invalid portfolioId");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    return portfolio.getPortfolioCostBasisByDate(date);
  }

  @Override
  public String getPortfolioPerformance(int portfolioId, LocalDate rangeStart, LocalDate rangeEnd) {
    if (portfolioId > portfolioMap.size() || portfolioId <= 0) {
      throw new IllegalArgumentException("Invalid portfolioId");
    }

    IFlexiblePortfolio portfolio = getPortfolioFromMap(portfolioId).getValue();
    return ("\nPerformance of Portfolio" + portfolioId + " from " + rangeStart + " to " + rangeEnd
        + "\n").concat(portfolio.getPortfolioPerformance(rangeStart, rangeEnd));
  }

  @Override
  public String getPortfolioCompositionOnADate(int portfolioId, LocalDate date) {
    if (portfolioId > portfolioMap.size() || portfolioId <= 0) {
      throw new IllegalArgumentException("Invalid portfolioId");
    }

    StringBuilder composition = new StringBuilder();
    composition.append("Portfolio").append(portfolioId).append("\n")
        .append(getPortfolioFromMap(portfolioId).getValue().getPortfolioCompositionOnADate(date))
        .append("\n");
    return composition.toString();

  }

  @Override
  public void setCommissionFee(double commissionFee) {
    this.transactionFee = commissionFee;
  }

  @Override
  public double getCommissionFee() {
    return this.transactionFee;
  }

  protected AbstractPortfolio createPortfolio(Map<IStock, Double> stockQty, LocalDate date) {
    return new FlexiblePortfolio(this.stockService, stockQty, this.transactionFee, date);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}