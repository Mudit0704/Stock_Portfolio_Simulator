package portfolio.controller;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import portfolio.model.MockModel;
import portfolio.view.IView;
import portfolio.view.MockView;

public class PortfolioControllerTest {

  @Test
  public void testGo() throws Exception {
    StringBuffer out = new StringBuffer();
    IView mockView = new MockView(out);
    Reader in = new StringReader("+ 3 4 + 8 9 q");
    IPortfolioController controller = new PortfolioController(in, mockView);
    StringBuilder log = new StringBuilder(); //log for mock model
    //IPortfolios x = new Portfolios();
    //x.sto
    controller.run(new MockModel(log));
    assertEquals("Input: 3 4\nInput: 8 9\n", log.toString()); //inputs reached the model correctly
    assertEquals("1234321\n1234321\n",out.toString()); //output of model transmitted correctly
  }

  @Test
  public void testPortfolioControllerConstructor() {
  }
}