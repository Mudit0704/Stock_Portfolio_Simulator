package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class StrategicPortfolio extends AbstractPortfolio {

  protected IStrategy strategy;
  protected AbstractPortfolio portfolio;
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService   the service responsible for calling the API required for stocks data.
   * @param stocks         stocks that will be stored in this portfolio.
   * @param transactionFee
   * @param date           date on which this portfolio is created.
   */
  public StrategicPortfolio(IStockService stockService, Map<IStock, Double> stocks,
    double transactionFee, LocalDate date) {
    super(stockService, stocks);
    portfolio = new FlexiblePortfolio(stockService, stocks, transactionFee, date);
  }

  @Override
  public void investStocksIntoStrategicPortfolio(Map<IStock, Double> stockProportions,
    Double totalAmount, IStrategy strategy, double transactionFee) {

    Map<LocalDate, Map<IStock, Double>> updatedFractionalQty = strategy.applyStrategy(stockProportions);

    for(Map.Entry<LocalDate, Map<IStock, Double>> stocksOnDate: updatedFractionalQty.entrySet()) {
      LocalDate date = stocksOnDate.getKey();
      Map<IStock, Double> proportions = stocksOnDate.getValue();
      for(Map.Entry<IStock, Double> proportion:proportions.entrySet()) {
        portfolio.addStocksToPortfolio(proportion.getKey(), proportion.getValue(), date, transactionFee);
      }
    }
  }

  @Override
  public void addStocksToPortfolio(IStock stocks, Double quantity, LocalDate date,
    double transactionFee) {
    portfolio.addStocksToPortfolio(stocks, quantity, date, transactionFee);
  }

  @Override
  public void sellStocksFromPortfolio(IStock stock, Double quantity, LocalDate date,
    double transactionFee) throws IllegalArgumentException {
    portfolio.sellStocksFromPortfolio(stock, quantity, date, transactionFee);
  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    return portfolio.getPortfolioCostBasisByDate(date);
  }

  @Override
  public String getPortfolioCompositionOnADate(LocalDate date) {
    return portfolio.getPortfolioCompositionOnADate(date);
  }

  @Override
  public String getPortfolioPerformance(LocalDate start, LocalDate end) {
    return portfolio.getPortfolioPerformance(start, end);
  }

  @Override
  public String getPortfolioComposition() {
    return portfolio.getPortfolioComposition();
  }

  @Override
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    return portfolio.getPortfolioValue(date);
  }

  @Override
  public void savePortfolio(String path)
    throws IllegalArgumentException, ParserConfigurationException {
    portfolio.savePortfolio(path);
  }

  @Override
  public void retrievePortfolio(String path)
    throws IOException, ParserConfigurationException, SAXException {
    portfolio.retrievePortfolio(path);
  }
}

// Abstract the validation method in the FlexiblePortfolio class and implement the method appropriately for this class
// remove the createFractionalStrategicPortfolio() from this class
// use only the investStocksIntoStrategicPortfolio() method from the upper layer
