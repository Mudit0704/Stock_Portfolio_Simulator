package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This class represents a dummy model to be used for testing flexible controller.
 */
public class MockFlexiblePortfolioModel implements IFlexiblePortfoliosModel {

  StringBuilder log;
  boolean dataPresent;

  /**
   * Constructor for this dummy model class which takes in logging params to use for testing
   * flexible controller.
   *
   * @param log to log the string input so far by the controller to the model.
   * @param dataPresent to represent if the data is input.
   */
  public MockFlexiblePortfolioModel(StringBuilder log, boolean dataPresent) {
    this.log = log;
    this.dataPresent = dataPresent;
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId,
      LocalDate date) {
    log.append(tickerSymbol).append("_").append(quantity).append("_").append(portfolioId)
        .append("_").append(date);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId,
      LocalDate date) throws IllegalArgumentException {
    log.append(tickerSymbol).append("_").append(quantity).append("_").append(portfolioId)
        .append("_").append(date);
  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    log.append(date).append("_").append(portfolioId);
    return 1.0;
  }

  @Override
  public String getPortfolioPerformance(int portfolioId, LocalDate rangeStart, LocalDate rangeEnd) {
    log.append(portfolioId).append("_").append(rangeStart).append("_").append(rangeEnd);
    return "Performance";
  }

  @Override
  public void setServiceType(ServiceType serviceType) {
    log.append(serviceType);
  }

  @Override
  public void setCommissionFee(double commissionFee) {
    log.append(commissionFee);
  }

  @Override
  public String getPortfolioComposition(int portfolioId) {
    log.append(portfolioId).append(" ");
    if (!dataPresent) {
      throw new IllegalArgumentException();
    }
    if (portfolioId <= 0) {
      throw new IllegalArgumentException();
    }
    return "Composition ";
  }

  @Override
  public Double getPortfolioValue(LocalDate date, int portfolioId) {
    if (!dataPresent) {
      throw new IllegalArgumentException();
    }
    log.append(date).append(" ").append(portfolioId);
    return 2d;
  }

  @Override
  public void savePortfolios() throws RuntimeException {
    if (!dataPresent) {
      throw new RuntimeException("No portfolios to save\n");
    }
  }

  @Override
  public void retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException {
    if (dataPresent) {
      throw new RuntimeException("Portfolios already populated\n");
    }
  }

  @Override
  public void createNewPortfolio(Map<String, Long> stocks) {
    log.append(stocks).append(" ");
  }

  @Override
  public String getAvailablePortfolios() {
    if (!dataPresent) {
      throw new IllegalArgumentException("No portfolios");
    }
    log.append("Available_Portfolios ");
    return "Available_Portfolios ";
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    log.append(tickerSymbol).append(" ");
    return "TICKER_SYMBOL".equals(tickerSymbol) || " TICKER_SYMBOL2".equals(tickerSymbol);
  }
}
