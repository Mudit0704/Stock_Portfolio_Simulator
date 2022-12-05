package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This interface has the implementations to operate on a portfolio .
 */
public interface Portfolio {

  /**
   * This implementation fetches the portfolio name of the current portfolio.
   *
   * @return String pfName portfolio name
   */
  String getPfName();

  /**
   * This implementation calculates the portfolio total based on a certain date.
   * @param date date entered by the user
   * @param type type of portfolio
   *
   * @return total of the portfolio
   */
  double getPortfolioTotal(LocalDate date, String type);

  /**
   * This implementation calculates the portfolio total based on a certain date.
   * @param date date entered by the user
   *
   * @return composition (double) of the portfolio
   */
  String getPortfolioComposition(LocalDate date);

  /**
   * This implementation calculates the cost basis of a portfolio based on a certain date.
   *
   * @return cost basis (double) of the portfolio
   */
  double getPortfolioCostBasis(LocalDate dateFromUser);

  /**
   * This implementation calculates the performance of a portfolio based on a certain date.
   * @param date1 start date entered by the user for the LineChartView
   * @param date2 end date entered by the user for the LineChartView
   *
   * @return performance LineChartView of the portfolio
   */
  StringBuilder getPortfolioPerformanceGraph(LocalDate date1,
                                             LocalDate date2,
                                             ArrayList<LocalDate> validDatesInAPI);

  /**
   * This implementation writes the ticker symbol, number of shares and date details into XML .
   */
  void writeStockDetailsInXML(String tickerSymbol, double numShares,
                              double commFee, LocalDate date);

  /**
   * This implementation writes the portfolio name into XML.
   */
  void writePFNameInXml();

  HashMap<String, Integer> getAxisForGraph(LocalDate date1,
                                   LocalDate date2, ArrayList<LocalDate> validDatesInAPI);

  void rebalanceStockInPortfolio(HashMap<LocalDate, Double> priceOnTimestamps, String tickerSymbol,
      Double stockProportion);

  List<String> getStocksInPortfolio(LocalDate date);
}
