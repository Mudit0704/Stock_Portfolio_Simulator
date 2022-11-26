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

class CreateDollarCostAveragePortfolioCommand extends AbstractCommandHandlers implements
    CommandHandler {

  CreateDollarCostAveragePortfolioCommand(JTextPane resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    AtomicInteger percentageTotal = new AtomicInteger();
    AtomicBoolean DoneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Fractional Investment");
    userInputDialog.setMinimumSize(new Dimension(550, 300));
    userInputDialog.setLocationRelativeTo(null);
    userInputDialog.setResizable(false);

    JPanel displayPanel = getResultDisplay("", "Proportions");

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(START_DATE);
    createDateLabelField(END_DATE);
    createNumericFields(TIME_FRAME);
    createNumericFields(TOTAL_AMOUNT);
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
        subWindowDisplay.append(fieldsMap.get(TICKER_SYMBOL).textField.getText() + "- >" + fieldsMap.get(
            PERCENTAGE).textField.getText() + "\n");
        fieldsMap.get(TICKER_SYMBOL).textField.setText("");
        fieldsMap.get(PERCENTAGE).textField.setText("");
        fieldsMap.get(START_DATE).textField.setEditable(false);
        fieldsMap.get(END_DATE).textField.setEditable(false);
        fieldsMap.get(TIME_FRAME).textField.setEditable(false);
        fieldsMap.get(TOTAL_AMOUNT).textField.setEditable(false);
        if (percentageTotal.get() == 100) {
          fieldsMap.get(TICKER_SYMBOL).textField.setEditable(false);
          fieldsMap.get(PERCENTAGE).textField.setEditable(false);
          OKButton.setEnabled(false);
        }
      }
    }
  }

  class CreateDollarCostAveragePortfolioTask extends SwingWorker<String, Object> {

    Features features;
    String startDate;
    String endDate;
    Map<String, Double> stockProportions;
    String totalAmount;
    String timeFrame;

    CreateDollarCostAveragePortfolioTask(Features features, Map<String, Double> stockProportions,
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
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
