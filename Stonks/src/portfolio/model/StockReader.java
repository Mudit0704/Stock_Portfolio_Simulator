package portfolio.model;

import java.time.LocalDate;

public interface StockReader {
  void readStockData(IStock stock, IStockService stockService);

  LocalDate nextAvailableDate(LocalDate presentDate);
}
