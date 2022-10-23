package portfolio.model;

class StockFactory {
  public static AbstractStock buildStock(StockType stockType, String tickerSymbol, int stockQuantity) {

    AbstractStock newStockOption = null ;
    switch (stockType) {
      case STOCK: {
        newStockOption = new Stock(tickerSymbol, stockQuantity);
      }
      default:
        break;
    }
    return newStockOption ;
  }
}
