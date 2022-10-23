package portfolio.model;

import java.util.Date;

/**
 * Interface to represent a stock unit and get the price of the stock.
 */
public interface IStock {

  /**
   * Gets the value of this stock from realtime API.
   *
   * @param date the date at which stock price is to be fetched for.
   * @return current value of this stock.
   */
  double getValue(Date date);
}
