package model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class extends the AbstractWrite class to write the stock details of a portfolio into the
 * XML.
 */
public class WriteStockDetailsXML extends AbstractWrite {

  /**
   * This function reads the list of stocks for a given date from the list of stocks csv files/API.
   *
   * @param pfName       (String) the portfolio name
   * @param filePath     (String) path of the stock csv files/API
   * @param date         (LocalDate) to retrieve the stocks based on the dates
   * @param tickerSymbol ticker symbol (String) that represents the company for which the user is
   *                     interested in investing
   * @param numShares    number of shares (int) that the user invested for a given ticker symbol
   */
  public void writeStockDetailsInXML(String pfName, String tickerSymbol, double numShares,
                                     double commFee, String filePath, LocalDate date) {

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

            //        NodeList portfolioDetails = portfolio.getChildNodes();
            //        for (int j = 0; j < portfolioDetails.getLength(); j++) {
            //          Node detail = portfolioDetails.item(j);
            //          if (detail.getNodeType() == Node.ELEMENT_NODE) {
            //            Element stockName = (Element) detail;
            //            if (Objects.equals(tickerSymbol.toUpperCase(), stockName.getAttribute(
            //                    "StockName")) && Objects.equals(String.valueOf(date), stockName
            //                    .getAttribute("DateBought"))) {   //Check if same stock and date
            //              // already
            //              // exist for that particular portfolio
            //              numShares = numShares + Double.parseDouble(stockName.getTextContent());
            //              stockName.setTextContent("" + numShares);
            //              portfolio.removeChild(
            //                      stockName);   //Remove the old stock. The new stock node
            //                      with added number
            //              // of shares will be created below with the same date
            //              break;
            //            }
            //          }
            //        }

            Node pfNameNode = doc.getElementsByTagName("pfName").item(i);

            Element stockName = doc.createElement("Stock");

            Attr attrStockName = doc.createAttribute("StockName");
            attrStockName.setValue(tickerSymbol.toUpperCase());
            stockName.setAttributeNode(attrStockName);

            if (date != null) {
              Attr attrDate = doc.createAttribute("DateBought");
              attrDate.setValue(String.valueOf(date));
              stockName.setAttributeNode(attrDate);

              Attr attrCommFee = doc.createAttribute("CommissionFee");
              attrCommFee.setValue(String.valueOf(commFee));
              stockName.setAttributeNode(attrCommFee);
            }

            Text elementValue = doc.createTextNode("" + numShares);
            stockName.appendChild(elementValue);

            pfNameNode.appendChild(stockName);
            break;
          }
        }
      }

      transformObjectHelper(filePath, doc); //Write

    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }


}
