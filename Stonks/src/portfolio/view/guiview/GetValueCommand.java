package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

/**
 * Command class containing the logic for getting a portfolio's value. Implements
 * {@link CommandHandler}.
 */
class GetValueCommand extends AbstractCommandHandlers implements CommandHandler {

  GetValueCommand(JTextPane resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);

  }

  @Override
  public void execute() {
    JPanel availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);

    JDialog userInputDialog = getUserInputDialog("Get Portfolio Value", 520, 200);

    try {
      availablePortfoliosDisplay = getResultDisplay(features.getAvailablePortfolios(),
          AVAILABLE_PORTFOLIOS);
      availablePortfoliosDisplay.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
    } catch (Exception e) {
      resultArea.setText("<html><center><h1>" + e.getLocalizedMessage() + "</center></html>");
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    createDateLabelField(DATE);
    createIntegerFields(PORTFOLIO_ID);

    JButton OKButton = getCustomButton("OK");
    OKButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        userInputDialog.dispose();
        OKClicked.set(true);
      }
    });

    addAllFieldsToInputPanel(userInputPanel);

    JPanel fieldsPanel = new JPanel(new BorderLayout());
    fieldsPanel.add(userInputPanel, BorderLayout.CENTER);
    fieldsPanel.add(OKButton, BorderLayout.EAST);
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));

    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (OKClicked.get()) {
      GetValueTask task = new GetValueTask(features, fieldsMap.get(DATE).textField.getText(),
          fieldsMap.get(PORTFOLIO_ID).textField.getText());
      mainFrame.setEnabled(false);
      progressBar.setIndeterminate(true);
      task.execute();
    }
  }

  class GetValueTask extends SwingWorker<String, Object> {

    Features features;
    String date;
    String portfolioId;

    GetValueTask(Features features, String date, String portfolioId) {
      this.features = features;
      this.date = date;
      this.portfolioId = portfolioId;
    }

    @Override
    protected String doInBackground() throws Exception {
      try {
        return "<html><center><h1>Portfolio Value on " + date + ": " + String.format("%.2f",
            features.getPortfolioValue(date, portfolioId)) + "</center></html>";
      } catch (Exception e) {
        return "<html><center><h1>" + e.getLocalizedMessage() + "</center></html>";
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
