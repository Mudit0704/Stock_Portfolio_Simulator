package model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * This class implements the InvestmentStrategies interface. It has implementations for calculating
 * the timestamps between a range of dates when the investments are supposed to take place.
 */

public class InvestmentStrategiesImpl implements InvestmentStrategies {

  LocalDate dateStart;
  LocalDate dateEnd;

  /**
   * Constructor to initialise the start and end date of the investment plan.
   *
   * @param dateStart start date of the investment plan.
   * @param dateEnd   end date of the investment plan.
   */
  public InvestmentStrategiesImpl(LocalDate dateStart, LocalDate dateEnd) {
    this.dateStart = dateStart;
    // if end date is null, take today's date as end date
    this.dateEnd = Objects.requireNonNullElseGet(dateEnd, LocalDate::now);
  }

  /**
   * Constructs a list of valid dates on which the investments are going to take place.
   *
   * @param intervals  interval after which the user wants to invest.
   * @param validDates list of valid dates available from the api.
   * @return a list with market open dates on which the investments are going to happen.
   */
  @Override
  public ArrayList<LocalDate> getTimestamps(int intervals, ArrayList<LocalDate> validDates) {

    ArrayList<LocalDate> workingTimestamps = new ArrayList<>();   //Working date of the new cycle

    Date date1 = java.sql.Date.valueOf(this.dateStart);
    Date date2 = java.sql.Date.valueOf(this.dateEnd);

    Calendar beginCalendar = Calendar.getInstance();
    Calendar finishCalendar = Calendar.getInstance();
    beginCalendar.setTime(date1);
    finishCalendar.setTime(date2);

    LocalDate currentTimeStamp = this.dateStart;

    while (beginCalendar.before(finishCalendar)) {
      currentTimeStamp = getWorkingTimestamp(currentTimeStamp, validDates);   // If market is
      // closed on the auto-buying date, get the next working date
      workingTimestamps.add(currentTimeStamp);
      beginCalendar.add(Calendar.DATE, intervals);
      currentTimeStamp = beginCalendar.getTime().toInstant()
              .atZone(ZoneId.systemDefault()).toLocalDate();    // Date to LocalDate
    }
    return workingTimestamps;
  }

  /**
   * Takes a date as a parameter and checks whether the market is open on that date. If not it will
   * return the next market open date on which the investment will take place.
   *
   * @param currentTimeStamp the current date.
   * @param validDates       list of valid dates available from the api.
   * @return the next market open date if market is closed on the given date.
   */
  @Override
  public LocalDate getWorkingTimestamp(LocalDate currentTimeStamp, ArrayList<LocalDate>
          validDates) {
    if (validDates.contains(currentTimeStamp)) {
      return currentTimeStamp;
    } else {
      for (int i = 0; i < validDates.size(); i++) {
        if (currentTimeStamp.compareTo(validDates.get(i)) > 0) {
          return validDates.get(i - 1);     // ValidDates are in descending order. Therefore,
          // return next working date.
        }
      }
    }
    return null;
  }
}
