package view;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Objects;

/**
 * stockViewImpl implements the stockview interface. This implementation performs operation for
 * printing the outputs as asked by the controller for the user on the text based UI.
 */
public class StockViewImpl implements StockView {

  private final PrintStream output;

  /**
   * Constructor that initialises the PrintStream object which is further used to print the output
   * on the UI.
   *
   * @param out object of the PrintStream interface
   */
  public StockViewImpl(PrintStream out) {
    this.output = out;
  }

  @Override
  public void printMenu() {
    this.output.print("" + "Welcome to the Stock Application\n" + "-----------------------\n"
            + "1. Create/ Fetch inflexible portfolio\n"
            + "2. Create/ Fetch flexible portfolio (Commission fee : $10 per transaction)\n"
            + "3. Quit\n" + "-----------------------\n" + "Enter Choice :\040");
  }

  @Override
  public void printSubMenu1() {
    this.output.print("" + "---------- INFLEXIBLE PORTFOLIO -------------\n"
            + "1. Create new inflexible portfolio\n"
            + "2. Upload existing portfolio file\n"
            + "3. Examine current composition for a portfolio\n"
            + "4. Get total value of the portfolio.\n"
            + "5. Quit\n"
            + "-----------------------\n" + "Enter Choice :\040");
  }

  @Override
  public void printSubMenu2() {
    this.output.print("" + "----------- FLEXIBLE PORTFOLIO ------------\n"
            + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
            //+ "2. Create new flexible portfolio using 'Dollar Cost Averaging' strategy\n"
            + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
            + "3. Upload existing portfolio file\n"
            + "4. Examine current composition for a portfolio\n"
            + "5. Cost Basis\n"
            + "6. Get total value of the portfolio.\n"
            + "7. Get performance LineChartView.\n"
            + "8. Quit\n"
            + "-----------------------\n" + "Enter Choice :\040");
  }

  @Override
  public void getPFName() {
    this.output.print(
            "Your new portfolio will be created now. "
                    + "\nEnter Portfolio Name : ");
  }

  @Override
  public void morePortfolios() {
    this.output.print(
            "Portfolio created and saved successfully. Do you want to create more portfolios? "
                    + "(Y/N) : ");
  }

  @Override
  public void getTickerSymbol() {
    this.output.print("Enter Ticker Symbol : ");
  }

  @Override
  public void moreTickerSymbols() {
    this.output.print(
            "Stock details added and saved successfully. Do you want to buy more stocks? (Y/N) : ");
  }

  @Override
  public void getNoOfShares() {
    this.output.print("Enter number of shares : ");
  }

  @Override
  public void askPfName(String purpose) {
    this.output.print("Enter the portfolio name whose " + purpose + " you want : ");
  }

  @Override
  public void askMorePfName(String purpose) {
    this.output.print("Do you want to view " + purpose + " of other portfolios? (Y/N) : ");
  }

  @Override
  public void printPfDetailsForComposition(String fullPfDetails) {
    this.output.print(fullPfDetails);
  }

  @Override
  public void printTotalPf(double pfTotal, String pfName) {
    this.output.println("Total value of '" + pfName + "' portfolio is = " + pfTotal);
  }

  @Override
  public void printCostBasis(double pfTotal, String pfName, LocalDate date) {
    this.output.println(
            "Cost Basis of '" + pfName + "' portfolio as on '" + date + "' is = " + pfTotal);
  }


  @Override
  public void exitMsg() {
    this.output.print("Exiting application. Thank You");
  }

  @Override
  public void errorMsg1() {
    this.output.println("!!! ERROR : Invalid Input. Please choose appropriate option.");
  }

  @Override
  public void errorMsg2() {
    this.output.print(
            "!!! ERROR : Portfolio name does not exists. If you want to modify an existing "
                + "portfolio"
                + ", "
                    + "please enter a valid portfolio name. \n "
                    + "Press 'Y' if you want to modify an existing portfolio or "
                    + "'N' if you want to go back to the menu : ");
  }

  @Override
  public void errorMsg3() {
    this.output.print(
            "!!! ERROR : Portfolio name already exists. If you want to modify an existing "
                    + "portfolio please select option 2 from the menu. \n "
                    + "Press 'Y' if you want to create a new portfolio or "
                    + "'N' if you want to go back to the menu : ");
  }

  @Override
  public void errorMsg4() {
    this.output.println("!!! ERROR : Invalid File path provided. Please provide valid path.");
  }

  @Override
  public void errorMsg5() {
    this.output.println(
            "!!! ERROR : Either market is closed on this date "
                    + "or the date format is not correct. "
                    + "System accepts only (yyyy-MM-dd) format. Please provide proper date.");
  }

  @Override
  public void errorMsg6() {
    this.output.println("!!! ERROR : Incorrect input. Valid inputs are 'P' or 'S'");
  }

  @Override
  public void errorMsg7() {
    this.output.print(
            "!!! ERROR : You are trying to sell more shares of this stock than you own. \n"
                    + "Do you want to continue selling other stocks? (Y/N) : ");
  }

  @Override
  public void errorMsg8() {
    this.output.println("!!! ERROR : Ticker Symbol should be from the below list only \n"
            + "(AAPL, AMZN, GOOG, MSFT, TSLA, BAC, BRK, HD, JNJ, "
            + "JPM, LLY, MA, NVDA, PFE, PG, TSM, UNH, V, WMT, XOM)");
  }

