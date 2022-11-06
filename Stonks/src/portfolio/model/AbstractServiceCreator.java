package portfolio.model;

public abstract class AbstractServiceCreator {
  public static IStockService serviceCreator(ServiceType serviceType) {

    switch (serviceType) {
      case ALPHAVANTAGE:
        return new StockService();
      default:
        return null;
    }
  }
}
