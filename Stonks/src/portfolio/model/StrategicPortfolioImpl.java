package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicPortfolioImpl extends FlexiblePortfolio implements IStrategicPortfolio {

  protected LocalDate strategyStartDate;
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

  public static class StrategicPortfolioBuilder extends AbstractStrategicPortfolioBuilder {
    protected LocalDate strategyStartDate ;
    protected Double totalAmount;
    static AbstractStrategicPortfolioBuilder strategicPortfolioBuilder = new StrategicPortfolioBuilder();

    @Override
    protected AbstractStrategicPortfolioBuilder setStrategyStartDate(LocalDate startDate) {
      this.strategyStartDate = startDate;
      return strategicPortfolioBuilder;
    }

    @Override
    protected AbstractStrategicPortfolioBuilder setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
      return strategicPortfolioBuilder;
    }

    @Override
    protected IStrategicPortfolio build() {
      return null;
    }
  }
}
