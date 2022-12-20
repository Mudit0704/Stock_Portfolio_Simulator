package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class has the implementations to read the stock prices and update values for total,
 * cost basis and performance of a portfolio over a certain time range.
 */

public class PriceList {

  private final String pfName;
  private final LocalDate dateFromUser;
  private final String filePath;
  private final ReadPortfolio readPf;

  private final ArrayList<String> stockList;

  /**
   * Constructs a PriceList that initialises the class.
   *
   * @param pfName       Portfolio Name(String), that was entered by the user in the text-based UI
   * @param dateFromUser (String) to retrieve the stocks based on the dates.
   * @param filePath     path of the XML file where the stock details will be placed. This is an
   *                     absolute path.
   */
  public PriceList(String pfName, LocalDate dateFromUser, String
          filePath) {
    this.pfName = pfName;
    this.dateFromUser = dateFromUser;
    this.filePath = filePath;
    readPf = new ReadPortfolio();

    stockList = new ArrayList<>(
            Arrays.asList("AAPL", "AMZN", "GOOG", "MSFT", "TSLA", "BAC", "BRK", "HD", "JNJ", "JPM",
                    "LLY", "MA", "NVDA", "PFE", "PG", "TSM", "UNH", "V", "WMT", "XOM"));
  }

  /**
   * This implementation updates the stock list for calculating total.
   *
   * @return a hashmap with the list of stocks and their prices on or before the current date
   */
  public HashMap<String, Double> stockListUpdate(String type) {
    HashMap<String, Double> stockPriceList = new HashMap<>();
    double value;
    HashMap<String, Double> pfDetails;
    if (type.equals("Inflexible")) {
      pfDetails = readPf.readXML(pfName, filePath,
              null);  // Read pfDetails from xml to get existing pf names
    } else {
      pfDetails = readPf.readXML(pfName, filePath,
              dateFromUser);  // Read pfDetails from xml to get existing pf names
    }

    for (String s : pfDetails.keySet()) {
      DataSource objDataSource = new DataSourceAPI(s, dateFromUser);
      value = objDataSource.stockPriceOnParticularDate();

      if (value == -1) {
        stockPriceList = null;
      } else {
        assert stockPriceList != null;
        stockPriceList.put(s, value);
      }
    }
    return stockPriceList;
  }

  public ArrayList<String> getValidStocks() {
    return stockList;
  }

  /**
   * This implementation gets the stock list and returns it for calculating total, cost basis and
   * performance of a portfolio.
   *
   * @return a hashmap with the list of stocks and their prices on or before the current date
   */
  public ArrayList<String> getPriceList() {
    return stockList;
  }

  /* Before API
  public HashMap<String, Integer> stockListUpdate(HashMap<String, Integer> StockPriceList,
                                                  String date) {
    try {
      ArrayList<String> stockList = new ArrayList<>(
              Arrays.asList("AAPL", "AMZN", "GOOG", "MSFT", "TSLA", "BAC", "BRK",
              "HD", "JNJ", "JPM",
                      "LLY", "MA", "NVDA", "PFE", "PG", "TSM", "UNH", "V", "WMT", "XOM"));
      Path currentPath = Paths.get(System.getProperty("user.dir"));

      for (String s : stockList) {
        Path fp = Paths.get(currentPath.toString(), "/stockPricesLists/stockPrices_" + s + ".csv");
        BufferedReader br = new BufferedReader(
                new FileReader(fp.toString()));
        String line;
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
          String[] str = line.split(",");
          String key = str[0];
          Integer value = Integer.parseInt(str[1]);
          if (Objects.equals(key, date)) {
            StockPriceList.put(s, value);
            break;
          } else {
            StockPriceList.put(s,
                    0);    // Assign stock price to 0 if stock price is not
                    available for that particular date
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(StockPriceList);
    return StockPriceList;
  }
   */

}
