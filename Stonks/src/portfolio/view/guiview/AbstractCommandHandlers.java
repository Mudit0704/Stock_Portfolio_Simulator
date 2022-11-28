package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import portfolio.controller.Features;

/**
 * Stores the common logic for different types of command handlers of the graphical user interface.
 */
abstract class AbstractCommandHandlers implements CommandHandler {

  public static final String INVALID = "Please Enter A Valid";
  public static final String VALID = "Valid";
  public static final String TICKER_SYMBOL = "Ticker Symbol";
  public static final String PORTFOLIO_ID = "Portfolio Id (Portfolio1 -> 1)";
  public static final String QUANTITY = "Quantity";
  public static final String TIME_FRAME = "Time Frame (Days)";
  public static final String TOTAL_AMOUNT = "Total Amount";
  public static final String PERCENTAGE = "Percentage";
  public static final String START_DATE = "Start Date";
  public static final String END_DATE = "End Date";
  public static final String PERFORMANCE_START_DATE = "Performance Start Date";
  public static final String PERFORMANCE_END_DATE = "Performance End Date";
  public static final String DATE = "Date";
  public static final String AVAILABLE_PORTFOLIOS = "Available Portfolios";
  public static final String TRANSACTION_FEE = "Transaction Fee";
  public static final String STRATEGY_MESSAGE = "Press OK to add one stock and "
      + "DONE when you are done adding stocks";
  JTextPane resultArea;
  Features features;
  JProgressBar progressBar;
  JFrame mainFrame;
  Map<String, LabelFieldPair> fieldsMap;
  Map<JTextField, Function<String, String>> validatorMap;
  JTextArea subWindowDisplay;

  /**
   * Initializes the members required by each button handler.
   *
   * @param resultArea  the text area where result of each command has to be displayed
   * @param features    an object of {@link portfolio.controller.GUIPortfolioController} to perform
   *                    the callback functionality between the view and controller
   * @param progressBar a progress bar representing the status of each command
   * @param mainFrame   the main window of the application
   */
  AbstractCommandHandlers(JTextPane resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    this.resultArea = resultArea;
    this.features = features;
    this.progressBar = progressBar;
    this.mainFrame = mainFrame;
    validatorMap = new LinkedHashMap<>();
    fieldsMap = new LinkedHashMap<>();
  }

  JDialog getUserInputDialog(String title, int width, int height) {
    JDialog userInputDialog = new JDialog(mainFrame, title);
    userInputDialog.setModalityType(ModalityType.APPLICATION_MODAL);
    userInputDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    userInputDialog.setMinimumSize(new Dimension(width, height));
    userInputDialog.setLocationRelativeTo(null);
    userInputDialog.setResizable(false);
    return userInputDialog;
  }

  JPanel getResultDisplay(String defaultText, String title) {
    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(5, 20);
    subWindowDisplay = displayArea;
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    displayPanel.add(scrollPane);
    displayArea.setText(defaultText);
    displayArea.setBorder(BorderFactory.createTitledBorder(title));
    return displayPanel;
  }

  String integerTextFieldValidator(String value) {
    try {
      if ("".equals(value.trim()) || Long.parseLong(value) < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      return INVALID;
    }

    return VALID;
  }

  String doubleTextFieldValidator(String value) {
    try {
      if ("".equals(value.trim()) || Double.parseDouble(value) < 0) {
        throw new NumberFormatException();
      }
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
      if (END_DATE.equals(entry.getKey().getName()) || TRANSACTION_FEE.equals(
          entry.getKey().getName())) {
        if (!"".equals(entry.getKey().getText().trim())) {
          String result = entry.getValue().apply(entry.getKey().getText());
          if (!VALID.equals(result)) {
            errorMessage = result + " " + entry.getKey().getName();
            break;
          }
        } else {
          if (END_DATE.equals(entry.getKey().getName())) {
            entry.getKey().setText("2100-12-31");
          } else {
            entry.getKey().setText("0");
          }
        }
      } else {
        String result = entry.getValue().apply(entry.getKey().getText());
        if (!VALID.equals(result)) {
          errorMessage = result + " " + entry.getKey().getName();
          break;
        }
      }
    }

    if (!errorMessage.isEmpty()) {
      JOptionPane.showMessageDialog(mainFrame, errorMessage, "Error",
          JOptionPane.ERROR_MESSAGE);
    }

    return errorMessage;
  }

  static JButton getCustomButton(String title) {
    JButton button = new JButton(title);
    button.setBackground(new Color(33, 108, 138));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("Tahoma", Font.BOLD, 12));
    return button;
  }

