package portfolio.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlexiblePortfolioImpl extends Portfolio
      implements IFlexiblePortfolio {

  private static class Pair<S, T> {

    S s;
    T t;

    public Pair(S s, T t) {
      this.s = s;
      this.t = t;
    }
  }

  private final List<FlexiblePortfolioImpl.Pair<IStock, Long>> stocks = new ArrayList<>();
  /**
   * Constructs an object of Portfolio and initializes its members.
   *
   * @param stockService the service responsible for calling the API required for stocks data.
   * @param stocks       stocks that will be stored in this portfolio.
   */
  public FlexiblePortfolioImpl(IStockService stockService, Map<IStock, Long> stocks) {
    super(stockService, stocks);
  }

  @Override
  public void addStocksToPortfolio(IStock stocks, Long quantity) {
    Pair<IStock, Long> stockLongPair = new Pair<>(stocks, quantity);
    this.stocks.add(stockLongPair);
  }

  @Override
  public void sellStocksFromPortfolio(Map<String, Long> stocks) throws IllegalArgumentException {

  }

  @Override
  public double getPortfolioCostBasisByDate(LocalDate date) {
    return 0;
  }
}
