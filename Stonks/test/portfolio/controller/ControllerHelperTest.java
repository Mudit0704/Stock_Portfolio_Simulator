package portfolio.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;
import portfolio.model.MockFlexiblePortfolioModel;
import portfolio.view.IView;
import portfolio.view.MockView;

public class ControllerHelperTest {

  private StringBuffer out;
  private IView mockView;
  private StringBuilder log;
  private Scanner scanner;

  private ControllerHelper controllerHelper;

  @Before
  public void setup() {
    out = new StringBuffer();
    mockView = new MockView(out);
    log = new StringBuilder();
  }

  @Test
  public void testPopulatePortfolioIdFromUserWithValidInput() throws IOException {
    Reader in = new StringReader("\n 1 E");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    int actualId = controllerHelper.populatePortfolioIdFromUser(
        new MockFlexiblePortfolioModel(log, true),
        scanner);

    assertEquals(1, actualId);
    assertEquals("Available_Portfolios ", log.toString());
    assertEquals("Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input ", out.toString());
  }

  @Test
  public void testPopulatePortfolioIdFromUserWhenNoPortfolio() throws IOException {
    Reader in = new StringReader("\n E");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    Integer actualId = controllerHelper.populatePortfolioIdFromUser(
        new MockFlexiblePortfolioModel(log, false),
        scanner);

    assertNull(actualId);
    assertEquals("", log.toString());
    assertEquals("Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "No portfolios\nEscape ", out.toString());
  }

  @Test
  public void testPopulatePortfolioIdFromUserWithInvalidInput() throws IOException {
    Reader in = new StringReader("\n Invalid_input\n 1 E");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    int actualId = controllerHelper.populatePortfolioIdFromUser(
        new MockFlexiblePortfolioModel(log, true),
        scanner);

    assertEquals(1, actualId);
    assertEquals("Available_Portfolios Available_Portfolios ", log.toString());
    assertEquals("Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input ", out.toString());
  }

  @Test
  public void testPopulatePortfolioIdFromUserWithInvalidInputZero() throws IOException {
    Reader in = new StringReader("\n 0\n 1 E");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    int actualId = controllerHelper.populatePortfolioIdFromUser(
        new MockFlexiblePortfolioModel(log, true),
        scanner);

    assertEquals(1, actualId);
    assertEquals("Available_Portfolios Available_Portfolios ", log.toString());
    assertEquals("Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input Invalid_Input Choose from available portfolios (eg: Portfolio1 -> give 1):\n"
        + "Available_Portfolios Ask_For_Input ", out.toString());
  }

  @Test
  public void testPopulateDateFromUserWithValidDate() throws IOException {
    Reader in = new StringReader("2019-10-25");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    LocalDate actualDate = controllerHelper.populateDateFromUser(scanner);

    assertEquals(LocalDate.parse("2019-10-25"), actualDate);
    assertEquals("", log.toString());
    assertEquals("Please enter the date (yyyy-mm-dd): ", out.toString());
  }

  @Test
  public void testPopulateDateFromUserWithInvalidDate() throws IOException {
    Reader in = new StringReader("Invalid_date\n 2019-10-25");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    LocalDate actualDate = controllerHelper.populateDateFromUser(scanner);

    assertEquals(LocalDate.parse("2019-10-25"), actualDate);
    assertEquals("", log.toString());
    assertEquals(
        "Please enter the date (yyyy-mm-dd): Invalid_Input Please enter the date (yyyy-mm-dd): ",
        out.toString());
  }

  @Test
  public void testPopulateStockDateFromUserValidStockValues() throws IOException {
    Reader in = new StringReader("\nTICKER_SYMBOL\n2");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);
    Map<String, Long> stocks = new HashMap<>();

    controllerHelper.populateStockDateFromUser(scanner,
        new MockFlexiblePortfolioModel(log, true), stocks);

    assertTrue(stocks.containsKey("TICKER_SYMBOL"));
    assertEquals(Optional.of(2L).get(), stocks.get("TICKER_SYMBOL"));
    assertEquals("TICKER_SYMBOL ", log.toString());
    assertEquals("Stock Symbol: Stock Quantity: ", out.toString());
  }

