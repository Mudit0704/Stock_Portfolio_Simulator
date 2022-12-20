package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import model.Model;
import view.StockView;

/**
 * This class displays the submenu for the Inflexible portfolios. Options include uploading
 * existing portfolio, composition examination, cost basis and total value of a portfolio.
 */

public class InflexiblePF extends AbstractPF {

  StockView view;
  ArrayList<LocalDate> datesFromAPI;

  /**
   * Constructs a Inflexible portfolio that calls the model and view to perform various actions on
   * an inflexible portfolio such as creation, upload, getting composition, total.
   *
   * @param view  object of View interface
   * @param model object of Model interface
   * @param filePath absolute path of file where portfolio will be saved.
   */
  public InflexiblePF(StockView view, Model model, String filePath,
                      ArrayList<LocalDate> datesFromAPI) {
    super(view, model, filePath, datesFromAPI);
    this.view = view;
    this.datesFromAPI = datesFromAPI;
  }

  /**
   * This implementation displays the submenu for the Inflexible portfolios. Options include
   * uploading existing portfolio, composition examination, cost basis and total value of a
   * portfolio.
   *
   * @param scan (Scanner) object of Scanner class that gets inputs from the user
   */
  public void inFlexible(Scanner scan) {
    int inputChoice = 1;
    while (inputChoice == 1) {
      this.view.printSubMenu1();
      try {
        int choice = scan.nextInt();
        if (choice == 1) {          // Create new inflexible portfolio
          this.createPortfolio(scan, "Inflexible");
        } else if (choice == 2) {   // Upload existing portfolio
          this.uploadExistingPortfolio(scan);
        } else if (choice == 3) {   // Examine composition
          this.getComposition(scan, "Inflexible");
        } else if (choice == 4) {   // Get total of a portfolio
          this.getTotal(scan, "Inflexible");
        } else if (choice == 5) {   // Quit
          System.out.println();
          inputChoice = 0;
        } else {                    // Invalid input
          this.view.errorMsg1();
        }
      } catch (Exception e) {
        //
      }
    }
  }

}
