package controller;

import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import model.Model;
import view.StockView;

/**
 * This class implements the stockController interface. This implementation displays the menu in
 * text based UI and gets input from the user for creating a new portfolio (flexible and inflexible)
 * of stocks. It redirects the flow to either flexible or inflexible class based on the choice
 * from the user.
 */
public class StockControllerImpl implements StockController {

  private final Model model;
  private final StockView view;
  private final Reader input;
  String filePath;
  int inputChoice = 1;
  Path currentPath;
  private InflexiblePF objInflex;
  private FlexiblePF objFlex;
  ArrayList<LocalDate> datesFromAPI;

  /**
   * Constructs a stockController that calls the model and view to display the menu, collect the
   * input and perform actions based on the selected choice. It also initializes the absolute path
   * of the file where details will be stored and a list of all the stocks and their prices as
   * available on the day of creating the portfolio.
   *
   * @param model object of stockModel interface
   * @param view  object of stockView interface
   * @param in    object of the Readable interface
   */
  public StockControllerImpl(Model model, StockView view, Reader in) {
    this.model = model;
    this.view = view;
    this.input = in;
    currentPath = Paths.get(System.getProperty("user.dir"));
    APICaller validDates = new APICaller();
    datesFromAPI = validDates.callAPI();
  }

  @Override
  public void selectOption() {
    Scanner scan = new Scanner(input);
    while (inputChoice == 1) {
      view.printMenu();
      try {
        int choice = scan.nextInt();

        if (choice == 1) {          // Inflexible portfolio
          //currentPath = Paths.get(System.getProperty("user.dir"));
          Path fp = Paths.get(this.currentPath.toString(), "portfoliosInflexible.xml");
          filePath = fp.toString();
          objInflex = new InflexiblePF(view, model, filePath, datesFromAPI);
          objInflex.inFlexible(scan);

        } else if (choice == 2) {          // flexible portfolio
          //currentPath = Paths.get(System.getProperty("user.dir"));
          Path fp = Paths.get(this.currentPath.toString(), "portfoliosFlexible.xml");
          filePath = fp.toString();
          objFlex = new FlexiblePF(view, model, filePath, datesFromAPI);
          objFlex.flexiblePF(scan);

        } else if (choice == 3) {   // Quit
          view.exitMsg();
          inputChoice = 0;

        } else {                    // Invalid input
          view.errorMsg1();
        }
      } catch (Exception e) {
        inputChoice = 1;
      }
    }
  }

}
