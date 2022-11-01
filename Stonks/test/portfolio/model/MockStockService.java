package portfolio.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents a dummy service that imitates an actual API fetch service to retrieve
 * stock data.
 */
public class MockStockService extends StockService {
  String filePath ;


  /**
   * Constructor to initialize mock service by taking input path to mock fetching API data from.
   *
   * @param filePath the path where dummy data exists.
   */
  public MockStockService(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    InputStream in;
    try {
      in = new FileInputStream(System.getProperty("user.dir") + filePath);
    } catch (FileNotFoundException e) {
      throw new IOException();
    }
    return in;
  }
}