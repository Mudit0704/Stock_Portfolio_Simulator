package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import portfolio.controller.FlexiblePortfolioController;
import portfolio.model.FlexiblePortfoliosModel;
import portfolio.view.FlexibleView;
import portfolio.view.IView;

/**
 * Main class that initializes Model, View and Controller objects.
 */
public class Main {

  /**
   * Initializes new Model and View objects and hands over them to the Controller before running
   * it.
   *
   * @param args command line arguments for main method.
   */
  public static void main(String[] args) {
    try {
      IView view = new FlexibleView(System.out);
      new FlexiblePortfolioController(new InputStreamReader(System.in), view).run(
          new FlexiblePortfoliosModel());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
