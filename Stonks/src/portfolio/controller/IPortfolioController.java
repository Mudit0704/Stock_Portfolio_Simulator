package portfolio.controller;

import java.io.IOException;
import portfolio.model.IPortfolios.PortfoliosBuilder;

public interface IPortfolioController {
  void run(PortfoliosBuilder portfoliosBuilder) throws IOException, InterruptedException;
}
