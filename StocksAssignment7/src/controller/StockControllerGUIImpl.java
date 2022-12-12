package controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import model.Model;
import view.ViewGUI;

/**
 * This class implements the Features and StockControllerGUI interface. It has implementation
 * methods for calling the view to perform various stock operations on the GUI. These include
 * creation and modification using earlier methods, investment strategy and dollar cost averaging,
 * uploading an existing portfolio, retrieving composition, total, cost basis and performance graph
 * of a portfolio.
 */
public class StockControllerGUIImpl implements StockController, Features {

  String filePath;
  Path currentPath;
  ArrayList<LocalDate> datesFromAPI;
  String purpose;
  private Model model;
  private ViewGUI view;

  /**
   * Constructor for the StockControllerGUIImpl class. It has a model and view object to communicate
   * with them. It also initialises the filepath where portfolio details will be saved.
   *
   * @param m (Model) object of the Model class
   * @param v (View) object of the view class
   */
  public StockControllerGUIImpl(Model m, ViewGUI v) {
    model = m;
    view = v;
    view.addFeatures(this);
    currentPath = Paths.get(System.getProperty("user.dir"));
    Path fp = Paths.get(this.currentPath.toString(), "portfoliosFlexible.xml");
    filePath = fp.toString();
    APICaller validDates = new APICaller();
    datesFromAPI = validDates.callAPI();
    this.purpose = "";
  }

  @Override
  public void selectOption() {
    view.getFlexibleMenu();
  }

  @Override
  public void echoTextTyped(String typed) {
    if (Objects.equals(typed, "1")) {   // Create
      view.createOptions();
    } else if (Objects.equals(typed, "2")) {   // Modify
      view.getModifyPage();
    } else if (Objects.equals(typed, "3")) {   // Upload
      view.uploadFile();
    } else if (Objects.equals(typed, "4")) {  // Composition
      this.purpose = "composition";
      view.getPfNameAndDate();
    } else if (Objects.equals(typed, "5")) {  // CB
      this.purpose = "cost basis";
      view.getPfNameAndDate();
    } else if (Objects.equals(typed, "6")) {  //Total
      this.purpose = "total";
      view.getPfNameAndDate();
    } else if (Objects.equals(typed, "7")) {  //Total
      this.purpose = "total";
      view.getGraphPage();
    } else if (Objects.equals(typed, "8")) {
      this.purpose = "rebalance";
      view.getPfNameAndDate();
    } else if (Objects.equals(typed, "9")) {
      System.exit(0);
    }
  }

  @Override
  public void createBySelf() {
    this.purpose = "create";
    view.getCreatePage(model.getStockList());
  }

  @Override
  public void createByInvtOnce() {
    this.purpose = "create";
    view.getCreateByInvtOncePage(model.getStockList());
  }

  @Override
  public void modifyInvtOnce() {
    this.purpose = "modify";
    view.getCreateByInvtOncePage(model.getStockList());
  }

  @Override
  public void createByDca() {
    this.purpose = "create";
    view.getDcaPage(model.getStockList());
  }

  @Override
  public void modifyStocksDca() {
    this.purpose = "modify";
    view.getDcaPage(model.getStockList());
  }

  @Override
  public void getGraph(String inPFName, String inDate, String inEndDate) {
    if (model.isPfNameUnique(inPFName, this.filePath)) {
      view.errorPfName();
    }
    if (!(model.isStartDateValid(inDate))) {
      view.errorDate();
    } else if (!(model.isEndDateValid(inEndDate, model.getDate(inDate)))) {
      view.errorDate();
    } else {
      view.drawGraph(model.getAxisForGraph(inPFName, this.filePath, model.getDate(inDate),
          model.getDate(inEndDate),
          datesFromAPI));
    }
  }

  // For create
  @Override
  public void echoStockDetails(String pfName, String tickerSymbol, String numShares,
      String commFee, String date, boolean flag) {

    if (!(model.isPfNameUnique(pfName, this.filePath)) && flag) {
      view.errorPfName();
    } else if (!(model.isNumberOfSharesValid(numShares))) {
      view.errorNs();
    } else if (!(model.isDateValid(date, "purchasing", datesFromAPI))) {
      view.errorDateMarketClosed();
    } else if (!(model.isCommFeeValid(commFee))) {
      view.errorCommFee();
    } else {
      if (flag) {
        model.writePortfolioName(pfName, this.filePath);
      }
      model.writeStockDetails(pfName, model.getTickerSymbol(tickerSymbol),
          model.getNumberOfShares(numShares), model.getCommFee(commFee),
          this.filePath, model.getDate(date));
      view.stockModifiedSuccess();
    }
  }

