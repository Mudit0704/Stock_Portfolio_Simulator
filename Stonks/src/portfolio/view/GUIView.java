package portfolio.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.CubicCurve2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import portfolio.controller.Features;

public class GUIView extends JFrame implements IGUIView {

  private JPanel mainPanel;
  private JScrollPane mainScrollPane;
  private JButton createDollarCostPortfolioButton, createFlexiblePortfolioButton,
      getPortfolioValueButton, getCostBasisButton, savePortfolioButton, retrievePortfolioButton,
      sellStocksButton, buyStocksButton, specificInvestmentButton;
  private JProgressBar progressBar;

  private JTextArea displayArea;

  public GUIView(String caption)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    super(caption);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    setMinimumSize(new Dimension(750, 500));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    displayMenu();

    pack();
    setVisible(true);
    setFocusable(true);
    setAlwaysOnTop(true);
    setLocationRelativeTo(null);
  }

  @Override
  public void displayMenu() {

    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    displayArea = new JTextArea(10, 20);
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    displayPanel.add(scrollPane);
    mainPanel.add(displayPanel, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new GridLayout(0, 2));
    createDollarCostPortfolioButton = new JButton("Create Portfolio Using Dollar-Cost Strategy");
    actionsPanel.add(createDollarCostPortfolioButton);
    createFlexiblePortfolioButton = new JButton("Create Flexible Portfolio");
    actionsPanel.add(createFlexiblePortfolioButton);
    getPortfolioValueButton = new JButton("Get Portfolio Value");
    actionsPanel.add(getPortfolioValueButton);
    getCostBasisButton = new JButton("Get Cost Basis");
    actionsPanel.add(getCostBasisButton);
    savePortfolioButton = new JButton("Save Portfolio");
    actionsPanel.add(savePortfolioButton);
    retrievePortfolioButton = new JButton("Retrieve Portfolio");
    actionsPanel.add(retrievePortfolioButton);
    sellStocksButton = new JButton("Sell Stocks");
    actionsPanel.add(sellStocksButton);
    buyStocksButton = new JButton("Buy Stocks");
    actionsPanel.add(buyStocksButton);
    specificInvestmentButton = new JButton("Specific Investment");
    actionsPanel.add(specificInvestmentButton);
    progressBar = new JProgressBar();
    actionsPanel.add(progressBar);
    mainPanel.add(actionsPanel, BorderLayout.PAGE_END);

    mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
  }

  @Override
  public void displayInvalidInput() {
    JOptionPane.showMessageDialog(GUIView.this, "Invalid Input", "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayCustomText(String customText) {

  }

  @Override
  public void addFeatures(Features features) {
    savePortfolioButton.addActionListener(e -> displayArea.setText(features.savePortfolio()));
    retrievePortfolioButton.addActionListener(
        e -> displayArea.setText(features.retrievePortfolio()));
    getPortfolioValueButton.addActionListener(e -> displayGetPortfolioValueWindow(features));
    getCostBasisButton.addActionListener(e -> displayGetPortfolioCostBasisWindow(features));
    sellStocksButton.addActionListener(e -> displaySellStocksWindow(features));
    createFlexiblePortfolioButton.addActionListener(e -> displayCreateNewFlexibleWindow(features));
  }

  @Override
  public void displayGetPortfolioValueWindow(Features features) {
    progressBar.setIndeterminate(true);
    JTextArea availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);

    JDialog getPortfolioValueDialog = getUserInputDialog("Get Portfolio Value");
    try {
      availablePortfoliosDisplay = getAvailablePortfoliosDisplay(features);
    } catch (Exception e) {
      displayArea.setText(e.getLocalizedMessage());
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    JLabel dateLabel = new JLabel("Enter date (yyyy-mm-dd): ");
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

    JPanel fieldsPanel = new JPanel(new GridBagLayout());
    fieldsPanel.add(datePortfolioIdPanel);
    GridBagConstraints gb = new GridBagConstraints();
    gb.weightx = 1;
    gb.fill = GridBagConstraints.BOTH;
    gb.gridwidth = GridBagConstraints.REMAINDER;
    fieldsPanel.add(OKButton, gb);

    JLabel availablePortfolios = new JLabel("Available Portfolios");

    getPortfolioValueDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    getPortfolioValueDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    getPortfolioValueDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    getPortfolioValueDialog.setVisible(true);

    if (OKClicked.get()) {
      GetValueTask task = new GetValueTask(features, dateValue.getText(),
          portfolioIdValue.getText());
      setEnabled(false);
      task.execute();
    }
    progressBar.setIndeterminate(false);
  }

  @Override
  public void displayGetPortfolioCostBasisWindow(Features features) {
    progressBar.setIndeterminate(true);
    JTextArea availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Get Portfolio Cost Basis");

    try {
      availablePortfoliosDisplay = getAvailablePortfoliosDisplay(features);
    } catch (Exception e) {
      displayArea.setText(e.getLocalizedMessage());
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    JLabel dateLabel = new JLabel("Enter date (yyyy-mm-dd): ");
    JTextField dateValue = new JTextField();
    JLabel portfolioIdLabel = new JLabel("Enter portfolioId (Portfolio1 -> 1): ");
    JTextField portfolioIdValue = new JTextField();
    JButton OKButton = new JButton("OK");
    OKButton.addActionListener(e -> {
      userInputDialog.dispose();
      OKClicked.set(true);
    });

    datePortfolioIdPanel.add(dateLabel);
    datePortfolioIdPanel.add(dateValue);
    datePortfolioIdPanel.add(portfolioIdLabel);
    datePortfolioIdPanel.add(portfolioIdValue);

    JPanel fieldsPanel = new JPanel(new GridBagLayout());
    fieldsPanel.add(datePortfolioIdPanel);
    GridBagConstraints gb = new GridBagConstraints();
    gb.weightx = 1;
    gb.fill = GridBagConstraints.BOTH;
    gb.gridwidth = GridBagConstraints.REMAINDER;
    fieldsPanel.add(OKButton, gb);

    JLabel availablePortfolios = new JLabel("Available Portfolios");

    userInputDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (OKClicked.get()) {
      GetCostBasisTask task = new GetCostBasisTask(features, dateValue.getText(),
          portfolioIdValue.getText());
      task.execute();
    }
    progressBar.setIndeterminate(false);
  }

  @Override
  public void displaySellStocksWindow(Features features) {
    progressBar.setIndeterminate(true);
    JTextArea availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Sell Stock");

    try {
      availablePortfoliosDisplay = getAvailablePortfoliosDisplay(features);
    } catch (Exception e) {
      displayArea.setText(e.getLocalizedMessage());
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    JLabel dateLabel = new JLabel("Enter date (yyyy-mm-dd): ");
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

    JPanel fieldsPanel = new JPanel(new GridBagLayout());
    fieldsPanel.add(datePortfolioIdPanel);
    GridBagConstraints gb = new GridBagConstraints();
    gb.weightx = 1;
    gb.fill = GridBagConstraints.BOTH;
    gb.gridwidth = GridBagConstraints.REMAINDER;
    fieldsPanel.add(OKButton, gb);

    JLabel availablePortfolios = new JLabel("Available Portfolios");

    userInputDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (OKClicked.get()) {
      try {
        features.sellPortfolioStocks(tickerSymbolValue.getText(), quantityValue.getText(),
            portfolioIdValue.getText(), dateValue.getText());
        displayArea.setText("Sold");
      } catch (Exception e) {
        displayArea.setText(e.getLocalizedMessage());
      }

    }
    progressBar.setIndeterminate(false);
  }

  @Override
  public void displayBuyStocksWindow(Features features) {
    progressBar.setIndeterminate(true);
    JTextArea availablePortfoliosDisplay;
    AtomicBoolean OKClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Buy Stock");

    try {
      availablePortfoliosDisplay = getAvailablePortfoliosDisplay(features);
    } catch (Exception e) {
      displayArea.setText(e.getLocalizedMessage());
      progressBar.setIndeterminate(false);
      return;
    }

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    JLabel dateLabel = new JLabel("Enter date (yyyy-mm-dd): ");
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

    JPanel fieldsPanel = new JPanel(new GridBagLayout());
    fieldsPanel.add(datePortfolioIdPanel);
    GridBagConstraints gb = new GridBagConstraints();
    gb.weightx = 1;
    gb.fill = GridBagConstraints.BOTH;
    gb.gridwidth = GridBagConstraints.REMAINDER;
    fieldsPanel.add(OKButton, gb);

    JLabel availablePortfolios = new JLabel("Available Portfolios");

    userInputDialog.add(availablePortfolios, BorderLayout.PAGE_START);
    userInputDialog.add(availablePortfoliosDisplay, BorderLayout.CENTER);
    userInputDialog.add(fieldsPanel, BorderLayout.PAGE_END);

    userInputDialog.setVisible(true);

    if (OKClicked.get()) {
      try {
        features.buyPortfolioStocks(tickerSymbolValue.getText(), quantityValue.getText(),
            portfolioIdValue.getText(), dateValue.getText());
        displayArea.setText("Bought");
      } catch (Exception e) {
        displayArea.setText(e.getLocalizedMessage());
      }

    }
    progressBar.setIndeterminate(false);
  }

  @Override
  public void displayCreateNewFlexibleWindow(Features features) {
    progressBar.setIndeterminate(true);
    AtomicBoolean DoneClicked = new AtomicBoolean(false);
    JDialog userInputDialog = getUserInputDialog("Create New Flexible Portfolio");
    userInputDialog.setMinimumSize(new Dimension(0, 0));
    userInputDialog.setResizable(false);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    userInputDialog.add(mainPanel);
    Map<String, Double> stocks = new HashMap<>();

    JPanel datePortfolioIdPanel = new JPanel(new GridLayout(0, 2));
    datePortfolioIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    JLabel dateLabel = new JLabel("Enter date (yyyy-mm-dd): ");
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
      createFlexiblePortfolioTask.execute();
    }
    progressBar.setIndeterminate(false);
  }

  private JTextArea getAvailablePortfoliosDisplay(Features features) {
    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(10, 20);
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    displayPanel.add(scrollPane);
    displayArea.setText(features.getAvailablePortfolios());
    return displayArea;
  }

  private JDialog getUserInputDialog(String title) {
    JDialog userInputDialog = new JDialog(GUIView.this, title);
    userInputDialog.setModalityType(ModalityType.APPLICATION_MODAL);
    userInputDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    userInputDialog.setMinimumSize(new Dimension(650, 400));
    userInputDialog.setLocationRelativeTo(null);
    return userInputDialog;
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
        displayArea.setText("Portfolio Value on " + date + ": $" + get());
        setEnabled(true);
      } catch (Exception ignore) {
      }
    }
  }

  class GetCostBasisTask extends SwingWorker<String, Object> {

    Features features;
    String date;
    String portfolioId;

    GetCostBasisTask(Features features, String date, String portfolioId) {
      this.features = features;
      this.date = date;
      this.portfolioId = portfolioId;
    }

    @Override
    protected String doInBackground() throws Exception {
      try {
        return String.format("%.2f", features.getCostBasis(date, portfolioId));
      } catch (Exception e) {
        return e.getLocalizedMessage();
      }
    }

    @Override
    protected void done() {
      try {
        displayArea.setText("Cost basis on " + date + ": $" + get());
      } catch (Exception ignore) {
      }
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
        features.createFlexiblePortfolio(stocks, date);
        return "Created";
      } catch (Exception e) {
        return e.getLocalizedMessage();
      }
    }

    @Override
    protected void done() {
      try {
        displayArea.setText(get());
      } catch (Exception ignore) {
      }
    }
  }
}
