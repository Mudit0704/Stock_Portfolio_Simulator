package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This interface represents a single portfolio of stocks. It is possible to build a portfolio,
 * store and retrieve its information and composition.
 */
interface IPortfolio {

  /**
   * Get a portfolio's composition.
   *
   * @return this portfolio's composition.
   */
  String getPortfolioComposition();

  /**
   * Returns the total value of this portfolio on the specified date.
   *
   * @param date the date on which the total value of the portfolio is to be determined.
   * @return the portfolio's total value on the specified date.
   * @throws IllegalArgumentException if the given date is invalid.
   */
  double getPortfolioValue(LocalDate date) throws IllegalArgumentException;

  /**
   * Saves the portfolio information into a file.
   *
   * @param path the location where portfolio information is to be stored.
   * @throws RuntimeException             if an error occurs while saving.
   * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the
   *                                      configuration requested.
   */
  void savePortfolio(String path) throws IllegalArgumentException, ParserConfigurationException;

  /**
   * Retrieves this portfolio's information from a saved file.
   *
   * @param path the path from where this portfolio's information is to be retrieved.
   * @throws IOException                  if I/O error occurs.
   * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the
   *                                      configuration requested.
   * @throws SAXException                 If any parse errors occur.
   */
  void retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;

  /**
   * Builds the portfolio based on the stocks and their quantities.
   *
   * @param stocks mapping of stock and quantity to be stored in the portfolio.
   */
  void setPortfolioStocks(Map<IStock, Long> stocks);
}
