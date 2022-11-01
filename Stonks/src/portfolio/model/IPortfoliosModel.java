package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This interface represents a collection of portfolios and the set of operations to create and
 * persist them.
 */
public interface IPortfoliosModel {

  /**
   * Get the composition of a single portfolio.
   *
   * @param portfolioId id of the portfolio for which composition is required.
   * @return the composition of all the portfolios.
   * @throws IllegalArgumentException if the portfolioId is invalid.
   */
  String getPortfolioComposition(int portfolioId);

  /**
   * Determines the values of the specified portfolio at any given date.
   *
   * @param date        date at which the value of the specified portfolio is to be fetched.
   * @param portfolioId the id of the portfolio specified.
   * @return the value of the specified portfolio based on the given date.
   * @throws IllegalArgumentException if the date or portfolioId is invalid.
   */
  Double getPortfolioValue(LocalDate date, int portfolioId);

  /**
   * Saves all portfolios' information into specified file path.
   *
   * @throws RuntimeException             if an error occurs while saving.
   * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the
   *                                      configuration requested.
   */
  void savePortfolios() throws RuntimeException, ParserConfigurationException;

  /**
   * Retrieves the portfolio information at the specified location.
   *
   * @throws IOException                  if an I/O error occurs.
   * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the
   *                                      configuration requested.
   * @throws SAXException                 If any parse errors occur.
   */
  void retrievePortfolios()
      throws IOException, ParserConfigurationException, SAXException;

  /**
   * Sets the specified stock information as part of portfolios.
   *
   * @param stocks the specified mapping of stocks and their quantity to be part of this portfolio.
   */
  void createNewPortfolio(Map<String, Long> stocks);

  /**
   * Gets all the available portfolios.
   *
   * @return available portfolios represented using a String.
   * @throws RuntimeException if no portfolios found.
   */
  String getAvailablePortfolios() throws RuntimeException;

  /**
   * Checks if a ticker symbol is valid, i.e, it is being traded currently.
   *
   * @param tickerSymbol symbol to be checked.
   * @return <code>true</code> if symbol is valid; otherwise <code>false</code>.
   */
  boolean isTickerSymbolValid(String tickerSymbol);
}
