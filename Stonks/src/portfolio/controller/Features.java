package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface Features {
  void createFlexiblePortfolio(Map<String, Double> stocks, LocalDate date);

  void sellPortfolioStocks(String tickerSymbol, Double quantity, int portfolioId,
      LocalDate date);

  void buyPortfolioStocks(String tickerSymbol, Double quantity, int portfolioId,
      LocalDate date);

  void getCostBasis(LocalDate date, int portfolioId);

  void getPortfolioValue(LocalDate date, int portfolioId);

  void savePortfolio() throws ParserConfigurationException;

  void retrievePortfolio() throws IOException, ParserConfigurationException, SAXException;

  void specificInvestmentOnAGivenDate(Map<String, Double> stockProportions, Double totalAmount,
      int portfolioId, LocalDate date);

  void createPortfolioUsingStrategy(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate startDate, LocalDate endDate);

}
