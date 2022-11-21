package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicPortfolioImpl extends FlexiblePortfolio implements IStrategicPortfolio {

  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee
   * @param date           date on which this portfolio is created.
   */
  public StrategicPortfolioImpl(IStockService stockService, Map<IStock, Long> stocks,
    double transactionFee, LocalDate date) {
    super(stockService, stocks, transactionFee, date);
  }

  @Override
  public void createPortfolioWithFractionalShares(Map<String, Double> stockProportions,
    Double totalAmount, int portfolioId, LocalDate date) {

  }

  public static class StrategicPortfolio extends AbstractStrategicPortfolioBuilder {

    @Override
    protected AbstractStrategicPortfolioBuilder setStrategyStartDate() {
      return null;
    }

    @Override
    protected AbstractStrategicPortfolioBuilder setTotalAmount() {
      return null;
    }

    @Override
    protected IStrategicPortfolio build() {
      return null;
    }
  }
}
