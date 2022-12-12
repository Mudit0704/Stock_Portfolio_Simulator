import static org.junit.Assert.assertEquals;

import controller.APICaller;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Model;
import model.ModelImpl;
import org.junit.Before;
import org.junit.Test;

public class RebalancingTest {

  Path currentPath;
  private Model model;

  APICaller validDates = new APICaller();
  ArrayList<LocalDate> datesFromAPI = validDates.callAPI();

  @Before
  public void setup() {
    model = new ModelImpl();
    currentPath = Paths.get(System.getProperty("user.dir"));


  }

  @Test
  public void testModelRebalanceAfterDCA() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestRebalance";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
        intervals, filePath, datesFromAPI, purpose);

    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 50);
    model.saveTsAndPerc("MSFT", 25);
    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022, 2, 2),
        datesFromAPI, "flexible");

    assertEquals(0.0, model.getCostBasis("TestRebalance", fp.toString(),
            LocalDate.of(2020, 8, 30)),
        0.00);
    assertEquals(1015.0, model.getCostBasis("TestRebalance", fp.toString(),
            LocalDate.of(2021, 1, 29)),
        0.00);
    assertEquals(6090.0, model.getCostBasis("TestRebalance", fp.toString(),
            LocalDate.of(2021, 6, 4)),
        0.00);
    assertEquals(27843.33, model.getCostBasis("TestRebalance", fp.toString(),
            LocalDate.of(2022, 2, 2)),
        0.1);
  }

  @Test
  public void testModelRebalanceBetweenDCA() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestRebalanceBetween";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
        intervals, filePath, datesFromAPI, purpose);

    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 50);
    model.saveTsAndPerc("MSFT", 25);
    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2021, 4, 1),
        datesFromAPI, "flexible");

    assertEquals(0.0, model.getCostBasis(pfName, fp.toString(),
            LocalDate.of(2020, 8, 30)),
        0.00);
    assertEquals(1015.0, model.getCostBasis(pfName, fp.toString(),
            LocalDate.of(2021, 1, 29)),
        0.00);
    assertEquals(9135.19, model.getCostBasis(pfName, fp.toString(),
            LocalDate.of(2021, 6, 4)),
        0.1);
    assertEquals(6090.19, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2021, 4, 2)), 0.1);
    assertEquals(7589.89, model.getTotal(pfName, fp.toString(),
        LocalDate.of(2021, 4, 29), "Flexible"), 0.1);
  }

  @Test
  public void testModelRebalance() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancing";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);
    model.saveTsAndPerc("GOOG", 25);
    assertEquals(2056.39, model.getTotal(pfName, fp.toString(),
        LocalDate.of(2022, 12, 2), "Flexible"), 0.1);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022, 12, 2),
        datesFromAPI, "Flexible");

    assertEquals(7279.37, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2022, 12, 2)), 0.1);

    assertEquals(2056.39, model.getTotal(pfName, fp.toString(),
        LocalDate.of(2022, 12, 2), "Flexible"), 0.1);

    assertEquals("* Portfolio Name : TestNormalRebalancing\n"
            + "* Ticker Symbol: GOOG, Number of Shares: 5.098656153922444\n"
            + "* Ticker Symbol: AAPL, Number of Shares: 3.4780968811311817\n"
            + "* Ticker Symbol: AMZN, Number of Shares: 5.461569106554765\n"
            + "* Ticker Symbol: TSLA, Number of Shares: 2.6382915939648974\n",
        model.getComposition(pfName, fp.toString(),
            LocalDate.of(2022, 12, 2)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelRebalanceInvalidWeights() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancing";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022, 12, 2),
        datesFromAPI, "Flexible");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelRebalanceBetweenTxnInvalidStocks() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancingBetweenTxn";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);
    model.saveTsAndPerc("GOOG", 25);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2016, 12, 2),
        datesFromAPI, "Flexible");
  }

  @Test
  public void testModelRebalanceBetweenTxn() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancingBetweenTxn";

    // Percentage Split
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 75);

    assertEquals(2946.90, model.getTotal(pfName, filePath,
        LocalDate.of(2016, 12, 2), "Flexible"), 0.1);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2016, 12, 2),
        datesFromAPI, "Flexible");

    assertEquals(2946.90, model.getTotal(pfName, filePath,
        LocalDate.of(2016, 12, 2), "Flexible"), 0.1);
    assertEquals(9711.56, model.getTotal(pfName, filePath,
        LocalDate.of(2017, 12, 6), "Flexible"), 0.1);
    assertEquals(9327.22,
        model.getCostBasis(pfName, filePath, LocalDate.of(2017, 12, 6)),
        0.1);
    assertEquals(4813.22,
        model.getCostBasis(pfName, filePath, LocalDate.of(2017, 10, 9)),
        0.1);

    assertEquals(5162.91, model.getTotal(pfName, filePath,
        LocalDate.of(2017, 10, 9), "Flexible"), 0.1);
  }

}
