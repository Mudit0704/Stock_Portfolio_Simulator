package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import portfolio.controller.GUIPortfolioController;
import portfolio.model.StrategicFlexiblePortfoliosModel;
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
      new GUIPortfolioController(new StrategicFlexiblePortfoliosModel(),
          new InputStreamReader(System.in), view);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
