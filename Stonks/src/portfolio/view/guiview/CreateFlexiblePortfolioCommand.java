package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.IFeatures;

/**
 * Command class containing the logic for creating a flexible portfolio. Implements
 * {@link ICommandHandler}.
 */
class CreateFlexiblePortfolioCommand extends AbstractCommandHandlers implements ICommandHandler {

  CreateFlexiblePortfolioCommand(JTextPane resultArea,
      IFeatures features, JProgressBar progressBar,
      JFrame mainFrame) {
    super(resultArea, features, progressBar, mainFrame);
  }

  @Override
  public void execute() {
    AtomicBoolean doneClicked = new AtomicBoolean(false);
    AtomicBoolean okClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Create New Flexible Portfolio", 450, 200);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(DATE);
    createTickerSymbolField();
    createDoubleFields(QUANTITY);

    JButton okButton = getCustomButton("OK");
    okButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        stocks.put(fieldsMap.get(TICKER_SYMBOL).textField.getText(),
            Double.parseDouble(fieldsMap.get(QUANTITY).textField.getText()));
        fieldsMap.get(TICKER_SYMBOL).textField.setText("");
        fieldsMap.get(QUANTITY).textField.setText("");
        fieldsMap.get(DATE).textField.setEditable(false);
        okClicked.set(true);
      }
    });

    JButton doneButton = getCustomButton("DONE");
    doneButton.addActionListener(e -> {
      if (okClicked.get()) {
        userInputDialog.dispose();
        doneClicked.set(true);
      } else {
        JOptionPane.showMessageDialog(mainFrame, "No Stocks Added", "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    addAllFieldsToInputPanel(userInputPanel);
    userInputPanel.add(okButton);
    userInputPanel.add(doneButton);

    mainPanel.add(new JLabel(STRATEGY_MESSAGE), BorderLayout.NORTH);
    mainPanel.add(userInputPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);

    if (doneClicked.get()) {
      CreateFlexiblePortfolioTask createFlexiblePortfolioTask = new CreateFlexiblePortfolioTask(
          features, stocks, fieldsMap.get(DATE).textField.getText());
      progressBar.setIndeterminate(true);
      createFlexiblePortfolioTask.execute();
      mainFrame.setEnabled(false);
    }
  }

  class CreateFlexiblePortfolioTask extends SwingWorker<String, Object> {

    IFeatures features;
    String date;
    Map<String, Double> stocks;

    CreateFlexiblePortfolioTask(IFeatures features, Map<String, Double> stocks, String date) {
      this.features = features;
      this.date = date;
      this.stocks = stocks;
    }

    @Override
    protected String doInBackground() {
      try {
        return features.createFlexiblePortfolio(stocks, date);
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
