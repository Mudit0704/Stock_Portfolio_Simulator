package portfolio.model;

/**
 * Represents an abstract strategy builder class and handles the common methods across all the
 * different kinds of strategies.
 *
 * @param <T> represents the concrete type of StrategyBuilder.
 */
public abstract class StrategyBuilder<T extends StrategyBuilder<T>> {

  /**
   * Sets the total amount to be invested in the portfolio.
   *
   * @param totalAmount the total amount to be invested in for the strategy.
   * @return the builder type.
   */
  protected abstract T setTotalAmount(Double totalAmount);

  /**
   * Builds the Strategic portfolio based on the inputs provided.
   *
   * @return the Strategic portfolio created with the builder so far.
   */
  protected abstract IStrategy build();

  /**
   * Returns the concrete type StrategyBuilder.
   *
   * @return the concrete type StrategyBuilder.
   */
  protected abstract T returnBuilder();
}
