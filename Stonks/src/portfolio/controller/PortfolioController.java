package portfolio.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import portfolio.model.IPortfolio;
import portfolio.model.Portfolio;

public class PortfolioController implements IPortfolioController{
  final Readable in;
  final Appendable out;

  public PortfolioController(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void go(IPortfolio portfolio) throws IOException {
    Objects.requireNonNull(portfolio);

    String tickerSymbol;
    int stockQuantity;
    this.out.append("Choose from the below menu: \n 1-> Create a new portfolio \n Q-> Exit from the application \n");
    this.out.append("Your input: ");
    Scanner scan = new Scanner(this.in);
    while (true) {
      switch (scan.next()) {
        case "1":
          this.out.append("Choose from the below menu: \n 1-> Add a new stock \n Q-> Exit from the operation \n");
          this.out.append("Your input: ");
          Map<String, Integer> stocks = new HashMap<>();
          while (true) {
            switch (scan.next()) {
              case "Q":
                portfolio = new Portfolio().setPortfolioStocks(stocks);
                return;
              default:
                this.out.append("Stock Symbol: ");
                tickerSymbol = scan.next();
                this.out.append("Stock Quantity: ");
                stockQuantity = scan.nextInt();
                stocks.put(tickerSymbol, stockQuantity);
                this.out.append("\n Choose from the below menu: \n 1-> Add a new stock \n Q-> Exit from the operation \n");
                this.out.append("Your input:");
                break;
            }
          }

        case "Q":
          return;
      }
    }

  }
}
