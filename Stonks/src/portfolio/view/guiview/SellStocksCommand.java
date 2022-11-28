package portfolio.view.guiview;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

/**
 * Command class containing the logic for selling stocks of a portfolio. Implements
 * {@link CommandHandler}.
 */
class SellStocksCommand extends AbstractCommandHandlers implements CommandHandler {

  SellStocksCommand(JTextPane resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    AtomicBoolean okClicked = new AtomicBoolean(false);
    if (createTransactionWindow(okClicked)) {
      return;
    }

    if (okClicked.get()) {
      SellStocksTask sellStocksTask = new SellStocksTask(features,
          fieldsMap.get(QUANTITY).textField.getText(),
          fieldsMap.get(DATE).textField.getText(), fieldsMap.get(PORTFOLIO_ID).textField.getText(),
          fieldsMap.get(TICKER_SYMBOL).textField.getText(),
          fieldsMap.get(TRANSACTION_FEE).textField.getText());
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
    String transactionFee;

    SellStocksTask(Features features, String quantity, String date, String portfolioId,
        String tickerSymbol, String transactionFee) {
      this.features = features;
      this.date = date;
      this.portfolioId = portfolioId;
      this.tickerSymbol = tickerSymbol;
      this.quantity = quantity;
      this.transactionFee = transactionFee;
    }

    @Override
    protected String doInBackground() {
      try {
        return features.sellPortfolioStocks(tickerSymbol, quantity, portfolioId, date,
            transactionFee);
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
