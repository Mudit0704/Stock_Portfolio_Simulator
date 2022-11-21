package portfolio.controller;

import static portfolio.controller.PortfolioController.CHOOSE_FROM_AVAILABLE_PORTFOLIOS;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import portfolio.model.IPortfoliosModel;
import portfolio.view.IView;

/**
 * Controller helper class which stores the common logic used by the controllers.
 */
class ControllerHelper {

  private final IView view;

  /**
   * Constructs an object of Controller helper and initializes its members.
   * @param view view object to display the output to the user.
   */
  ControllerHelper(IView view) {
    this.view = view;
  }

  protected Integer populatePortfolioIdFromUser(IPortfoliosModel portfolios, Scanner scan)
      throws IOException {
    int portfolioId;
    while (true) {
      try {
        view.displayCustomText(CHOOSE_FROM_AVAILABLE_PORTFOLIOS);
        String availablePortfolios = portfolios.getAvailablePortfolios();
        view.displayCustomText(availablePortfolios);
        view.askForInput();
        scan.nextLine();
        portfolioId = scan.nextInt();
        if (portfolioId <= 0) {
          throw new IllegalArgumentException();
        }
        break;
      } catch (InputMismatchException | IllegalArgumentException e) {
        if ("No portfolios".equals(e.getMessage())) {
          view.displayCustomText("No portfolios\n");
          performExitOperationSequence(scan);
          return null;
        }
        view.displayInvalidInput();
      }
    }
    return portfolioId;
  }

  protected LocalDate populateDateFromUser(Scanner scan) throws IOException {
    LocalDate date;
    while (true) {
      try {
        view.displayCustomText("Please enter the date (yyyy-mm-dd): ");
        date = LocalDate.parse(scan.next());
        break;
      } catch (DateTimeParseException e) {
        scan.nextLine();
        view.displayInvalidInput();
      }
    }
    return date;
  }

  protected void populateStockDataFromUser(Scanner scan, IPortfoliosModel portfolios,
      Map<String, Double> stocks)
      throws IOException {
    String tickerSymbol;
    double stockQuantity;
    view.displayCustomText("Stock Symbol: ");
    scan.nextLine();
    tickerSymbol = scan.nextLine();
    while (!portfolios.isTickerSymbolValid(tickerSymbol)) {
      view.displayCustomText("Invalid Ticker Symbol\n");
      view.displayCustomText("Stock Symbol: ");
      tickerSymbol = scan.nextLine();
    }
    while (true) {
      view.displayCustomText("Stock Quantity: ");
      try {
        stockQuantity = scan.nextLong();
        if (stockQuantity <= 0) {
          throw new IllegalArgumentException();
        }
        break;
      } catch (InputMismatchException | IllegalArgumentException e) {
        scan.nextLine();
        view.displayInvalidInput();
      }
    }
    if (stocks.containsKey(tickerSymbol)) {
      stocks.put(tickerSymbol, stocks.get(tickerSymbol) + stockQuantity);
    } else {
      stocks.put(tickerSymbol, stockQuantity);
    }
  }

  protected void performExitOperationSequence(Scanner scan) throws IOException {
    view.displayEscapeFromOperation();
    scan.nextLine();
    while (!"E".equals(scan.next())) {
      scan.nextLine();
      view.displayInvalidInput();
      view.displayEscapeFromOperation();
    }
  }
}
