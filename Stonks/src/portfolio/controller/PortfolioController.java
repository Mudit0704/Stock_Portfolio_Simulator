package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import portfolio.model.IPortfolios;
import portfolio.model.ServiceType;
import portfolio.view.IView;

public class PortfolioController implements IPortfolioController {

  private final static String CREATE_PORTFOLIO_SUB_MENU =
      "Choose from the below menu: \n 1 -> Add a new stock "
          + "\n E -> Exit from the operation \n";
  private final static String SAVE_RETRIEVE_PORTFOLIO_MENU =
      "Choose from the below menu: \n 1 -> Save portfolio "
          + "\n 2 -> Retrieve portfolio \n E -> Exit from the operation \n";
  private static final String CHOOSE_FROM_AVAILABLE_PORTFOLIOS = "Choose from available portfolios (eg: Portfolio1 -> give 1):\n";

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
    view.displayCustomText(CHOOSE_FROM_AVAILABLE_PORTFOLIOS);
    String availablePortfolios = portfolios.getAvailablePortfolios();
    view.displayCustomText(availablePortfolios);
    if ("No portfolios\n".equals(availablePortfolios)) {
      displayExitOperationSequence(scan);
      return;
    }
    view.askForInput();
    try {
      int portfolioId = scan.nextInt();
      view.displayCustomText(portfolios.getPortfolioComposition(portfolioId));
    } catch (InputMismatchException e) {
      view.displayInvalidInput();
    }
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
      throws IOException, InterruptedException {
    String path;
    view.displayCustomText(SAVE_RETRIEVE_PORTFOLIO_MENU);
    view.askForInput();
    String choice = scan.next();
    if ("E".equals(choice)) {
      return;
    }
    view.displayCustomText("Please enter the directory path: ");
    path = scan.next();
    try {
      if ("1".equals(choice)) {
        view.displayCustomText(
            portfolios.savePortfolio(path) ? "Saved\n" : "No portfolios to save\n");
      } else if ("2".equals(choice)) {
        view.displayCustomText(
            portfolios.retrievePortfolio(path) ? "Retrieved\n" : "Portfolios already populated\n");
      } else {
        throw new IOException();
      }
    } catch (Exception e) {
      view.displayInvalidInput();
      return;
    }
    displayExitOperationSequence(scan);
  }

  private void getPortfolioValuesForGivenDate(IPortfolios portfolios, Scanner scan)
      throws IOException, InterruptedException {
    LocalDate date;
    int portfolioId;
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
      String portfolioValue = portfolios.getPortfolioValue(date, portfolioId);
      view.displayCustomText(portfolioValue);
      if ("Invalid portfolioId\n".equals(portfolioValue)) {
        displayExitOperationSequence(scan);
        return;
      }
    } catch (Exception e) {
      view.displayInvalidInput();
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
    portfolios.setPortfolioServiceType(ServiceType.STOCK);
    view.displayCustomText("Stock Symbol: ");
    scan.next();
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
