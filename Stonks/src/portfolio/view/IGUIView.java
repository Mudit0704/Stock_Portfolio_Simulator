package portfolio.view;

import portfolio.controller.Features;

public interface IGUIView extends ICommonView {
  void addFeatures(Features features);

  void displayGetPortfolioValueWindow(Features features);

  void displayGetPortfolioCostBasisWindow(Features features);

  void displaySellStocksWindow(Features features);

  void displayBuyStocksWindow(Features features);

  void displayCreateNewFlexibleWindow(Features features);

  void displayFractionInvestmentWindow(Features features);

  void displayCreateDollarCostAveragingWindow(Features features);
}
