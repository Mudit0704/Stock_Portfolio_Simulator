package portfolio.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FlexiblePortfolioImpl extends Portfolio
      implements IFlexiblePortfolio {

  private final Map<IStock, Long> stockQuantityMap = new HashMap<>();
  private final LocalDate creationDate;

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
  public double getPortfolioValue(LocalDate date) throws IllegalArgumentException {
    if(date.isBefore(this.creationDate)) {
      return 0.0;
    }

    double portfolioValue = 0;

    for (Map.Entry<IStock, Long> stock : stockQuantityMap.entrySet()) {
      try {
        portfolioValue += stock.getKey().getValue(date) * stock.getValue();
      } catch (DateTimeParseException e) {
        throw new RuntimeException("API failure...\n");
      }
    }
    return portfolioValue;
  }
}