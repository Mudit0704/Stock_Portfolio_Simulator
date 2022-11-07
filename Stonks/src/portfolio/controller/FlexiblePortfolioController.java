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

  private static final String MAIN_MENU =
      "Choose from the below menu: \n 1 -> Create a static portfolio \n"
          + " 2 -> Create a flexible portfolio \n"
          + " E -> Exit from the application \n";
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
    Integer portfolioId;
    portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
    if(portfolioId == null) return;
    LocalDate date = controllerHelper.populateDateFromUser(scan);

    view.displayCustomText(String.valueOf(portfolios.getCostBasis(date, portfolioId)));
  }

  void sellAStock(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    Integer portfolioId;
    portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
    if(portfolioId == null) return;

    Map<String, Long> stock = new LinkedHashMap<>();
    controllerHelper.populateStockDateFromUser(scan, portfolios, stock);
    Entry<String, Long> entry = stock.entrySet().iterator().next();
    portfolios.sellStockFromPortfolio(entry.getKey(), entry.getValue(), portfolioId);
  }

  void purchaseNewStock(IFlexiblePortfoliosModel portfolios, Scanner scan) throws IOException {
    Integer portfolioId;
    portfolioId = controllerHelper.populatePortfolioIdFromUser(portfolios, scan);
    if(portfolioId == null) return;

    Map<String, Long> stock = new LinkedHashMap<>();
    controllerHelper.populateStockDateFromUser(scan, portfolios, stock);
    Entry<String, Long> entry = stock.entrySet().iterator().next();
    portfolios.addStocksToPortfolio(entry.getKey(), entry.getValue(), portfolioId);
  }

  void populateStockAndPortfolioId(Map<String, Long> stock, Integer portfolioId) {

  }
}
