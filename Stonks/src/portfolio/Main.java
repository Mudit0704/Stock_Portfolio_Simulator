package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.GUIPortfolioController;
import portfolio.model.StrategicFlexiblePortfoliosModel;
import portfolio.view.FlexibleView;
import portfolio.view.IView;
import portfolio.view.guiview.GUIView;

/**
 * Main class that initializes Model, View and Controller objects.
 */
public class Main {

  /**
   * Initializes new Model and View objects and hands over them to the Controller before running
   * it.
   *
   * @param args command line arguments for main method.
   * @throws IOException if an I/O error occurs.
   */
  public static void main(String[] args) throws IOException {
    IView view = null;
    try {
      view = new FlexibleView(System.out);
      new GUIPortfolioController(new StrategicFlexiblePortfoliosModel(),
          new InputStreamReader(System.in), view, new GUIView("Stonks"));
    } catch (IOException | UnsupportedLookAndFeelException | ClassNotFoundException |
             InstantiationException | IllegalAccessException e) {
      view.displayCustomText("Error occurred while trying to start application\n");
    }
  }
}
