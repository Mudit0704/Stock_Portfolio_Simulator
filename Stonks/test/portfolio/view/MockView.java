package portfolio.view;

import java.io.IOException;

public class MockView implements IView {

  Appendable out;

  public MockView(Appendable out) {
    this.out = out;
  }

  @Override
  public void displayEscapeFromOperation() throws IOException {
    this.out.append("Escape");
  }

  @Override
  public void displayMenu() throws IOException {
    this.out.append("Menu");
  }

  @Override
  public void displayCustomText(String customText) throws IOException {
    this.out.append("Custom Text");
  }

  @Override
  public void askForInput() throws IOException {
    this.out.append("Ask For Input");
  }

  @Override
  public void displayInvalidInput() throws IOException {
    this.out.append("Invalid Input");
  }

  @Override
  public void clearScreen() throws IOException, InterruptedException {
    //do nothing
  }
}
