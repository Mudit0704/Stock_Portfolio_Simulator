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
public interface IPortfolios {

  IPortfolios.PortfoliosBuilder PortfoliosBuilder = new PortfoliosBuilder();

  /**
   * @return the composition of all the portfolios.
   */
  String getPortfolioComposition(String portfolioId);

  /**
   * Determines the values of the specified portfolio at any given date.
   *
   * @param date        date at which the value of the specified portfolio is to be fetched.
   * @param portfolioId the id of the portfolio specified.
   * @return the value of the specified portfolio based.
   */
  String getPortfolioValue(LocalDate date, int portfolioId);

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
  String getAvailablePortfolios();

  /**
   * Checks if a ticker symbol is valid, i.e, it is being traded currently.
   *
   * @param tickerSymbol symbol to be checked.
   * @return <code>true</code> if symbol is valid; otherwise <code>false</code>.
   */
  public boolean isTickerSymbolValid(String tickerSymbol);

  /**
   * Builder class to generalize the creation of portfolios.
   */
  class PortfoliosBuilder {

    private IStockService stockService;

    PortfoliosBuilder() {
      stockService = null;
    }

    /**
     * Sets the stock service object for a portfolio.
     *
     * @param serviceType Type of service represented using {@link ServiceType}.
     * @return the updated PortfolioBuilder object.
     */
    public PortfoliosBuilder setStockService(ServiceType serviceType) {
      if (serviceType == ServiceType.STOCK) {
        this.stockService = new StockService();
      }
      return this;
    }

    /**
     * Creates the portfolio object with the properties set in the builder.
     *
     * @param portfolioType Type of portfolio represented using {@link PortfolioType}.
     * @return the requested portfolio type object with the properties set in the builder.
     */
    public IPortfolios build(PortfolioType portfolioType) {
      IPortfolios result = null;
      if (portfolioType == PortfolioType.STOCKS) {
        result = new Portfolios(stockService);
      }
      return result;
    }
  }
}
