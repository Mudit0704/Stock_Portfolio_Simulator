package portfolio.controller;

import java.time.LocalDate;
import java.util.Map;

public interface Features {
  String createFlexiblePortfolio(Map<String, Double> stocks, String date);

  String sellPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee);

  String buyPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee);

  double getCostBasis(String date, String portfolioId);

  double getPortfolioValue(String date, String portfolioId);

  String savePortfolio();

  String retrievePortfolio();

  String getAvailablePortfolios();

  String fractionalInvestmentOnAGivenDate(Map<String, Double> stockProportions, String totalAmount,
      String portfolioId, String date);

  String createDollarCostAveragePortfolio(Map<String, Double> stockProportions, String totalAmount,
      String startDate, String endDate, String timeFrame);

  boolean isTickerSymbolValid(String tickerSymbol);

  Map<LocalDate, Double> getPortfolioPerformance(String startDate, String endDate, String portfolioId);
}
