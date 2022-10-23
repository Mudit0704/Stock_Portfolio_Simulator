package portfolio.model;

import java.io.FileNotFoundException;
import java.util.Date;

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
  double getPortfolioValue(Date date);

  /**
   * @param path
   * @throws IllegalArgumentException
   */
  void savePortfolio(String path) throws IllegalArgumentException;

  /**
   * @param path
   * @throws FileNotFoundException
   */
  void retrievePortfolio(String path) throws FileNotFoundException;
}
