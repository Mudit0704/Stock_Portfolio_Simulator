package portfolio.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import portfolio.model.IPortfolios;
import portfolio.view.IView;

public class PortfolioController implements IPortfolioController {

  //region Variables
  private final static String CREATE_PORTFOLIO_SUB_MENU =
      "Choose from the below menu: \n 1 -> Add a new stock "
          + "\n E -> Exit from the operation \n";
  private final static String SAVE_RETRIEVE_PORTFOLIO_MENU =
      "Choose from the below menu: \n 1 -> Save portfolio "
          + "\n 2 -> Retrieve portfolio \n E -> Exit from the operation \n";
  private final static String CHOOSE_FROM_AVAILABLE_PORTFOLIOS = "Choose from available portfolios "
      + "(eg: Portfolio1 -> give 1):\n";

  private final Readable in;
  private final IView view;
  //endregion

  /**
   * Constructs an object of Portfolio Controller and initializes its members accordingly.
   *
   * @param in   A {@link Readable} object to get user input.
   * @param view A {@link IView} object to display application operations/results.
   */
  public PortfolioController(Readable in, IView view) {
    this.in = in;
    this.view = view;
  }

  //region Public methods
  @Override
  public void run(IPortfolios portfolios) throws IOException {
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
          scan.nextLine();
          break;
      }
    }

  }
  //endregion

  //region Private Methods
  private void getPortfolioComposition(IPortfolios portfolios, Scanner scan)
      throws IOException {
    view.clearScreen();
    String result;
    while (true) {
      try {
        view.displayCustomText(CHOOSE_FROM_AVAILABLE_PORTFOLIOS);
        String availablePortfolios = portfolios.getAvailablePortfolios();
        view.displayCustomText(availablePortfolios);
        view.askForInput();
        scan.nextLine();
        int portfolioId = scan.nextInt();
        result = portfolios.getPortfolioComposition(portfolioId);
        break;
      } catch (InputMismatchException | IllegalArgumentException e) {
        if ("No portfolios".equals(e.getMessage())) {
          view.displayCustomText("No portfolios\n");
          displayExitOperationSequence(scan);
          return;
        }
        view.displayInvalidInput();
      }
    }

    view.displayCustomText(result);
    displayExitOperationSequence(scan);
  }

  private void saveRetrievePortfolios(IPortfolios portfolios, Scanner scan)
      throws IOException {
    try {
      while (true) {
        view.displayCustomText(SAVE_RETRIEVE_PORTFOLIO_MENU);
        view.askForInput();
        switch (scan.next()) {
          case "E":
            return;
          case "1":
            portfolios.savePortfolios();
            view.displayCustomText("Saved\n");
            displayExitOperationSequence(scan);
            return;
          case "2":
            portfolios.retrievePortfolios();
            view.displayCustomText("Retrieved\n");
            displayExitOperationSequence(scan);
            return;
          default:
            view.displayInvalidInput();
            scan.nextLine();
            break;
        }
      }
    } catch (RuntimeException | ParserConfigurationException | SAXException e) {
      if ("No portfolios to save\n".equals(e.getMessage())
          || "Portfolios already populated\n".equals(e.getMessage())) {
        view.displayCustomText(e.getMessage());
      } else {
        view.displayCustomText("Encountered a problem while saving or retrieving \n");
      }
    } catch (FileNotFoundException e) {
      view.displayCustomText("No files found to retrieve\n");
    }
    displayExitOperationSequence(scan);
  }

  private void getPortfolioValuesForGivenDate(IPortfolios portfolios, Scanner scan)
      throws IOException {
    LocalDate date;
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
        view.displayCustomText("Please enter the date (yyyy-mm-dd): ");
        date = LocalDate.parse(scan.next());
        String portfolioValue = String.format("%.2f",
            portfolios.getPortfolioValue(date, portfolioId));
        view.displayCustomText("Portfolio" + portfolioId + "\n" + portfolioValue + "\n");
        break;
      } catch (DateTimeParseException | IllegalArgumentException | InputMismatchException e) {
        if ("No portfolios".equals(e.getMessage())) {
          view.displayCustomText("No portfolios\n");
          displayExitOperationSequence(scan);
          return;
        }
        view.displayInvalidInput();
      }
      catch (RuntimeException e) {
        view.displayCustomText("\nFailed to get value, please try again after some time...\n");
      }
    }

    displayExitOperationSequence(scan);
  }

  private void generatePortfolios(Scanner scan, IPortfolios portfolios)
      throws IOException {
    Map<String, Long> stocks = new HashMap<>();
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
          scan.nextLine();
          break;
      }
    }
  }

  private void addNewStock(Scanner scan, IPortfolios portfolios, Map<String, Long> stocks)
      throws IOException {
    String tickerSymbol;
    long stockQuantity;
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
    if(stocks.containsKey(tickerSymbol)) {
      stocks.put(tickerSymbol, stocks.get(tickerSymbol) + stockQuantity);
    }
    else {
      stocks.put(tickerSymbol, stockQuantity);
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
  //endregion
}
