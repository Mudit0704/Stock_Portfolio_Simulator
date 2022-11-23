package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
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
import javax.swing.SwingWorker;
import portfolio.controller.Features;

public class PerformFractionalInvestmentCommand extends AbstractCommandHandlers implements
    CommandHandler {

  PerformFractionalInvestmentCommand(JTextArea resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
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
    displayArea.setText(features.getAvailablePortfolios());

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    datePortfolioIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    JLabel dateLabel = new JLabel("Enter date (YYYY-MM-DD): ");
    JTextField dateValue = new JTextField();
    JLabel totalAmountLabel = new JLabel("Enter total amount: ");
    JTextField totalAmountValue = new JTextField();
    JLabel portfolioIdLabel = new JLabel("Enter portfolioId (Portfolio1 -> 1): ");
    JTextField portfolioIdValue = new JTextField();
    JLabel tickerSymbolLabel = new JLabel("Enter Ticker Symbol: ");
    JTextField tickerSymbolValue = new JTextField();
    JLabel percentageLabel = new JLabel("Enter Percentage: ");
    JTextField percentageValue = new JTextField();
    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      if (stocks.isEmpty()) {
        displayArea.setText("");
      }
      stocks.put(tickerSymbolValue.getText(), Double.parseDouble(percentageValue.getText()));
      displayArea.append(tickerSymbolValue.getText() + "- >" + percentageValue.getText() + "\n");
      tickerSymbolValue.setText("");
      percentageValue.setText("");
      dateValue.setEditable(false);
      totalAmountValue.setEditable(false);
      portfolioIdValue.setEditable(false);
    });
    JButton DoneButton = new JButton("DONE");
    DoneButton.addActionListener(e -> {
      userInputDialog.dispose();
      DoneClicked.set(true);
    });

    datePortfolioIdPanel.add(dateLabel);
    datePortfolioIdPanel.add(dateValue);
    datePortfolioIdPanel.add(totalAmountLabel);
    datePortfolioIdPanel.add(totalAmountValue);
    datePortfolioIdPanel.add(portfolioIdLabel);
    datePortfolioIdPanel.add(portfolioIdValue);
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
      InvestFractionalTask investFractionalTask = new InvestFractionalTask(features, stocks,
          totalAmountValue.getText(), portfolioIdValue.getText(), dateValue.getText());
      progressBar.setIndeterminate(true);
      investFractionalTask.execute();
      mainFrame.setEnabled(false);
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
    protected String doInBackground() throws Exception {
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
        resultArea.setText(get());
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
