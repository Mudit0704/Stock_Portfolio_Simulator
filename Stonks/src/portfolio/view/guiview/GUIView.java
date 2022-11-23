package portfolio.view.guiview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.Features;
import portfolio.view.IGUIView;

public class GUIView extends JFrame implements IGUIView {

  private final JPanel mainPanel;
  private JButton createDollarCostPortfolioButton, createFlexiblePortfolioButton,
      getPortfolioValueButton, getCostBasisButton, savePortfolioButton, retrievePortfolioButton,
      sellStocksButton, buyStocksButton, fractionalInvestmentButton;
  private JProgressBar progressBar;

  private JTextArea displayArea;

  public GUIView(String caption)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    super(caption);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
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
    fractionalInvestmentButton = new JButton("Fractional Investment");
    actionsPanel.add(fractionalInvestmentButton);
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
    getPortfolioValueButton.addActionListener(e -> displayGetPortfolioValueWindow(features));
    getCostBasisButton.addActionListener(e -> displayGetPortfolioCostBasisWindow(features));
    sellStocksButton.addActionListener(e -> displaySellStocksWindow(features));
    buyStocksButton.addActionListener(e -> displayBuyStocksWindow(features));
    createFlexiblePortfolioButton.addActionListener(e -> displayCreateNewFlexibleWindow(features));
    fractionalInvestmentButton.addActionListener(e -> displayFractionInvestmentWindow(features));
    createDollarCostPortfolioButton.addActionListener(
        e -> displayCreateDollarCostAveragingWindow(features));
    retrievePortfolioButton.addActionListener(
        e -> displayArea.setText(features.retrievePortfolio()));
  }

  @Override
  public void displayGetPortfolioValueWindow(Features features) {
    new GetValueCommand(displayArea, features, progressBar, GUIView.this).execute();
  }

  @Override
  public void displayGetPortfolioCostBasisWindow(Features features) {
    new GetCostBasisCommand(displayArea, features, progressBar, GUIView.this).execute();
  }

  @Override
  public void displaySellStocksWindow(Features features) {
    new SellStocksCommand(displayArea, features, progressBar, GUIView.this).execute();
  }

  @Override
  public void displayBuyStocksWindow(Features features) {
    new BuyStocksCommand(displayArea, features, progressBar, GUIView.this).execute();
  }

  @Override
  public void displayCreateNewFlexibleWindow(Features features) {
    new CreateFlexiblePortfolioCommand(displayArea, features, progressBar, GUIView.this).execute();
  }

  @Override
  public void displayFractionInvestmentWindow(Features features) {
    new PerformFractionalInvestmentCommand(displayArea, features, progressBar,
        GUIView.this).execute();
  }

  @Override
  public void displayCreateDollarCostAveragingWindow(Features features) {
    new CreateDollarCostAveragePortfolioCommand(displayArea, features, progressBar,
        GUIView.this).execute();
  }
}