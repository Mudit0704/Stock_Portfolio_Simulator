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
    FlexiblePortfolioController x = new FlexiblePortfolioController(in, view);
    Scanner scan = new Scanner(in);

    try {
      while (!done) {
        view.displayCustomText(MAIN_MENU);
        view.askForInput();
        switch (scan.next()) {
          case "1":
            x.run(model);
            break;
          case "2":
            this.model = model;
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
  public void createFlexiblePortfolio(Map<String, Double> stocks, LocalDate date) {
    //model.createNewPortfolioOnADate(stocks, date);
  }

  @Override
  public void sellPortfolioStocks(String tickerSymbol, Double quantity, int portfolioId,
      LocalDate date) {
    //model.sellStockFromPortfolio(tickerSymbol, quantity, portfolioId, date);
  }

  @Override
  public void buyPortfolioStocks(String tickerSymbol, Double quantity, int portfolioId,
      LocalDate date) {
    //model.addStocksToPortfolio(tickerSymbol, quantity, portfolioId, date);
  }


  @Override
  public void getCostBasis(LocalDate date, int portfolioId) {
    model.getCostBasis(date, portfolioId);
  }

  @Override
  public void getPortfolioValue(LocalDate date, int portfolioId) {
    model.getPortfolioValue(date, portfolioId);
  }

  @Override
  public void savePortfolio() throws ParserConfigurationException {
    model.savePortfolios();
  }

  @Override
  public void retrievePortfolio() throws IOException, ParserConfigurationException, SAXException {
    model.retrievePortfolios();
  }

  @Override
  public void specificInvestmentOnAGivenDate(Map<String, Double> stockProportions,
      Double totalAmount, int portfolioId, LocalDate date) {
    model.investStrategicPortfolio(stockProportions, totalAmount, portfolioId, date);
  }

  @Override
  public void createPortfolioUsingStrategy(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate startDate, LocalDate endDate) {
    model.createStrategicPortfolio(stockProportions, totalAmount, startDate, endDate);
  }
}
