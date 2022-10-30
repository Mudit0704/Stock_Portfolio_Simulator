package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This interface represents a collection of portfolios and the set of operations to create and
 * persist them.
 */
public interface IPortfolios {

  /**
   * @return the composition of all the portfolios.
   */
  String getPortfolioComposition(int portfolioId);

  /**
   * Determines the values of the specified portfolio at any given date.
   *
   * @param date        date at which the value of the specified portfolio is to be fetched.
   * @param portfolioId the id of the portfolio specified.
   * @return the value of the specified portfolio based on the given date.
   */
  Double getPortfolioValue(LocalDate date, int portfolioId);

  /**
   * Saves all portfolios' information into specified file path.
   *
   * @return true if the operation is successful, false otherwise.
   * @throws IllegalArgumentException in case specified file path is not found.
   */
  boolean savePortfolios() throws IllegalArgumentException;

  /**
   * Retrieves the portfolio information at the specified location.
   *
   * @return true if the file retrieval is successful, false otherwise.
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  boolean retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException;

  /**
   * Sets the specified stock information as part of portfolios.
   *
   * @param stocks the specified mapping of stocks and their quantity to be part of this portfolio.
   */
  void createNewPortfolio(Map<String, Integer> stocks);

  /**
   * Gets all the available portfolios.
   *
   * @return available portfolios represented using a String.
   */
  String getAvailablePortfolios() throws AttributeNotFoundException;

  /**
   * Checks if a ticker symbol is valid, i.e, it is being traded currently.
   *
   * @param tickerSymbol symbol to be checked.
   * @return <code>true</code> if symbol is valid; otherwise <code>false</code>.
   */
  boolean isTickerSymbolValid(String tickerSymbol);
}
