//import org.junit.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.io.Reader;
//import java.io.StringReader;
//import java.time.LocalDate;
//
//import controller.StockController;
//import controller.StockControllerImpl;
//import model.Model;
//import view.StockView;
//import view.StockViewImpl;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * A JUnit  test class to test the StockController. The controller
// */
//public class StockControllerTest {
//
//  OutputStream outStream = new ByteArrayOutputStream();
//
//  /// Test 1 Start///
//  @Test
//  public void testController1() {      //Composition
//    Reader in = new StringReader("2\n4\nCollege\n2021-06-06\nN\n8\n3");
//    String expectedLog = "date : 2021-06-06\n"
//            + "pfNameComp : College, filePath : C:\\Users\\rahul\\IdeaProjects"
//            + "\\A5\\portfoliosFlexible.xml, date : 2021-06-06";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel1(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 2 Start///
//  @Test
//  public void testController2() {      //Cost Basis
//    Reader in = new StringReader("2\n5\nCollege\n2021-01-04\nN\n8\n3");
//    String expectedLog = "pfName : College, filePath : C:\\Users\\rahul\\IdeaProjects\\A5\\"
//            + "portfoliosFlexible.xml, date : 2021-01-04";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel2(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 3 Start///
//  @Test
//  public void testController3() {      //Total
//    Reader in = new StringReader("2\n6\nCollege\n2021-01-04\nN\n8\n3");
//    String expectedLog = "pfName : College, filePath : C:\\Users\\rahul\\IdeaProjects\\A5\\"
//            + "portfoliosFlexible.xml, date : 2021-01-04";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel3(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 4 Start///
//  @Test
//  public void testController4() {      //Graph
//    Reader in = new StringReader("2\n7\nCollege\n2021-01-04\n2022-01-05\nN\n8\n3");
//    String expectedLog = "pfName : College, filePath : C:\\Users\\rahul\\IdeaProjects\\A5\\"
//            + "portfoliosFlexible.xml, start date : 2021-01-04, end date : 2022-01-05";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel4(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 5 Start///
//  @Test
//  public void testController5() {      // Date check
//    Reader in = new StringReader("2\n5\nCollege\n2021-01-04\nN\n8\n3");
//    String expectedLog = "Date received : 2021-01-04";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel5(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 6 Start///
//  @Test
//  public void testController6() {      // Invalid date pass
//    Reader in = new StringReader("2\n5\nCollege\n2021-01a\nN\n8\n3");
//    String expectedLog = "Date received : 2021-01a";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel6(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 7 Start///
//  @Test
//  public void testController7() {      // Invalid number of shares
//    Reader in = new StringReader("2\n1\nColl\nAAPL\n2.5\n2\n2021-01-04\nN\n8\n3");
//    String expectedLog = "Date received : 2021-01-04";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel7(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  /// Test 8 Start///
//  @Test
//  public void testController8() {      // Create 2 Pf 2,3 Stock
//    Reader in = new StringReader("2\n1\nSavePF\nAAPL\n2\n2021-01-04\nY"
//            + "\nAMZN\n3\n2021-04-05\nN\nY\n"
//            + "RetirePF\nAAPL\n4\n2021-01-04\nY\n"
//            + "\nAMZN\n3\n2021-04-06\nN\nN\n8\n3");
//    String expectedLog = "\n"
//            + "pfName : SavePF, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "tickerSymbol : AAPL\n"
//            + "num : 2.0\n"
//            + "Date received : 2021-01-04pfName : SavePF, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "pfName : SavePF, tickerSymbol : AAPL, numShares : 2, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-01-04\n"
//            + "tickerSymbol : AMZN\n"
//            + "num : 3.0\n"
//            + "Date received : 2021-04-05\n"
//            + "pfName : SavePF, tickerSymbol : AMZN, numShares : 3, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-04-05\n"
//            + "pfName : RetirePF, filePath : C:\\Users\\rahul\\IdeaProjects\\A5\\"
//            + "portfoliosFlexible.xml\n"
//            + "tickerSymbol : AAPL\n"
//            + "num : 4.0\n"
//            + "Date received : 2021-01-04pfName : RetirePF, filePath : C:\\Users\\rahul\\"
//            + "IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "pfName : RetirePF, tickerSymbol : AAPL, numShares : 4, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-01-04\n"
//            + "tickerSymbol : AMZN\n"
//            + "num : 3.0\n"
//            + "Date received : 2021-04-06\n"
//            + "pfName : RetirePF, tickerSymbol : AMZN, numShares : 3, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-04-06";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel8(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  ///// Test 9 start
//  @Test
//  public void testController9() {     // Create 1 pf 1 stock
//    Reader in = new StringReader("2\n1\nSavings\nAAPL\n2\n2021-01-04\nN\nN\n8\n3");
//    String expectedLog = "\n"
//            + "pfName : Savings, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "tickerSymbol : AAPL\n"
//            + "num : 2.0\n"
//            + "Date received : 2021-01-04pfName : Savings, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects"
//            + "\\A5\\portfoliosFlexible.xml\n"
//            + "pfName : Savings, tickerSymbol : AAPL, numShares : 2, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-01-04";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel9(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  ///// Test 10 start
//  @Test
//  public void testController10() {     // Modify - Buy stocks
//    Reader in = new StringReader("2\n2\nCollege\nP\nAAPL\n15\n2021-06-17\nN\nN\n8\n3");
//    String expectedLog = "\n"
//            + "pfName : College, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "tickerSymbol : AAPL\n"
//            + "num : 15.0\n"
//            + "Date received : 2021-06-17\n"
//            + "pfName : College, tickerSymbol : AAPL, numShares : 15, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-06-17";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel10(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  ///// Test 11 start
//  @Test
//  public void testController11() {     // Modify - Buy stocks
//    Reader in = new StringReader("2\n2\nCollege\nS\nAAPL\n15\n2021-06-17\nN\nN\n8\n3");
//    String expectedLog = "\n"
//            + "pfName : College, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml\n"
//            + "tickerSymbol : AAPL\n"
//            + "num : 15.0\n"
//            + "Date received : 2021-06-17\n"
//            + "pfName : College, tickerSymbol : AAPL, numShares : -15, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-06-17\n"
//            + "pfName : College, tickerSymbol : AAPL, numShares : -15, filePath : "
//            + "C:\\Users\\rahul\\IdeaProjects\\A5\\portfoliosFlexible.xml, date : 2021-06-17";
//    StringBuilder log = new StringBuilder(); //log for mock model
//    Model model = new LoggingModel11(log);
//    StockView view = new StockViewImpl(new PrintStream(outStream));
//    StockController controller = new StockControllerImpl(model, view, in);
//    controller.selectOption();
//    assertEquals(expectedLog, log.toString());
//  }
//
//  static class LoggingModel1 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel1(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      log.append("pfNameComp : ").append(pfNameComp).append(", filePath : ")
//              .append(filePath).append(", date : ").append(date);
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("date : " + date + "\n");
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel2 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel2(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      log.append("pfName : ").append(pfNameTotal).append(", filePath : ")
//              .append(filePath).append(", date : ").append(date);
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel3 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel3(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      log.append("pfName : ").append(pfNameTotal).append(", filePath : ")
//              .append(filePath).append(", date : ").append(date);
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel4 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel4(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      log.append("pfName : ").append(pfNameGraph).append(", filePath : ")
//              .append(filePath).append(", start date : ").append(date1)
//              .append(", end date : ").append(date2);
//      return null;
//    }
//  }
//
//  static class LoggingModel5 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel5(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("Date received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel6 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel6(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("Date received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel7 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel7(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      //
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      //
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("Date received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel8 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel8(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      log.append("pfName : " + pfName + ", filePath : " + filePath);
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      log.append("\npfName : " + pfName + ", filePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      log.append("\ntickerSymbol : " + tickerSymbol);
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      log.append("\nfilePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      log.append("\nnum : " + num);
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("\nDate received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel9 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel9(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      log.append("pfName : " + pfName + ", filePath : " + filePath);
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      return false;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      log.append("\npfName : " + pfName + ", filePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      log.append("\ntickerSymbol : " + tickerSymbol);
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      log.append("\nfilePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      log.append("\nnum : " + num);
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("\nDate received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel10 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel10(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      log.append("pfName : " + pfName + ", filePath : " + filePath);
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//      return true;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      log.append("\npfName : " + pfName + ", filePath : " + filePath);
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      log.append("\ntickerSymbol : " + tickerSymbol);
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      log.append("\nfilePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      log.append("\nnum : " + num);
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("\nDate received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//  static class LoggingModel11 implements Model {
//
//    private final StringBuilder log;
//
//    public LoggingModel11(StringBuilder log) {
//      this.log = log;
//    }
//
//    @Override
//    public void writePortfolioName(String pfName, String filePath) {
//      log.append("pfName : " + pfName + ", filePath : " + filePath);
//    }
//
//    @Override
//    public boolean checkIfCanSell(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//      return true;
//    }
//
//    @Override
//    public void writeStockDetails(String pfName, String tickerSymbol,
//                                  int numShares, String filePath, LocalDate date) {
//      log.append("\npfName : " + pfName + ", tickerSymbol : " + tickerSymbol
//              + ", numShares : " + numShares + ", filePath : " + filePath
//              + ", date : " + date);
//    }
//
//    @Override
//    public String getComposition(String pfNameComp, String filePath, LocalDate date) {
//      return "";
//    }
//
//    @Override
//    public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
//      return 0;
//    }
//
//    @Override
//    public boolean isPfNameUnique(String pfName, String filePath) {
//      log.append("\npfName : " + pfName + ", filePath : " + filePath);
//      return false;
//    }
//
//    @Override
//    public double getCostBasis(String pfNameTotal, String filePath, LocalDate date) {
//      return 0;
//    }
//
//    @Override
//    public boolean isTickerSymbolValid(String tickerSymbol) {
//      log.append("\ntickerSymbol : " + tickerSymbol);
//      return true;
//    }
//
//    @Override
//    public boolean checkValidFilePath(String filePath) {
//      log.append("\nfilePath : " + filePath);
//      return true;
//    }
//
//    @Override
//    public boolean isNumberOfSharesValid(double num) {
//      log.append("\nnum : " + num);
//      return true;
//    }
//
//    @Override
//    public boolean isDateValid(String date, String purpose) {
//      log.append("\nDate received : " + date);
//      return true;
//    }
//
//    @Override
//    public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath,
//                                             LocalDate date1, LocalDate date2) {
//      return null;
//    }
//  }
//
//
//}
//
