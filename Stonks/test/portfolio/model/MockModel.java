package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class MockModel implements IPortfolios {

  @Override
  public String getPortfolioComposition(String portfolioId) {
    return null;
  }

  @Override
  public String getPortfolioValue(LocalDate date, int portfolioId) {
    return null;
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

  }

  @Override
  public String getAvailablePortfolios() {
    return null;
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    return false;
  }
}
