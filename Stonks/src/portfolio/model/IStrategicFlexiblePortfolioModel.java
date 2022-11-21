package portfolio.model;

import java.time.LocalDate;

public interface IStrategicFlexiblePortfolioModel extends IFlexiblePortfoliosModel {
  void createStrategicPortfolio();

  void addFractionalStocksToPortfolio(String tickerSymbol, Double quantity,
      int portfolioId, LocalDate date);
}
