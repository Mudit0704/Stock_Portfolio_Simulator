package portfolio.controller;

import java.io.IOException;
import portfolio.model.IPortfolios;

public interface IPortfolioController {
  void run(IPortfolios portfolios) throws IOException, InterruptedException;
}
