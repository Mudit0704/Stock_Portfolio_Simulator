package portfolio.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for {@link AbstractPerformanceVisualizer}.
 */
public class AbstractPerformanceVisualizerTest {

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
  public void visualize() {
    LocalDate start = LocalDate.parse("2019-10-25");
    LocalDate end = LocalDate.parse("2019-11-23");
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    int timeSpanJump = 1;
    IPerformanceVisualizer visualizer = new DaysPerformanceVisualizer(portfolio);
    daysPerformanceVisualizer.populatePortfolioValues(start, end, 1, dateValue);

    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);
    int scale = AbstractPerformanceVisualizer.getScale(minValue.orElseThrow(),
        maxValue.orElseThrow(), 1);

    StringBuilder expectedString = new StringBuilder();
    expectedString.append("\nVisualizing using the period of days\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      expectedString.append(mapEntry.getKey().minusDays(1)).append(" -> ")
          .append(mapEntry.getKey().toString()).append(": ");
      AbstractPerformanceVisualizer.populateBar(minValue.orElseThrow(), scale, expectedString, mapEntry);
    }
    expectedString.append("\nBase: ").append(String.format("%,.2f", minValue.orElseThrow())).append("\n");
    expectedString.append("A line without asterisk means the performance during that timespan was"
        + " less than or equal to the base given above").append("\n");
    expectedString.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");

    assertEquals(expectedString.toString(),
        visualizer.visualize(start, end, timeSpanJump, dateValue));
  }

  @Test
  public void getScale() {
    LocalDate start = LocalDate.parse("2019-10-25");
    LocalDate end = LocalDate.parse("2019-11-23");
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    daysPerformanceVisualizer.populatePortfolioValues(start, end, 1, dateValue);

    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);
    int actualScale = AbstractPerformanceVisualizer.getScale(minValue.orElseThrow(),
        maxValue.orElseThrow(), 1);

    assertEquals(107, actualScale);
  }

  @Test
  public void populateBar() {
    Entry<LocalDate, Double> dateValue = new SimpleEntry<>(LocalDate.parse("2019-10-25"), 6000D);

    StringBuilder expectedString = new StringBuilder();
    AbstractPerformanceVisualizer.populateBar(5500D, 100, expectedString,
        dateValue);

    assertEquals("*****\n", expectedString.toString());
  }
}