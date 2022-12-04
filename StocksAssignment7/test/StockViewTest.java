import java.time.LocalDate;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import view.StockView;
import view.StockViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit  test class to test the StockView. These tests include the assertion of menu, options and
 * prompts.
 */
public class StockViewTest {

  OutputStream outStream = new ByteArrayOutputStream();

  @Test
  public void testViewPrintMainMenu() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printMenu();
    assertEquals("" + "Welcome to the Stock Application\n" + "-----------------------\n"
        + "1. Create/ Fetch inflexible portfolio\n"
        + "2. Create/ Fetch flexible portfolio (Commission fee : $10 per transaction)\n"
        + "3. Quit\n" + "-----------------------\n" + "Enter Choice :\040", outStream.toString());
  }

  @Test
  public void testViewPrintSubMenu1() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printSubMenu1();
    assertEquals("" + "-----------------------\n"
        + "1. Create new inflexible portfolio\n"
        + "2. Upload existing portfolio file\n"
        + "3. Examine current composition for a portfolio\n"
        + "4. Cost Basis\n"
        + "5. Get total value of the portfolio.\n"
        + "6. Get performance LineChartView.\n"
        + "7. Quit\n"
        + "-----------------------\n" + "Enter Choice :\040", outStream.toString());
  }

  @Test
  public void testViewPrintSubMenu2() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printSubMenu2();
    assertEquals("" + "-----------------------\n"
        + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
        + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
        + "3. Upload existing portfolio file\n"
        + "4. Examine current composition for a portfolio\n"
        + "5. Cost Basis\n"
        + "6. Get total value of the portfolio.\n"
        + "7. Get performance LineChartView.\n"
        + "8. Quit\n"
        + "-----------------------\n" + "Enter Choice :\040", outStream.toString());
  }

  @Test
  public void testViewGetPFName() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.getPFName();
    assertEquals("Your new portfolio will be created now."
        + "\nEnter Portfolio Name : ", outStream.toString());
  }

  @Test
  public void testViewMorePortfolios() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.morePortfolios();
    assertEquals(
        "Portfolio created and saved successfully. Do you want to create more portfolios? "
            + "(Y/N) : ", outStream.toString());
  }

  @Test
  public void testViewGetTickerSymbol() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.getTickerSymbol();
    assertEquals("Enter Ticker Symbol : ", outStream.toString());
  }

  @Test
  public void testViewMoreTickerSymbols() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.moreTickerSymbols();
    assertEquals(
        "Stock details added and saved successfully. Do you want to buy more stocks? "
            + "(Y/N) : ", outStream.toString());
  }

  @Test
  public void testViewGetNoOfShares() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.getNoOfShares();
    assertEquals("Enter number of shares : ", outStream.toString());
  }

  @Test
  public void testViewAskPfNameForComposition() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askPfName("composition");
    assertEquals("Enter the portfolio name whose composition you want : ",
        outStream.toString());
  }

  @Test
  public void testViewAskPfNameForTotal() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askPfName("total");
    assertEquals("Enter the portfolio name whose total you want : ",
        outStream.toString());
  }

  @Test
  public void testViewAskPfNameForCostBasis() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askPfName("cost basis");
    assertEquals("Enter the portfolio name whose cost basis you want : ",
        outStream.toString());
  }

  @Test
  public void testViewAskMorePfNameForComposition() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askMorePfName("composition");
    assertEquals("Do you want to view composition of other portfolios? (Y/N) : ",
        outStream.toString());
  }

  @Test
  public void testViewAskMorePfNameForTotal() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askMorePfName("total");
    assertEquals("Do you want to view total of other portfolios? (Y/N) : ",
        outStream.toString());
  }

  @Test
  public void testViewAskMorePfNameForCostBasis() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askMorePfName("cost basis");
    assertEquals("Do you want to view cost basis of other portfolios? (Y/N) : ",
        outStream.toString());
  }

  //note
  @Test
  public void testViewPrintPfDetailsForComposition() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printPfDetailsForComposition("Portfolio Name : College\n"
        + " Ticker Symbol : AAPL, Number of Shares: 10\n"
        + " Ticker Symbol : AMZN, Number of Shares: 5\n"
        + " Ticker Symbol : MSFT, Number of Shares: 10");
    assertEquals("Portfolio Name : College\n"
        + " Ticker Symbol : AAPL, Number of Shares: 10\n"
        + " Ticker Symbol : AMZN, Number of Shares: 5\n"
        + " Ticker Symbol : MSFT, Number of Shares: 10", outStream.toString());
  }

  //note
  @Test
  public void testViewPrintTotalPf() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printTotalPf(100, "College");
    assertEquals("Total value of 'College' portfolio is = 100.0\n", outStream.toString());
  }

  @Test
  public void testViewPrintCostBasis() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.printCostBasis(0.00, "College", LocalDate.ofEpochDay(2022 - 07 - 30));
    assertEquals("Cost Basis of 'College' portfolio as on '1975-06-09' is = 0.0\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg1() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg1();
    assertEquals("!!! ERROR : Invalid Input. Please choose appropriate option.\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg2() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg2();
    assertEquals("!!! ERROR : Portfolio name does not exists. If you want to modify an "
            + "existing portfolio, "
            + "please select option 2 from the main menu. \n"
            + " Press 'Y' if you want to modify an existing portfolio or "
            + "'N' if you want to go back to the main menu : ",
        outStream.toString());
  }

  @Test
  public void testErrorMsg3() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg3();
    assertEquals("!!! ERROR : Portfolio name already exists. If you want to modify an "
            + "existing portfolio"
            + "please select option 2 from the main menu. \n"
            + " Press 'Y' if you want to create a new portfolio or "
            + "'N' if you want to go back to the main menu : ",
        outStream.toString());
  }

  @Test
  public void testErrorMsg4() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg4();
    assertEquals("!!! ERROR : Invalid File path provided. Please provide valid path.\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg5() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg5();
    assertEquals("!!! ERROR : Either market is closed on this date or the date format is "
            + "not correct. System accepts only (yyyy-MM-dd) format. Please provide proper date.\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg6() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg6();
    assertEquals("!!! ERROR : Incorrect input. Valid inputs are 'P' or 'S'\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg7() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg7();
    assertEquals("!!! ERROR : You are trying to sell more shares of this stock than you own"
            + ". \n"
            + "Do you want to continue selling other stocks? (Y/N) : ",
        outStream.toString());
  }

  @Test
  public void testErrorMsg8() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg8();
    assertEquals("!!! ERROR : Ticker Symbol should be from the below list only \n"
            + "(AAPL, AMZN, GOOG, MSFT, TSLA, BAC, BRK, HD, JNJ, "
            + "JPM, LLY, MA, NVDA, PFE, PG, TSM, UNH, V, WMT, XOM)\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg9() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg9();
    assertEquals("!!! ERROR : Number of shares entered is not a valid input. It should be "
            + "a positive integer only\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg10() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg10();
    assertEquals("!!! ERROR : Portfolio name does not exist. Please enter a valid "
            + "portfolio "
            + "name.\n",
        outStream.toString());
  }

  @Test
  public void testErrorMsg11() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.errorMsg11();
    assertEquals("!!! ERROR : You're trying to get composition before purchasing a stock\n",
        outStream.toString());
  }

  @Test
  public void testMarketClosed() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.marketClosed(LocalDate.ofEpochDay(2022 - 10 - 31));
    assertEquals("Market was closed on : 1975-06-05\n", outStream.toString());
  }

  @Test
  public void testGetFileURL() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.getFileURL();
    assertEquals("Enter the file path you want to upload : ", outStream.toString());
  }

  @Test
  public void testSuccessFileURL() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.successFileURL();
    assertEquals("File upload successful, please proceed\n", outStream.toString());
  }

  @Test
  public void testModifyPurchaseOrBuy() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.modifyPurchaseOrSell();
    assertEquals("Your portfolio will now be modified. "
            + "A commission fee of $10 will be charged everytime you purchase or sell. \n"
            + "Do you want to purchase more stocks or sell them? "
            + "(Enter 'P' for purchasing or 'S' for selling) : ", outStream.toString());
  }

  @Test
  public void testModifyGetPfName() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.modifyGetPfName();
    assertEquals("Enter the portfolio name you want to modify : ", outStream.toString());
  }

  @Test
  public void testMoreModifications() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.moreModifications();
    assertEquals("Do you want to make more modifications? (Y/N) : ", outStream.toString());
  }

  @Test
  public void testModifyMoreStocks() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.modifyMoreStocks();
    assertEquals("Portfolio updated successfully. Do you want to purchase or sell more "
        + "stocks? (Y/N) : ", outStream.toString());
  }

  @Test
  public void testAskDateForComposition() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askDate("composition");
    assertEquals("Enter the date (yyyy-MM-dd) for composition: ", outStream.toString());
  }

  @Test
  public void testAskDateForTotal() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askDate("total");
    assertEquals("Enter the date (yyyy-MM-dd) for total: ", outStream.toString());
  }

  @Test
  public void testAskDateForCostBasis() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askDate("cost basis");
    assertEquals("Enter the date (yyyy-MM-dd) for cost basis: ", outStream.toString());
  }

  @Test
  public void testAskPerformanceStartDate() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askPerformanceDate("Performance1");
    assertEquals("Enter the start date (yyyy-MM-dd) : ", outStream.toString());
  }

  @Test
  public void testAskPerformanceEndDate() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.askPerformanceDate("Performance2");
    assertEquals("Enter the end date (yyyy-MM-dd) : ", outStream.toString());
  }

  @Test
  public void testViewErrorExitMsg() {
    StockView view = new StockViewImpl(new PrintStream(outStream));
    view.exitMsg();
    assertEquals("Exiting application. Thank You", outStream.toString());
  }

}