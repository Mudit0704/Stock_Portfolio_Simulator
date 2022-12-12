package view;

import java.io.PrintStream;

/**
 * Text based view class for providing the re-balance a portfolio functionality.
 */
public class RebalanceStockViewImpl extends StockViewImpl {

  /**
   * Constructor that initialises the PrintStream object which is further used to print the output
   * on the UI.
   *
   * @param out object of the PrintStream interface
   */
  public RebalanceStockViewImpl(PrintStream out) {
    super(out);
  }

  @Override
  public void printSubMenu2() {
    this.output.print("" + "----------- FLEXIBLE PORTFOLIO ------------\n"
        + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
        + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
        + "3. Upload existing portfolio file\n"
        + "4. Examine current composition for a portfolio\n"
        + "5. Cost Basis\n"
        + "6. Get total value of the portfolio.\n"
        + "7. Get performance LineChartView.\n"
        + "8. Rebalance a portfolio.\n"
        + "9. Quit\n"
        + "-----------------------\n" + "Enter Choice :\040");
  }
}
