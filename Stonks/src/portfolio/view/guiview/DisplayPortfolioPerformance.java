package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
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
import javax.swing.WindowConstants;
import portfolio.controller.Features;

/**
 * Command class containing the logic for displaying a portfolio's performance. Implements
 * {@link CommandHandler}.
 */
public class DisplayPortfolioPerformance extends AbstractCommandHandlers implements CommandHandler {

  DisplayPortfolioPerformance(JTextPane resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    JPanel availablePortfoliosDisplay;
    AtomicBoolean okClicked = new AtomicBoolean(false);

    JDialog userInputDialog = getUserInputDialog("Portfolio performance", 660, 200);

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
    createDateLabelField(PERFORMANCE_START_DATE);
    createDateLabelField(PERFORMANCE_END_DATE);
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
      GetPortfolioPerformanceTask task = new GetPortfolioPerformanceTask(features,
          fieldsMap.get(PERFORMANCE_START_DATE).textField.getText(),
          fieldsMap.get(PERFORMANCE_END_DATE).textField.getText(),
          fieldsMap.get(PORTFOLIO_ID).textField.getText());
      mainFrame.setEnabled(false);
      progressBar.setIndeterminate(true);
      task.execute();
    }
  }

  class GetPortfolioPerformanceTask extends SwingWorker<Object, Object> {

    Features features;
    String startDate;
    String endDate;
    String portfolioId;

    GetPortfolioPerformanceTask(Features features, String startDate, String endDate,
        String portfolioId) {
      this.features = features;
      this.startDate = startDate;
      this.endDate = endDate;
      this.portfolioId = portfolioId;
    }

    @Override
    protected Object doInBackground() {
      try {
        PerformanceLineChartDialog performanceLineChartWindow = new PerformanceLineChartDialog(
            "Portfolio Performance",
            features.getPortfolioPerformance(startDate, endDate, portfolioId));
        performanceLineChartWindow.setModalityType(ModalityType.APPLICATION_MODAL);
        performanceLineChartWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        performanceLineChartWindow.setAlwaysOnTop(true);
        performanceLineChartWindow.pack();
        performanceLineChartWindow.setSize(600, 400);
        performanceLineChartWindow.setLocationRelativeTo(null);
        performanceLineChartWindow.setVisible(true);
      } catch (IllegalArgumentException e) {
        resultArea.setText("<html><center><h1>" + e.getLocalizedMessage() + "</center></html>");
      }
      mainFrame.setEnabled(true);
      progressBar.setIndeterminate(false);
      return null;
    }
  }
}
