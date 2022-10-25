package portfolio.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 */
public interface IPortfolio {

  /**
   * @return
   */
  String getPortfolioComposition();

  /**
   * @param date
   * @return
   */
  String getPortfolioValue(Date date);

  /**
   * @param path
   * @throws IllegalArgumentException
   */
  void savePortfolio(String path) throws IllegalArgumentException;

  /**
   * @param path
   * @throws FileNotFoundException
   */
  boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException;
}
