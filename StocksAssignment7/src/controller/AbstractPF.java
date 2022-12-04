package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import model.Model;
import view.StockView;

/**
 * This abstract class has implementations for the flexible portfolio.
 * These implementations include getting total, composition and upload.
 */
public abstract class AbstractPF {

  private final Model model;
  private final StockView view;
  private final ControlValidations valid;
  ArrayList<LocalDate> datesFromAPI;
  private String filePath;

  /**
   * Constructor initialises the AbstractPF class to instantiate the model and view for
   * various operations to be performed with them.
   *
   * @param view         object of stockView interface
   * @param model        object of stockModel interface
   * @param filePath     absolute path of the file
   * @param datesFromAPI list of valid dates from api
   */
  public AbstractPF(StockView view, Model model, String filePath,
                    ArrayList<LocalDate> datesFromAPI) {
    this.model = model;
    this.view = view;
    this.filePath = filePath;
    valid = new ControlValidations(view, model, filePath);
    this.datesFromAPI = datesFromAPI;
  }

  /**
   * This method creates the portfolio by calling the view to display messages, and the model to
   * print the ticker symbols and number of shares.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user.
   * @param type to determine what type of portfolio is to be created.
   */
  protected void createPortfolio(Scanner scan, String type) {

    String morePortfoliosFlag = "Y";

    while (Objects.equals(morePortfoliosFlag.toUpperCase(), "YES") || Objects
            .equals(morePortfoliosFlag.toUpperCase(), "Y")) {
      this.view.getPFName();     //Tell view to ask for portfolio name from user
      String pfName = scan.next();

      // Check if pfName is unique
      if (this.model.isPfNameUnique(pfName, this.filePath)) {

        String moreStocksFlag = "Y";
        int writePF = 0;
        while (Objects.equals(moreStocksFlag.toUpperCase(), "YES") || Objects
                .equals(moreStocksFlag.toUpperCase(), "Y")) {

          String tickerSymbol = valid.tickerSymbolCheck(scan);
          double numShares = valid.numOfSharesCheck(scan);
          LocalDate date = null;
          double commFee = 0.0;
          if (Objects.equals(type, "Flexible")) {
            date = valid.dateCheck(scan, "purchasing", datesFromAPI);
            commFee = valid.commFeeCheck(scan);
          }
          if (writePF == 0) {
            this.model.writePortfolioName(pfName, this.filePath);
            writePF = 1;
          }

          //Tell model to save the stock details name in xml
          this.model.writeStockDetails(pfName, tickerSymbol, numShares, commFee,
                  this.filePath, date);
          this.view.moreTickerSymbols();
          moreStocksFlag = scan.next();
        }
        this.view.morePortfolios();
      } else {
        if (Objects.equals(type, "Flexible")) {
          this.view.errorMsg3();
        } else if (Objects.equals(type, "Inflexible")) {
          this.view.errorMsg12();
        }
      }
      morePortfoliosFlag = scan.next();
    }
  }

  /**
   * This method gets the file path of the new file which the user chooses to upload. Any and
   * all operations will now happen on the file present on this new path.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  protected void uploadExistingPortfolio(Scanner scan) {
    this.view.getFileURL();
    this.filePath = scan.next();
    while (!(this.model.checkValidFilePath(this.filePath))) {
      this.view.errorMsg4();
      this.view.getFileURL();
      this.filePath = scan.next();
    }
    this.view.successFileURL();
  }

  /**
   * This method gets the composition of a portfolio by calling the view to display messages, and
   * the model to calculate the composition based on a specified date.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user.
   * @param type to determine composition of what type of portfolio is required.
   */
  protected void getComposition(Scanner scan, String type) {
    String morePfName = "Y";

    while (Objects.equals(morePfName, "Yes") || Objects
            .equals(morePfName, "Y") || Objects.equals(morePfName, "y")) {
      this.view.askPfName("composition");   //Ask for single pf name whose composition is required
      String pfNameComp = scan.next();
      //LocalDate date = valid.dateCheck(scan, "composition");
      LocalDate date = null;
      if (Objects.equals(type, "Flexible")) {
        date = valid.dateCheck(scan, "composition", datesFromAPI);
      }
      // Check if pfName is unique
      if (!this.model.isPfNameUnique(pfNameComp, this.filePath)) {
        String fullPfDetails = this.model.getComposition(pfNameComp, this.filePath, date);
        if (!Objects.equals(fullPfDetails, "")) {
          this.view.printPfDetailsForComposition(fullPfDetails);
        } else {
          this.view.errorMsg11();
        }
        this.view.askMorePfName("composition");   //Ask for more pf names for composition.
        morePfName = scan.next();
      } else {
        this.view.errorMsg10();
      }
    }
  }

  /**
   * This method gets the total value of a portfolio by calling the view to display messages, and
   * the model to calculate the composition based on a specified date.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   * @param type to determine total is to be calculated for what type of portfolio.
   */
  protected void getTotal(Scanner scan, String type) {
    String moreTotal = "Y";

    while (Objects.equals(moreTotal, "Yes") || Objects
            .equals(moreTotal, "Y") || Objects.equals(moreTotal, "y")) {
      this.view.askPfName("total"); //Ask for single pf name whose total is required
      String pfNameTotal = scan.next();
      LocalDate date = valid.dateCheck(scan, "total", datesFromAPI);

      if (!this.model.isPfNameUnique(pfNameTotal, this.filePath)) {
        double pfTotal = this.model.getTotal(pfNameTotal, this.filePath, date, type);
        if (pfTotal == -1) {
          this.view.marketClosed(date);
        } else {
          this.view.printTotalPf(pfTotal, pfNameTotal);
        }
        this.view.askMorePfName("total");
        moreTotal = scan.next();
      } else {
        this.view.errorMsg10();
      }
    }
  }

}
