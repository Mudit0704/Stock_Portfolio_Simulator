import java.io.InputStreamReader;
import java.util.Scanner;

import controller.StockControllerGUIImpl;
import controller.StockController;
import controller.StockControllerImpl;
import model.Model;
import model.ModelImpl;
import view.StockView;
import view.StockViewImpl;
import view.ViewGUI;
import view.ViewGUIImpl;

/**
 * stockMain that is the main function for the Stocks Application. This function will create objects
 * for the model, view and controller and will call the controller for displaying the menu and
 * selecting an option.
 */
public class StockMain {

  /**
   * Main function that creates the controller, view and model objects. Also, this will call the
   * controller to display the menu.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    System.out.println("Choose UI : ");
    System.out.println("1. Text Based UI");
    System.out.println("2. GUI Based UI ");
    System.out.print("Enter choice :  ");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();

    Model model = new ModelImpl();
    if (choice == 1) {
      StockView view = new StockViewImpl(System.out);
      StockController controller = new StockControllerImpl(model, view,
              new InputStreamReader(System.in));
      controller.selectOption();
    } else if (choice == 2) {
      ViewGUI objView = new ViewGUIImpl("Stock Application");
      StockController contr = new StockControllerGUIImpl(model, objView);
      contr.selectOption();
    }
  }
}
