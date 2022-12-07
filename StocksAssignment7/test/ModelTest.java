import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import controller.APICaller;
import model.Model;
import model.ModelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A JUnit  test class to test the StockModel.
 */
public class ModelTest {

  Path currentPath;
  private Model model;

  APICaller validDates = new APICaller();
  ArrayList<LocalDate> datesFromAPI = validDates.callAPI();

  @Before
  public void setup() {
    model = new ModelImpl();
    currentPath = Paths.get(System.getProperty("user.dir"));


  }

  @Test
  public void testModel1WritePFNameToXML() {
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    model.writePortfolioName("Retirement", filePath);
    boolean expectedOutput = true;
    boolean actualOutput = false;

    //// Read xml and check if stock details name are written in that
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new File(filePath));
      document.getDocumentElement().normalize();
      NodeList portfolioList = document.getElementsByTagName("pfName");
      for (int i = 0; i < portfolioList.getLength(); i++) {
        Node portfolio = portfolioList.item(i);
        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;
          // Check if new pf name is in xml
          if (Objects.equals("RETIREMENT",
                  (portfolioElement.getAttribute("name")).toUpperCase())) {
            actualOutput = true;
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testModel2WriteStockDetailsToXML() {
    /*
    Resetting the xml is required to pass this test everytime, else assert will fail.
    Remove the stock details of 'retirement' portfolio to check if stock details are saved
    or not. Instead of this you can change the ticker symbol as well below.
     */
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "Retirement";
    String tickerSymbol = "MSFT";
    double numberOfShares = 10.0;
    double commissionFee = 10.5;

    LocalDate date = LocalDate.of(2022, 1, 4);

    model.writeStockDetails(pfName, tickerSymbol, numberOfShares, commissionFee, filePath, date);
    boolean expectedOutput = true;
    boolean actualOutput = false;

    /// Read if stock details are saved to the file
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = db.parse(filePath);

      NodeList pfNameList = doc.getElementsByTagName("pfName");
      for (int i = 0; i < pfNameList.getLength(); i++) {
        Node portfolio = pfNameList.item(i);

        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;

          if (Objects.equals(pfName.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {

            NodeList portfolioDetails = portfolio.getChildNodes();
            for (int j = 0; j < portfolioDetails.getLength(); j++) {
              Node detail = portfolioDetails.item(j);
              if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element stockName = (Element) detail;
                if ((Objects.equals(tickerSymbol, stockName.getAttribute(
                        "StockName"))) && (Objects.equals(date.toString(),
                        stockName.getAttribute("DateBought")))
                        && (Objects.equals(stockName.getTextContent(),
                        String.valueOf(numberOfShares)))) {
                  actualOutput = true;
                }
              }
            }
            break;
          }
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
    /// Read if stock details are saved to the file

    assertEquals(expectedOutput, actualOutput);

  }

  @Test
  public void testModel3WriteStockDetailsToXML() {    // Check if sold
    /*
    Resetting the xml is required to pass this test everytime, else assert will fail.
     */
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "Retirement";
    String tickerSymbol = "MSFT";
    int numberOfShares = -5;    //Selling 5 stocks
    double commissionFee = 10.5;

    LocalDate date = LocalDate.of(2022, 1, 6);

    model.writeStockDetails(pfName, tickerSymbol, numberOfShares, commissionFee, filePath, date);
    Double expectedOutput = 15.0;    //Original count = 20, After selling 5 the new count = 15.
    Double actualOutput = 0.0;

    /// Read if modified stock details are saved to the file
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = db.parse(filePath);
      NodeList pfNameList = doc.getElementsByTagName("pfName");
      for (int i = 0; i < pfNameList.getLength(); i++) {
        Node portfolio = pfNameList.item(i);

        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;

          if (Objects.equals(pfName.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {

            NodeList portfolioDetails = portfolio.getChildNodes();
            for (int j = 0; j < portfolioDetails.getLength(); j++) {
              Node detail = portfolioDetails.item(j);
              if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element stockName = (Element) detail;
                if ((Objects.equals(tickerSymbol, stockName.getAttribute(
                        "StockName")))) {
                  actualOutput = actualOutput + Double.parseDouble(stockName.getTextContent());
                }
              }
            }
            break;
          }
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }

    /// Read if stock details are saved to the file
    assertEquals(expectedOutput, actualOutput);

  }

  @Test
  public void testModel32WriteStockDetailsToXML() {    // Check if bought
    /*
    Resetting the xml is required to pass this test everytime, else assert will fail.
     */
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "Retirement";
    String tickerSymbol = "MSFT";
    int numberOfShares = 5;    //Buying 5 stocks
    double commissionFee = 10.5;

    LocalDate date = LocalDate.of(2022, 1, 6);

    model.writeStockDetails(pfName, tickerSymbol, numberOfShares, commissionFee, filePath, date);
    int expectedOutput = 25;    //Original count = 20, After buying 5 the new count = 25.
    int actualOutput = 0;

    /// Read if new stock details are saved to the file
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = db.parse(filePath);
      NodeList pfNameList = doc.getElementsByTagName("pfName");
      for (int i = 0; i < pfNameList.getLength(); i++) {
        Node portfolio = pfNameList.item(i);

        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;

          if (Objects.equals(pfName.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {

            NodeList portfolioDetails = portfolio.getChildNodes();
            for (int j = 0; j < portfolioDetails.getLength(); j++) {
              Node detail = portfolioDetails.item(j);
              if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element stockName = (Element) detail;
                if ((Objects.equals(tickerSymbol, stockName.getAttribute(
                        "StockName")))) {
                  actualOutput = actualOutput + Integer.parseInt(stockName.getTextContent());
                }
              }
            }
            break;
          }
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }

    /// Read if stock details are saved to the file
    assertEquals(expectedOutput, actualOutput);

  }

  @Test
  public void testModel3GetComposition() {    // composition (1) - Flexible PF - Before buying date
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 1, 4);
    String expectedOutput = "";
    assertEquals(expectedOutput, model.getComposition("College", fp.toString(), date));
  }

  @Test
  public void testModel4GetComposition() {    // composition (2) - Flexible PF
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 7, 5);
    String expectedOutput = "* Portfolio Name : College\n"
            + "* Ticker Symbol: AAPL, Number of Shares: 25.0\n"
            + "* Ticker Symbol: AMZN, Number of Shares: 25.0\n";
    assertEquals(expectedOutput, model.getComposition("College", fp.toString(), date));
  }

  @Test
  public void testModel5GetComposition() {    // composition (3) - Flexible PF - Full
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 8, 16);
    String expectedOutput = "* Portfolio Name : College\n"
            + "* Ticker Symbol: AAPL, Number of Shares: 25.0\n"
            + "* Ticker Symbol: AMZN, Number of Shares: 29.0\n";
    assertEquals(expectedOutput, model.getComposition("College", fp.toString(), date));
  }

  @Test
  public void testModel6GetComposition() {    // composition (6) - Inflexible PF
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 8, 16);
    String expectedOutput = "* Portfolio Name : College\n"
            + "* Ticker Symbol: AAPL, Number of Shares: 25.0\n"
            + "* Ticker Symbol: AMZN, Number of Shares: 29.0\n";
    assertEquals(expectedOutput, model.getComposition("College", fp.toString(), date));
  }

  @Test
  public void testModel7GetTotalP() {   // total (1) - Flexible PF - Before buying date
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 1, 4);
    assertEquals(0.00, model.getTotal("College", fp.toString(), date,
            "Flexible"), 0.00);
  }

  @Test
  public void testModel8GetTotalP() {   // total(2)-Flexible PF-If market is closed on that date.
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 6, 5);
    assertEquals(-1.0, model.getTotal("College", fp.toString(), date,
            "Flexible"), 0.00);
  }

