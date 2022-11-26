package portfolio.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DateNavigator implements IDateNavigator {

  protected final IStockService service;
  private Set<LocalDate> dateSet;

  private DateNavigator(ServiceType serviceType) {
    this.service = AbstractServiceCreator.serviceCreator(serviceType);
    dateSet = new HashSet<>(this.service.getStockPrices("ALK").keySet());
  }

  static IDateNavigator dateNavigator;

  static IDateNavigator getInstance() {
    if(dateNavigator == null) {
      dateNavigator = new DateNavigator(ServiceType.ALPHAVANTAGE);
    }
    return dateNavigator;
  }

  @Override
  public LocalDate getNextAvailableDate(LocalDate date) {
    LocalDate tempDate = date;
    while(!this.dateSet.contains(tempDate) && !tempDate.isAfter(LocalDate.now())) {
      tempDate = tempDate.plusDays(1);
    }
    return tempDate;
  }
}