  @Override
  public void createDcaPortfolio(String pfName, String invtAmt, String commFee, String tickerSymbol,
      String perc, String startDate, String endDate, String interval,
      boolean flagForPfName, boolean flagForStocks) {

    if (((!(this.model.isPfNameUnique(pfName, this.filePath))
        && Objects.equals(this.purpose, "create"))
        || ((this.model.isPfNameUnique(pfName, this.filePath))
        && Objects.equals(this.purpose, "modify"))) && flagForPfName) {

      if (Objects.equals(this.purpose, "create")) {
        view.errorPfName();
      } else if (Objects.equals(this.purpose, "modify")) {
        view.errorPfName2();
      }
    } else if (!(model.isInvestmentAmtValid(invtAmt)) && flagForPfName) {
      view.errorInvtAmt();
    } else if (!(model.isCommFeeValid(commFee))) {
      view.errorCommFee();
      //    } else if (!(model.isTickerSymbolValid(tickerSymbol)) && flagForStocks) {
      //      view.errorTs();
    } else if (!(model.isPercentageValid(perc)) && flagForStocks) {
      view.errorPerc();
    } else {
      if (flagForStocks) {
        if (model.percentageSumMoreThan100(model.getPercentage(perc))) {
          view.errorPerc();
        } else if (!(model.isPercentagesSum100())) {
          model.saveTsAndPerc(tickerSymbol, model.getPercentage(perc));
          if (model.isPercentagesSum100()) {
            view.dcaInvested100();
          } else {
            view.dcaStockAdded();
          }
        }
      } else {
        if (!(model.isStartDateValid(startDate))) {
          view.errorDate();
        } else if (endDate.length() != 0 && !(model.isEndDateValid(endDate,
            model.getDate(startDate)))) {
          view.errorDate();
        } else if (!(model.isIntervalValid(interval))) {
          view.errorInterval();
        } else {
          if (endDate.length() == 0) {
            endDate = "N";
          }
          model.dollarCostAveraging(pfName, model.getInvestmentAmount(invtAmt),
              model.getCommFee(commFee), model.getDate(startDate), model.getDate(endDate),
              model.getInterval(interval),
              this.filePath, datesFromAPI, this.purpose);
          model.resetTsAndPerc();
          if (Objects.equals(this.purpose, "create")) {
            view.portfolioSuccess("created");
          } else if (Objects.equals(this.purpose, "modify")) {
            view.portfolioSuccess("modified");
          }
        }
      }
    }
  }

  @Override
  public void echoPfNameWithDate(String pfName, String date) {

    if (model.isPfNameUnique(pfName, this.filePath)) {
      view.errorPfName2();

    } else if (((Objects.equals(this.purpose, "cost basis"))
        || (Objects.equals(this.purpose, "composition")))
        && !(model.isStartDateValid(date))) {
      view.errorDate();
    } else if (Objects.equals(this.purpose, "total")
        && !(model.isDateValid(date, this.purpose, datesFromAPI))) {
      view.errorDateMarketClosed();

    } else if ((!(model.isPfNameUnique(pfName, this.filePath))) && (model.isStartDateValid(date))) {
      if (Objects.equals(this.purpose, "cost basis")) {
        double cb = model.getCostBasis(pfName, filePath, model.getDate(date));
        view.outputPage("$" + cb);

      } else if (Objects.equals(this.purpose, "total")) {
        double total = model.getTotal(pfName, filePath, model.getDate(date), "Flexible");
        view.outputPage("$" + total);

      } else if (Objects.equals(this.purpose, "composition")) {
        String comp = model.getComposition(pfName, filePath, model.getDate(date));
        if (Objects.equals(comp, "")) {
          view.outputPage("Nothing to display");
        } else {
          view.outputPage(comp);
        }
      } else if (Objects.equals(this.purpose, "rebalance")) {
        view.rebalancePortfolio(pfName, date);
      }
    }
  }

  @Override
  public void getFilePath(String filePath) {
    if (filePath.contains(".xml")) {
      this.filePath = filePath;
      view.fileUploadSuccess();
    } else {
      view.errorFileUpload();
    }
  }

  @Override
  public void modifyStockBuy() {
    this.purpose = "buy";
    view.modifyStocks(model.getStockList());
  }

  @Override
  public void modifyStockSell() {
    this.purpose = "sell";
    view.modifyStocks(model.getStockList());
  }

