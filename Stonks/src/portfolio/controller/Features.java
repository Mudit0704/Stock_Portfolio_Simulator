package portfolio.controller;

import java.time.LocalDate;
import java.util.Map;

public interface Features {
  void createFlexiblePortfolio(Map<String, Double> stocks, String date);

  void sellPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date);

  void buyPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date);

  double getCostBasis(String date, String portfolioId);

  double getPortfolioValue(String date, String portfolioId);

  String savePortfolio();

  String retrievePortfolio();

  String getAvailablePortfolios();

  String getPortfolioComposition(String portfolioId);

  void specificInvestmentOnAGivenDate(Map<String, Double> stockProportions, Double totalAmount,
      int portfolioId, LocalDate date);

  void createPortfolioUsingStrategy(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate startDate, LocalDate endDate);

}
