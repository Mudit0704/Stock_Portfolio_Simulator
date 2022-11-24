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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

public class CreateDollarCostAveragePortfolioCommand extends AbstractCommandHandlers implements
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

    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(5, 20);
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    displayPanel.add(scrollPane);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    datePortfolioIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    JLabel startDateLabel = new JLabel("Enter start date (YYYY-MM-DD): ");
    JTextField startDateValue = new JTextField();
    startDateValue.setName("Start Date");
    validatorMap.put(startDateValue, this::dateTextFieldValidator);

    JLabel endDateLabel = new JLabel("Enter end date (YYYY-MM-DD): ");
    JTextField endDateValue = new JTextField();
    endDateValue.setName("End Date");
    validatorMap.put(endDateValue, this::dateTextFieldValidator);

    JLabel timeFrameLabel = new JLabel("Enter time frame: ");
    JTextField timeFrameValue = new JTextField();
    timeFrameValue.setName("Time Frame");
    validatorMap.put(timeFrameValue, this::numberTextFieldValidator);

    JLabel totalAmountLabel = new JLabel("Enter total amount: ");
    JTextField totalAmountValue = new JTextField();
    totalAmountValue.setName("Total Amount");
    validatorMap.put(totalAmountValue, this::numberTextFieldValidator);

    JLabel tickerSymbolLabel = new JLabel("Enter Ticker Symbol: ");
    JTextField tickerSymbolValue = new JTextField();
    tickerSymbolValue.setName("Ticker Symbol");
    validatorMap.put(tickerSymbolValue, this::tickerSymbolTextFieldValidator);

    JLabel percentageLabel = new JLabel("Enter Percentage: ");
    JTextField percentageValue = new JTextField();
    percentageValue.setName("Percentage");
    validatorMap.put(percentageValue, this::numberTextFieldValidator);

    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        if (stocks.isEmpty()) {
          displayArea.setText("");
        }
        stocks.put(tickerSymbolValue.getText(), Double.parseDouble(percentageValue.getText()));
        percentageTotal.addAndGet(Integer.parseInt(percentageValue.getText()));
        displayArea.append(tickerSymbolValue.getText() + "- >" + percentageValue.getText() + "\n");
        tickerSymbolValue.setText("");
        percentageValue.setText("");
        startDateValue.setEditable(false);
        endDateValue.setEditable(false);
        timeFrameValue.setEditable(false);
        totalAmountValue.setEditable(false);
        if(percentageTotal.get() == 100) {
          tickerSymbolValue.setEditable(false);
          percentageValue.setEditable(false);
          OKButton.setEnabled(false);
        }
      }
    });
    JButton DoneButton = new JButton("DONE");
    DoneButton.addActionListener(e -> {
      userInputDialog.dispose();
      DoneClicked.set(true);
    });

    datePortfolioIdPanel.add(startDateLabel);
    datePortfolioIdPanel.add(startDateValue);
    datePortfolioIdPanel.add(endDateLabel);
    datePortfolioIdPanel.add(endDateValue);
    datePortfolioIdPanel.add(timeFrameLabel);
    datePortfolioIdPanel.add(timeFrameValue);
    datePortfolioIdPanel.add(totalAmountLabel);
    datePortfolioIdPanel.add(totalAmountValue);
    datePortfolioIdPanel.add(tickerSymbolLabel);
    datePortfolioIdPanel.add(tickerSymbolValue);
    datePortfolioIdPanel.add(percentageLabel);
    datePortfolioIdPanel.add(percentageValue);
    datePortfolioIdPanel.add(OKButton);
    datePortfolioIdPanel.add(DoneButton);

    mainPanel.add(new JLabel("Press OK to add one stock and "
        + "DONE when you are adding stocks"), BorderLayout.NORTH);
    mainPanel.add(displayPanel, BorderLayout.CENTER);
    mainPanel.add(datePortfolioIdPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);

    if (DoneClicked.get()) {
      CreateDollarCostAveragePortfolioTask createDollarCostAveragePortfolioTask =
          new CreateDollarCostAveragePortfolioTask(features, stocks, totalAmountValue.getText(),
              startDateValue.getText(), endDateValue.getText(), timeFrameValue.getText());
      progressBar.setIndeterminate(true);
      createDollarCostAveragePortfolioTask.execute();
      mainFrame.setEnabled(false);
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
    protected String doInBackground() throws Exception {
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
        resultArea.setText(get());
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
