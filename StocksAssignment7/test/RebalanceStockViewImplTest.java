import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import view.RebalanceStockViewImpl;
import view.StockView;

/**
 * JUnit test class for testing {@link RebalanceStockViewImpl}.
 */
public class RebalanceStockViewImplTest {

  @Test
  public void printSubMenu2() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes);
    StockView stockView = new RebalanceStockViewImpl(out);

    stockView.printSubMenu2();

    assertEquals("----------- FLEXIBLE PORTFOLIO ------------\n"
        + "1. Create new flexible portfolio (Commission fee : $10 per transaction)\n"
        + "2. Modify existing portfolio (Commission fee : $10 per transaction)\n"
        + "3. Upload existing portfolio file\n"
        + "4. Examine current composition for a portfolio\n"
        + "5. Cost Basis\n"
        + "6. Get total value of the portfolio.\n"
        + "7. Get performance LineChartView.\n"
        + "8. Rebalance a portfolio.\n"
        + "9. Quit\n"
        + "-----------------------\n"
        + "Enter Choice : ", bytes.toString());
  }
}