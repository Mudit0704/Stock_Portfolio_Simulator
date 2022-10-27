package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import portfolio.model.IPortfolios;
import portfolio.model.Portfolios;
import portfolio.view.IView;

public class PortfolioController implements IPortfolioController {

  final static String CREATE_PORTFOLIO_SUB_MENU =
      "Choose from the below menu: \n 1 -> Add a new stock "
          + "\n E -> Exit from the operation \n";
  final static String SAVE_RETRIEVE_PORTFOLIO_MENU =
      "Choose from the below menu: \n 1 -> Save portfolio "
          + "\n 2 -> Retrieve portfolio \n E -> Exit from the operation \n";

  final Readable in;
  final IView view;

  public PortfolioController(Readable in, IView view) {
    this.in = in;
    this.view = view;
  }

  @Override
  public void run(IPortfolios portfolio) throws IOException, InterruptedException {
    Objects.requireNonNull(portfolio);
    Scanner scan = new Scanner(this.in);

    while (true) {
      view.clearScreen();
      view.displayMenu();
      view.askForInput();
      switch (scan.next()) {
        case "1":
          view.clearScreen();
          generatePortfolios(scan, portfolio);
          break;
        case "2":
          view.clearScreen();
          view.displayCustomText("Choose from available portfolios (eg: Portfolio1 -> give 1):\n");
          String availablePortfolios = portfolio.getAvailablePortfolios();
          view.displayCustomText(availablePortfolios);
          if ("No portfolios\n".equals(availablePortfolios)) {
            displayExitOperationSequence(scan);
            break;
          }
          view.askForInput();
          try {
            int portfolioId = scan.nextInt();
            view.displayCustomText(portfolio.getPortfolioComposition(portfolioId));
          } catch (InputMismatchException e) {
            view.displayInvalidInput();
          }
          displayExitOperationSequence(scan);
          break;
        case "3":
          view.clearScreen();
          getPortfolioValuesForGivenDate(portfolio, scan);
          break;
        case "4":
          view.clearScreen();
          saveRetrievePortfolios(portfolio, scan);
          break;
        case "E":
          return;
        default:
          view.displayInvalidInput();
      }
    }

  }

  private void displayExitOperationSequence(Scanner scan) throws IOException, InterruptedException {
    view.displayEscapeFromOperation();
    while (!"E".equals(scan.next())) {
      view.displayInvalidInput();
      view.displayEscapeFromOperation();
    }
    view.clearScreen();
  }

  private void saveRetrievePortfolios(IPortfolios portfolio, Scanner scan)
      throws IOException, InterruptedException {
    String path;
    view.displayCustomText(SAVE_RETRIEVE_PORTFOLIO_MENU);
    view.askForInput();
    String choice = scan.next();
    if("E".equals(choice)) return;
    view.displayCustomText("Please enter the directory path: ");
    path = scan.next();
    try {
      if ("1".equals(choice)) {
        view.displayCustomText(
            portfolio.savePortfolio(path) ? "Saved\n" : "No portfolios to save\n");
      } else if ("2".equals(choice)) {
        view.displayCustomText(
            portfolio.retrievePortfolio(path) ? "Retrieved\n" : "Portfolios already populated\n");
      } else {
        throw new IOException();
      }
    } catch (Exception e) {
      view.displayInvalidInput();
      return;
    }
    displayExitOperationSequence(scan);
  }

  private void getPortfolioValuesForGivenDate(IPortfolios portfolio, Scanner scan)
      throws IOException, InterruptedException {
    LocalDate date;
    int portfolioId;
    view.displayCustomText("Choose from available portfolios (eg: Portfolio1 -> give 1):\n");
    String availablePortfolios = portfolio.getAvailablePortfolios();
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
      String portfolioValue = portfolio.getPortfolioValue(date, portfolioId);
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
    String tickerSymbol;
    int stockQuantity;
    Map<String, Integer> stocks = new HashMap<>();
    while (true) {
      view.displayCustomText(CREATE_PORTFOLIO_SUB_MENU);
      view.askForInput();
      switch (scan.next()) {
        case "E":
          if (stocks.size() > 0) {
            portfolios.setPortfolioStocks(stocks);
          }
          view.clearScreen();
          return;
        case "1":
          view.displayCustomText("Stock Symbol: ");
          tickerSymbol = scan.next();
          view.displayCustomText("Stock Quantity: ");
          try {
            stockQuantity = scan.nextInt();
          } catch (InputMismatchException e) {
            scan.nextLine();
            view.displayInvalidInput();
            continue;
          }
          stocks.put(tickerSymbol, stockQuantity);
          break;
        default:
          view.displayInvalidInput();
          break;
      }
    }
  }
}