  @Test
  public void testPopulateStockDateFromUserValidStockValuesStocksAlreadyExisting() throws IOException {
    Reader in = new StringReader("\nTICKER_SYMBOL\n2");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);
    Map<String, Long> stocks = new HashMap<>();
    stocks.put("TICKER_SYMBOL", 2L);

    controllerHelper.populateStockDateFromUser(scanner,
        new MockFlexiblePortfolioModel(log, true), stocks);

    assertTrue(stocks.containsKey("TICKER_SYMBOL"));
    assertEquals(Optional.of(4L).get(), stocks.get("TICKER_SYMBOL"));
    assertEquals("TICKER_SYMBOL ", log.toString());
    assertEquals("Stock Symbol: Stock Quantity: ", out.toString());
  }

  @Test
  public void testPopulateStockDateFromUserInvalidStockSymbol() throws IOException {
    Reader in = new StringReader("\nINVALID_SYMBOL\nTICKER_SYMBOL\n2");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);
    Map<String, Long> stocks = new HashMap<>();

    controllerHelper.populateStockDateFromUser(scanner,
        new MockFlexiblePortfolioModel(log, true), stocks);

    assertTrue(stocks.containsKey("TICKER_SYMBOL"));
    assertEquals(Optional.of(2L).get(), stocks.get("TICKER_SYMBOL"));
    assertEquals("INVALID_SYMBOL TICKER_SYMBOL ", log.toString());
    assertEquals("Stock Symbol: Invalid Ticker Symbol\n"
        + "Stock Symbol: Stock Quantity: ", out.toString());
  }

  @Test
  public void testPopulateStockDateFromUserInvalidStockQuantity() throws IOException {
    Reader in = new StringReader("\nTICKER_SYMBOL\nInvalid_Quantity\n2");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);
    Map<String, Long> stocks = new HashMap<>();

    controllerHelper.populateStockDateFromUser(scanner,
        new MockFlexiblePortfolioModel(log, true), stocks);

    assertTrue(stocks.containsKey("TICKER_SYMBOL"));
    assertEquals(Optional.of(2L).get(), stocks.get("TICKER_SYMBOL"));
    assertEquals("TICKER_SYMBOL ", log.toString());
    assertEquals("Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: ", out.toString());
  }

  @Test
  public void testPopulateStockDateFromUserInvalidStockQuantityZero() throws IOException {
    Reader in = new StringReader("\nTICKER_SYMBOL\n0\n2");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);
    Map<String, Long> stocks = new HashMap<>();

    controllerHelper.populateStockDateFromUser(scanner,
        new MockFlexiblePortfolioModel(log, true), stocks);

    assertTrue(stocks.containsKey("TICKER_SYMBOL"));
    assertEquals(Optional.of(2L).get(), stocks.get("TICKER_SYMBOL"));
    assertEquals("TICKER_SYMBOL ", log.toString());
    assertEquals("Stock Symbol: Stock Quantity: Invalid_Input Stock Quantity: ", out.toString());
  }

  @Test
  public void testPerformExitOperationSequenceWithValidInput() throws IOException {
    Reader in = new StringReader("\nE");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    controllerHelper.performExitOperationSequence(scanner);

    assertEquals("", log.toString());
    assertEquals("Escape ", out.toString());
  }

  @Test
  public void testPerformExitOperationSequenceWithInvalidInput() throws IOException {
    Reader in = new StringReader("\n Invalid_Input\n E");
    scanner = new Scanner(in);
    controllerHelper = new ControllerHelper(mockView);

    controllerHelper.performExitOperationSequence(scanner);

    assertEquals("", log.toString());
    assertEquals("Escape Invalid_Input Escape ", out.toString());
  }
}
