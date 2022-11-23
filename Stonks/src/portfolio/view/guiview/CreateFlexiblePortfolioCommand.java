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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

public class CreateFlexiblePortfolioCommand extends AbstractCommandHandlers implements
    CommandHandler {

  CreateFlexiblePortfolioCommand(JTextArea resultArea,
      Features features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    AtomicBoolean DoneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Create New Flexible Portfolio");
    userInputDialog.setMinimumSize(new Dimension(450, 200));
    userInputDialog.setLocationRelativeTo(null);
    userInputDialog.setResizable(false);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    datePortfolioIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    JLabel dateLabel = new JLabel("Enter date (YYYY-MM-DD): ");
    JTextField dateValue = new JTextField();
    JLabel tickerSymbolLabel = new JLabel("Enter Ticker Symbol: ");
    JTextField tickerSymbolValue = new JTextField();
    JLabel quantityLabel = new JLabel("Enter Quantity: ");
    JTextField quantityValue = new JTextField();
    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      stocks.put(tickerSymbolValue.getText(), Double.parseDouble(quantityValue.getText()));
      tickerSymbolValue.setText("");
      quantityValue.setText("");
      dateValue.setEditable(false);
    });
    JButton DoneButton = new JButton("DONE");
    DoneButton.addActionListener(e -> {
      userInputDialog.dispose();
      DoneClicked.set(true);
    });

    datePortfolioIdPanel.add(dateLabel);
    datePortfolioIdPanel.add(dateValue);
    datePortfolioIdPanel.add(tickerSymbolLabel);
    datePortfolioIdPanel.add(tickerSymbolValue);
    datePortfolioIdPanel.add(quantityLabel);
    datePortfolioIdPanel.add(quantityValue);
    datePortfolioIdPanel.add(OKButton);
    datePortfolioIdPanel.add(DoneButton);

    mainPanel.add(new JLabel("Press OK to add one stock and "
        + "DONE when you are adding stocks"), BorderLayout.NORTH);
    mainPanel.add(datePortfolioIdPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);

    if (DoneClicked.get()) {
      CreateFlexiblePortfolioTask createFlexiblePortfolioTask = new CreateFlexiblePortfolioTask(
          features, stocks, dateValue.getText());
      progressBar.setIndeterminate(true);
      createFlexiblePortfolioTask.execute();
      mainFrame.setEnabled(false);
    }
  }

  class CreateFlexiblePortfolioTask extends SwingWorker<String, Object> {
    Features features;
    String date;
    Map<String, Double> stocks;

    CreateFlexiblePortfolioTask(Features features, Map<String, Double> stocks, String date) {
      this.features = features;
      this.date = date;
      this.stocks = stocks;
    }

    @Override
    protected String doInBackground() throws Exception {
      try {
        return features.createFlexiblePortfolio(stocks, date);
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
