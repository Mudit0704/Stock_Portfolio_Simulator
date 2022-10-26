package portfolio.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import portfolio.model.IPortfolio;
import portfolio.model.Portfolio;
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
  public void run(IPortfolio portfolio) throws IOException {
    Objects.requireNonNull(portfolio);
    Scanner scan = new Scanner(this.in);

    while (true) {
      view.displayMenu();
      view.askForInput();
      switch (scan.next()) {
        case "1":
          view.clearScreen();
          portfolio = generatePortfolio(scan);
          break;
        case "2":
          view.clearScreen();
          view.displayCustomText(portfolio.getPortfolioComposition() + "\n");
          displayExitOperationSequence(scan);
          break;
        case "3":
          view.clearScreen();
          getStockValueForGivenDate(portfolio, scan);
          break;
        case "4":
          view.clearScreen();
          saveRetrievePortfolio(portfolio, scan);
          break;
        case "E":
          return;
        default:
          view.displayInvalidInput();
      }
    }

  }

  private void displayExitOperationSequence(Scanner scan) throws IOException {
    view.displayEscapeFromOperation();
    while (!"E".equals(scan.next())) {
      view.displayInvalidInput();
      view.displayEscapeFromOperation();
    }
    view.clearScreen();
  }

  private void saveRetrievePortfolio(IPortfolio portfolio, Scanner scan) throws IOException {
    String path;
    view.displayCustomText(SAVE_RETRIEVE_PORTFOLIO_MENU);
    view.askForInput();
    String choice = scan.next();
    if (choice.isEmpty() || choice.isBlank()) {
      view.displayInvalidInput();
      return;
    }
    view.displayCustomText("Please enter the path: ");
    path = scan.next();
    try {
      if ("1".equals(choice)) {
        portfolio.savePortfolio(path);
        view.displayCustomText("Saved\n");
        displayExitOperationSequence(scan);
      } else if ("2".equals(choice)) {
        if (portfolio.retrievePortfolio(path)) {
          view.displayCustomText("Retrieved\n");
          displayExitOperationSequence(scan);
        } else {
          view.displayCustomText("Portfolio already has stocks\n");
        }

      } else if (!"E".equals(choice)) {
        view.displayInvalidInput();
      }
    } catch (Exception e) {
      view.displayInvalidInput();
    }
  }

  private void getStockValueForGivenDate(IPortfolio portfolio, Scanner scan) throws IOException {
    LocalDate date;
    view.displayCustomText("Please enter the date (yyyy-mm-dd): ");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    String userDate = scan.next();

    date = LocalDate.parse(userDate.isEmpty() || userDate.isBlank() ?
        String.valueOf(LocalDateTime.now()) : userDate);
    view.displayCustomText(portfolio.getPortfolioValue(date) + "\n");
    displayExitOperationSequence(scan);
  }

  private IPortfolio generatePortfolio(Scanner scan) throws IOException {
    String tickerSymbol;
    IPortfolio portfolio;
    int stockQuantity;
    Map<String, Integer> stocks = new HashMap<>();
    while (true) {
      view.displayCustomText(CREATE_PORTFOLIO_SUB_MENU);
      view.askForInput();
      String userInput = scan.next();
      if ("E".equals(userInput)) {
        portfolio = new Portfolio().setPortfolioStocks(stocks);
        view.clearScreen();
        break;
      } else if ("1".equals(userInput)) {
        view.displayCustomText("Stock Symbol: ");
        tickerSymbol = scan.next();
        if (tickerSymbol.isEmpty() || tickerSymbol.isBlank()) {
          view.displayInvalidInput();
          continue;
        }
        view.displayCustomText("Stock Quantity: ");
        try {
          stockQuantity = scan.nextInt();
        } catch (InputMismatchException e) {
          scan.nextLine();
          view.displayInvalidInput();
          continue;
        }

        stocks.put(tickerSymbol, stockQuantity);
      } else {
        view.displayInvalidInput();
      }
    }
    return portfolio;
  }
}
