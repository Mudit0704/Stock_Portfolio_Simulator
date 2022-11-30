package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.IFeatures;

/**
 * Command class containing the logic for getting a portfolio's cost basis. Implements
 * {@link ICommandHandler}.
 */
class GetCostBasisCommand extends AbstractCommandHandlers implements ICommandHandler {

  GetCostBasisCommand(JTextPane resultArea, IFeatures features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel availablePortfoliosDisplay;
    AtomicBoolean okClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Get Portfolio Cost Basis", 520, 200);

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

    JButton okButton = getCustomButton("OK");
    okButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        userInputDialog.dispose();
        okClicked.set(true);
      }
    });

    addAllFieldsToInputPanel(userInputPanel);

    JPanel fieldsPanel = new JPanel(new BorderLayout());
    fieldsPanel.add(userInputPanel, BorderLayout.CENTER);
    fieldsPanel.add(okButton, BorderLayout.EAST);
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));

    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (okClicked.get()) {
      GetCostBasisTask task = new GetCostBasisTask(features,
          fieldsMap.get(DATE).textField.getText(),
          fieldsMap.get(PORTFOLIO_ID).textField.getText());
      progressBar.setIndeterminate(true);
      task.execute();
      mainFrame.setEnabled(false);
    }
  }

  class GetCostBasisTask extends SwingWorker<String, Object> {

    IFeatures features;
    String date;
    String portfolioId;

    GetCostBasisTask(IFeatures features, String date, String portfolioId) {
      this.features = features;
      this.date = date;
      this.portfolioId = portfolioId;
    }

    @Override
    protected String doInBackground() throws Exception {
      try {
        return "<html><center><h1>Cost Basis on " + date + ": " + String.format("%.2f",
            features.getCostBasis(date, portfolioId)) + "</center></html>";
      } catch (Exception e) {
        return "<html><center><h1>" + e.getLocalizedMessage() + "</center></html>";
      }
    }

    @Override
    protected void done() {
      try {
        resultArea.setText(get());
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
      mainFrame.setEnabled(true);
      progressBar.setIndeterminate(false);
    }
  }
}
