package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import portfolio.model.IPortfolios;
import portfolio.view.IView;

public class PortfolioController implements IPortfolioController {

  private final static String CREATE_PORTFOLIO_SUB_MENU =
      "Choose from the below menu: \n 1 -> Add a new stock "
          + "\n E -> Exit from the operation \n";
  private final static String SAVE_RETRIEVE_PORTFOLIO_MENU =
      "Choose from the below menu: \n 1 -> Save portfolio "
          + "\n 2 -> Retrieve portfolio \n E -> Exit from the operation \n";
  private static final String CHOOSE_FROM_AVAILABLE_PORTFOLIOS = "Choose from available portfolios "
      + "(eg: Portfolio1 -> give 1):\n";

  final Readable in;
  final IView view;

  public PortfolioController(Readable in, IView view) {
    this.in = in;
    this.view = view;
  }

  @Override
  public void run(IPortfolios portfolios) throws IOException, InterruptedException {
    Objects.requireNonNull(portfolios);
    Scanner scan = new Scanner(this.in);

    while (true) {
      view.clearScreen();
      view.displayMenu();
      view.askForInput();
      switch (scan.next()) {
        case "1":
          view.clearScreen();
          generatePortfolios(scan, portfolios);
          break;
        case "2":
          getPortfolioComposition(portfolios, scan);
          break;
        case "3":
          view.clearScreen();
          getPortfolioValuesForGivenDate(portfolios, scan);
          break;
        case "4":
          view.clearScreen();
          saveRetrievePortfolios(portfolios, scan);
          break;
        case "E":
          return;
        default:
          view.displayInvalidInput();
      }
    }

  }

  private void getPortfolioComposition(IPortfolios portfolios, Scanner scan)
      throws IOException, InterruptedException {
    view.clearScreen();
    String result;
    while(true) {
      try {
        view.displayCustomText(CHOOSE_FROM_AVAILABLE_PORTFOLIOS);
        String availablePortfolios = portfolios.getAvailablePortfolios();
        view.displayCustomText(availablePortfolios);
        if ("No portfolios\n".equals(availablePortfolios)) {
          displayExitOperationSequence(scan);
          return;
        }
        view.askForInput();
        scan.nextLine();
        int portfolioId = scan.nextInt();
        result = portfolios.getPortfolioComposition(portfolioId);
        break;
      } catch (InputMismatchException | IllegalArgumentException e) {
        view.displayInvalidInput();
      }
    }

    view.displayCustomText(result);
    displayExitOperationSequence(scan);
  }

  private void displayExitOperationSequence(Scanner scan) throws IOException, InterruptedException {
    view.displayEscapeFromOperation();
    while (!"E".equals(scan.next())) {
      view.displayInvalidInput();
      view.displayEscapeFromOperation();
    }
    view.clearScreen();
  }

  private void saveRetrievePortfolios(IPortfolios portfolios, Scanner scan)
      throws IOException {
    view.displayCustomText(SAVE_RETRIEVE_PORTFOLIO_MENU);
    view.askForInput();

    try {
      while (true) {
        switch (scan.next()) {
          case "E":
            displayExitOperationSequence(scan);
            return;
          case "1":
            if (!portfolios.savePortfolios()) {
              view.displayCustomText("No portfolios to save\n");
              displayExitOperationSequence(scan);
              return;
            }
            view.displayCustomText("Saved\n");
            displayExitOperationSequence(scan);
            return;
          case "2":
            if (!portfolios.retrievePortfolios()) {
              view.displayCustomText(
                  "Portfolios already populated OR No files found to retrieve\n");
              displayExitOperationSequence(scan);
              return;
            }
            view.displayCustomText("Retrieved\n");
            displayExitOperationSequence(scan);
            return;
          default:
            view.displayInvalidInput();
            break;
        }
      }
    } catch (Exception e) {
      view.displayCustomText("Encountered a problem while saving. Please check if the xml file "
          + "provided is in correct format\n");
    }
  }

  private void getPortfolioValuesForGivenDate(IPortfolios portfolios, Scanner scan)
      throws IOException, InterruptedException {
    LocalDate date;
    int portfolioId;

    while (true) {
      view.displayCustomText(CHOOSE_FROM_AVAILABLE_PORTFOLIOS);
      String availablePortfolios = portfolios.getAvailablePortfolios();
      view.displayCustomText(availablePortfolios);
      if ("No portfolios\n".equals(availablePortfolios)) {
        displayExitOperationSequence(scan);
        return;
      }
      view.askForInput();
      try {
        portfolioId = scan.nextInt();
        view.displayCustomText("Please enter the date (yyyy-mm-dd): ");
        date = LocalDate.parse(scan.next());
        String portfolioValue = String.format("%.2f", portfolios.getPortfolioValue(date, portfolioId));
        view.displayCustomText("Portfolio" + portfolioId + "\n" + portfolioValue + "\n");
        break;
      } catch (DateTimeParseException | IllegalArgumentException e) {
        view.displayInvalidInput();

      }
    }

    displayExitOperationSequence(scan);
  }

  private void generatePortfolios(Scanner scan, IPortfolios portfolios)
      throws IOException, InterruptedException {
    Map<String, Integer> stocks = new HashMap<>();
    while (true) {
      view.displayCustomText(CREATE_PORTFOLIO_SUB_MENU);
      view.askForInput();
      switch (scan.next()) {
        case "E":
          if (stocks.size() > 0) {
            portfolios.createNewPortfolio(stocks);
          }
          view.clearScreen();
          return;
        case "1":
          addNewStock(scan, portfolios, stocks);
          break;
        default:
          view.displayInvalidInput();
          break;
      }
    }
  }

  private void addNewStock(Scanner scan, IPortfolios portfolios, Map<String, Integer> stocks)
      throws IOException {
    String tickerSymbol;
    int stockQuantity;
    view.displayCustomText("Stock Symbol: ");
    scan.nextLine();
    tickerSymbol = scan.nextLine();
    while (!portfolios.isTickerSymbolValid(tickerSymbol)) {
      view.displayCustomText("Invalid Ticker Symbol\n");
      view.displayCustomText("Stock Symbol: ");
      tickerSymbol = scan.nextLine();
    }
    view.displayCustomText("Stock Quantity: ");
    try {
      stockQuantity = scan.nextInt();
    } catch (InputMismatchException e) {
      scan.nextLine();
      view.displayInvalidInput();
      return;
    }
    stocks.put(tickerSymbol, stockQuantity);
  }
}