  @Override
  public void buyOrSellStocks(String pfName, String tickerSymbol, String numShares,
      String commFee, String date) {
    if (model.isPfNameUnique(pfName, this.filePath)) {
      view.errorPfName2();
      //    } else if (!(model.isTickerSymbolValid(tickerSymbol))) {
      //      view.errorTs();
    } else if (!(model.isNumberOfSharesValid(numShares))) {
      view.errorNs();
    } else if (!(model.isDateValid(date, "purchasing", datesFromAPI))) {
      view.errorDateMarketClosed();
    } else if (!(model.isCommFeeValid(commFee))) {
      view.errorCommFee();
    } else {
      if (this.purpose.equals("buy")) {
        model.writeStockDetails(pfName, model.getTickerSymbol(tickerSymbol),
            model.getNumberOfShares(numShares), model.getCommFee(commFee),
            this.filePath, model.getDate(date));
        view.stockModifiedSuccess();
      } else if (this.purpose.equals("sell")) {
        double shares = model.getNumberOfShares(numShares) * (-1);
        if (model.checkIfCanSell(pfName, model.getTickerSymbol(tickerSymbol),
            shares, this.filePath, model.getDate(date))) {
          model.writeStockDetails(pfName, model.getTickerSymbol(tickerSymbol),
              shares, model.getCommFee(commFee), this.filePath, model.getDate(date));
          view.stockModifiedSuccess();
        } else {
          view.errorSell(); // cannot sell error message in view (need to change)
        }
      }
    }
  }

  @Override
  public void createInvtPortfolio(String pfName, String invtAmt, String commFee,
      String tickerSymbol, String perc, String date,
      boolean flagForPfName, boolean flagForStocks) {

    if (((!(this.model.isPfNameUnique(pfName, this.filePath))
        && Objects.equals(this.purpose, "create"))
        || ((this.model.isPfNameUnique(pfName, this.filePath))
        && Objects.equals(this.purpose, "modify"))) && flagForPfName) {

      if (Objects.equals(this.purpose, "create")) {
        view.errorPfName();
      } else if (Objects.equals(this.purpose, "modify")) {
        view.errorPfName2();
      }
    } else if (!(model.isInvestmentAmtValid(invtAmt)) && flagForPfName) {
      view.errorInvtAmt();
    } else if (!(model.isCommFeeValid(commFee)) && flagForPfName) {
      view.errorCommFee();
      //    } else if (!(model.isTickerSymbolValid(tickerSymbol)) && flagForStocks) {
      //      view.errorTs();
    } else if (!(model.isPercentageValid(perc)) && flagForStocks) {
      view.errorPerc();
    } else {
      if (flagForStocks) {
        if (model.percentageSumMoreThan100(model.getPercentage(perc))) {
          view.errorPerc();
        } else if (!(model.isPercentagesSum100())) {
          model.saveTsAndPerc(tickerSymbol, model.getPercentage(perc));
          if (model.isPercentagesSum100()) {
            view.dcaInvested100();
          } else {
            view.dcaStockAdded();
          }
        }
      } else {
        if (!(model.isStartDateValid(date))) {
          view.errorDate();
        } else {
          model.dollarCostAveraging(pfName, model.getInvestmentAmount(invtAmt),
              model.getCommFee(commFee), model.getDate(date), null, 0,
              this.filePath, datesFromAPI,
              this.purpose);
          model.resetTsAndPerc();

          if (Objects.equals(this.purpose, "create")) {
            view.portfolioSuccess("created");
          } else if (Objects.equals(this.purpose, "modify")) {
            view.portfolioSuccess("modified");
          }

        }
      }
    }
  }

  @Override
  public void rebalancePortfolio(String pfName, String date) {
    model.rebalanceExistingPortfolio(pfName, this.filePath, model.getDate(date), datesFromAPI,
        "Flexible");
    view.rebalanceSuccess();
  }

  @Override
  public void addStockWeightsForRebalance(String tickerSymbol, String percentage) {
    if (model.percentageSumMoreThan100(model.getPercentage(percentage))) {
      view.errorPerc();
    } else {
      model.saveTsAndPerc(tickerSymbol, model.getPercentage(percentage));
      if (model.isPercentagesSum100()) {
        view.rebalanceDone100();
      } else {
        view.rebalanceStockAdded();
      }
    }
  }

  @Override
  public List<String> getPortfolioStocks(String pfName, String date) {
    List<String> stocks = model.getTickerSymbolsInPortfolio(pfName, this.filePath, LocalDate.parse(date));
    if (stocks.isEmpty()) {
      view.displayNoStockAvailableWindow();
    }
    return stocks;
  }

}
