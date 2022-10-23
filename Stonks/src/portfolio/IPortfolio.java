package portfolio;

import java.io.FileNotFoundException;

public interface IPortfolio {
  String getPortfolioComposition();

  double getPortfolioValue();

  void savePortfolio(String path) throws IllegalArgumentException;

  void retrievePortfolio(String path) throws FileNotFoundException;
}
