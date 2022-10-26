package portfolio.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.Scanner;

class MockStockService implements IAPIStockService {


  @Override
  public InputStream queryAPI(String queryString) throws IOException {
    return null;
  }

  @Override
  public InputStream getStockPrices(String tickerSymbol) {
    InputStream in;
    try {
      in = new FileInputStream(System.getProperty("user.dir") + "/test/testData.txt");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return in;
  }
}