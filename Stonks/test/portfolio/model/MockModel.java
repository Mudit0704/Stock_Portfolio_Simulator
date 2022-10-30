package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class MockModel implements IPortfolios {
  StringBuilder log;
  public MockModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public String getPortfolioComposition(int portfolioId) {

    return null;
  }

  @Override
  public Double getPortfolioValue(LocalDate date, int portfolioId) {
    log.append(date).append(portfolioId);
    return 2d;
  }

  @Override
  public boolean savePortfolios() throws IllegalArgumentException {
    return false;
  }

  @Override
  public boolean retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException {
    return false;
  }

  @Override
  public void createNewPortfolio(Map<String, Integer> stocks) {
    log.append(stocks + " ");
  }

  @Override
  public String getAvailablePortfolios() {
    return null;
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    log.append(tickerSymbol + " ");
    return "TICKER_SYMBOL".equals(tickerSymbol) || " TICKER_SYMBOL2".equals(tickerSymbol);
  }
}
