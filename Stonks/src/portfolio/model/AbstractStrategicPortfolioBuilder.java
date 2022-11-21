package portfolio.model;

public abstract class AbstractStrategicPortfolioBuilder {

  protected abstract AbstractStrategicPortfolioBuilder setStrategyStartDate();

  protected abstract AbstractStrategicPortfolioBuilder setTotalAmount();
  /**
   * Builds the Strategic portfolio based on the inputs provided.
   *
   * @return the Strategic portfolio created with the builder so far.
   */
  protected abstract IStrategicPortfolio build();
}
