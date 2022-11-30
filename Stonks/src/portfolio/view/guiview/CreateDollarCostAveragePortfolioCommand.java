package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
import portfolio.controller.IFeatures;

/**
 * Command class containing the logic for creating a portfolio using the dollar cost average
 * strategy. Implements {@link ICommandHandler}.
 */
class CreateDollarCostAveragePortfolioCommand extends AbstractCommandHandlers implements
    ICommandHandler {

  CreateDollarCostAveragePortfolioCommand(JTextPane resultArea,
      IFeatures features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    DoubleAdder percentageTotal = new DoubleAdder();
    AtomicBoolean doneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Create a Portfolio Using Dollar Cost Averaging",
        550, 300);

    JPanel displayPanel = getResultDisplay("", "Proportions");

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = addAllElementsToUserInputDialog(percentageTotal, doneClicked,
        userInputDialog, displayPanel, mainPanel);

    if (doneClicked.get()) {
      performDoneOperation(stocks);
    }
  }

  private void performDoneOperation(Map<String, Double> stocks) {
    CreateDollarCostAveragePortfolioTask createDollarCostAveragePortfolioTask =
        new CreateDollarCostAveragePortfolioTask(features, stocks,
            fieldsMap.get(TOTAL_AMOUNT).textField.getText(),
            fieldsMap.get(START_DATE).textField.getText(),
            fieldsMap.get(END_DATE).textField.getText(),
            fieldsMap.get(TIME_FRAME).textField.getText());
    progressBar.setIndeterminate(true);
    createDollarCostAveragePortfolioTask.execute();
    mainFrame.setEnabled(false);
  }

  private Map<String, Double> addAllElementsToUserInputDialog(DoubleAdder percentageTotal,
      AtomicBoolean doneClicked, JDialog userInputDialog, JPanel displayPanel, JPanel mainPanel) {
    Map<String, Double> stocks = new HashMap<>();

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(START_DATE);
    createDateLabelField(END_DATE);
    createIntegerFields(TIME_FRAME);
    createDoubleFields(TOTAL_AMOUNT);
    createTickerSymbolField();
    createDoubleFields(PERCENTAGE);

    JButton okButton = getCustomButton("OK");
    okButton.addActionListener(e -> okFunctionality(percentageTotal, stocks, okButton));
    JButton doneButton = getCustomButton("DONE");
    doneButton.addActionListener(
        e -> strategyDONEFunctionality(percentageTotal, doneClicked, userInputDialog));

    addAllFieldsToInputPanel(userInputPanel);
    userInputPanel.add(okButton);
    userInputPanel.add(doneButton);

    mainPanel.add(new JLabel(STRATEGY_MESSAGE), BorderLayout.NORTH);
    mainPanel.add(displayPanel, BorderLayout.CENTER);
    mainPanel.add(userInputPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);
    return stocks;
  }

  private void okFunctionality(DoubleAdder percentageTotal, Map<String, Double> stocks,
      JButton okButton) {
    if (validator(validatorMap).isEmpty() && isPercentageTotalValid(percentageTotal)) {
      stocks.put(fieldsMap.get(TICKER_SYMBOL).textField.getText(),
          Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()));
      percentageTotal.add(Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()));
      subWindowDisplay.append(
          fieldsMap.get(TICKER_SYMBOL).textField.getText() + "- >" + fieldsMap.get(
              PERCENTAGE).textField.getText() + "\n");
      fieldsMap.get(TICKER_SYMBOL).textField.setText("");
      fieldsMap.get(PERCENTAGE).textField.setText("");
      fieldsMap.get(START_DATE).textField.setEditable(false);
      fieldsMap.get(END_DATE).textField.setEditable(false);
      fieldsMap.get(TIME_FRAME).textField.setEditable(false);
      fieldsMap.get(TOTAL_AMOUNT).textField.setEditable(false);
      if (percentageTotal.doubleValue() == 100) {
        fieldsMap.get(TICKER_SYMBOL).textField.setEditable(false);
        fieldsMap.get(PERCENTAGE).textField.setEditable(false);
        okButton.setEnabled(false);
      }
    }
  }

  class CreateDollarCostAveragePortfolioTask extends SwingWorker<String, Object> {

    IFeatures features;
    String startDate;
    String endDate;
    Map<String, Double> stockProportions;
    String totalAmount;
    String timeFrame;

    CreateDollarCostAveragePortfolioTask(IFeatures features, Map<String, Double> stockProportions,
        String totalAmount,
        String startDate, String endDate, String timeFrame) {
      this.features = features;
      this.startDate = startDate;
      this.endDate = endDate;
      this.timeFrame = timeFrame;
      this.stockProportions = stockProportions;
      this.totalAmount = totalAmount;
    }

    @Override
    protected String doInBackground() {
      try {
        return features.createDollarCostAveragePortfolio(stockProportions, totalAmount,
            startDate, endDate, timeFrame);
      } catch (Exception e) {
        return e.getLocalizedMessage();
      }
    }

    @Override
    protected void done() {
      try {
        resultArea.setText("<html><center><h1>" + get() + "</center></html>");
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
      mainFrame.setEnabled(true);
      progressBar.setIndeterminate(false);
    }
  }
}