  void createDateLabelField(String fieldName) {
    JLabel dateLabel = new JLabel("Enter " + fieldName + " (YYYY-MM-DD): ");
    JTextField dateValue = new JTextField();
    dateValue.setName(fieldName);

    validatorMap.put(dateValue, this::dateTextFieldValidator);
    fieldsMap.put(dateValue.getName(), new LabelFieldPair(dateLabel, dateValue));
  }

  void createTickerSymbolField() {
    JLabel tickerSymbolLabel = new JLabel("Enter Ticker Symbol: ");
    JTextField tickerSymbolValue = new JTextField();
    tickerSymbolValue.setName(TICKER_SYMBOL);

    validatorMap.put(tickerSymbolValue, this::tickerSymbolTextFieldValidator);
    fieldsMap.put(tickerSymbolValue.getName(),
        new LabelFieldPair(tickerSymbolLabel, tickerSymbolValue));
  }

  void createIntegerFields(String fieldName) {
    JLabel quantityLabel = new JLabel("Enter " + fieldName + ": ");
    JTextField quantityValue = new JTextField();
    quantityValue.setName(fieldName);

    validatorMap.put(quantityValue, this::integerTextFieldValidator);
    fieldsMap.put(quantityValue.getName(), new LabelFieldPair(quantityLabel, quantityValue));
  }

  void createDoubleFields(String fieldName) {
    JLabel quantityLabel = new JLabel("Enter " + fieldName + ": ");
    JTextField quantityValue = new JTextField();
    quantityValue.setName(fieldName);

    validatorMap.put(quantityValue, this::doubleTextFieldValidator);
    fieldsMap.put(quantityValue.getName(), new LabelFieldPair(quantityLabel, quantityValue));
  }

  void addAllFieldsToInputPanel(JPanel inputPanel) {
    for (Entry<String, LabelFieldPair> entry : fieldsMap.entrySet()) {
      inputPanel.add(entry.getValue().label);
      inputPanel.add(entry.getValue().textField);
    }
  }

  boolean isPercentageTotalValid(DoubleAdder percentageTotal) {
    if (percentageTotal.doubleValue() + Double.parseDouble(fieldsMap.get(PERCENTAGE).textField
        .getText())  > 100d) {
      JOptionPane.showMessageDialog(mainFrame, "Percentage total cannot be greater than 100",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return false;
    } else if (Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()) <= 0d) {
      JOptionPane.showMessageDialog(mainFrame, "Percentage value should be greater"
          + " than 0 and less than 100", "Error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  boolean createTransactionWindow(AtomicBoolean okClicked) {
    JPanel availablePortfoliosDisplay;
    JDialog userInputDialog = getUserInputDialog("Transaction", 520, 250);

    try {
      availablePortfoliosDisplay = getResultDisplay(features.getAvailablePortfolios(),
          AVAILABLE_PORTFOLIOS);
      availablePortfoliosDisplay.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
    } catch (Exception e) {
      resultArea.setText("<html><center><h1>" + e.getLocalizedMessage() + "</center></html>");
      progressBar.setIndeterminate(false);
      return true;
    }
    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    createDateLabelField(DATE);
    createIntegerFields(PORTFOLIO_ID);
    createTickerSymbolField();
    createDoubleFields(QUANTITY);
    createDoubleFields(TRANSACTION_FEE);

    JButton okButton = getCustomButton("OK");
    okButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        userInputDialog.dispose();
        okClicked.set(true);
      }
    });

    addAllFieldsToInputPanel(userInputPanel);

    JPanel fieldsPanel = new JPanel(new BorderLayout());
    fieldsPanel.add(userInputPanel, BorderLayout.CENTER);
    fieldsPanel.add(okButton, BorderLayout.EAST);
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));

    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);
    return false;
  }

  void strategyDONEFunctionality(DoubleAdder percentageTotal, AtomicBoolean doneClicked,
      JDialog userInputDialog) {
    if (percentageTotal.doubleValue() != 100) {
      JOptionPane.showMessageDialog(mainFrame, "Percentage total is not 100", "Error",
          JOptionPane.ERROR_MESSAGE);
    } else {
      userInputDialog.dispose();
      doneClicked.set(true);
    }
  }

  static class LabelFieldPair {

    JLabel label;
    JTextField textField;

    LabelFieldPair(JLabel label, JTextField textField) {
      this.label = label;
      this.textField = textField;
    }
  }
}
