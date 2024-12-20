package portfolio.view.guiview;

import static portfolio.view.guiview.AbstractCommandHandlers.getCustomButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.IFeatures;
import portfolio.view.IGUIView;

/**
 * JFrame representing the main user interface of the application. Implements {@link IGUIView} and
 * contains all the logic required to implement the GUI of the application.
 */
public class GUIView extends JFrame implements IGUIView {

  final JPanel mainPanel;
  JButton createDollarCostAveragePortfolioButton;
  JButton createFlexiblePortfolioButton;
  JButton getPortfolioValueButton;
  JButton getCostBasisButton;
  JButton savePortfolioButton;
  JButton retrievePortfolioButton;
  JButton sellStocksButton;
  JButton buyStocksButton;
  JButton fractionalInvestmentButton;
  JButton portfolioPerformanceButton;
  JButton applyDCAOnExistingPortfolio;
  JProgressBar progressBar;
  JTextPane displayArea;

  /**
   * Constructs an object of {@link GUIView} and initializes its members.
   *
   * @param title the title to be used by GUI view window
   * @throws UnsupportedLookAndFeelException if lnf.isSupportedLookAndFeel() is false
   * @throws ClassNotFoundException          if the LookAndFeel class could not be found
   * @throws InstantiationException          if a new instance of the class couldn't be created
   * @throws IllegalAccessException          if the class or initializer isn't accessible
   */
  public GUIView(String title) throws UnsupportedLookAndFeelException, ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    super(title);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);

    setMinimumSize(new Dimension(850, 400));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    displayMenu();
    setAlwaysOnTop(true);
    setLocationRelativeTo(null);
  }

  @Override
  public void displayMenu() {
    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    displayArea = new JTextPane();
    displayArea.setEditable(false);
    displayArea.setContentType("text/html");
    displayArea.setText("<html><center><h1>Welcome to STONKS platform!</h1></center></html>");
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    displayPanel.add(scrollPane);
    displayPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    mainPanel.add(displayPanel, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new GridLayout(0, 1));
    createDollarCostAveragePortfolioButton = getCustomButton(
        "Create Portfolio Using Dollar-Cost Average Strategy");
    actionsPanel.add(createDollarCostAveragePortfolioButton);
    createFlexiblePortfolioButton = getCustomButton("Create Flexible Portfolio");
    actionsPanel.add(createFlexiblePortfolioButton);
    getPortfolioValueButton = getCustomButton("Get Portfolio Value");
    actionsPanel.add(getPortfolioValueButton);
    getCostBasisButton = getCustomButton("Get Cost Basis");
    actionsPanel.add(getCostBasisButton);
    savePortfolioButton = getCustomButton("Save Portfolio");
    actionsPanel.add(savePortfolioButton);
    retrievePortfolioButton = getCustomButton("Retrieve Portfolio");
    actionsPanel.add(retrievePortfolioButton);
    sellStocksButton = getCustomButton("Sell Stocks");
    actionsPanel.add(sellStocksButton);
    buyStocksButton = getCustomButton("Buy Stocks");
    actionsPanel.add(buyStocksButton);
    applyDCAOnExistingPortfolio = getCustomButton("Apply DCA on existing portfolio");
    actionsPanel.add(applyDCAOnExistingPortfolio);
    fractionalInvestmentButton = getCustomButton("Apply Fractional Investment");
    actionsPanel.add(fractionalInvestmentButton);
    portfolioPerformanceButton = getCustomButton("Portfolio Performance");
    actionsPanel.add(portfolioPerformanceButton);
    progressBar = new JProgressBar();
    actionsPanel.add(progressBar);
    actionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    mainPanel.add(actionsPanel, BorderLayout.WEST);
  }

  @Override
  public void displayInvalidInput() {
    JOptionPane.showMessageDialog(this, "Invalid Input", "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayCustomText(String customText) {
    JOptionPane.showMessageDialog(this, customText, "Information",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void addFeatures(IFeatures features) {
    savePortfolioButton.addActionListener(e -> displayArea.setText("<html><center><h1>"
        + features.savePortfolio() + "</h1></center></html>"));
    getPortfolioValueButton.addActionListener(e -> displayGetPortfolioValueWindow(features));
    getCostBasisButton.addActionListener(e -> displayGetPortfolioCostBasisWindow(features));
    sellStocksButton.addActionListener(e -> displaySellStocksWindow(features));
    buyStocksButton.addActionListener(e -> displayBuyStocksWindow(features));
    createFlexiblePortfolioButton.addActionListener(e -> displayCreateNewFlexibleWindow(features));
    applyDCAOnExistingPortfolio.addActionListener(
        e -> displayApplyDCAOnExistingPortfolioWindow(features));
    fractionalInvestmentButton.addActionListener(e -> displayFractionInvestmentWindow(features));
    createDollarCostAveragePortfolioButton.addActionListener(
        e -> displayCreateDollarCostAveragingWindow(features));
    retrievePortfolioButton.addActionListener(e -> displayArea.setText("<html><center><h1>"
        + features.retrievePortfolio() + "</h1></center></html>"));
    portfolioPerformanceButton.addActionListener(
        e -> displayPortfolioPerformanceLineChart(features));

    pack();
    setVisible(true);
  }

  @Override
  public void displayGetPortfolioValueWindow(IFeatures features) {
    new GetValueCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayGetPortfolioCostBasisWindow(IFeatures features) {
    new GetCostBasisCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displaySellStocksWindow(IFeatures features) {
    new SellStocksCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayBuyStocksWindow(IFeatures features) {
    new BuyStocksCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayCreateNewFlexibleWindow(IFeatures features) {
    new CreateFlexiblePortfolioCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayFractionInvestmentWindow(IFeatures features) {
    new ApplyFractionalInvestmentCommand(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayCreateDollarCostAveragingWindow(IFeatures features) {
    new CreateDollarCostAveragePortfolioCommand(displayArea, features, progressBar,
        this).execute();
  }

  @Override
  public void displayPortfolioPerformanceLineChart(IFeatures features) {
    new DisplayPortfolioPerformance(displayArea, features, progressBar, this).execute();
  }

  @Override
  public void displayApplyDCAOnExistingPortfolioWindow(IFeatures features) {
    new ApplyDCAOnExistingPortfolioCommand(displayArea, features, progressBar, this).execute();
  }
}