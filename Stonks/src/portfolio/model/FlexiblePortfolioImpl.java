package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class FlexiblePortfolioImpl extends Portfolio
      implements IFlexiblePortfolio {

  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   */
  public FlexiblePortfolioImpl(IStockService stockService, Map<IStock, Long> stocks) {
    super(stockService, stocks);
  }

  @Override
  public void addStocksToPortfolio(Map<String, Long> stocks) {

  }

  @Override
  public void sellStocksFromPortfolio(Map<String, Long> stocks) throws IllegalArgumentException {

  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    return 0;
  }
}
