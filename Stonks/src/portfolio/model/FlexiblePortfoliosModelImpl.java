package portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexiblePortfoliosModelImpl extends PortfoliosModel
    implements IFlexiblePortfoliosModel {

  private List<IFlexiblePortfolio> portfolioList;
  private IStockService stockService;

  public FlexiblePortfoliosModelImpl(ServiceType serviceType) {
    stockService = AbstractServiceCreator.serviceCreator(serviceType);
  }

  @Override
  public void addStocksToPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if(portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio ID");
    }

    IFlexiblePortfolio portfolio = portfolioList.get(portfolioId);
    IStock stockObj = new Stock(tickerSymbol, this.stockService);

    portfolio.addStocksToPortfolio(stockObj, quantity);
  }

  @Override
  public void sellStockFromPortfolio(String tickerSymbol, Long quantity, int portfolioId) {
    if(portfolioId < 0 || portfolioId > portfolioList.size()) {
      throw new IllegalArgumentException("Invalid portfolio Id");
    }

    IFlexiblePortfolio portfolio = this.portfolioList.get(portfolioId);
    IStock stock = new Stock(tickerSymbol, this.stockService);
    portfolio.sellStocksFromPortfolio(stock, quantity);
  }

  @Override
  public double getCostBasis(LocalDate date, int portfolioId) {
    return 0;
  }
}
