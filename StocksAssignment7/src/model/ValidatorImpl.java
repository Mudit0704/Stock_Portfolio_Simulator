package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class has the implementations to check various fields such as stocks, dates, portfolio names
 * and ticker symbols.
 */
public class ValidatorImpl implements Validator {

  private final ReadPortfolio readPf;

  /**
   * Constructs a ValidatorImpl that initialises the read portfolio.
   */
  public ValidatorImpl() {
    readPf = new ReadPortfolio();
  }

  @Override
  public boolean checkIfCanSell(String pfName, String tickerSymbol, double numShares,
                                String filePath, LocalDate date) {

    HashMap<String, Double> pfDetails;
    pfDetails = readPf.readXML(pfName, filePath, date); // Read Pfdetails from xml
    if (pfDetails.isEmpty()) {
      return false;
    } else if (pfDetails.containsKey(tickerSymbol.toUpperCase())) {
      for (Map.Entry<String, Double> set : pfDetails.entrySet()) {
        if (Objects.equals(set.getKey(), tickerSymbol.toUpperCase())) {
          if (pfDetails.get(set.getKey()) > Math.abs(numShares)) {
            return true;
          }
        } else {
          continue;
        }
      }
    }
    return false;
  }

  @Override
  public boolean isPfNameUnique(String pfName, String filePath) {
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
          if (Objects.equals(pfName.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {
            return false;   //return false if portfolio name is not unique.
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }
    return true;   //return true if portfolio name is unique.
  }

  @Override
  public boolean isDateValid(String strDate, String purpose, ArrayList<LocalDate> datesFromAPI) {
    try {
      LocalDate dateFromUser = LocalDate.parse(strDate);
      if (Objects.equals(purpose, "Performance1") || Objects.equals(purpose, "Performance2")
              || Objects.equals(purpose, "composition")
              || Objects.equals(purpose, "cost basis")) {
        return true;
      }
      if (datesFromAPI.contains(dateFromUser)) {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  @Override
  public boolean checkValidFilePath(String path) {
    File f = new File(path);
    return f.exists() && !f.isDirectory();
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    PriceList objList = new PriceList("", null, "");
    ArrayList<String> stockList = objList.getPriceList();
    return stockList.contains(tickerSymbol.toUpperCase());
  }

  @Override
  public boolean isInvestmentAmtValid(String invtAmt) {
    try {
      double invt = Double.parseDouble(invtAmt);
      return invt > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isCommFeeValid(String commFee) {
    try {
      double comm = Double.parseDouble(commFee);
      return comm >= 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isNumOfStocksValid(String numOfStocks) {
    try {
      int num = Integer.parseInt(numOfStocks);
      return num > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isPercentageValid(String perc, HashMap<String, Double> mapForTsPerc) {
    try {
      double percentage = Double.parseDouble(perc);
      return percentage > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isIntervalValid(String interval) {
    try {
      int interv = Integer.parseInt(interval);
      return interv > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isStartDateValid(String strDate) {
    try {
      LocalDate.parse(strDate);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isEndDateValid(String strDate, LocalDate startDate) {
    try {
      LocalDate endDate = LocalDate.parse(strDate);
      return startDate.compareTo(endDate) < 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isNumberOfSharesValid(String num) {
    try {
      double numShares = Double.parseDouble(num);
      return (numShares > 0 && numShares % 1 == 0);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean doPercentagesSumTo100(HashMap<String, Double> mapForTsPerc) {
    double sum = 0.0;
    for (double value : mapForTsPerc.values()) {
      sum += value;
    }
    return sum == 100;
  }

}
