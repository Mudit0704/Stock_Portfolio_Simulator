package portfolio;

import java.io.IOException;
import java.io.InputStreamReader;
import portfolio.controller.PortfolioController;
import portfolio.model.Portfolios;
import portfolio.view.IView;
import portfolio.view.View;

public class Main {

  public static void main(String[] args) {
    try {
      IView view = new View(System.out);
      new PortfolioController(new InputStreamReader(System.in), view).run(Portfolios.getBuilder());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
