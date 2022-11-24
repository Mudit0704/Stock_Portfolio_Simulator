package portfolio.view.guiview;

import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import portfolio.controller.Features;

abstract class AbstractCommandHandlers implements CommandHandler {

  public static final String INVALID = "Please Enter A Valid";
  public static final String VALID = "Valid";
  JTextPane resultArea;
  Features features;
  JProgressBar progressBar;
  JFrame mainFrame;

  Map<JTextField, Function<String, String>> validatorMap;

  AbstractCommandHandlers(JTextPane resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    this.resultArea = resultArea;
    this.features = features;
    this.progressBar = progressBar;
    this.mainFrame = mainFrame;
    validatorMap = new LinkedHashMap<>();
  }

  JDialog getUserInputDialog(String title) {
    JDialog userInputDialog = new JDialog(mainFrame, title);
    userInputDialog.setModalityType(ModalityType.APPLICATION_MODAL);
    userInputDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    return userInputDialog;
  }

  JPanel getAvailablePortfoliosDisplay(Features features) {
    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(5, 20);
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    displayPanel.add(scrollPane);
    displayArea.setText(features.getAvailablePortfolios());
    return displayPanel;
  }

  String numberTextFieldValidator(String value) {
    try {
      Double.parseDouble(value);
      Long.parseLong(value);
    } catch (NumberFormatException e) {
      return INVALID;
    }

    return VALID;
  }

  String dateTextFieldValidator(String value) {
    try {
      LocalDate.parse(value);
    } catch (DateTimeParseException e) {
      return INVALID;
    }

    return VALID;
  }

  String tickerSymbolTextFieldValidator(String value) {
    return features.isTickerSymbolValid(value) ? VALID : INVALID;
  }

  String validator(Map<JTextField, Function<String, String>> validatorMap) {
    String errorMessage = "";

    for (Entry<JTextField, Function<String, String>> entry : validatorMap.entrySet()) {
      String result = entry.getValue().apply(entry.getKey().getText());
      if(!VALID.equals(result)) {
        errorMessage = result + " " + entry.getKey().getName();
        break;
      }
    }

    if(!errorMessage.isEmpty()) {
      JOptionPane.showMessageDialog(mainFrame, errorMessage, "Error",
          JOptionPane.ERROR_MESSAGE);
    }

    return errorMessage;
  }
}
