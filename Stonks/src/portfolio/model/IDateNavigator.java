package portfolio.model;

import java.time.LocalDate;

/**
 * This interface facilitates navigating across dates for checking available transaction dates.
 */
public interface IDateNavigator {

  /**
   * Gets the recent transaction date from the specified date. The date can be as recent as the
   * specified date or the very next day from the specified date.
   *
   * @param date the date from which the next transaction date is to be determined.
   * @return the date next to specified date which is eligible for trading.
   */
  LocalDate getNextAvailableDate(LocalDate date);
}
