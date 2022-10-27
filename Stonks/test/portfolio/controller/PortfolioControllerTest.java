package portfolio.controller;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import portfolio.model.MockPortfolios;
import portfolio.view.IView;

public class PortfolioControllerTest {

  @Test
  public void testGo() throws Exception {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("+ 3 4 + 8 9 q");
    IView dummyView;
    //PortfolioController controller6 = new PortfolioController(in, out);
    StringBuilder log = new StringBuilder(); //log for mock model
    //controller6.run(new MockPortfolios());
    assertEquals("Input: 3 4\nInput: 8 9\n", log.toString()); //inputs reached the model correctly
    assertEquals("1234321\n1234321\n",out.toString()); //output of model transmitted correctly
  }

  @Test
  public void testPortfolioControllerConstructor() {
  }
}