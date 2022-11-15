package portfolio.model;


import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test class for {@link MonthsPerformanceVisualizer}.
 */
public class MonthsPerformanceVisualizerTest {

  AbstractPortfolio portfolio;
  MockStockService mockStockService;
  AbstractPerformanceVisualizer monthsPerformanceVisualizer;

  @Before
  public void setUp() throws Exception {
    mockStockService = new MockStockService("/test/testExtensiveData.txt");
    portfolio = new FlexiblePortfolio(mockStockService, new HashMap<>(), 0,
        LocalDate.now());
    portfolio.retrievePortfolio("test/test_model_inner/test_multiple_transaction.xml");
    monthsPerformanceVisualizer = new MonthsPerformanceVisualizer(portfolio);
  }

  @Test
  public void populatePortfolioValues() {
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    monthsPerformanceVisualizer.populatePortfolioValues(LocalDate.parse("2019-10-25"),
        LocalDate.parse("2020-05-01"), 1, dateValue);

    assertEquals(LocalDate.parse("2019-10-31"),
        dateValue.keySet().stream().min(LocalDate::compareTo).orElseThrow());
    assertEquals(LocalDate.parse("2020-05-31"),
        dateValue.keySet().stream().max(LocalDate::compareTo).orElseThrow());
  }

  @Test
  public void populateString() {
    Map<LocalDate, Double> dateValue = new LinkedHashMap<>();
    monthsPerformanceVisualizer.populatePortfolioValues(LocalDate.parse("2019-10-25"),
        LocalDate.parse("2020-05-01"), 1, dateValue);

    Optional<Double> minValue = dateValue.values().stream().min(Double::compareTo);
    Optional<Double> maxValue = dateValue.values().stream().max(Double::compareTo);
    int scale = AbstractPerformanceVisualizer.getScale(minValue.orElseThrow(),
        maxValue.orElseThrow(), 1);

    StringBuilder expectedString = new StringBuilder();
    expectedString.append("\nVisualizing using the period of ").append(1).append("month(s)\n");

    for (Map.Entry<LocalDate, Double> mapEntry : dateValue.entrySet()) {
      expectedString.append(mapEntry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.US))
          .append(mapEntry.getKey().getYear()).append(": ");
      AbstractPerformanceVisualizer.populateBar(minValue.orElseThrow(), scale, expectedString,
          mapEntry);
    }
    expectedString.append("\nBase: ").append(String.format("%,.2f", minValue.orElseThrow()))
        .append("\n");
    expectedString.append("A line without asterisk means the performance during that timespan was"
        + " equal to the base given above").append("\n");
    expectedString.append("Scale: * = ").append("Base+").append("$").append(scale).append("\n");

    String actualString = monthsPerformanceVisualizer.populateString(dateValue,
        minValue.orElseThrow(), scale);

    assertEquals(expectedString.toString(), actualString);
  }
}