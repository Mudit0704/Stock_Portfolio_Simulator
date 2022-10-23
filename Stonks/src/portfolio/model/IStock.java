package portfolio.model;

/**
 * Interface to represent a stock unit.
 */
public interface IStock {

  /**
   * Gets the value of this stock from realtime API.
   * @return current value of this stock.
   */
  double getValue();
}
