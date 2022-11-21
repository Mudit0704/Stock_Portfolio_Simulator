package portfolio.model;

import java.time.LocalDate;

public abstract class AbstractStrategicPortfolioBuilder {

  protected abstract AbstractStrategicPortfolioBuilder setStrategyStartDate(LocalDate startDate);

  protected abstract AbstractStrategicPortfolioBuilder setTotalAmount(Double totalAmount);
  /**
   * Builds the Strategic portfolio based on the inputs provided.
   *
   * @return the Strategic portfolio created with the builder so far.
   */
  protected abstract IStrategicPortfolio build();
}
