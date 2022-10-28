package portfolio.model;

public final class MockPortfolios extends Portfolios{
  private final IStockService stockService;

  public MockPortfolios() {
    this.stockService = new MockStockService();
  }
}