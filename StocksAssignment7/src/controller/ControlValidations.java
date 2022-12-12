package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import model.Model;
import view.StockView;

/**
 * This class tracks all the validation checks to be performed by the model on inputs from the user.
 * It takes the input and passes it to the model for validations. Until the model confirms, methods
 * of this class will repeatedly call the view to display error messages and then later ask for
 * inputs.
 */
public class ControlValidations {

  StockView view;
  Model model;
  String filePath;

  /**
   * Constructor that calls the model and view to validate the portfolio details based on the inputs
   * like dates, ticker symbol, stocks, percentage, amount etc.
   *
   * @param model    object of stockModel interface
   * @param view     object of stockView interface
   * @param filePath absolute path of the file
   */
  public ControlValidations(StockView view, Model model, String filePath) {
    this.view = view;
    this.model = model;
    this.filePath = filePath;
  }

  /**
   * This method takes in the date for performing operations (say composition, total, cost basis
   * etc.) as and when entered by the user. It takes the date as a string and passes it to the model
   * for validating.
   *
   * @param scan    (Scanner) object of Scanner class that gets inputs from the user
   * @param purpose (String) purpose denotes if it's for buying or selling the portfolio
   * @return date in LocalDate format
   */
  public LocalDate dateCheck(Scanner scan, String purpose, ArrayList<LocalDate> datesFromAPI) {
    int flag = 0;
    LocalDate date = null;
    String strDate;
    while (flag == 0) {     // controller uses flag to call view for dates repeatedly if incorrect
      // date is passed.
      if (Objects.equals(purpose, "Performance1") || Objects.equals(purpose, "Performance2")) {
        view.askPerformanceDate(purpose);
      } else {
        view.askDate(purpose);
      }
      strDate = scan.next();
      if (model.isDateValid(strDate, purpose, datesFromAPI)) {     // Model validates date
        date = LocalDate.parse(strDate);
        flag = 1;
      } else {
        view.errorMsg5();
      }
    }
    return date;
  }

  /**
   * This method takes in the number of shares of a stock the user wants to buy for creating or
   * modifying the portfolio.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   * @return the number of shares (double).
   */
  public double numOfSharesCheck(Scanner scan) {
    view.getNoOfShares();   //Tell view to ask for number of shares user wants to buy.
    String numShares = scan.next();
    while (!model.isNumberOfSharesValid(numShares)) {   //Check if input is valid.
      view.errorMsg9();
      view.getNoOfShares();
      numShares = scan.next();
    }
    return model.getNumberOfShares(numShares);   //Get number of stocks
  }

  /**
   * This method takes the ticker symbol for creating or modifying the portfolio.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   * @return the ticker symbol (String)
   */
  public String tickerSymbolCheck(Scanner scan) {
    view.getTickerSymbol();   // Tell view to ask for ticker symbols from user
    String tickerSymbol = scan.next();
    while (!model.isTickerSymbolValid(tickerSymbol)) {
      view.errorMsg8();
      view.getTickerSymbol();
      tickerSymbol = scan.next();
    }
    return tickerSymbol;
  }

  /**
   * This method asks the view for commission fee and pass it to the model.
   *
   * @param scan – (Scanner) object of Scanner class that gets inputs from the user
   * @return commission fee (Double)
   */
  public Double commFeeCheck(Scanner scan) {
    view.getCommFee();   //Tell view to ask for commission fee
    String commFee = scan.next();
    while (!model.isCommFeeValid(commFee)) {
      view.errorMsg20();
      view.getCommFee();
      commFee = scan.next();
    }
    return model.getCommFee(commFee);
  }

  /**
   * This method validates the user input for percentage and returns its double representation.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   * @param ts   Ticker Symbol of stock for which percentage has to be asked
   * @return double representation of String percentage
   */
  public double percentageCheck(Scanner scan, String ts) {
    view.getPercentageForDCA(ts);   //Tell view to ask for percentage for DCA
    String perc = scan.next();
    while (!model.isPercentageValid(perc)) {   //Check if input is valid.
      view.errorMsg15();
      view.getPercentageForDCA(ts);
      perc = scan.next();
    }
    return model.getPercentage(perc);   //Get percentage
  }

  //
  //  public Double investmentAmountCheck(Scanner scan) {
  //    view.getCommFee();   //Tell view to ask for investment amount
  //    String invtAmt = scan.next();
  //    while(!model.isCommFeeValid(invtAmt)) {
  //      view.errorMsg13();
  //      view.getCommFee();
  //      invtAmt = scan.next();
  //    }
  //    return model.getCommFee(invtAmt);
  //  }
  //
  //  public int numOfStockCheck(Scanner scan) {
  //    view.getNumOfSharesForDCA();   //Tell view to ask for number of stocks for DCA
  //    String numOfStocks = scan.next();
  //    while(!model.isNumOfStocksValid(numOfStocks)) {   //Check if input is valid.
  //      view.errorMsg14();
  //      view.getInvestmentAmount();
  //      numOfStocks = scan.next();
  //    }
  //    return model.getNumOfStock(numOfStocks);   //Get number of stocks
  //  }
  //
  //  public double percentageCheck(Scanner scan, String ts) {
  //    view.getPercentageForDCA(ts);   //Tell view to ask for percentage for DCA
  //    String perc = scan.next();
  //    while(!model.isPercentageValid(perc)) {   //Check if input is valid.
  //      view.errorMsg15();
  //      view.getPercentageForDCA(ts);
  //      perc = scan.next();
  //    }
  //    return model.getPercentage(perc);   //Get percentage
  //  }
  //
  //  public int intervalCheck(Scanner scan) {
  //    view.getIntervalForDCA();   //Tell view to ask for interval for DCA
  //    String interval = scan.next();
  //    while(!model.isIntervalValid(interval)) {   //Check if input is valid.
  //      view.errorMsg16();
  //      view.getIntervalForDCA();
  //      interval = scan.next();
  //    }
  //    return model.getInterval(interval);   //Get interval
  //  }
  //
  //  public String tickerSymbolForDCACheck(Scanner scan, int i) {
  //    view.getTSNameForDCA(i);   //Tell view to ask for ticker symbol for DCA
  //    String tickerSymbol = scan.next();
  //    while (!model.isTickerSymbolValid(tickerSymbol)) {
  //      view.errorMsg8();
  //      view.getTickerSymbol();
  //      tickerSymbol = scan.next();
  //    }
  //    return model.getTickerSymbol(tickerSymbol); //Get ticker symbol
  //  }
  //
  //  public LocalDate startDateCheckForDCA(Scanner scan) {
  //    view.getStartDateForDCA();    // Msg for start date. Cannot be null
  //    String date = scan.next();
  //
  //    if (!Objects.equals(date, "N")) {
  //      while (!model.isStartDateValid(date)) {
  //        view.errorMsg17();
  //        view.getStartDateForDCA();    // Msg for start date. Cannot be null
  //        date = scan.next();
  //      }
  //    }
  //    return model.getDate(date);
  //    }
  //
  //  public LocalDate endDateCheckForDCA(Scanner scan, LocalDate startDate) {
  //    view.getEndDateForDCA();      // Msg for end date. Can be null. Enter 'N' for null end date.
  //    String date = scan.next();
  //
  //    if (!Objects.equals(date, "N")) {
  //      while (!model.isEndDateValid(date, startDate)) {
  //        view.errorMsg18();
  //        view.getEndDateForDCA();  // Msg for end date. Can be null. Enter 'N' for null end date.
  //        date = scan.next();
  //      }
  //    }
  //    return model.getDate(date);
  //  }

}

