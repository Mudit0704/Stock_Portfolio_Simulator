package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import portfolio.model.IFlexiblePortfoliosModel;
import portfolio.model.IPortfoliosModel;
import portfolio.model.PortfoliosModel;
import portfolio.model.ServiceType;
import portfolio.view.IView;
import portfolio.view.View;

public class FlexiblePortfolioController extends PortfolioController implements
    IFlexiblePortfolioController {

  private static final String MAIN_MENU =
      "Choose from the below menu: \n 1 -> Create a static portfolio \n"
          + " 2 -> Create a flexible portfolio \n"
          + " E -> Exit from the application \n";
  private final Readable in;
  private final IView view;

  /**
   * Constructs an object of Portfolio Controller and initializes its members accordingly.
   *
   * @param in   A {@link Readable} object to get user input.
   * @param view A {@link IView} object to display application operations/results.
   */
  public FlexiblePortfolioController(Readable in, IView view) {
    super(in, new View(System.out));
    this.in = in;
    this.view = view;
  }

  @Override
  public void run(IFlexiblePortfoliosModel portfolios) throws IOException {
    Scanner scan = new Scanner(this.in);

    try {
      while (true) {
        view.displayCustomText(MAIN_MENU);
        view.askForInput();
        switch (scan.next()) {
          case "1":
            super.run(new PortfoliosModel());
            break;
          case "2":
            runFlexiblePortfolioOperations(portfolios, scan);
            break;
          case "E":
            return;
          default:
            view.displayInvalidInput();
            scan.nextLine();
            break;
        }
      }
    } catch (NoSuchElementException e) {
      view.displayCustomText("\n----Exiting----\n");
    }
  }

  private void runFlexiblePortfolioOperations(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {
    portfolios.setServiceType(ServiceType.ALPHAVANTAGE);
    while (true) {
      view.displayMenu();
      view.askForInput();
      switch (scan.next()) {
        case "1":
          super.generatePortfolios(scan, portfolios);
          break;
        case "2":
          super.getPortfolioComposition(portfolios, scan);
          break;
        case "3":
          super.getPortfolioValuesForGivenDate(portfolios, scan);
          break;
        case "4":
          super.saveRetrievePortfolios(portfolios, scan);
          break;
        case "5":
          purchaseNewStock(portfolios, scan);
          break;
        case "6":
          sellAStock(portfolios, scan);
          break;
        case "7":
          getPortfolioCostBasis(portfolios, scan);
          break;
        case "8":
          getPortfolioPerformance(portfolios, scan);
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

  void getPortfolioPerformance(IFlexiblePortfoliosModel portfolios, Scanner scan) {
  }

  void getPortfolioCostBasis(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    int portfolioId;
    try {
      portfolioId = askForPortfolioId(portfolios, scan);
    } catch (IllegalArgumentException e) {
      return;
    }
    LocalDate date = askForDate(scan);

    view.displayCustomText(String.valueOf(portfolios.getCostBasis(date, portfolioId)));
  }

  void sellAStock(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    int portfolioId;
    Map<String, Long> stock = new LinkedHashMap<>();
    try {
      portfolioId = askForPortfolioId(portfolios, scan);
    } catch (IllegalArgumentException e) {
      return;
    }

    super.addNewStock(scan, portfolios, stock);
    Entry<String, Long> entry = stock.entrySet().iterator().next();
    portfolios.sellStockFromPortfolio(entry.getKey(), entry.getValue(), portfolioId);
  }

  void purchaseNewStock(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    int portfolioId;
    Map<String, Long> stock = new LinkedHashMap<>();
    try {
      portfolioId = askForPortfolioId(portfolios, scan);
    } catch (IllegalArgumentException e) {
      return;
    }

    super.addNewStock(scan, portfolios, stock);
    Entry<String, Long> entry = stock.entrySet().iterator().next();
    portfolios.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioId);
  }

  int askForPortfolioId(IPortfoliosModel portfolios, Scanner scan) throws IOException {
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
          displayExitOperationSequence(scan);
          throw new IllegalArgumentException();
        }
        view.displayInvalidInput();
      }
    }
    return portfolioId;
  }

  LocalDate askForDate(Scanner scan) throws IOException {
    LocalDate date;
    while (true) {
      try {
        view.displayCustomText("Please enter the date (yyyy-mm-dd): ");
        date = LocalDate.parse(scan.next());
        break;
      } catch (DateTimeParseException | IllegalArgumentException | InputMismatchException e) {
        view.displayInvalidInput();
      }
    }
    return date;
  }
}
