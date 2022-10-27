package portfolio.model;

class StockFactory {
  public static IStock buildStock(StockType stockType, String tickerSymbol, int stockQuantity) {

    IStock newStockOption = null ;
    switch (stockType) {
      case STOCK: {
        newStockOption = new Stock(tickerSymbol, new StockService());
      }
      default:
        break;
    }
    return newStockOption ;
  }
}
