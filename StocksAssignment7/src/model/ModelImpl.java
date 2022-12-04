package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * ModelImpl implements the Model interface. This implementation performs operation for
 * writing to the XML file, getting the composition of a portfolio from the XML file, getting the
 * total of shares for a given portfolio, checking the validity of a ticker symbol, portfolio name.
 */
public class ModelImpl implements Model {

  Validator valid;
  HashMap<String, Double> mapForTsPerc;
  private Portfolio objPF;

  public ModelImpl() {
    mapForTsPerc = new LinkedHashMap<>();
  }

  @Override
  public void writeStockDetails(String pfName, String tickerSymbol, double numShares,
                                double commFee, String filePath, LocalDate date) {
    objPF = new PortfolioImpl(pfName, filePath);
    objPF.writeStockDetailsInXML(tickerSymbol, numShares, commFee, date);
  }

  @Override
  public void writePortfolioName(String pfName, String filePath) {
    objPF = new PortfolioImpl(pfName, filePath);
    objPF.writePFNameInXml();
  }

  @Override
  public String getComposition(String pfNameComp, String filePath, LocalDate date) {
    objPF = new PortfolioImpl(pfNameComp, filePath);
    return (objPF.getPortfolioComposition(date));
  }

  @Override
  public double getTotal(String pfNameTotal, String filePath, LocalDate date, String type) {
    objPF = new PortfolioImpl(pfNameTotal, filePath);
    return (objPF.getPortfolioTotal(date, type));
  }

  @Override
  public double getCostBasis(String pfNameCB, String filePath, LocalDate dateFromUser) {
    objPF = new PortfolioImpl(pfNameCB, filePath);
    return (objPF.getPortfolioCostBasis(dateFromUser));
  }

  @Override
  public StringBuilder getPerformanceGraph(String pfNameGraph, String filePath, LocalDate date1,
                                           LocalDate date2, ArrayList<LocalDate> validDatesInAPI) {
    objPF = new PortfolioImpl(pfNameGraph, filePath);
    return (objPF.getPortfolioPerformanceGraph(date1, date2, validDatesInAPI));
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    valid = new ValidatorImpl();
    return (valid.isTickerSymbolValid(tickerSymbol.toUpperCase()));
  }

  @Override
  public boolean checkValidFilePath(String filePath) {
    valid = new ValidatorImpl();
    return (valid.checkValidFilePath(filePath));
  }

  @Override
  public boolean isNumberOfSharesValid(String num) {
    valid = new ValidatorImpl();
    return (valid.isNumberOfSharesValid(num));
  }

  @Override
  public boolean isPfNameUnique(String pfName, String filePath) {
    if (Objects.equals(pfName, "")) {
      return false;
    }
    valid = new ValidatorImpl();
    return (valid.isPfNameUnique(pfName, filePath));
  }

  @Override
  public boolean isDateValid(String strDate, String purpose, ArrayList<LocalDate> datesFromAPI) {
    valid = new ValidatorImpl();
    return (valid.isDateValid(strDate, purpose, datesFromAPI));
  }

  @Override
  public boolean checkIfCanSell(String pfName, String tickerSymbol, double numShares,
                                String filePath, LocalDate date) {
    valid = new ValidatorImpl();
    return (valid.checkIfCanSell(pfName, tickerSymbol, numShares, filePath, date));
  }

  @Override
  public boolean isInvestmentAmtValid(String invtAmt) {
    valid = new ValidatorImpl();
    return (valid.isInvestmentAmtValid(invtAmt));
  }

  @Override
  public boolean isCommFeeValid(String commFee) {
    valid = new ValidatorImpl();
    return (valid.isCommFeeValid(commFee));
  }

  @Override
  public double getInvestmentAmount(String invtAmt) {
    return Double.parseDouble(invtAmt);
  }

  @Override
  public double getCommFee(String commFee) {
    return Double.parseDouble(commFee);
  }

  //  @Override
  //  public boolean isNumOfStocksValid(String numOfStocks) {
  //    valid = new ValidatorImpl();
  //    return (valid.isNumOfStocksValid(numOfStocks));
  //  }
  //
  //  @Override
  //  public int getNumOfStock(String numOfStocks) {
  //    return Integer.parseInt(numOfStocks);
  //  }

  @Override
  public boolean isPercentageValid(String perc) {
    valid = new ValidatorImpl();
    return (valid.isPercentageValid(perc, mapForTsPerc));
  }

