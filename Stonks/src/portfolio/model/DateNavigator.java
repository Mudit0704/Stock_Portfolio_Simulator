package portfolio.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This singleton class implements the IDateNavigator interface. This class offers a single object
 * of its type and facilitates the execution of transaction on next working date.
 */
public class DateNavigator implements IDateNavigator {

  protected final IStockService service;
  private Set<LocalDate> dateSet;

  private DateNavigator(ServiceType serviceType) {
    this.service = AbstractServiceCreator.serviceCreator(serviceType);
    dateSet = new HashSet<>(this.service.getStockPrices("ALK").keySet());
  }

  static IDateNavigator dateNavigator;

  /**
   * Gets the only single instance of the DateNavigator class.
   *
   * @return the IDateNavigator instance.
   */
  public static IDateNavigator getInstance() {
    if (dateNavigator == null) {
      dateNavigator = new DateNavigator(ServiceType.ALPHAVANTAGE);
    }
    return dateNavigator;
  }

  @Override
  public LocalDate getNextAvailableDate(LocalDate date) {
    LocalDate tempDate = date;
    while (!this.dateSet.contains(tempDate) && !tempDate.isAfter(LocalDate.now())) {
      tempDate = tempDate.plusDays(1);
    }
    return tempDate;
  }
}
