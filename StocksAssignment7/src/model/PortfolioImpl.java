package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class has the implementations to operate on a portfolio .
 */
public class PortfolioImpl implements Portfolio {
  private final ReadPortfolio readPf;
  private final String pfName;
  private final String filePath;

  /**
   * Constructs a PortfolioImpl that initialises the portfolio names and file path of the XML.
   */
  public PortfolioImpl(String pfName, String filePath) {
    this.pfName = pfName;
    this.filePath = filePath;
    readPf = new ReadPortfolio();
  }

  @Override
  public String getPfName() {
    return this.pfName;
  }

  @Override
  public double getPortfolioTotal(LocalDate date, String type) {

    HashMap<String, Double> stockPriceList;
    PriceList price1 = new PriceList(this.pfName, date, this.filePath);
    stockPriceList = price1.stockListUpdate(type); //Get stock price list
    double total = 0;
    double price = 0;
    HashMap<String, Double> pfDetails;
    if (Objects.equals(type, "Inflexible")) {
      pfDetails = readPf.readXML(this.pfName, this.filePath, null);
    } else {
      pfDetails = readPf.readXML(this.pfName, this.filePath, date);  // Read pfdetails from xml
    }

    if (stockPriceList == null) {
      return -1;
    } else {
      for (Map.Entry<String, Double> set : pfDetails.entrySet()) {   //Calculate Total
        if (stockPriceList.containsKey(set.getKey())) {
          price = stockPriceList.get(set.getKey()) * set.getValue();
        }
        total = total + price;
      }
      return total;
    }
  }

  @Override
  public String getPortfolioComposition(LocalDate date) {

    StringBuilder pfComp = new StringBuilder();
    HashMap<String, Double> pfDetails;
    pfDetails = readPf.readXML(this.pfName, this.filePath, date); // Read Pfdetails from xml
    if (!(pfDetails.isEmpty())) {
      pfComp.append("* Portfolio Name : ").append(this.pfName).append("\n");
      pfDetails.forEach((key, value) -> pfComp.append("* Ticker Symbol: ").append(key)
              .append(", Number of Shares: ").append(value).append("\n"));
    }
    return pfComp.toString();
  }

  @Override
  public double getPortfolioCostBasis(LocalDate dateFromUser) {
    double total = 0;
    double fee = 0;
    double stockPrice;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      Document document = factory.newDocumentBuilder().parse(new File(this.filePath));
      document.getDocumentElement().normalize();
      NodeList portfolioList = document.getElementsByTagName("pfName");

      for (int i = 0; i < portfolioList.getLength(); i++) {
        Node portfolio = portfolioList.item(i);

        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;

          if (Objects.equals(this.pfName.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {
            NodeList portfolioDetails = portfolio.getChildNodes();
            for (int j = 0; j < portfolioDetails.getLength(); j++) {
              Node detail = portfolioDetails.item(j);

              if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element stockName = (Element) detail;

                String dateFromPf = stockName.getAttribute("DateBought");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate datePF = LocalDate.parse(dateFromPf, formatter);

                String tickerSymbol = stockName.getAttribute("StockName");
                double commissionFee = Double.parseDouble(
                        stockName.getAttribute("CommissionFee"));
                double value = Double.parseDouble(stockName.getTextContent());
                if (dateFromUser.compareTo(datePF) >= 0) {
                  fee = fee + commissionFee;
                  if (value > 0) {
                    DataSource objDataSource = new DataSourceAPI(tickerSymbol, datePF);
                    stockPrice = objDataSource.stockPriceOnParticularDate();
                    total = total + (stockPrice * value);
                  }
                }
              }
            }
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }
    total = total + fee;
    return total;
  }

  @Override
  public StringBuilder getPortfolioPerformanceGraph(LocalDate date1,
                                                    LocalDate date2,
                                                    ArrayList<LocalDate> validDatesInAPI) {
    Graph gr = new Graph(date1, date2);
    ArrayList<String> timestampList = gr.generateTimestamps();

    ArrayList<Integer> totals = new ArrayList<>();
    HashMap<String, LocalDate> timestampWithLastDates = gr.timestampLastDate(
            timestampList, date2, validDatesInAPI);

    StringBuilder finalGraph = new StringBuilder();
    for (Map.Entry<String, LocalDate> set : timestampWithLastDates.entrySet()) {
      LocalDate dateFromTimeRange = set.getValue();
      double value = 0;
      if (dateFromTimeRange != null) {
        value = getPortfolioTotal(dateFromTimeRange, "Flexible");
      }
      totals.add((int) value);
    }

    int max = Collections.max(totals);
    int scale = max / 50;
    if (scale != 0) {
      for (int i = 0; i < totals.size(); i++) {
        int numOfStars = totals.get(i) / scale;
        String stars = "*".repeat(Math.max(0, numOfStars));
        finalGraph.append(timestampList.get(i)).append(" : ").append(stars).append("\n");
      }
      finalGraph.append("\nScale: * = $").append(scale).append("\n\n");
    } else {
      finalGraph.append("You did not own any stocks during this time period.\n");
    }
    return finalGraph;
  }

  @Override
  public void writeStockDetailsInXML(String tickerSymbol, double numShares,
                                     double commFee, LocalDate date) {
    WriteStockDetailsXML st = new WriteStockDetailsXML();
    st.writeStockDetailsInXML(pfName, tickerSymbol, numShares, commFee, this.filePath, date);
  }

  @Override
  public void writePFNameInXml() {
    WritePFNameXML pf = new WritePFNameXML();
    pf.writePFNameInXml(this.pfName, this.filePath);
  }

  @Override
  public HashMap<String, Integer> getAxisForGraph(LocalDate date1,
                                                  LocalDate date2,
                                                  ArrayList<LocalDate> validDatesInAPI) {

    // {Timestamp : Value}
    HashMap<String, Integer> xyAxis = new LinkedHashMap<>();

    // {JAN 2022, FEB 2022, MAR 2022, APR 2022, MAY 2022, JUNE 2022}
    // {2018, 2019, 2020, 2021, 2022, 2023}
    Graph gr = new Graph(date1, date2);
    ArrayList<String> timestampList = gr.generateTimestamps();

    // {JAN 2022 : 2022-08-30, FEB 2022 : 2022-02-28}
    HashMap<String, LocalDate> timestampWithLastDates = gr.timestampLastDate(
            timestampList, date2, validDatesInAPI);

    int i = 0;
    for (Map.Entry<String, LocalDate> set : timestampWithLastDates.entrySet()) {
      LocalDate dateFromTimeRange = set.getValue();
      double value = 0;
      if (dateFromTimeRange != null) {
        value = getPortfolioTotal(dateFromTimeRange, "Flexible");
      }
      xyAxis.put(timestampList.get(i), (int) value);
      i += 1;
    }
    return xyAxis;
  }

}
