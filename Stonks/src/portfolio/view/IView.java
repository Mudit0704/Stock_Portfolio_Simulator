package portfolio.view;

import java.io.IOException;

public interface IView {

  void displayEscapeFromOperation() throws IOException;

  void displayMenu() throws IOException;

  void displayCustomText(String customText) throws IOException;

  void askForInput() throws IOException;

  void displayInvalidInput() throws IOException;

  void clearScreen() throws IOException, InterruptedException;
}
