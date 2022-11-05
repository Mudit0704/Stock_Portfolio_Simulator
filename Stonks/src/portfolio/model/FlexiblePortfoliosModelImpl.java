package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class FlexiblePortfoliosModelImpl extends PortfoliosModel
    implements IFlexiblePortfoliosModel {

  @Override
  public void addStocksToPortfolio(Map<String, Long> stocks, int portfolioId) {

  }

  @Override
  public void sellStockFromPortfolio(Map<String, Long> stocks, int portfolioId) {

  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    return 0;
  }
}
