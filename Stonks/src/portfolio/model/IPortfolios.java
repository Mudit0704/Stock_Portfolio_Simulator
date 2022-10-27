package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface IPortfolios {

  public String getPortfolioComposition();

  public String getPortfolioValue(LocalDate date, int portfolioId);

  public boolean savePortfolio(String path) throws IllegalArgumentException;

  public boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;

  public void setPortfolioStocks(Map<String, Integer> stocks);
}
