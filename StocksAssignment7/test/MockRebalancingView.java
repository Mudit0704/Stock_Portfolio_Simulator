import java.io.PrintStream;
import view.ViewGUI;
import view.ViewGUIImpl;

/**
 * Class to mock the {@link ViewGUIImpl} class for rebalancing a portolio testing.
 */
public class MockRebalancingView extends ViewGUIImpl implements ViewGUI {

  PrintStream out;

  MockRebalancingView(String caption, PrintStream out) {
    super(caption);
    this.out = out;
  }

  @Override
  public void errorPerc() {
    out.print("Error");
  }

  @Override
  public void rebalanceDone100() {
    out.print("Reached 100");
  }

  @Override
  public void rebalanceStockAdded() {
    out.print("All stockes added");
  }

  @Override
  public void rebalanceSuccess() {
    out.print("Rebalance Success");
  }
}
