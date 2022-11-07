package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
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

  void runFlexiblePortfolioOperations(IFlexiblePortfoliosModel portfolios, Scanner scan)
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
          performTransaction(portfolios, scan);
          break;
        case "6":
          getPortfolioCostBasis(portfolios, scan);
          break;
        case "7":
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
    //view.displayCustomText(portfolios);
  }

  void getPortfolioCostBasis(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    Integer portfolioId;
    portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
    if (portfolioId == null) {
      return;
    }
    LocalDate date = controllerHelper.populateDateFromUser(scan);

    view.displayCustomText(String.valueOf(portfolios.getCostBasis(date, portfolioId)));
  }

  void performTransaction(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    view.displayCustomText("NOTE : You will be charged a commission fee for each transaction \n");

    while (true) {
      Integer portfolioId;
      portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
      if (portfolioId == null) {
        return;
      }
      view.displayCustomText("Please provide stock details for the transaction: \n");
      Map<String, Long> stock = new LinkedHashMap<>();
      controllerHelper.populateStockDateFromUser(scan, portfolios, stock);
      Entry<String, Long> entry = stock.entrySet().iterator().next();
      view.displayCustomText(TRANSACTION_MENU);
      view.askForInput();
      try {
        switch (scan.next()) {
          case "1":
            portfolios.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioId);
            view.displayCustomText("Purchased\n");
            controllerHelper.displayExitOperationSequence(scan);
            return;
          case "2":
            portfolios.sellStockFromPortfolio(entry.getKey(), entry.getValue(), portfolioId);
            view.displayCustomText("Sold\n");
            controllerHelper.displayExitOperationSequence(scan);
            return;
          case "E":
            return;
          default:
            view.displayInvalidInput();
            scan.nextLine();
            break;
        }
      } catch (IllegalArgumentException e){
        if("Invalid portfolio Id".equals(e.getMessage())) {
          view.displayCustomText(e.getMessage()+"\n");
        }
      }
    }
  }
}
