package portfolio.controller;

import java.io.IOException;
import portfolio.model.IPortfolio;

public interface IPortfolioController {
  void run(IPortfolio portfolio) throws IOException;
}
