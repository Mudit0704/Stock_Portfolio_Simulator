package portfolio.model;

import java.time.LocalDate;

/**
 * Interface to represent a stock unit and get the price of the stock.
 */
interface IStock {

  /**
   * Gets the value of this stock from realtime API.
   *
   * @param date the date at which stock price is to be fetched for.
   * @return current value of this stock.
   */
  double getValue(LocalDate date);

  /**
   * Returns this stock's ticker symbol.
   *
   * @return a string representing the ticker symbol of this stock.
   */
  String getStockTicker();
}