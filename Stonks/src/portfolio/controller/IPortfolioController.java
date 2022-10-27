package portfolio.controller;

import java.io.IOException;
import portfolio.model.Portfolios;

public interface IPortfolioController {
  void run(Portfolios portfolio) throws IOException, InterruptedException;
}
