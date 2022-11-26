package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

class PerformFractionalInvestmentCommand extends AbstractCommandHandlers implements
    CommandHandler {

  PerformFractionalInvestmentCommand(JTextPane resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel displayPanel;
    AtomicInteger percentageTotal = new AtomicInteger();
    AtomicBoolean DoneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Fractional Investment");
    userInputDialog.setMinimumSize(new Dimension(550, 300));
    userInputDialog.setLocationRelativeTo(null);
    userInputDialog.setResizable(false);

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
    Map<String, Double> stocks = new HashMap<>();

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(DATE);
    createNumericFields(TOTAL_AMOUNT);
    createNumericFields(PORTFOLIO_ID);
    createTickerSymbolField();
    createNumericFields(PERCENTAGE);

    JButton OKButton = getCustomButton("OK");
    OKButton.addActionListener(e -> OKFunctionality(percentageTotal, stocks, OKButton));

    JButton DoneButton = getCustomButton("DONE");
    DoneButton.addActionListener(e -> {
      if(percentageTotal.get() != 100) {
        JOptionPane.showMessageDialog(mainFrame, "Percentage total is not 100", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        userInputDialog.dispose();
        DoneClicked.set(true);
      }
    });

    addAllFieldsToInputPanel(userInputPanel);
    userInputPanel.add(OKButton);
    userInputPanel.add(DoneButton);

    mainPanel.add(new JLabel("Press OK to add one stock and "
        + "DONE when you are done adding stocks"), BorderLayout.NORTH);
    mainPanel.add(displayPanel, BorderLayout.CENTER);
    mainPanel.add(userInputPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);

    if (DoneClicked.get()) {
      InvestFractionalTask investFractionalTask = new InvestFractionalTask(features, stocks,
          fieldsMap.get(TOTAL_AMOUNT).textField.getText(),
          fieldsMap.get(PORTFOLIO_ID).textField.getText(), fieldsMap.get(DATE).textField.getText());
      progressBar.setIndeterminate(true);
      investFractionalTask.execute();
      mainFrame.setEnabled(false);
    }
  }

  private void OKFunctionality(AtomicInteger percentageTotal, Map<String, Double> stocks,
      JButton OKButton) {
    if (validator(validatorMap).isEmpty()) {
      if(percentageTotal.get() + Integer.parseInt(fieldsMap.get(PERCENTAGE).textField.getText()) > 100) {
        JOptionPane.showMessageDialog(mainFrame, "Percentage total cannot be greater than 100", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else if (Integer.parseInt(fieldsMap.get(PERCENTAGE).textField.getText()) <= 0) {
        JOptionPane.showMessageDialog(mainFrame, "Percentage value should be greater"
                + " than 0 less than 100", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        stocks.put(fieldsMap.get(TICKER_SYMBOL).textField.getText(),
            Double.parseDouble(fieldsMap.get(PERCENTAGE).textField.getText()));
        percentageTotal.addAndGet(Integer.parseInt(fieldsMap.get(PERCENTAGE).textField.getText()));
        subWindowDisplay.append(
            fieldsMap.get(TICKER_SYMBOL).textField.getText() + "- >" + fieldsMap.get(
                PERCENTAGE).textField.getText() + "\n");
        fieldsMap.get(TICKER_SYMBOL).textField.setText("");
        fieldsMap.get(PERCENTAGE).textField.setText("");
        fieldsMap.get(DATE).textField.setEditable(false);
        fieldsMap.get(TOTAL_AMOUNT).textField.setEditable(false);
        fieldsMap.get(PORTFOLIO_ID).textField.setEditable(false);
        if (percentageTotal.get() == 100) {
          fieldsMap.get(TICKER_SYMBOL).textField.setEditable(false);
          fieldsMap.get(PERCENTAGE).textField.setEditable(false);
          OKButton.setEnabled(false);
        }
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
        return features.fractionalInvestmentOnAGivenDate(stockProportions, totalAmount, portfolioId,
            date);
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
