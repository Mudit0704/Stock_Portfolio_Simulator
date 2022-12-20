package model;

import org.w3c.dom.Document;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This abstract class is used to write the XML values into the portfolio based on the type
 * (flexible or inflexible).
 */
public abstract class AbstractWrite {

  /**
   * This method is responsible for printing the parent and child nodes in the xml. It uses the
   * pretty print functionality for maintaining the alignment and indentation of the XML file. It
   * uses the standard java methods of the TransformerFactory class.
   *
   * @param filePath path of the XML file where the stock details will be available. This is an
   *                 absolute path
   * @param doc      DOM source file after parsing the XML file
   */
  protected void transformObjectHelper(String filePath, Document doc) {
    try {
      Path currentPath = Paths.get(System.getProperty("user.dir"));
      Path fp = Paths.get(currentPath.toString(), "prettyprint.xsl");
      String filep = fp.toString();
      Transformer transformer = TransformerFactory.newInstance()
              .newTransformer(new StreamSource(new File(filep)));
      StreamResult result = new StreamResult(new File(filePath));
      DOMSource source = new DOMSource(doc);
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(source, result);
    } catch (TransformerException e) {
      throw new RuntimeException();
    }
  }
}
