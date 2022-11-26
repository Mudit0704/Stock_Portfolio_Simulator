package portfolio.model;

import java.time.LocalDate;

public interface IDateNavigator {
  /**
   * Gets the cached object for the specified key.
   *
   * @param date the date to determine if stocks were traded on that day.
   * @return the date next to specified date which is eligible for trading.
   */
  LocalDate getNextAvailableDate(LocalDate date);
}
