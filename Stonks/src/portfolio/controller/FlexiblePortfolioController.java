package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import portfolio.model.IFlexiblePortfoliosModel;
import portfolio.model.PortfoliosModel;
import portfolio.model.ServiceType;
import portfolio.view.IView;
import portfolio.view.View;

/**
 * This class represents the implementation of the controller of this application. It takes user
 * inputs and performs the operations by forwarding them to the model. Also takes model's response
 * and outputs to the view.
 */
public class FlexiblePortfolioController extends PortfolioController implements
    IFlexiblePortfolioController {

  protected static final String MAIN_MENU =
      "Choose from the below menu: \n 1 -> Create a static portfolio \n"
          + " 2 -> Create a flexible portfolio \n"
          + " E -> Exit from the application \n";

  protected static final String TRANSACTION_MENU =
      "Choose from the below menu: \n 1 -> Purchase a new stock \n"
          + " 2 -> Sell a stock \n"
          + " E -> Exit from the operation \n";
  private final Readable in;
  private final IView view;
  private final ControllerHelper controllerHelper;

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
    this.controllerHelper = new ControllerHelper(view);
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

  protected void runFlexiblePortfolioOperations(IFlexiblePortfoliosModel portfolios, Scanner scan)
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
          getPortfolioComposition(portfolios, scan);
          break;
        case "3":
          super.getPortfolioValuesForGivenDate(portfolios, scan);
          break;
        case "4":
          super.saveRetrievePortfolios(portfolios, scan);
          break;
        case "5":
          performTransaction(portfolios, scan);
          break;
        case "6":
          getPortfolioCostBasis(portfolios, scan);
          break;
        case "7":
          getPortfolioPerformance(portfolios, scan);
          break;
        case "8":
          setCommissionFee(portfolios, scan);
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

  protected void setCommissionFee(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {
    while (true) {
      try {
        view.displayCustomText("Please enter the fee value: \n");
        view.askForInput();
        double commissionFee = scan.nextDouble();
        portfolios.setCommissionFee(commissionFee);
        view.displayCustomText("Fee updated.\n");
        controllerHelper.performExitOperationSequence(scan);
        return;
      } catch (InputMismatchException e) {
        scan.nextLine();
        view.displayInvalidInput();
      }
    }
  }

  protected void getPortfolioPerformance(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {
    while (true) {
      try {
        Integer portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
        if (portfolioId == null) {
          return;
        }
        LocalDate startDate = controllerHelper.populateDateFromUser(scan);
        LocalDate endDate = controllerHelper.populateDateFromUser(scan);
        view.displayCustomText(portfolios.getPortfolioPerformance(portfolioId, startDate, endDate));
        controllerHelper.performExitOperationSequence(scan);
        return;
      } catch (IllegalArgumentException e) {
        view.displayCustomText(e.getMessage());
      }
    }
  }

  protected void getPortfolioCostBasis(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {
    while (true) {
      try {
        Integer portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
        if (portfolioId == null) {
          return;
        }
        LocalDate date = controllerHelper.populateDateFromUser(scan);
        view.displayCustomText(String.valueOf(portfolios.getCostBasis(date, portfolioId)));
        controllerHelper.performExitOperationSequence(scan);
        break;
      } catch (IllegalArgumentException e) {
        view.displayCustomText(e.getMessage());
      }
    }
  }

  protected void performTransaction(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {
    while (true) {
      Integer portfolioId;
      try {
        portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
        if (portfolioId == null) {
          return;
        }
        view.displayCustomText("Please provide stock details for the transaction: \n");
        Map<String, Long> stock = new LinkedHashMap<>();
        controllerHelper.populateStockDataFromUser(scan, portfolios, stock);
        Entry<String, Long> entry = stock.entrySet().iterator().next();
        view.displayCustomText("Please enter date for the transaction: \n");
        LocalDate date = controllerHelper.populateDateFromUser(scan);
        if (date.isAfter(LocalDate.now())) {
          throw new IllegalArgumentException("Invalid date for transaction.");
        }
        view.displayCustomText(TRANSACTION_MENU);
        view.askForInput();
        switch (scan.next()) {
          case "1":
            portfolios.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioId, date);
            view.displayCustomText("Purchased\n");
            controllerHelper.performExitOperationSequence(scan);
            return;
          case "2":
            portfolios.sellStockFromPortfolio(entry.getKey(), entry.getValue(), portfolioId, date);
            view.displayCustomText("Sold\n");
            controllerHelper.performExitOperationSequence(scan);
            return;
          case "E":
            return;
          default:
            view.displayInvalidInput();
            scan.nextLine();
            break;
        }
      } catch (IllegalArgumentException e) {
        view.displayCustomText(e.getMessage() + "\n");
      }
    }
  }

  protected void getPortfolioComposition(IFlexiblePortfoliosModel portfolios, Scanner scan)
      throws IOException {

    String result;
    while (true) {
      try {
        Integer portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
        if (portfolioId == null) {
          return;
        }
        LocalDate date = controllerHelper.populateDateFromUser(scan);
        result = portfolios.getPortfolioCompositionOnADate(portfolioId, date);
        break;
      } catch (IllegalArgumentException e) {
        view.displayCustomText(e.getMessage());
      }
    }

    view.displayCustomText(result);
    controllerHelper.performExitOperationSequence(scan);
  }
}
