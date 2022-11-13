package portfolio.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for {@link DaysPerformanceVisualizer}.
 */
public class DaysPerformanceVisualizerTest {

  AbstractPortfolio portfolio;
  MockStockService mockStockService;
  AbstractPerformanceVisualizer daysPerformanceVisualizer;


  @Before
  public void setUp() throws Exception {
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
    portfolio = new FlexiblePortfolioImpl(mockStockService, new HashMap<>(), 0,
      LocalDate.now());
    portfolio.retrievePortfolio("test/test_model_inner/test_multiple_transaction.xml");
    daysPerformanceVisualizer = new DaysPerformanceVisualizer(portfolio);
  }

  @Test
  public void testPopulatePortfolioValues() {
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    daysPerformanceVisualizer.populatePortfolioValues(LocalDate.parse("2019-10-25"),
        LocalDate.parse("2019-11-23"), 1, dateValue);

    assertEquals(LocalDate.parse("2019-10-25"),
        dateValue.keySet().stream().min(LocalDate::compareTo).orElseThrow());
    assertEquals(LocalDate.parse("2019-11-23"),
        dateValue.keySet().stream().max(LocalDate::compareTo).orElseThrow());
  }

  @Test
  public void testPopulateString() {
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    daysPerformanceVisualizer.populatePortfolioValues(LocalDate.parse("2019-10-25"),
        LocalDate.parse("2019-10-30"), 1, dateValue);

    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);
    int scale = AbstractPerformanceVisualizer.getScale(minValue.orElseThrow(),
        maxValue.orElseThrow(), 1);

    StringBuilder expectedString = new StringBuilder();
    expectedString.append("\nVisualizing using the period of days\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      expectedString.append(mapEntry.getKey().minusDays(1)).append(" -> ")
          .append(mapEntry.getKey().toString()).append(": ");
      AbstractPerformanceVisualizer.populateBar(minValue.orElseThrow(), scale, expectedString,
          mapEntry);
    }
    expectedString.append("\nBase: ").append(String.format("%,.2f", minValue.orElseThrow()))
        .append("\n");
    expectedString.append("A line without asterisk means the performance during that timespan was"
        + " less than or equal to the base given above").append("\n");
    expectedString.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");

    String actualString = daysPerformanceVisualizer.populateString(dateValue,
        minValue.orElseThrow(),
        scale);

    assertEquals(expectedString.toString(), actualString);
  }
}