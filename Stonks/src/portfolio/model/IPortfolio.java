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
   */
  double getPortfolioValue(LocalDate date);

  /**
   * Saves the portfolio information into a file.
   *
   * @param path the location where portfolio information is to be stored.
   * @throws IllegalArgumentException if the given file path is invalid.
   */
  boolean savePortfolio(String path) throws IllegalArgumentException;

  /**
   * Retrieves this portfolio's information from a saved file.
   *
   * @param path the path from where this portfolio's information is to be retrieved.
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;

  /**
   * Builds the portfolio based on the stocks and their quantities.
   *
   * @param stocks mapping of stock and quantity to be stored in the portfolio.
   */
  void setPortfolioStocks(Map<IStock, Integer> stocks);
}
