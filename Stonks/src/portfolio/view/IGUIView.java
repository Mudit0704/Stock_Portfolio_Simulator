package portfolio.view;

import portfolio.controller.Features;

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
  void addFeatures(Features features);

  /**
   * Displays the window responsible for getting a portfolio's value.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayGetPortfolioValueWindow(Features features);

  /**
   * Displays the window responsible for getting a portfolio's cost basis.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayGetPortfolioCostBasisWindow(Features features);

  /**
   * Displays the window responsible for selling a portfolio's stock.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displaySellStocksWindow(Features features);

  /**
   * Displays the window responsible for buying a portfolio's stock.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayBuyStocksWindow(Features features);

  /**
   * Displays the window responsible for creating a new flexible portfolio.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayCreateNewFlexibleWindow(Features features);

  /**
   * Displays the window responsible for performing fractional investment on an existing portfolio.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayFractionInvestmentWindow(Features features);

  /**
   * Displays the window responsible for creating a portfolio using dollar cost average strategy.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayCreateDollarCostAveragingWindow(Features features);

  /**
   * Displays the window responsible for displaying a portfolio's performance.
   *
   * @param features an object of {@link portfolio.controller.GUIPortfolioController} to perform the
   *                 callback functionality between the view and controller
   */
  void displayPortfolioPerformanceLineChart(Features features);
}
