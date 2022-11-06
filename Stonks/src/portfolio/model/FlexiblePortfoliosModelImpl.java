package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FlexiblePortfoliosModelImpl extends AbstractPortfolioModel
    implements IFlexiblePortfoliosModel {

  public FlexiblePortfoliosModelImpl() {
    apiOptimizer = StockCache.getInstance();
    portfolioList = new ArrayList<>();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio ID");
    }

    AbstractPortfolio portfolio = portfolioList.get(portfolioId - 1);
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.addStocksToPortfolio(stock, quantity);
  }

  @Override
  public void createNewPortfolio(Map<String, Long> stocks) {
    Map<IStock, Long> stockQty = new HashMap<>();

    for (Map.Entry<String, Long> entry : stocks.entrySet()) {
      stockQty.put(new Stock(entry.getKey(), this.stockService), entry.getValue());
    }

    AbstractPortfolio portfolio = null;
    portfolioList.add(portfolio.createPortfolio(stockQty));

    for (Map.Entry<String, Long> entry : stocks.entrySet()) {
      this.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioList.size() - 1);
    }
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    AbstractPortfolio portfolio = this.portfolioList.get(portfolioId - 1);
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.sellStocksFromPortfolio(stock, quantity);
  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    return 0;
  }

  @Override
  public void savePortfolios() throws RuntimeException, ParserConfigurationException {

  }

  @Override
  public void retrievePortfolios() throws IOException, ParserConfigurationException, SAXException {

  }
}