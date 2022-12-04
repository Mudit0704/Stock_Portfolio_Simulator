package model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This class is used to write the values into the XML given the values of the portfolio like ticker
 * symbols and the number of stocks.
 */
public class WritePFNameXML extends AbstractWrite {

  /**
   * This function writes the list of stocks for a given date from the list of stocks csv
   * files/API.
   *
   * @param pfName   (String) the portfolio name
   * @param filePath (String) path of the stock csv files/API
   */
  public void writePFNameInXml(String pfName, String filePath) {
    try {
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = db.parse(filePath);

      Element pfList = doc.getDocumentElement();

      // Start - Get the PF Name. Create an element with this pf name
      Element newPfName = doc.createElement("pfName");
      Attr name = doc.createAttribute("name");
      name.setValue(pfName);
      newPfName.setAttributeNode(name);
      pfList.appendChild(newPfName);

      transformObjectHelper(filePath, doc); //Write

    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }


}
