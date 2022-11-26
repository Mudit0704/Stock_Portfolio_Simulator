package portfolio.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import portfolio.model.IStrategicFlexiblePortfolioModel;
import portfolio.model.ServiceType;
import portfolio.model.StrategyType;
import portfolio.view.guiview.GUIView;
import portfolio.view.IGUIView;
import portfolio.view.IView;

public class GUIPortfolioController implements Features {

  protected static final String MAIN_MENU =
      "Choose from the below menu: \n 1 -> Use text-based UI \n"
          + " 2 -> Use graphical user interface \n"
          + " E -> Exit from the application \n";

  IStrategicFlexiblePortfolioModel model;
  IGUIView view;

  public GUIPortfolioController(IStrategicFlexiblePortfolioModel model, Readable in, IView view)
      throws IOException {
    boolean done = false;
    FlexiblePortfolioController flexiblePortfolioController = new FlexiblePortfolioController(in,
        view);
    Scanner scan = new Scanner(in);

    try {
      while (!done) {
        view.displayCustomText(MAIN_MENU);
        view.askForInput();
        switch (scan.next()) {
          case "1":
            flexiblePortfolioController.run(model);
            break;
          case "2":
            this.model = model;
            this.model.setServiceType(ServiceType.ALPHAVANTAGE);
            this.view = new GUIView("Stonks");
            this.view.addFeatures(this);
            done = true;
            break;
          case "E":
            return;
          default:
            view.displayInvalidInput();
            scan.nextLine();
            break;
        }
      }
    } catch (NoSuchElementException e) {
      view.displayCustomText("\n----Exiting----\n");
    } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
             IllegalAccessException e) {
      view.displayCustomText("Unable to display GUI\n");
    }
  }

  @Override
  public String createFlexiblePortfolio(Map<String, Double> stocks, String date) {
    model.createNewPortfolioOnADate(stocks, LocalDate.parse(date));
    return "Created";
  }

  @Override
  public String sellPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee) {
    model.setCommissionFee(Double.parseDouble(transactionFee));
    model.sellStockFromPortfolio(tickerSymbol, Double.parseDouble(quantity),
        Integer.parseInt(portfolioId), LocalDate.parse(date));
    return "Sold";
  }

  @Override
  public String buyPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date, String transactionFee) {
    model.setCommissionFee(Double.parseDouble(transactionFee));
    model.addStocksToPortfolio(tickerSymbol, Double.parseDouble(quantity),
        Integer.parseInt(portfolioId), LocalDate.parse(date));
    return "Bought";
  }


  @Override
  public double getCostBasis(String date, String portfolioId) {
    return model.getCostBasis(LocalDate.parse(date), Integer.parseInt(portfolioId));
  }

  @Override
  public double getPortfolioValue(String date, String portfolioId) {
    return model.getPortfolioValue(LocalDate.parse(date), Integer.parseInt(portfolioId));
  }

  @Override
  public String savePortfolio() {
    try {
      model.savePortfolios();
    } catch (ParserConfigurationException e) {
      return "Error while saving";
    } catch (RuntimeException e) {
      return e.getLocalizedMessage();
    }

    return "Saved";
  }

  @Override
  public String retrievePortfolio() {
    try {
      model.retrievePortfolios();
    } catch (ParserConfigurationException | SAXException e) {
      return "Error while retrieving";
    } catch (IOException | RuntimeException e) {
      return e.getLocalizedMessage();
    }

    return "Retrieved";
  }

  @Override
  public String getAvailablePortfolios() {
    return model.getAvailablePortfolios();
  }

  @Override
  public String getPortfolioComposition(String portfolioId) {
    return model.getPortfolioComposition(Integer.parseInt(portfolioId));
  }

  @Override
  public String fractionalInvestmentOnAGivenDate(Map<String, Double> stockProportions,
      String totalAmount, String portfolioId, String date) {
    model.setStrategy(StrategyType.NORMAL, LocalDate.parse(date), LocalDate.parse(date),
        0, Double.parseDouble(totalAmount));
    model.investStrategicPortfolio(stockProportions, Integer.parseInt(portfolioId));
    return "Invested";
  }

  @Override
  public String createDollarCostAveragePortfolio(Map<String, Double> stockProportions, String totalAmount,
      String startDate, String endDate, String timeFrame) {
    model.setStrategy(StrategyType.DOLLARCOSTAVERAGING, LocalDate.parse(startDate), LocalDate.parse(endDate),
        Integer.parseInt(timeFrame), Double.parseDouble(totalAmount));
    model.createStrategicPortfolio(stockProportions, LocalDate.parse(startDate));
    return "Created";
  }

  @Override
  public boolean isTickerSymbolValid(String tickerSymbol) {
    return model.isTickerSymbolValid(tickerSymbol);
  }

  @Override
  public Map<LocalDate, Double> getPortfolioPerformance(String startDate, String endDate,
      String portfolioId) {
    return model.lineChartPerformanceAnalysis(LocalDate.parse(startDate), LocalDate.parse(endDate),
        Integer.parseInt(portfolioId));
  }
}
