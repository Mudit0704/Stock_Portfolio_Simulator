package portfolio.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class MockStockService extends StockService {
  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    InputStream in;
    try {
      in = new FileInputStream(System.getProperty("user.dir") + "/test/testData.txt");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return in;
  }
}