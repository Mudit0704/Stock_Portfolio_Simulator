package portfolio.controller;

import java.io.IOException;
import portfolio.model.IPortfoliosModel;

/**
 * Represents the controller and its related operations required to implement portfolios.
 */
public interface IPortfolioController {

  /**
   * Starts the controller's operations.
   *
   * @param portfolios Represents the model object and is of type {@link IPortfoliosModel}.
   * @throws IOException if an I/O error occurs.
   */
  void run(IPortfoliosModel portfolios) throws IOException;
}
