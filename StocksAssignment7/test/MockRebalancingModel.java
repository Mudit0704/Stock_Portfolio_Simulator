import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Model;
import model.ModelImpl;

/**
 * Class to mock the {@link ModelImpl} class for rebalancing a portolio testing.
 */
public class MockRebalancingModel extends ModelImpl implements Model {
  StringBuilder log;
  Boolean invalidData;
  Boolean percentage100;

  MockRebalancingModel(StringBuilder log, Boolean invalidData, Boolean percentage100) {
    this.log = log;
    this.invalidData = invalidData;
    this.percentage100 = percentage100;
  }

  @Override
  public void rebalanceExistingPortfolio(String pfName, String filePath, LocalDate date,
      ArrayList<LocalDate> validDatesInAPI, String type) {
    log.append(pfName).append(filePath).append(date).append(type);
    }

  @Override
  public List<String> getTickerSymbolsInPortfolio(String pfName, String filePath, LocalDate date) {
    log.append(pfName).append(filePath).append(date);
    List<String> result = new ArrayList<>();
    result.add(pfName);
    return result;
  }

  @Override
  public boolean isPfNameUnique(String pfName, String filePath) {
    log.append(pfName).append(filePath);
    return invalidData;
  }

  @Override
  public void saveTsAndPerc(String tickerSymbol, double percentage) {
    log.append(tickerSymbol).append(percentage);
  }

  @Override
  public boolean isPercentagesSum100() {
    return percentage100;
  }

  @Override
  public boolean percentageSumMoreThan100(double percentage) {
    log.append(percentage);
    return percentage > 100D;
  }
}
