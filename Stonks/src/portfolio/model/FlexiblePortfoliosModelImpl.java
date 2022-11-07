package portfolio.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FlexiblePortfoliosModelImpl extends AbstractPortfolioModel {

  public FlexiblePortfoliosModelImpl() {
    apiOptimizer = StockCache.getInstance();
    portfolioList = new ArrayList<>();
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio ID");
    }

    IFlexiblePortfolio portfolio = portfolioList.get(portfolioId - 1);
    IStock stock = apiOptimizer.cacheGetObj(tickerSymbol);
    if (stock == null) {
      stock = new Stock(tickerSymbol, this.stockService);
      apiOptimizer.cacheSetObj(tickerSymbol, stock);
    }
    portfolio.addStocksToPortfolio(stock, quantity);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if (portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = this.portfolioList.get(portfolioId - 1);
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

  protected AbstractPortfolio createPortfolio(Map<IStock, Long> stockQty) {
    return new FlexiblePortfolioImpl(this.stockService, stockQty);
  }

  @Override
  protected String getPath() {
    return "flexiblePortfolio/";
  }
}