  @Override
  public double getPercentage(String perc) {
    return Double.parseDouble(perc);
  }

  @Override
  public boolean isIntervalValid(String interval) {
    valid = new ValidatorImpl();
    return (valid.isIntervalValid(interval));
  }

  @Override
  public int getInterval(String interval) {
    return Integer.parseInt(interval);
  }

  @Override
  public String getTickerSymbol(String ts) {
    return ts;
  }

  @Override
  public boolean isStartDateValid(String strDate) {
    valid = new ValidatorImpl();
    return (valid.isStartDateValid(strDate));
  }

  @Override
  public boolean isEndDateValid(String strDate, LocalDate startDate) {
    valid = new ValidatorImpl();
    return (valid.isEndDateValid(strDate, startDate));
  }

  @Override
  public LocalDate getDate(String strDate) {
    if (Objects.equals(strDate, "N")) {
      return LocalDate.now();
    }
    return LocalDate.parse(strDate);
  }

  @Override
  public void saveTsAndPerc(String tickerSymbol, double percentage) {
    if (mapForTsPerc.containsKey(tickerSymbol)) {
      percentage = percentage + mapForTsPerc.get(tickerSymbol);
    }
    mapForTsPerc.put(tickerSymbol, percentage);
  }

  @Override
  public void resetTsAndPerc() {
    mapForTsPerc = new HashMap<>();
  }

  @Override
  public void dollarCostAveraging(String pfName, double invtAmt, double commFee,
                                  LocalDate startDate, LocalDate endDate,
                                  int intervals, String filePath,
                                  ArrayList<LocalDate> validDates, String purpose) {

    objPF = new PortfolioImpl(pfName, filePath);
    if (Objects.equals(purpose, "create")) {
      objPF.writePFNameInXml();
    }

    DollarCostAvg conv = new DollarCostAvg();
    // mapForTSPrice = {ticker symbol : amount equivalent of percentage}
    HashMap<String, Double> mapForTSPrice = conv.getValueForPerc(this.mapForTsPerc, invtAmt);

    // timestampsList = List of working timestamps on which investments are going to happen
    InvestmentStrategies objDcaTimestamps = new InvestmentStrategiesImpl(startDate, endDate);
    ArrayList<LocalDate> timestampsList = new ArrayList<>();
    if (intervals != 0) {
      timestampsList = objDcaTimestamps.getTimestamps(intervals, validDates);
    } else {
      timestampsList.add(objDcaTimestamps.getWorkingTimestamp(startDate, validDates));
    }

    //Collections.reverse(timestampsList);

    for (Map.Entry<String, Double> tsSet : mapForTSPrice.entrySet()) {
      String tickerSymbol = tsSet.getKey();
      // priceOnTimestamps = Price of stock on the timestamp. {Date : price from api on x date}
      HashMap<LocalDate, Double> priceOnTimestamps = conv.getPriceOnTimestamps(
              tickerSymbol, timestampsList);

      double numShares;
      for (Map.Entry<LocalDate, Double> datePrice : priceOnTimestamps.entrySet()) {
        numShares = tsSet.getValue() / datePrice.getValue();
        objPF.writeStockDetailsInXML(tickerSymbol, numShares, commFee, datePrice.getKey());
      }
    }
  }

  @Override
  public double getNumberOfShares(String numShares) {
    return Double.parseDouble(numShares);
  }

  @Override
  public boolean isPercentagesSum100() {
    valid = new ValidatorImpl();
    return (valid.doPercentagesSumTo100(mapForTsPerc));
  }

  @Override
  public boolean percentageSumMoreThan100(double percentage) {
    double sum = 0.0;
    for (double value : mapForTsPerc.values()) {
      sum += value;
    }
    return (sum + percentage) > 100;
  }

  @Override
  public ArrayList<String> getStockList() {
    PriceList objList = new PriceList("", null, "");
    return objList.getPriceList();
  }

  @Override
  public HashMap<String, Integer> getAxisForGraph(String pfNameGraph, String filePath,
                                                  LocalDate date1, LocalDate date2,
                                                  ArrayList<LocalDate> validDatesInAPI) {

    objPF = new PortfolioImpl(pfNameGraph, filePath);
    return (objPF.getAxisForGraph(date1, date2, validDatesInAPI));
  }

}
