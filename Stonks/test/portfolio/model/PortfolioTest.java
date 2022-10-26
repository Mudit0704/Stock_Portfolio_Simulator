package portfolio.model;

import java.io.File;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PortfolioTest {

  @Rule
  private TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void setPortfolioStocks() {
  }

  @Test
  public void getPortfolioComposition() {
  }

  @Test
  public void getPortfolioValue() {
  }

  @Test
  public void savePortfolio() throws IOException {
    IPortfolio portfolio = new Portfolio();

    File file = folder.newFile("aadish.xml");
    portfolio.savePortfolio(file.getPath());
  }

  @Test
  public void retrievePortfolio() {
  }
}