  @Test
  public void testModel9GetTotalP() {   // total (3) - Flexible PF
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 7, 5);
    assertEquals(6376.5, model.getTotal("College", fp.toString(), date,
            "Flexible"), 0.00);
  }

  @Test
  public void testModel10GetTotalP() {   // total (4) - Flexible PF
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 8, 16);
    assertEquals(8524.369999999999, model.getTotal("College", fp.toString(),
            date, "Flexible"), 0.00);
  }

  @Test
  public void testModel11GetTotalP() {   // total (5) - Inflexible PF
    Path fp = Paths.get(currentPath.toString(), "InflexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 8, 16);
    assertEquals(5088.49, model.getTotal("Savings", fp.toString(), date,
            "Inflexible"), 0.00);
  }

  @Test
  public void testModel12GetCostBasis() {   // Cost Basis (1) - Flexible PF
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 1, 4);
    assertEquals(0.0, model.getCostBasis("College", fp.toString(), date),
            0.00);
  }

  @Test
  public void testModel13GetCostBasis() {   // Cost Basis (2) - Flexible PF - For 1 of 4 stocks
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 2, 4);
    assertEquals(4352.5, model.getCostBasis("College", fp.toString(), date),
            0.00);
  }

  @Test
  public void testModel14GetCostBasis() {   // Cost Basis (3) - Flexible PF - For 3 out of 4 stocks
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 7, 14);
    assertEquals(64117.149999999994, model.getCostBasis("College",
            fp.toString(), date), 0.00);
  }

  @Test
  public void testModel15GetCostBasis() {   // Cost Basis (4) - Flexible PF - For all stocks
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    LocalDate date = LocalDate.of(2022, 8, 14);
    assertEquals(64147.149999999994, model.getCostBasis("College",
            fp.toString(), date), 0.00);
  }

  @Test
  public void testModel16IsPfNameUnique1() {
    String pfName = "Health";    //Already exists
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    assertFalse(model.isPfNameUnique(pfName, filePath));
  }

  @Test
  public void testModel17IsPfNameUnique2() {
    String pfName = "Savings";    //New pfName
    Path currentPath = Paths.get(System.getProperty("user.dir"));
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    assertTrue(model.isPfNameUnique(pfName, filePath));
  }

  @Test
  public void testModel18IsNumberOfSharesValid() {
    assertTrue(model.isNumberOfSharesValid("5"));
  }

  @Test
  public void testModel19IsNumberOfSharesValid() {
    assertFalse(model.isNumberOfSharesValid("-1.0"));
  }

  @Test
  public void testModel20IsNumberOfSharesValid() {
    assertFalse(model.isNumberOfSharesValid("5.4"));
  }


  @Test
  public void testModel28CheckSell() {   // Selling before buying
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    assertFalse(model.checkIfCanSell("COLLEGE", "AAPL", 15,
            fp.toString(), LocalDate.of(2022, 1, 4)));
  }

  @Test
  public void testModel29CheckSell() {   //Selling more than owner owns
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    assertFalse(model.checkIfCanSell("COLLEGE", "AAPL", 125,
            fp.toString(), LocalDate.of(2022, 1, 4)));
  }

  @Test
  public void testModel30CheckSell() {   //Selling valid
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    assertFalse(model.checkIfCanSell("COLLEGE", "AAPL", 125,
            fp.toString(), LocalDate.of(2022, 6, 4)));
  }

  @Test
  public void testModel31ValidTS() {   // Valid TS
    assertTrue(model.isTickerSymbolValid("AAPL"));
  }

  @Test
  public void testModel32ValidTS() {   // Invalid TS
    assertFalse(model.isTickerSymbolValid("AAL"));
  }

  @Test
  public void testModel33GetPerformanceGraph() {   // Performance Graph - Month Wise - 10 mos
    LocalDate startDate = LocalDate.of(2022, 1, 1);
    LocalDate endDate = LocalDate.of(2022, 10, 10);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");


    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("JAN 2022 : \n"
            + "FEB 2022 : ***\n"
            + "MAR 2022 : ***\n"
            + "APR 2022 : ***\n"
            + "MAY 2022 : **************************************************\n"
            + "JUN 2022 : ****\n"
            + "JUL 2022 : ********\n"
            + "AUG 2022 : *****\n"
            + "SEP 2022 : *****\n"
            + "OCT 2022 : *****\n"
            + "\n"
            + "Scale: * = $1276\n"
            + "\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("College", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel34GetPerformanceGraph() {   // Performance Graph - Year Wise - 12 years
    LocalDate startDate = LocalDate.of(2011, 1, 13);
    LocalDate endDate = LocalDate.of(2022, 8, 10);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    String expectedOutput = "2011 : *\n"
            + "2012 : ****\n"
            + "2013 : *******\n"
            + "2014 : *****\n"
            + "2015 : ************\n"
            + "2016 : *************\n"
            + "2017 : *****************\n"
            + "2018 : **********************\n"
            + "2019 : ***************************\n"
            + "2020 : ************************************************\n"
            + "2021 : **************************************************\n"
            + "2022 : ***\n"
            + "\n"
            + "Scale: * = $1533\n\n";

    assertEquals(expectedOutput, model.getPerformanceGraph("Health", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel35GetPerformanceGraph() {   // Performance Graph - Date Wise - 10 days
    LocalDate startDate = LocalDate.of(2022, 3, 6);
    LocalDate endDate = LocalDate.of(2022, 4, 1);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("2022-03-06 : *********************************************\n"
            + "2022-03-07 : ********************************************\n"
            + "2022-03-08 : ********************************************\n"
            + "2022-03-09 : *********************************************\n"
            + "2022-03-10 : ********************************************\n"
            + "2022-03-11 : *******************************************\n"
            + "2022-03-12 : *******************************************\n"
            + "2022-03-13 : *******************************************\n"
            + "2022-03-14 : ******************************************\n"
            + "2022-03-15 : *******************************************\n"
            + "2022-03-16 : ********************************************\n"
            + "2022-03-17 : *********************************************\n"
            + "2022-03-18 : **********************************************\n"
            + "2022-03-19 : **********************************************\n"
            + "2022-03-20 : **********************************************\n"
            + "2022-03-21 : **********************************************\n"
            + "2022-03-22 : ***********************************************\n"
            + "2022-03-23 : ***********************************************\n"
            + "2022-03-24 : ************************************************\n"
            + "2022-03-25 : *************************************************\n"
            + "2022-03-26 : *************************************************\n"
            + "2022-03-27 : *************************************************\n"
            + "2022-03-28 : *************************************************\n"
            + "2022-03-29 : **************************************************\n"
            + "2022-03-30 : *************************************************\n"
            + "2022-03-31 : *************************************************\n"
            + "2022-04-01 : ************************************************\n"
            + "\n"
            + "Scale: * = $89\n\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("College",
            fp.toString(), startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel36GetPerformanceGraph() {   // Bar Graph - No stocks during time interval
    LocalDate startDate = LocalDate.of(2021, 3, 6);
    LocalDate endDate = LocalDate.of(2021, 10, 16);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("You did not own any stocks during this time period.\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("College", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel37GetPerformanceGraph() {   // Performance Graph - 5 day interval
    LocalDate startDate = LocalDate.of(2022, 5, 5);
    LocalDate endDate = LocalDate.of(2022, 9, 10);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("2022-05-05 : ***\n"
            + "2022-05-10 : ********************************************\n"
            + "2022-05-15 : **********************************************\n"
            + "2022-05-20 : ********************************************\n"
            + "2022-05-25 : *******************************************\n"
            + "2022-05-30 : ***********************************************\n"
            + "2022-06-04 : **************************************************\n"
            + "2022-06-09 : ****\n"
            + "2022-06-14 : ****\n"
            + "2022-06-19 : ****\n"
            + "2022-06-24 : ****\n"
            + "2022-06-29 : ****\n"
            + "2022-07-04 : ****\n"
            + "2022-07-09 : *****\n"
            + "2022-07-14 : ******\n"
            + "2022-07-19 : *******\n"
            + "2022-07-24 : *******\n"
            + "2022-07-29 : *******\n"
            + "2022-08-03 : ********\n"
            + "2022-08-08 : ********\n"
            + "2022-08-13 : ******\n"
            + "2022-08-18 : ******\n"
            + "2022-08-23 : ******\n"
            + "2022-08-28 : ******\n"
            + "2022-09-02 : *****\n"
            + "2022-09-07 : ******\n"
            + "\n"
            + "Scale: * = $1296\n"
            + "\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("College", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel38GetPerformanceGraph() {   // Performance Graph - 2 month interval
    LocalDate startDate = LocalDate.of(2011, 1, 13);
    LocalDate endDate = LocalDate.of(2014, 8, 10);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("JAN 2011 : \n"
            + "MAR 2011 : \n"
            + "MAY 2011 : \n"
            + "JUL 2011 : *************\n"
            + "SEP 2011 : ************\n"
            + "NOV 2011 : ***********\n"
            + "JAN 2012 : ***********\n"
            + "MAR 2012 : ***********\n"
            + "MAY 2012 : ***************************\n"
            + "JUL 2012 : *****************************\n"
            + "SEP 2012 : ********************************\n"
            + "NOV 2012 : ********************************\n"
            + "JAN 2013 : *********************************\n"
            + "MAR 2013 : *********************************\n"
            + "MAY 2013 : **********************************\n"
            + "JUL 2013 : **************************************\n"
            + "SEP 2013 : ***************************************\n"
            + "NOV 2013 : **************************************************\n"
            + "JAN 2014 : *********************************************\n"
            + "MAR 2014 : ******************************************\n"
            + "MAY 2014 : ***************************************\n"
            + "JUL 2014 : ****************************************\n"
            + "\n"
            + "Scale: * = $220\n"
            + "\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("Health", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel39GetPerformanceGraph() {   // Performance Graph - 2 month interval
    LocalDate startDate = LocalDate.of(1980, 1, 13);
    LocalDate endDate = LocalDate.of(2022, 8, 10);
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");

    StringBuilder expectedOutput = new StringBuilder();
    expectedOutput.append("1980 : \n"
            + "1982 : \n"
            + "1984 : \n"
            + "1986 : \n"
            + "1988 : \n"
            + "1990 : \n"
            + "1992 : \n"
            + "1994 : \n"
            + "1996 : \n"
            + "1998 : \n"
            + "2000 : \n"
            + "2002 : \n"
            + "2004 : \n"
            + "2006 : \n"
            + "2008 : \n"
            + "2010 : \n"
            + "2012 : ****\n"
            + "2014 : *****\n"
            + "2016 : **************\n"
            + "2018 : ***********************\n"
            + "2020 : **************************************************\n"
            + "2022 : ***\n"
            + "\n"
            + "Scale: * = $1498\n"
            + "\n");

    assertEquals(expectedOutput.toString(), model.getPerformanceGraph("Health", fp.toString(),
            startDate, endDate, datesFromAPI).toString());
  }

  @Test
  public void testModel33ValidFilePath() {   //This might fail in other systems. Therefore catch
    try {
      String filePath = "C:/Users/rahul/IdeaProjects/A5/FlexiblePortfoliosModelTest.xml";
      assertTrue(model.checkValidFilePath(filePath));
      fail();
    } catch (Error e) {
      //
    }
  }

  @Test
  public void testModel34ValidInvestmentAmt() {   //Valid investment amount
    assertTrue(model.isInvestmentAmtValid("50.0"));
  }

  @Test
  public void testModel35ValidInvestmentAmt() {   //Negative investment amount
    assertFalse(model.isInvestmentAmtValid("-50"));
  }

  @Test
  public void testModel36ValidInvestmentAmt() {   //Fractional valid investment amount
    assertTrue(model.isInvestmentAmtValid("50.56"));
  }

  @Test
  public void testModel37ValidInvestmentAmt() {   // investment amt can't be 0
    assertFalse(model.isInvestmentAmtValid("0.0"));
  }

  @Test
  public void testModel38ValidInvestmentAmt() {   // investment amt valid
    assertTrue(model.isInvestmentAmtValid("60"));
  }

  @Test
  public void testModel39ValidCommFee() {   //Valid Commission fee
    assertTrue(model.isCommFeeValid("50"));
  }

  @Test
  public void testModel40ValidCommFee() {   //Negative Commission fee
    assertFalse(model.isCommFeeValid("-50"));
  }

  @Test
  public void testModel41ValidCommFee() {   //Fractional valid Commission fee
    assertTrue(model.isCommFeeValid("50.56"));
  }

  @Test
  public void testModel42ValidCommFee() {   // Commission fee can be 0
    assertTrue(model.isCommFeeValid("0.0"));
  }

  @Test
  public void testModel43ValidCommFee() {   // Commission fee valid
    assertTrue(model.isCommFeeValid("60"));
  }

  @Test
  public void testModel44ValidPercentage() {   //Valid percentage
    assertTrue(model.isPercentageValid("50"));
  }

  @Test
  public void testModel45ValidPercentage() {   //Negative percentage
    assertFalse(model.isPercentageValid("-50"));
  }

  @Test
  public void testModel46ValidPercentage() {   //Fractional valid percentage
    assertTrue(model.isPercentageValid("50.56"));
  }

  @Test
  public void testModel47ValidPercentage() {   // percentage can't be 0
    assertFalse(model.isPercentageValid("0.0"));
  }

  @Test
  public void testModel48ValidPercentage() {   // percentage valid
    assertTrue(model.isPercentageValid("60"));
  }

  @Test
  public void testModel49ValidPercentage() {   //Valid Interval
    assertTrue(model.isIntervalValid("50"));
  }

  @Test
  public void testModel50ValidInterval() {   //Negative Interval
    assertFalse(model.isIntervalValid("-50"));
  }

  @Test
  public void testModel51ValidInterval() {   //Fractional Interval is invalid
    assertFalse(model.isIntervalValid("50.56"));
  }

  @Test
  public void testModel52ValidInterval() {   // Interval can't be 0
    assertFalse(model.isIntervalValid("0.0"));
  }

  @Test
  public void testModel53ValidInterval() {   // Interval valid
    assertTrue(model.isIntervalValid("60"));
  }

  @Test
  public void testModel54StartDate() {   //
    assertTrue(model.isStartDateValid("2022-01-01"));
  }

  @Test
  public void testModel55StartDate() {   //
    assertFalse(model.isStartDateValid("2022-"));
  }

  @Test
  public void testModel56StartDate() {
    assertTrue(model.isStartDateValid("2024-02-07"));
  }

  @Test
  public void testModel58EndDate() {   // Invalid end date
    assertFalse(model.isEndDateValid("2022-02-04", LocalDate.of(
            2022, 4, 1)));
  }

  @Test
  public void testModel59EndDate() {   // Valid end date
    assertTrue(model.isEndDateValid("2022-04-06", LocalDate.of(
            2022, 5, 5)));
  }

  @Test
  public void testModel60PercentagesSum100() {   //
    assertFalse(model.isPercentagesSum100());
  }

  @Test
  public void testModel61GetStockList() {   //
    assertEquals("[AAPL, AMZN, GOOG, MSFT, TSLA, BAC, BRK, HD, JNJ, JPM, "
            + "LLY, MA, NVDA, PFE, PG, TSM, UNH, V, WMT, XOM]", model.getStockList().toString());
  }

  @Test
  public void testModel62DcaTest1() {   //
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "NewCollege";
    double investmentAmount = 0.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
            intervals, filePath, datesFromAPI, purpose);

    boolean actualOutput = false;
    boolean expectedOutput = true;

    //// Read xml and check if stock details name are written in that
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new File(filePath));
      document.getDocumentElement().normalize();
      NodeList portfolioList = document.getElementsByTagName("pfName");
      for (int i = 0; i < portfolioList.getLength(); i++) {
        Node portfolio = portfolioList.item(i);
        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;
          // Check if new pf name is in xml
          if (Objects.equals("NEWCOLLEGE",
                  (portfolioElement.getAttribute("name")).toUpperCase())) {
            actualOutput = true;
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }

    assertEquals(expectedOutput, actualOutput);

  }

  @Test
  public void testModel62DcaTest2() {   //  DCA - CB
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "NewCollege";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
            intervals, filePath, datesFromAPI, purpose);

    assertEquals(0.0, model.getCostBasis("NewCollege", fp.toString(),
                    LocalDate.of(2020,8,30)),
            0.00);
    assertEquals(1015.0, model.getCostBasis("NewCollege", fp.toString(),
                    LocalDate.of(2021,1,29)),
            0.00);
    assertEquals(6090.0, model.getCostBasis("NewCollege", fp.toString(),
                    LocalDate.of(2021,6,4)),
            0.00);
    assertEquals(13195.0, model.getCostBasis("NewCollege", fp.toString(),
                    LocalDate.of(2022,3,4)),
            0.00);
  }

  @Test
  public void testModel62DcaTest3() {   //  DCA - Composition
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "FAANG";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 90;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
            intervals, filePath, datesFromAPI, purpose);

    assertEquals("* Portfolio Name : FAANG\n"
            + "* Ticker Symbol: AAPL, Number of Shares: 10.66019669070247\n"
            + "* Ticker Symbol: AMZN, Number of Shares: 0.37773033643214443\n"
            + "* Ticker Symbol: MSFT, Number of Shares: 8.433917391936827\n",
            model.getComposition("FAANG", fp.toString(),
                    LocalDate.of(2022,3,4)));
  }


  @Test
  public void testModel62DcaTest4() {   //  DCA - Value
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();
    String pfName = "FAANG2";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 120;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
            intervals, filePath, datesFromAPI, purpose);

    assertEquals(0.0, model.getTotal("FAANG2", fp.toString(),
                    LocalDate.of(2020,8,30), "flexible"),
            0.00);
    assertEquals(1036.9451345007383, model.getTotal("FAANG2", fp.toString(),
                    LocalDate.of(2021,1,29), "flexible"),
            0.00);
    assertEquals(2031.5279025763343, model.getTotal("FAANG2", fp.toString(),
                    LocalDate.of(2021,6,4), "flexible"),
            0.00);
    assertEquals(4139.000913881557, model.getTotal("FAANG2", fp.toString(),
                    LocalDate.of(2022,3,4), "flexible"),
            0.00);
  }



  @Test
  public void testModel63AxisGraph() {   //
    Path fp = Paths.get(currentPath.toString(), "FlexiblePortfoliosModelTest.xml");
    String filePath = fp.toString();

    String ex = "{MAR 2022=4365, APR 2022=3941, MAY 2022=63825, JUN 2022=6073, JUL 2022=10270, "
            + "AUG 2022=7606, SEP 2022=7655}";

    assertEquals(ex, model.getAxisForGraph("College", filePath,
            LocalDate.of(2022, 3, 1),
            LocalDate.of(2022, 9, 1), datesFromAPI).toString());

  }

  @Test
  public void testModelRebalanceAfterDCA() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestRebalance";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
      intervals, filePath, datesFromAPI, purpose);

    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 50);
    model.saveTsAndPerc("MSFT", 25);
    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022,2,2),
        datesFromAPI, "flexible");

    assertEquals(0.0, model.getCostBasis("TestRebalance", fp.toString(),
        LocalDate.of(2020,8,30)),
      0.00);
    assertEquals(1015.0, model.getCostBasis("TestRebalance", fp.toString(),
        LocalDate.of(2021,1,29)),
      0.00);
    assertEquals(6090.0, model.getCostBasis("TestRebalance", fp.toString(),
        LocalDate.of(2021,6,4)),
      0.00);
    assertEquals(27843.33, model.getCostBasis("TestRebalance", fp.toString(),
      LocalDate.of(2022,2,2)),
      0.1);
  }

  @Test
  public void testModelRebalanceBetweenDCA() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestRebalanceBetween";
    double investmentAmount = 1000.0;
    double commFee = 5;
    LocalDate startDate = LocalDate.of(2021, 1, 2);
    LocalDate endDate = LocalDate.of(2022, 1, 2);
    int intervals = 30;
    String purpose = "create";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 30);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("MSFT", 45);
    // Creation using DCA
    model.dollarCostAveraging(pfName, investmentAmount, commFee, startDate, endDate,
      intervals, filePath, datesFromAPI, purpose);

    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 50);
    model.saveTsAndPerc("MSFT", 25);
    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2021,4,1),
      datesFromAPI, "flexible");

    assertEquals(0.0, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2020,8,30)),
      0.00);
    assertEquals(1015.0, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2021,1,29)),
      0.00);
    assertEquals(9135.19, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2021,6,4)),
      0.1);
    assertEquals(6090.19, model.getCostBasis(pfName, fp.toString(),
          LocalDate.of(2021,4,2)), 0.0);
    assertEquals(7589.89, model.getTotal(pfName, fp.toString(),
      LocalDate.of(2021,4,29), "Flexible"), 0.1);
  }

  @Test
  public void testModelRebalance() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancing";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);
    model.saveTsAndPerc("GOOG", 25);
    assertEquals(2056.39, model.getTotal(pfName, fp.toString(),
        LocalDate.of(2022,12,2), "Flexible"), 0.1);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022,12,2),
      datesFromAPI, "Flexible");

    assertEquals(7279.37, model.getCostBasis(pfName, fp.toString(),
        LocalDate.of(2022,12,2)), 0.1);

    assertEquals(2056.39, model.getTotal(pfName, fp.toString(),
        LocalDate.of(2022,12,2), "Flexible"), 0.1);

    assertEquals("* Portfolio Name : TestNormalRebalancing\n"
        + "* Ticker Symbol: GOOG, Number of Shares: 5.098656153922444\n"
        + "* Ticker Symbol: AAPL, Number of Shares: 3.4780968811311817\n"
        + "* Ticker Symbol: AMZN, Number of Shares: 5.461569106554765\n"
        + "* Ticker Symbol: TSLA, Number of Shares: 2.6382915939648974\n",
        model.getComposition(pfName, fp.toString(),
        LocalDate.of(2022,12,2)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelRebalanceInvalidWeights() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancing";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2022,12,2),
      datesFromAPI, "Flexible");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelRebalanceBetweenTxnInvalidStocks() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancingBetweenTxn";

    // Percentage Split
    model.saveTsAndPerc("AAPL", 25);
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 25);
    model.saveTsAndPerc("GOOG", 25);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2016,12,2),
      datesFromAPI, "Flexible");
  }

  @Test
  public void testModelRebalanceBetweenTxn() {   //
    Path fp = Paths.get(currentPath.toString(), "TestRebalancedPortfolios.xml");
    String filePath = fp.toString();
    String pfName = "TestNormalRebalancingBetweenTxn";

    // Percentage Split
    model.saveTsAndPerc("AMZN", 25);
    model.saveTsAndPerc("TSLA", 75);

    assertEquals(2946.90, model.getTotal(pfName, filePath,
        LocalDate.of(2016, 12,2), "Flexible"), 0.1);

    model.rebalanceExistingPortfolio(pfName, filePath, LocalDate.of(2016,12,2),
        datesFromAPI, "Flexible");

    assertEquals(2946.90, model.getTotal(pfName, filePath,
        LocalDate.of(2016, 12,2), "Flexible"), 0.1);
    assertEquals(9711.56, model.getTotal(pfName, filePath,
        LocalDate.of(2017, 12,6), "Flexible"), 0.1);
    assertEquals(9327.22,
        model.getCostBasis(pfName, filePath, LocalDate.of(2017, 12,6)),
        0.1);
    assertEquals(4813.22,
        model.getCostBasis(pfName, filePath, LocalDate.of(2017, 10,9)),
        0.1);

    assertEquals(5162.91, model.getTotal(pfName, filePath,
        LocalDate.of(2017, 10,9), "Flexible"), 0.1);
  }
}