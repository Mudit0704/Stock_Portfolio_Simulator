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

class GetValueCommand extends AbstractCommandHandlers implements CommandHandler {

  GetValueCommand(JTextArea resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);

  }

  @Override
  public void execute() {
    JPanel availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);

    JDialog getPortfolioValueDialog = getUserInputDialog("Get Portfolio Value");
    getPortfolioValueDialog.setMinimumSize(new Dimension(470, 200));
    getPortfolioValueDialog.setLocationRelativeTo(null);
    getPortfolioValueDialog.setResizable(false);

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
    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      getPortfolioValueDialog.dispose();
      OKClicked.set(true);
    });

    datePortfolioIdPanel.add(dateLabel);
    datePortfolioIdPanel.add(dateValue);
    datePortfolioIdPanel.add(portfolioIdLabel);
    datePortfolioIdPanel.add(portfolioIdValue);

    JPanel fieldsPanel = new JPanel(new BorderLayout());
    fieldsPanel.add(datePortfolioIdPanel, BorderLayout.CENTER);
    fieldsPanel.add(OKButton, BorderLayout.EAST);
    fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));

    JLabel availablePortfolios = new JLabel("Available Portfolios");
    availablePortfolios.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

    getPortfolioValueDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    getPortfolioValueDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    getPortfolioValueDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    getPortfolioValueDialog.setVisible(true);

    if (OKClicked.get()) {
      GetValueTask task = new GetValueTask(features, dateValue.getText(),
          portfolioIdValue.getText());
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
        return String.format("%.2f", features.getPortfolioValue(date, portfolioId));
      } catch (Exception e) {
        return e.getLocalizedMessage();
      }
    }

    @Override
    protected void done() {
      try {
        resultArea.setText("Portfolio Value on " + date + ": $" + get());
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
