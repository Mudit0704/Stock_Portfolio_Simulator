package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import model.Model;
import view.StockView;

/**
 * This class extends the AbstractPF class and calls the view to display the submenu for the
 * flexible portfolios. Options include creation of a portfolio , modifying a portfolio, uploading
 * existing portfolio, composition examination, cost basis, total value of a portfolio and
 * performance graph over an interval.
 */
public class FlexiblePF extends AbstractPF {

  StockView view;
  Model model;
  ControlValidations valid;
  String filePath;
  ArrayList<LocalDate> datesFromAPI;

  /**
   * Constructs a Flexible portfolio that calls the model and view to perform various actions on a
   * flexible portfolio such as creation, update, getting composition, total and cost basis and
   * performance analysis of a portfolio over a certain range.
   *
   * @param model    object of stockModel interface
   * @param view     object of stockView interface
   * @param filePath absolute path of the file
   */
  public FlexiblePF(StockView view, Model model, String filePath,
      ArrayList<LocalDate> datesFromAPI) {
    super(view, model, filePath, datesFromAPI);
    this.view = view;
    this.model = model;
    this.filePath = filePath;
    valid = new ControlValidations(view, model, filePath);
    this.datesFromAPI = datesFromAPI;
  }

  /**
   * This implementation displays the submenu for the flexible portfolios. Options include uploading
   * existing portfolio, composition examination, cost basis and total value of a portfolio.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  public void flexiblePF(Scanner scan) {
    int inputChoice = 1;
    while (inputChoice == 1) {
      view.printSubMenu2();
      try {
        int choice = scan.nextInt();
        if (choice == 1) {          // Create new flexible portfolio
          this.createPortfolio(scan, "Flexible");
          // } else if (choice == 2) {   // Modify Existing flexible Portfolio
          // this.createOrModifyPortfolioUsingDCA(scan, "create");
        } else if (choice == 2) {   // Modify Existing flexible Portfolio
          this.modifyPortfolio(scan);
        } else if (choice == 3) {   // Upload existing portfolio
          this.uploadExistingPortfolio(scan);
        } else if (choice == 4) {   // Examine composition
          this.getComposition(scan, "Flexible");
        } else if (choice == 5) {   // Cost Basis
          this.getCostBasis(scan);
        } else if (choice == 6) {   // Get total of a portfolio
          this.getTotal(scan, "Flexible");
        } else if (choice == 7) {   // Get performance
          this.getPerformanceGraph(scan);
        } else if (choice == 8) {   // Quit
          this.rebalancePortfolio(scan);
        } else if (choice == 9) {   // Quit
          System.out.println();
          inputChoice = 0;
        } else {                    // Invalid input
          view.errorMsg1();
        }
      } catch (Exception e) {
        //
      }
    }
  }

  /**
   * This method modifies the portfolio by calling the view to display messages, and the model to
   * edit the portfolio by updating the ticker symbols and stocks.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  private void modifyPortfolio(Scanner scan) {
    String moreModifyFlag = "Y";

    while (Objects.equals(moreModifyFlag.toUpperCase(), "YES") || Objects.equals(
        moreModifyFlag.toUpperCase(), "Y")) {

      view.modifyGetPfName();
      String pfName = scan.next();
      if (model.isPfNameUnique(pfName, this.filePath)) {
        view.errorMsg2();
        moreModifyFlag = scan.next();
        continue;
      } else {
        view.modifyPurchaseOrSell();
        String modifyOption = scan.next();
        if (Objects.equals(modifyOption, "1")) {
          String moreStocksFlag = "Y";
          while (Objects.equals(moreStocksFlag.toUpperCase(), "YES") || Objects.equals(
              moreStocksFlag.toUpperCase(), "Y")) {

            String tickerSymbol = valid.tickerSymbolCheck(scan);
            double numShares = valid.numOfSharesCheck(scan);
            LocalDate date = valid.dateCheck(scan, "purchasing", this.datesFromAPI);
            double commFee = valid.commFeeCheck(scan);

            //Tell model to save the stock details name in xml
            model.writeStockDetails(pfName, tickerSymbol, numShares, commFee, this.filePath, date);
            view.moreTickerSymbols();
            moreStocksFlag = scan.next();
          }

        } else if (Objects.equals(modifyOption, "2")) {
          String moreStocksFlag = "Y";
          while (Objects.equals(moreStocksFlag.toUpperCase(), "YES") || Objects.equals(
              moreStocksFlag.toUpperCase(), "Y")) {

            String tickerSymbol = valid.tickerSymbolCheck(scan);
            double numShares = valid.numOfSharesCheck(scan);
            numShares = numShares * (-1);
            LocalDate date = valid.dateCheck(scan, "selling", this.datesFromAPI);
            double commFee = valid.commFeeCheck(scan);

            if (model.checkIfCanSell(pfName, tickerSymbol, numShares, this.filePath, date)) {
              model.writeStockDetails(pfName, tickerSymbol, numShares, commFee, this.filePath,
                  date);
            } else {
              view.errorMsg7();
              String moreSellFlag = scan.next();
              if (Objects.equals(moreSellFlag.toUpperCase(), "YES") || Objects.equals(
                  moreSellFlag.toUpperCase(), "Y")) {
                continue;
              }
            }
            view.modifyMoreStocks();
            moreStocksFlag = scan.next();
          }

          //        } else if (Objects.equals(modifyOption.toUpperCase(), "2")) {
          //          createOrModifyPortfolioUsingDCA(scan, "modify");
        } else {
          view.errorMsg6();
        }
      }
      view.moreModifications();
      moreModifyFlag = scan.next();
    }
  }

  /**
   * This method gets the cost basis of a portfolio by calling the view to display messages, and the
   * model to calculate the cost basis based on a specified date.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  private void getCostBasis(Scanner scan) {
    String moreTotal = "Y";

    while (Objects.equals(moreTotal, "Yes") || Objects.equals(moreTotal, "Y") || Objects
        .equals(moreTotal, "y")) {
      view.askPfName("cost basis"); //Ask for single pf name whose total is required
      String pfNameCB = scan.next();
      LocalDate date = valid.dateCheck(scan, "cost basis", this.datesFromAPI);

      if (!model.isPfNameUnique(pfNameCB, this.filePath)) {
        double pfCostBasis = model.getCostBasis(pfNameCB, this.filePath, date);
        view.printCostBasis(pfCostBasis, pfNameCB, date);
        view.askMorePfName("cost basis");
        moreTotal = scan.next();
      } else {
        view.errorMsg10();
      }
    }
  }

  /**
   * This method gets the performance of a portfolio over a certain time range by calling the view
   * and the model to calculate the total and plot the same.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  private void getPerformanceGraph(Scanner scan) {
    String morePfName = "Y";

    while (Objects.equals(morePfName, "Yes") || Objects.equals(morePfName, "Y") || Objects
        .equals(morePfName, "y")) {
      view.askPfName("performance LineChartView");
      String pfNameGraph = scan.next();
      LocalDate date1 = valid.dateCheck(scan, "Performance1", this.datesFromAPI);
      LocalDate date2 = valid.dateCheck(scan, "Performance2", this.datesFromAPI);

      if (!model.isPfNameUnique(pfNameGraph, this.filePath)) {
        StringBuilder pfGraph = model.getPerformanceGraph(pfNameGraph, this.filePath, date1, date2,
            this.datesFromAPI);

        view.printGraph(pfGraph, pfNameGraph, date1, date2);
        view.askMorePfName("performance LineChartView");
        morePfName = scan.next();
      } else {
        view.errorMsg10();
      }
    }
  }

  private void rebalancePortfolio(Scanner scan) {
    String morePfName = "Y";

    while (Objects.equals(morePfName, "Yes") || Objects.equals(morePfName, "Y") || Objects
        .equals(morePfName, "y")) {
      view.askPfName("rebalance");
      String pfNameGraph = scan.next();
      LocalDate date = valid.dateCheck(scan, "rebalance", this.datesFromAPI);

      if (!model.isPfNameUnique(pfNameGraph, this.filePath)) {
        String fullPfDetails = this.model.getComposition(pfNameGraph, this.filePath, date);
        if (!Objects.equals(fullPfDetails, "")) {
          this.view.printPfDetailsForComposition(fullPfDetails);
        } else {
          this.view.errorMsg11();
        }

        String tickerSymbol;
        double percentage;
        do {
          tickerSymbol = valid.tickerSymbolCheck(scan);
          percentage = valid.percentageCheck(scan, tickerSymbol);  //percentage of stock

          if (this.model.percentageSumMoreThan100(percentage)) {
            view.errorMsg19();
          } else {
            this.model.saveTsAndPerc(tickerSymbol, percentage);
          }
        } while (!(model.isPercentagesSum100()));
        model.rebalanceExistingPortfolio(pfNameGraph, this.filePath, date,
            this.datesFromAPI, "Flexible");

        view.askMorePfName("rebalance");
        morePfName = scan.next();
      } else {
        view.errorMsg10();
      }
    }
  }

  //
  //  private void createOrModifyPortfolioUsingDCA(Scanner scan, String purpose) {
  //
  //    String morePortfoliosFlag = "Y";
  //
  //    while (Objects.equals(morePortfoliosFlag.toUpperCase(), "YES") || Objects
  //            .equals(morePortfoliosFlag.toUpperCase(), "Y")) {
  //      if (Objects.equals(purpose, "create")) {
  //        this.view.getPFName();     //Tell view to ask for portfolio name from user
  //      } else if (Objects.equals(purpose, "modify")) {
  //        this.view.modifyGetPfName();
  //      }
  //      String pfName = scan.next();
  //
  //      // Check if pfName is unique
  //      if ((this.model.isPfNameUnique(pfName, this.filePath)&&Objects.equals(purpose,"create"))
  //              || (!(this.model.isPfNameUnique(pfName, this.filePath)) && Objects.equals
  //              (purpose, "modify")) ) {
  //
  //        Double invtAmt = valid.investmentAmountCheck(scan); //investment amount
  //
  //        String tickerSymbol;
  //        double percentage;
  //        do {
  //          tickerSymbol = valid.tickerSymbolCheck(scan);
  //          percentage = valid.percentageCheck(scan, tickerSymbol);  //percentage of stock
  //
  //          if (this.model.percentageSumMoreThan100(percentage)) {
  //            view.errorMsg19();
  //          } else {
  //            this.model.saveTsAndPerc(tickerSymbol, percentage);
  //          }
  //        } while(!(model.isPercentagesSum100()));
  //
  //        LocalDate startDate = valid.startDateCheckForDCA(scan);  // start date DCA
  //        LocalDate endDate = valid.endDateCheckForDCA(scan, startDate);    // end date DCA
  //        int intervals = valid.intervalCheck(scan);
  //        this.model.dollarCostAveraging(pfName, invtAmt, startDate, endDate, intervals,
  //                this.filePath, datesFromAPI, purpose);
  //
  //        view.successDCAPortfolios();
  //      } else {
  //        if (Objects.equals(purpose, "create")) {
  //          this.view.errorMsg3();
  //        } else if (Objects.equals(purpose, "modify")) {
  //          this.view.errorMsg2();
  //        }
  //      }
  //      morePortfoliosFlag = scan.next();
  //    }
  //  }

}
