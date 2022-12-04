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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class is used to read the values from the portfolio given the values of the ticker symbols
 * and the number of stocks.
 */
public class ReadPortfolio {

  /**
   * This function reads the list of stocks for a given date from the portfolio.
   *
   * @param pfNameComp   (String) the portfolio name whose composition is required
   * @param filePath     (String) path of the xml file
   * @param dateFromUser (String) to retrieve the stocks based on the date entered by user.
   * @return a hashmap with the list of stocks and the number of shares owned.
   */
  public HashMap<String, Double> readXML(String pfNameComp, String filePath,
                                          LocalDate dateFromUser) {
    HashMap<String, Double> pfDetails = new LinkedHashMap<>();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      Document document = factory.newDocumentBuilder().parse(new File(filePath));
      document.getDocumentElement().normalize();
      NodeList portfolioList = document.getElementsByTagName("pfName");

      for (int i = 0; i < portfolioList.getLength(); i++) {
        Node portfolio = portfolioList.item(i);

        if (portfolio.getNodeType() == Node.ELEMENT_NODE) {
          Element portfolioElement = (Element) portfolio;

          if (Objects.equals(pfNameComp.toUpperCase(),
                  (portfolioElement.getAttribute("name")).toUpperCase())) {
            NodeList portfolioDetails = portfolio.getChildNodes();
            for (int j = 0; j < portfolioDetails.getLength(); j++) {
              Node detail = portfolioDetails.item(j);

              if (detail.getNodeType() == Node.ELEMENT_NODE) {
                Element stockName = (Element) detail;
                LocalDate datePF;
                if (dateFromUser != null) {
                  String dateFromPf = stockName.getAttribute("DateBought");
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                  datePF = LocalDate.parse(dateFromPf, formatter);
                } else {
                  datePF = LocalDate.of(3000, 1, 1);
                }

                String key = stockName.getAttribute("StockName");
                double value = Double.parseDouble(stockName.getTextContent());
                if (dateFromUser != null) {
                  if (dateFromUser.compareTo(datePF) >= 0) {
                    if (pfDetails.containsKey(key)) {
                      value = value + pfDetails.get(key);
                    }
                    pfDetails.put(key, value);
                  }
                } else {
                  if (pfDetails.containsKey(key)) {
                    value = value + pfDetails.get(key);
                  }
                  pfDetails.put(key, value);
                }
              }
            }
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }
    return pfDetails;
  }

}