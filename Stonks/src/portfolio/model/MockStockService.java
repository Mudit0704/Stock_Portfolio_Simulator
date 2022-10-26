package portfolio.model;

import java.io.IOException;
import java.io.InputStream;

class MockStockService implements IAPIService {

  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    return System.in;
  }

}