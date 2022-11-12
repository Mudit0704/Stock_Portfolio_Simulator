package portfolio.model;

import java.util.Map;

public class MockFlexiPortfolio extends FlexiblePortfolioImpl {

  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee
   */
  public MockFlexiPortfolio(IStockService stockService, Map<IStock, Long> stocks,
    double transactionFee) {
    super(stockService, stocks, transactionFee);
  }
}
