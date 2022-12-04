package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This interface consists of methods which will handle the dates of investment for any type of
 * investment strategy as and when required. It will return the valid timestamps of when the
 * investments should take place. Ex. in Dollar Cost Averaging, timestamps will be according to
 * the interval entered by the user. Similarly, for some other strategy, the same interface can
 * be implemented and the methods can be designed to return timestamps according to the new
 * requirement.
 */
public interface InvestmentStrategies {

  /**
   * returns an arraylist of valid timestamps for dollar cost averaging. Valid timestamps are the
   * timestamps after the interval (n number of days).
   *
   * @param intervalInDays interval after which the user wants to invest.
   * @param validDates list of valid dates available from the api.
   *
   * @return returns an arraylist with the timestamps on which the scheduled investment should
   *     take place.
   */
  ArrayList<LocalDate> getTimestamps(int intervalInDays, ArrayList<LocalDate> validDates);

  /**
   * Returns the next working date if market is closed on a given date.
   * Example if user wants to on 1st Jan 2020 and if the market is closed, investment will
   * take place on the 2nd Jan 2020 and so on.
   *
   * @param currentTimeStamp the current date.
   * @param validDates list of valid dates available from the api.
   *
   * @return the same or the closest next date on which market is open.
   */
  LocalDate getWorkingTimestamp(LocalDate currentTimeStamp, ArrayList<LocalDate> validDates);
}
