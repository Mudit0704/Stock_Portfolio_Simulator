package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import portfolio.controller.PortfolioController;
import portfolio.model.PortfoliosModel;
import portfolio.view.IView;
import portfolio.view.View;

/**
 * Main class that initializes Model, View and Controller objects.
 */
public class Main {

  /**
   * Initializes new Model and View objects and hands over them to the Controller before running it.
   * @param args command line arguments for main method.
   */
  public static void main(String[] args) {
    try {
      IView view = new View(System.out);
      new PortfolioController(new InputStreamReader(System.in), view).run(new PortfoliosModel());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
