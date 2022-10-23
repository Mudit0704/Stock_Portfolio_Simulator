package portfolio;

/**
 * Interface to represent a stock unit.
 */
public interface Stock {

  /**
   * Gets the value of this stock from realtime API.
   * @return current value of this stock.
   */
  double getValue();
}
