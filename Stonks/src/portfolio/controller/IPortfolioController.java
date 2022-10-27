package portfolio.controller;

import java.io.IOException;
import portfolio.model.IPortfolios;
import portfolio.model.Portfolios;

public interface IPortfolioController {
  void run(IPortfolios portfolio) throws IOException, InterruptedException;
}
