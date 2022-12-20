package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * This class has the implementations to print the performance of a portfolio over a range of time.
 */
public class Graph {

  static String date;
  private static ArrayList<String> intervals;
  LocalDate dateStart;
  LocalDate dateEnd;

  /**
   * Constructs a Graph that plots the stock prices based on a certain date range.
   */
  public Graph(LocalDate dateStart, LocalDate dateEnd) {
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;

    intervals = new ArrayList<>();
    date = "";
  }

  /**
   * This implementation generates intervals for plotting the LineChartView.
   *
   * @return Array list of time ranges
   */
  public ArrayList<String> generateTimestamps() {

    LocalDate endDate = this.dateStart;
    LocalDate startDate = this.dateEnd;

    int daysBetween = (int) ChronoUnit.DAYS.between(endDate, startDate);
    int monthsBetween = (int) ChronoUnit.MONTHS.between(endDate, startDate);
    int yearsBetween = (int) ChronoUnit.YEARS.between(endDate, startDate);

    Date date1 = java.sql.Date.valueOf(endDate);
    Date date2 = java.sql.Date.valueOf(startDate);

    Calendar beginCalendar = Calendar.getInstance();
    Calendar finishCalendar = Calendar.getInstance();
    beginCalendar.setTime(date1);
    finishCalendar.setTime(date2);

    if (yearsBetween >= 5) {    // Years interval
      intervals = interval(beginCalendar, finishCalendar, yearsBetween, "YEAR");
    } else if (monthsBetween > 5 && monthsBetween <= 59) {    // Months interval
      intervals = interval(beginCalendar, finishCalendar, monthsBetween, "MONTH");
    } else if (daysBetween >= 0 && daysBetween <= 152) {     // Days interval
      intervals = interval(beginCalendar, finishCalendar, daysBetween, "DATE");
    }

    return intervals;
  }

  /**
   * This implementation generates the last dates of intervals for plotting the LineChartView.
   *
   * @return Array list of time ranges
   */
  public HashMap<String, LocalDate> timestampLastDate(ArrayList<String> timestampList,
                                                      LocalDate endDate,
                                                      ArrayList<LocalDate> validDatesInAPI) {
    HashMap<String, LocalDate> timestampsWithDates = new LinkedHashMap<>();
    String dateFullFormat;
    for (String d : timestampList) {
      if (d.length() == 4) {
        dateFullFormat = d + "-12-31";
      } else if (d.length() == 8) {
        String strStandardFormat = "";
        SimpleDateFormat standardFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        try {
          Date date = sdf.parse(d);
          strStandardFormat = standardFormat.format(date);
        } catch (Exception e) {
          //
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(strStandardFormat, formatter);
        date = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        dateFullFormat = date.toString();
      } else {
        dateFullFormat = d;
      }
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate dtFullFormat = LocalDate.parse(dateFullFormat, formatter);

      dtFullFormat = getLastWorkingDate(dtFullFormat, validDatesInAPI);

      if (d.equals(timestampList.get(timestampList.size() - 1))) {
        dtFullFormat = getLastWorkingDate(endDate, validDatesInAPI);
      }

      timestampsWithDates.put(d, dtFullFormat);
    }
    return timestampsWithDates;
  }

  /**
   * This implementation generates the last dates of intervals for plotting the LineChartView.
   *
   * @return Array list of time ranges
   */
  private ArrayList<String> interval(Calendar beginCalendar, Calendar finishCalendar,
                                     int intervalRange, String intervalType) {

    if (Objects.equals(intervalType, "DATE")) {   // Partition on days
      while (beginCalendar.before(finishCalendar)) {
        DateFormat formatterYd = new SimpleDateFormat("yyyy-MM-dd");
        date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
        intervals.add(date);
        if (intervalRange >= 0 && intervalRange <= 4) {
          System.out.println("Date range too short.");
        } else if (intervalRange >= 5 && intervalRange <= 30) {
          beginCalendar.add(Calendar.DATE, 1);
        } else if (intervalRange >= 31 && intervalRange <= 153) {
          beginCalendar.add(Calendar.DATE, 5);
        }

        if (beginCalendar.equals(finishCalendar)) {
          date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
          intervals.add(date);
        }
      }
    }

    if (Objects.equals(intervalType, "MONTH")) {     // Partition on months
      while (beginCalendar.before(finishCalendar)) {
        DateFormat formatterYd = new SimpleDateFormat("MMM yyyy");
        date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
        intervals.add(date);
        if (intervalRange >= 5 && intervalRange <= 30) {
          beginCalendar.add(Calendar.MONTH, 1);
        } else if (intervalRange >= 31 && intervalRange <= 59) {
          beginCalendar.add(Calendar.MONTH, 2);
        }

        if (beginCalendar.equals(finishCalendar)) {
          date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
          intervals.add(date);
        }
      }
    }

    if (Objects.equals(intervalType, "YEAR")) {    // Partition on years
      while (beginCalendar.before(finishCalendar)) {
        DateFormat formatterYd = new SimpleDateFormat("yyyy");
        date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
        intervals.add(date);
        if (intervalRange >= 5 && intervalRange <= 30) {
          beginCalendar.add(Calendar.YEAR, 1);
        } else if (intervalRange > 30) {
          int intervalSlot = intervalRange / 20;
          intervalSlot = (int) Math.floor(intervalSlot);
          beginCalendar.add(Calendar.YEAR, intervalSlot);
        }

        if (beginCalendar.equals(finishCalendar)) {
          date = formatterYd.format(beginCalendar.getTime()).toUpperCase();
          intervals.add(date);
        }
      }
    }
    return intervals;
  }

  /**
   * This implementation generates the last market open date for the intervals.
   *
   * @return LocalDate last market open date for the interval
   */
  private LocalDate getLastWorkingDate(LocalDate actualLastDate,
                                       ArrayList<LocalDate> validDatesInAPI) {
    if (validDatesInAPI.contains(actualLastDate)) {   // If market is open on last date of month
      return actualLastDate;
    } else {
      for (LocalDate localDate : validDatesInAPI) {
        // If market is closed on last date of month, return the last working date
        if (actualLastDate.compareTo(localDate) > 0) {
          return localDate;
        }
      }
    }
    return actualLastDate;
  }

}
