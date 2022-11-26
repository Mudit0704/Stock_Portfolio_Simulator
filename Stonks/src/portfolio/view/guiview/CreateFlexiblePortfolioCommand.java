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
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import portfolio.controller.Features;

/**
 * Command class containing the logic for creating a flexible portfolio. Implements
 * {@link CommandHandler}.
 */
class CreateFlexiblePortfolioCommand extends AbstractCommandHandlers implements CommandHandler {

  CreateFlexiblePortfolioCommand(JTextPane resultArea,
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

    JPanel userInputPanel = new JPanel(new GridLayout(0, 2));
    userInputPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    createDateLabelField(DATE);
    createTickerSymbolField();
    createNumericFields(QUANTITY);

    JButton OKButton = getCustomButton("OK");
    OKButton.addActionListener(e -> {
      if (validator(validatorMap).isEmpty()) {
        stocks.put(fieldsMap.get(TICKER_SYMBOL).textField.getText(),
            Double.parseDouble(fieldsMap.get(QUANTITY).textField.getText()));
        fieldsMap.get(TICKER_SYMBOL).textField.setText("");
        fieldsMap.get(QUANTITY).textField.setText("");
        fieldsMap.get(DATE).textField.setEditable(false);
      }
    });

    JButton DoneButton = getCustomButton("DONE");
    DoneButton.addActionListener(e -> {
      userInputDialog.dispose();
      DoneClicked.set(true);
    });

    addAllFieldsToInputPanel(userInputPanel);
    userInputPanel.add(OKButton);
    userInputPanel.add(DoneButton);

    mainPanel.add(new JLabel("Press OK to add one stock and "
        + "DONE when you are done adding stocks"), BorderLayout.NORTH);
    mainPanel.add(userInputPanel, BorderLayout.SOUTH);

    userInputDialog.pack();
    userInputDialog.setVisible(true);

    if (DoneClicked.get()) {
      CreateFlexiblePortfolioTask createFlexiblePortfolioTask = new CreateFlexiblePortfolioTask(
          features, stocks, fieldsMap.get(DATE).textField.getText());
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
        resultArea.setText("<html><center><h1>" + get() + "</center></html>");
        mainFrame.setEnabled(true);
        progressBar.setIndeterminate(false);
      } catch (Exception ignore) {
      }
    }
  }
}
