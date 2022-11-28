package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.DoubleAdder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;
import portfolio.controller.GUIPortfolioController;

public class ApplyDCAOnExistingPortfolioCommand extends AbstractCommandHandlers implements
    CommandHandler {

  /**
   * Initializes the members required by each button handler.
   *
   * @param resultArea  the text area where result of each command has to be displayed
   * @param features    an object of {@link GUIPortfolioController} to perform the callback
   *                    functionality between the view and controller
   * @param progressBar a progress bar representing the status of each command
   * @param mainFrame   the main window of the application
   */
  ApplyDCAOnExistingPortfolioCommand(JTextPane resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel displayPanel;
    DoubleAdder percentageTotal = new DoubleAdder();
    AtomicBoolean DoneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Apply Dollar Cost Averaging", 550, 300);

    try {
      displayPanel = getResultDisplay(features.getAvailablePortfolios(), AVAILABLE_PORTFOLIOS);
    } catch (Exception e) {
      resultArea.setText("<html><center><h1>" + e.getLocalizedMessage() + "</center></html>");
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = addAllElementsToUserInputDialog(displayPanel, percentageTotal,
        DoneClicked, userInputDialog, mainPanel);

    if (DoneClicked.get()) {
      performDoneOperation(stocks);
    }
  }

  private Map<String, Double> addAllElementsToUserInputDialog(JPanel displayPanel,
      DoubleAdder percentageTotal, AtomicBoolean DoneClicked, JDialog userInputDialog,
      JPanel mainPanel) {
    Map<String, Double> stocks = new HashMap<>();

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(START_DATE);
    createDateLabelField(END_DATE);
    createIntegerFields(TIME_FRAME);
    createDoubleFields(TOTAL_AMOUNT);
    createIntegerFields(PORTFOLIO_ID);
    createTickerSymbolField();
    createDoubleFields(PERCENTAGE);

    JButton OKButton = getCustomButton("OK");
    OKButton.addActionListener(e -> OKFunctionality(percentageTotal, stocks, OKButton));
    JButton DoneButton = getCustomButton("DONE");
    DoneButton.addActionListener(
        e -> strategyDONEFunctionality(percentageTotal, DoneClicked, userInputDialog));

    addAllFieldsToInputPanel(userInputPanel);
    userInputPanel.add(OKButton);
    userInputPanel.add(DoneButton);

    mainPanel.add(new JLabel(STRATEGY_MESSAGE), BorderLayout.NORTH);
    mainPanel.add(displayPanel, BorderLayout.CENTER);
    mainPanel.add(userInputPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);
    return stocks;
  }

  private void performDoneOperation(Map<String, Double> stocks) {
    ApplyDollarCostAveragePortfolioTask applyDollarCostAveragePortfolioTask =
        new ApplyDollarCostAveragePortfolioTask(features, stocks,
            fieldsMap.get(TOTAL_AMOUNT).textField.getText(),
            fieldsMap.get(START_DATE).textField.getText(),
            fieldsMap.get(END_DATE).textField.getText(),
            fieldsMap.get(TIME_FRAME).textField.getText(),
            fieldsMap.get(PORTFOLIO_ID).textField.getText());
    progressBar.setIndeterminate(true);
    applyDollarCostAveragePortfolioTask.execute();
    mainFrame.setEnabled(false);
  }

  private void OKFunctionality(DoubleAdder percentageTotal, Map<String, Double> stocks,
      JButton OKButton) {
    if (validator(validatorMap).isEmpty() && isPercentageTotalValid(percentageTotal)) {
      stocks.put(fieldsMap.get(TICKER_SYMBOL).textField.getText(),
          Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()));
      if (percentageTotal.doubleValue() == 0) {
        subWindowDisplay.setText("");
      }
      percentageTotal.add(Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()));
      subWindowDisplay.setBorder(BorderFactory.createTitledBorder("Proportions"));
      subWindowDisplay.append(
          fieldsMap.get(TICKER_SYMBOL).textField.getText() + "- >" + fieldsMap.get(
              PERCENTAGE).textField.getText() + "\n");
      fieldsMap.get(TICKER_SYMBOL).textField.setText("");
      fieldsMap.get(PERCENTAGE).textField.setText("");
      fieldsMap.get(START_DATE).textField.setEditable(false);
      fieldsMap.get(END_DATE).textField.setEditable(false);
      fieldsMap.get(TIME_FRAME).textField.setEditable(false);
      fieldsMap.get(TOTAL_AMOUNT).textField.setEditable(false);
      fieldsMap.get(PORTFOLIO_ID).textField.setEditable(false);
      if (percentageTotal.doubleValue() == 100) {
        fieldsMap.get(TICKER_SYMBOL).textField.setEditable(false);
        fieldsMap.get(PERCENTAGE).textField.setEditable(false);
        OKButton.setEnabled(false);
      }
    }
  }

  class ApplyDollarCostAveragePortfolioTask extends SwingWorker<String, Object> {

    Features features;
    String startDate;
    String endDate;
    Map<String, Double> stockProportions;
    String totalAmount;
    String timeFrame;
    String portfolioId;

    ApplyDollarCostAveragePortfolioTask(Features features, Map<String, Double> stockProportions,
        String totalAmount, String startDate, String endDate, String timeFrame,
        String portfolioId) {
      this.features = features;
      this.startDate = startDate;
      this.endDate = endDate;
      this.timeFrame = timeFrame;
      this.stockProportions = stockProportions;
      this.totalAmount = totalAmount;
      this.portfolioId = portfolioId;
    }

    @Override
    protected String doInBackground() {
      try {
        return features.applyDollarCostAveragePortfolio(stockProportions, totalAmount,
            startDate, endDate, timeFrame, portfolioId);
      } catch (Exception e) {
        return e.getLocalizedMessage();
      }
    }

    @Override
    protected void done() {
      try {
        resultArea.setText("<html><center><h1>" + get() + "</center></html>");
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
