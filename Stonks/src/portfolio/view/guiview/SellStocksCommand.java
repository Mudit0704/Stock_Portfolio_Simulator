package portfolio.view.guiview;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

class SellStocksCommand extends AbstractCommandHandlers implements CommandHandler {

  SellStocksCommand(JTextPane resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    AtomicBoolean OKClicked = new AtomicBoolean(false);
    if (CreateTransactionWindow(OKClicked)) {
      return;
    }

    if (OKClicked.get()) {
      SellStocksTask sellStocksTask = new SellStocksTask(features,
          fieldsMap.get(QUANTITY).textField.getText(),
          fieldsMap.get(DATE).textField.getText(), fieldsMap.get(PORTFOLIO_ID).textField.getText(),
          fieldsMap.get(TICKER_SYMBOL).textField.getText());
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
    protected String doInBackground() {
      try {
        return features.sellPortfolioStocks(tickerSymbol, quantity, portfolioId, date);
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
