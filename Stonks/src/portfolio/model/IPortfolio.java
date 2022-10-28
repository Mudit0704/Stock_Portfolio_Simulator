package portfolio.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 */
public interface IPortfolio {

  /**
   * @return
   */
  String getPortfolioComposition();

  /**
   * @param date
   * @return
   */
  double getPortfolioValue(LocalDate date);

  /**
   * @param path
   * @throws IllegalArgumentException
   */
  boolean savePortfolio(String path) throws IllegalArgumentException;

  /**
   * @param path
   * @throws FileNotFoundException
   */
  boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;

  void setPortfolioStocks(Map<String, Integer> stocks);

  /**
   * Checks if a ticker symbol is valid, i.e, it is being traded currently.
   * @param tickerSymbol symbol to be checked.
   * @return <code>true</code> if symbol is valid; otherwise <code>false</code>.
   */
  public boolean isTickerSymbolValid(String tickerSymbol);
}
