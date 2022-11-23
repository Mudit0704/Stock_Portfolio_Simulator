package portfolio.controller;

import java.io.FileNotFoundException;
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
import portfolio.view.GUIView;
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
  public void createFlexiblePortfolio(Map<String, Double> stocks, String date) {
    model.createNewPortfolioOnADate(stocks, LocalDate.parse(date));
  }

  @Override
  public void sellPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date) {
    model.sellStockFromPortfolio(tickerSymbol, Double.parseDouble(quantity),
        Integer.parseInt(portfolioId), LocalDate.parse(date));
  }

  @Override
  public void buyPortfolioStocks(String tickerSymbol, String quantity, String portfolioId,
      String date) {
    model.addStocksToPortfolio(tickerSymbol, Double.parseDouble(quantity),
        Integer.parseInt(portfolioId), LocalDate.parse(date));
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
  public void specificInvestmentOnAGivenDate(Map<String, Double> stockProportions,
      Double totalAmount, int portfolioId, LocalDate date) {
    //model.investStrategicPortfolio(stockProportions, totalAmount, portfolioId, date);
  }

  @Override
  public void createPortfolioUsingStrategy(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate startDate, LocalDate endDate) {
//    model.createStrategicPortfolio(stockProportions, totalAmount, startDate, endDate);
  }
}
