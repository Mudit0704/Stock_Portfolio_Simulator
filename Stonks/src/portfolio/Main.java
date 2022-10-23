package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import portfolio.controller.PortfolioController;
import portfolio.model.Portfolio;

public class Main {

  public static void main(String[] args) {
    //model
    try {
      new PortfolioController(new InputStreamReader(System.in), System.out).go(new Portfolio());
    } catch (IOException e) {
      e.printStackTrace();
    }
    //controller
  }
}
