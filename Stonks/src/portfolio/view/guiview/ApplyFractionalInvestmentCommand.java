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
import portfolio.controller.Features;

/**
 * Command class containing the logic for performing a fractional investment on an existing
 * portfolio. Implements {@link CommandHandler}.
 */
class ApplyFractionalInvestmentCommand extends AbstractCommandHandlers implements
    CommandHandler {

  ApplyFractionalInvestmentCommand(JTextPane resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel displayPanel;
    Map<String, Double> stocks = new HashMap<>();
    DoubleAdder percentageTotal = new DoubleAdder();
    AtomicBoolean doneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Apply Fractional Investment", 550, 300);

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

    addAllElementsToUserInputDialog(displayPanel, stocks, percentageTotal, doneClicked,
        userInputDialog, mainPanel);

    if (doneClicked.get()) {
      InvestFractionalTask investFractionalTask = new InvestFractionalTask(features, stocks,
          fieldsMap.get(TOTAL_AMOUNT).textField.getText(),
          fieldsMap.get(PORTFOLIO_ID).textField.getText(), fieldsMap.get(DATE).textField.getText());
      progressBar.setIndeterminate(true);
      investFractionalTask.execute();
      mainFrame.setEnabled(false);
    }
  }

  private void addAllElementsToUserInputDialog(JPanel displayPanel, Map<String, Double> stocks,
      DoubleAdder percentageTotal, AtomicBoolean doneClicked, JDialog userInputDialog,
      JPanel mainPanel) {
    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(DATE);
    createDoubleFields(TOTAL_AMOUNT);
    createIntegerFields(PORTFOLIO_ID);
    createDoubleFields(PERCENTAGE);
    createTickerSymbolField();

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
  }

  private void okFunctionality(DoubleAdder percentageTotal, Map<String, Double> stocks,
      JButton okButton) {
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
      fieldsMap.get(DATE).textField.setEditable(false);
      fieldsMap.get(TOTAL_AMOUNT).textField.setEditable(false);
      fieldsMap.get(PORTFOLIO_ID).textField.setEditable(false);
      if (percentageTotal.doubleValue() == 100) {
        fieldsMap.get(TICKER_SYMBOL).textField.setEditable(false);
        fieldsMap.get(PERCENTAGE).textField.setEditable(false);
        okButton.setEnabled(false);
      }
    }
  }

  class InvestFractionalTask extends SwingWorker<String, Object> {

    Features features;
    String date;
    Map<String, Double> stockProportions;
    String totalAmount;
    String portfolioId;

    InvestFractionalTask(Features features, Map<String, Double> stockProportions,
        String totalAmount,
        String portfolioId, String date) {
      this.features = features;
      this.date = date;
      this.stockProportions = stockProportions;
      this.totalAmount = totalAmount;
      this.portfolioId = portfolioId;
    }

    @Override
    protected String doInBackground() {
      try {
        return features.performFractionalInvestmentOnAGivenDate(stockProportions, totalAmount,
            portfolioId,
            date);
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
