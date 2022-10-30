package portfolio.view;

import java.io.IOException;

public class MockView implements IView {

  Appendable out;

  public MockView(Appendable out) {
    this.out = out;
  }

  @Override
  public void displayEscapeFromOperation() throws IOException {

  }

  @Override
  public void displayMenu() throws IOException {

  }

  @Override
  public void displayCustomText(String customText) throws IOException {

  }

  @Override
  public void askForInput() throws IOException {

  }

  @Override
  public void displayInvalidInput() throws IOException {

  }

  @Override
  public void clearScreen() throws IOException, InterruptedException {

  }
}