  @Override
  public void errorMsg9() {
    this.output.println("!!! ERROR : Number of shares entered is not a valid input. It should be "
            + "a positive integer only");
  }

  @Override
  public void errorMsg10() {
    this.output.println(
            "!!! ERROR : Portfolio name does not exist. Please enter a valid portfolio name.");
  }

  @Override
  public void errorMsg11() {
    this.output.println("!!! ERROR : You're trying to get composition before purchasing a stock");
  }

  @Override
  public void errorMsg12() {
    this.output.print(
            "!!! ERROR : Portfolio name already exists. You cannot modify an existing portfolio as"
                + " you "
                    + " chose to create an 'inflexible portfolio'. \n "
                    + "Press 'Y' if you want to create a new portfolio or "
                    + "'N' if you want to go back to the menu : ");
  }


  @Override
  public void marketClosed(LocalDate date) {
    this.output.println("Market was closed on : " + date);
  }

  @Override
  public void getFileURL() {
    this.output.print("Enter the file path you want to upload : ");
  }

  @Override
  public void successFileURL() {
    this.output.println("File upload successful, please proceed");
  }

  @Override
  public void modifyPurchaseOrSell() {
    this.output.print("Your portfolio will now be modified. A commission fee of $10 will be charged"
            + " everytime you purchase or sell. \n"
            + "1. Do you want to purchase stocks \n"
            //+ "2. Do you want to purchase stocks using dollar cost averaging strategy? \n"
            + "2. Do you want to sell stocks?\n"
            + " Enter choice : ");
  }

  @Override
  public void modifyGetPfName() {
    this.output.print("Enter the portfolio name you want to modify : ");
  }

  @Override
  public void moreModifications() {
    this.output.print("Do you want to make more modifications? (Y/N) : ");
  }

  @Override
  public void modifyMoreStocks() {
    this.output.print(
            "Portfolio updated successfully. Do you want to purchase or sell more stocks? (Y/N) :"
                + " ");
  }

  @Override
  public void printGraph(StringBuilder pfGraph, String pfNameGraph, LocalDate date1,
                         LocalDate date2) {
    this.output.print(pfGraph);
  }

  @Override
  public void askDate(String purpose) {
    this.output.print("Enter the date (yyyy-MM-dd) for " + purpose + ": ");
  }

  @Override
  public void askPerformanceDate(String purpose) {
    if (Objects.equals(purpose, "Performance1")) {
      this.output.print("Enter the start date (yyyy-MM-dd) : ");
    } else if (Objects.equals(purpose, "Performance2")) {
      this.output.print("Enter the end date (yyyy-MM-dd) : ");
    }
  }

  @Override
  public void errorMsg13() {
    this.output.println(
            "!!! ERROR : Invalid investment amount. ");
  }

  @Override
  public void errorMsg14() {
    this.output.println(
            "!!! ERROR : Invalid number. Number of companies should be an integer (Ex. 2)");
  }

  @Override
  public void errorMsg15() {
    this.output.println(
            "!!! ERROR : Invalid percentage. Either percentage is not a number or the sum of "
                    + "percents you're investing is greater than 100");
  }

  @Override
  public void errorMsg16() {
    this.output.println(
            "!!! ERROR : Invalid interval. Interval should be in number of days. (Ex. 30) ");
  }

  @Override
  public void errorMsg17() {
    this.output.println(
            "!!! ERROR : Invalid date format. System accepts only (yyyy-MM-dd) format. ");
  }

  @Override
  public void errorMsg18() {
    this.output.println(
            "!!! ERROR : Either end date is before start date or date format is invalid. "
                    + "System accepts only (yyyy-MM-dd) format.");
  }

  @Override
  public void errorMsg19() {
    this.output.println(
            "!!! Sum of percentages exceed 100. Please enter last details again.");
  }

  @Override
  public void errorMsg20() {
    this.output.println(
            "!!! ERROR : Invalid commission fee. ");
  }

  @Override
  public void getInvestmentAmount() {
    this.output.print("Enter the amount of money you want to invest : ");
  }

  @Override
  public void getCommFee() {
    this.output.print("Enter the commission fee : ");
  }

  @Override
  public void getNumOfSharesForDCA() {
    this.output.print("Enter the number of stock companies you want to invest : ");
  }

  @Override
  public void getPercentageForDCA(String ts) {
    this.output.print("Enter the percent you want to invest in " + ts + ": ");
  }

  @Override
  public void getTSNameForDCA(int i) {
    this.output.print("Enter ticker symbol " + i + ": ");
  }

  @Override
  public void getIntervalForDCA() {
    this.output.print("Enter the interval (number of days) in which you want to invest : ");
  }

  @Override
  public void getStartDateForDCA() {
    this.output.print("Enter the start date (yyyy-MM-dd) : ");
  }

  @Override
  public void getEndDateForDCA() {
    this.output.print("Enter the end date (yyyy-MM-dd). Enter 'N' If you want the strategy "
            + "to be ongoing : ");
  }

  @Override
  public void successDCAPortfolios() {
    this.output.print(
            "\nPortfolio created and saved successfully according to DCA strategy. Do you "
                    + "want to repeat the process (Y/N)?");
  }

}
