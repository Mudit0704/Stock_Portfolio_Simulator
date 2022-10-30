package portfolio.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class MockStockService extends StockService {
  String filePath ;

  public MockStockService() {

  }

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