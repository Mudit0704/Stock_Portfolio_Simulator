package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

public class SellStocksCommand extends AbstractCommandHandlers implements CommandHandler {

  SellStocksCommand(JTextArea resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Sell Stock");
    userInputDialog.setMinimumSize(new Dimension(470, 200));
    userInputDialog.setLocationRelativeTo(null);
    userInputDialog.setResizable(false);

    try {
      availablePortfoliosDisplay = getAvailablePortfoliosDisplay(features);
      availablePortfoliosDisplay.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
    } catch (Exception e) {
      resultArea.setText(e.getLocalizedMessage());
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    JLabel dateLabel = new JLabel("Enter date (YYYY-MM-DD): ");
    JTextField dateValue = new JTextField();
    JLabel portfolioIdLabel = new JLabel("Enter portfolioId (Portfolio1 -> 1): ");
    JTextField portfolioIdValue = new JTextField();
    JLabel tickerSymbolLabel = new JLabel("Enter Ticker Symbol: ");
    JTextField tickerSymbolValue = new JTextField();
    JLabel quantityLabel = new JLabel("Enter Quantity: ");
    JTextField quantityValue = new JTextField();
    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      userInputDialog.dispose();
      OKClicked.set(true);
    });

    datePortfolioIdPanel.add(dateLabel);
    datePortfolioIdPanel.add(dateValue);
    datePortfolioIdPanel.add(portfolioIdLabel);
    datePortfolioIdPanel.add(portfolioIdValue);
    datePortfolioIdPanel.add(tickerSymbolLabel);
    datePortfolioIdPanel.add(tickerSymbolValue);
    datePortfolioIdPanel.add(quantityLabel);
    datePortfolioIdPanel.add(quantityValue);

    JPanel fieldsPanel = new JPanel(new BorderLayout());
    fieldsPanel.add(datePortfolioIdPanel, BorderLayout.CENTER);
    fieldsPanel.add(OKButton, BorderLayout.EAST);
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));

    JLabel availablePortfolios = new JLabel("Available Portfolios");
    availablePortfolios.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

    userInputDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (OKClicked.get()) {
      SellStocksTask sellStocksTask = new SellStocksTask(features, quantityValue.getText(),
          dateValue.getText(), portfolioIdValue.getText(), tickerSymbolValue.getText());
      progressBar.setIndeterminate(true);
      sellStocksTask.execute();
      mainFrame.setEnabled(false);
    }
  }

  class SellStocksTask extends SwingWorker<String, Object> {
    Features features;
    String date;
    String portfolioId;
    String quantity;
    String tickerSymbol;

    SellStocksTask(Features features, String quantity, String date, String portfolioId
        , String tickerSymbol) {
      this.features = features;
      this.date = date;
      this.portfolioId = portfolioId;
      this.tickerSymbol = tickerSymbol;
      this.quantity = quantity;
    }

    @Override
    protected String doInBackground() throws Exception {
      try {
        return features.sellPortfolioStocks(tickerSymbol, quantity, portfolioId, date);
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
