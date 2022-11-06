package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FlexiblePortfolioImpl extends AbstractPortfolio {
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   */
  public FlexiblePortfolioImpl(IStockService stockService, Map<IStock, Long> stocks) {
    super(stockService, stocks);
    creationDate = LocalDate.now();

    for(Map.Entry<IStock, Long> stock:stocks.entrySet()) {
      this.addStocksToPortfolio(stock.getKey(), stock.getValue());
    }
  }

  @Override
  public String getPortfolioComposition() {
    return stockQuantityMap.size() > 0 ? stockQuantityMap.entrySet().stream()
        .map(x -> x.getKey().getStockTicker() + " -> " + x.getValue() + "\n")
        .collect(Collectors.joining()) : "No stocks in the portfolio";
  }

  @Override
  public void addStocksToPortfolio(IStock stock, Long quantity) {
    long stockQty = 0;
    if(stockQuantityMap.containsKey(stock)) {
      stockQty = stockQuantityMap.get(stock);
    }
    stockQuantityMap.put(stock, stockQty + quantity);
  }

  @Override
  public void sellStocksFromPortfolio(IStock stock, Long quantity) throws IllegalArgumentException {
    long stockQty = 0;
    if (!stockQuantityMap.containsKey(stock)) {
      throw new IllegalArgumentException("Cannot sell stock if portfolio doesn't contain it.");
    }

    stockQty = stockQuantityMap.get(stock);

    if (stockQty - quantity < 0) {
      throw new IllegalArgumentException("Do not have more than " + quantity + " shares presently");
    }

    stockQuantityMap.put(stock, stockQty - quantity);
  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    return 0;
  }

  @Override
  public void savePortfolio(String path)
      throws IllegalArgumentException, ParserConfigurationException {

  }

  @Override
  public void retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException {

  }
}