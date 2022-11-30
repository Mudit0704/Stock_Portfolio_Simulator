package portfolio.view;

import portfolio.controller.IFeatures;

/**
 * Represents all the operations related to the GUI of the portfolio application.
 */
public interface IGUIView extends ICommonView {

  /**
   * Adds all the event handlers for different buttons representing the features supported by the
   * application.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void addFeatures(IFeatures features);

  /**
   * Displays the window responsible for getting a portfolio's value.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayGetPortfolioValueWindow(IFeatures features);

  /**
   * Displays the window responsible for getting a portfolio's cost basis.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayGetPortfolioCostBasisWindow(IFeatures features);

  /**
   * Displays the window responsible for selling a portfolio's stock.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displaySellStocksWindow(IFeatures features);

  /**
   * Displays the window responsible for buying a portfolio's stock.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayBuyStocksWindow(IFeatures features);

  /**
   * Displays the window responsible for creating a new flexible portfolio.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayCreateNewFlexibleWindow(IFeatures features);

  /**
   * Displays the window responsible for performing fractional investment on an existing portfolio.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayFractionInvestmentWindow(IFeatures features);

  /**
   * Displays the window responsible for creating a portfolio using dollar cost average strategy.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayCreateDollarCostAveragingWindow(IFeatures features);

  /**
   * Displays the window responsible for displaying a portfolio's performance.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayPortfolioPerformanceLineChart(IFeatures features);

  /**
   * Displays the window responsible for applying DCA on an existing portfolio.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayApplyDCAOnExistingPortfolioWindow(IFeatures features);
}
