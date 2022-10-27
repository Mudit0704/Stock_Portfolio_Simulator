package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This interface represents a collection of portfolios and the set of operations to create
 * and persist them.
 */
public interface IPortfolios {

  /**
   *
   * @return the composition of all the portfolios.
   */
  String getPortfolioComposition();

  /**
   * Determines the values of the specified portfolio at any given date.
   *
   * @param date date at which the value of the specified portfolio is to be fetched.
   * @param portfolioId the id of the portfolio specified.
   * @return the value of the specified portfolio based.
   */
  String getPortfolioValue(LocalDate date, int portfolioId);

  /**
   * Saves all portfolios' information into specified file path.
   *
   * @param path the path at which portfolio files are to be stored.
   * @return true if the operation is successful, false otherwise.
   * @throws IllegalArgumentException in case specified file path is not found.
   */
  boolean savePortfolio(String path) throws IllegalArgumentException;

  /**
   * Retrieves the portfolio information at the specified location.
   *
   * @param path the location from where portfolios are to be retrieved.
   * @return true if the file retrieval is successful, false otherwise.
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;

  /**
   * Sets the specified stock information as part of portfolios.
   *
   * @param stocks the specified mapping of stocks and their quantity to be part of this portfolio.
   */
  void setPortfolioStocks(Map<String, Integer> stocks);
}
