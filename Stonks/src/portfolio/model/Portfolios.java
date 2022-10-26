package portfolio.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Portfolios {
  private List<IPortfolio> portfolios = new ArrayList<>();

  public String getPortfolioComposition() {
    int portfolioNo = 0;
    StringBuilder composition = new StringBuilder("No portfolios");
    if (portfolios.size() > 0) {
      composition = new StringBuilder();
      for (IPortfolio portfolio : portfolios) {
        portfolioNo++;
        composition.append("Portfolio"+portfolioNo+"\n"+portfolio.getPortfolioComposition()+"\n");
      }
    }
    return composition.toString();
  }

  public double getPortfolioValue(LocalDate date) {
    double result = 0d;
    if (portfolios.size() > 0) {
      for(IPortfolio portfolio : portfolios) {
        result+=portfolio.getPortfolioValue(LocalDate.now());
      }
    }
    return result;
  }


  public boolean savePortfolio(String path) throws IllegalArgumentException {
    if(portfolios.size() == 0) {
      return false;
    }
    int portfolioNo = 0;
    for(IPortfolio portfolio : portfolios) {
      portfolioNo++;
      portfolio.savePortfolio(path+"portfolio"+portfolioNo+".xml");
    }

    return true;
  }

  public boolean retrievePortfolio(String path)
      throws IOException, ParserConfigurationException, SAXException {
    boolean result = false;

    File dir = new File(path);
    File[] files = dir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".xml");
      }
    });

    if (files != null && files.length != 0) {
      for (File file : files) {
        IPortfolio portfolio = new Portfolio();
        portfolio.retrievePortfolio(path + file.getName());
        portfolios.add(portfolio);
      }
      result = true;
    }

    return result;
  }

  public void setPortfolioStocks(Map<String, Integer> stocks) {
    IPortfolio portfolio = new Portfolio();
    portfolio.setPortfolioStocks(stocks);
    portfolios.add(portfolio);
  }
}
