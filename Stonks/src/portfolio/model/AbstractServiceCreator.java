package portfolio.model;

/**
 * Abstract class handling the creation of service objects.
 */
public abstract class AbstractServiceCreator {

  /**
   * Creates an object of the serviceType passed as parameter.
   *
   * @param serviceType type of service for which the object has to be created
   * @return the requested service type object
   */
  public static IStockService serviceCreator(ServiceType serviceType) {

    if (serviceType == ServiceType.ALPHAVANTAGE) {
      return new StockService();
    } else {
      return null;
    }
  }
}
