package portfolio.model;

public abstract class StrategyBuilder<T extends StrategyBuilder<T>> {

  protected abstract T setTotalAmount(Double totalAmount);
  /**
   * Builds the Strategic portfolio based on the inputs provided.
   *
   * @return the Strategic portfolio created with the builder so far.
   */
  protected abstract IStrategy build();

  protected abstract T returnBuilder();
}
