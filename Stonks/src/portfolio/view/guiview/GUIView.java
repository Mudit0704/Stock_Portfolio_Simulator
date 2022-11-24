package portfolio.view.guiview;

import static portfolio.view.guiview.AbstractCommandHandlers.getCustomButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
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
import portfolio.controller.Features;
import portfolio.view.IGUIView;

public class GUIView extends JFrame implements IGUIView {

  private final JPanel mainPanel;
  private JButton createDollarCostPortfolioButton, createFlexiblePortfolioButton,
      getPortfolioValueButton, getCostBasisButton, savePortfolioButton, retrievePortfolioButton,
      sellStocksButton, buyStocksButton, fractionalInvestmentButton;
  private JProgressBar progressBar;

  private JTextPane displayArea;

  public GUIView(String caption)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    super(caption);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir")+"/image.png");
    setIconImage(icon);
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);

    setMinimumSize(new Dimension(850, 400));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    displayMenu();

    pack();
    setVisible(true);
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
    createDollarCostPortfolioButton = getCustomButton("Create Portfolio Using Dollar-Cost Strategy");
    actionsPanel.add(createDollarCostPortfolioButton);
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
    fractionalInvestmentButton = getCustomButton("Fractional Investment");
    actionsPanel.add(fractionalInvestmentButton);
    progressBar = new JProgressBar();
    actionsPanel.add(progressBar);
    actionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    mainPanel.add(actionsPanel, BorderLayout.WEST);
  }

  @Override
  public void displayInvalidInput() {
    JOptionPane.showMessageDialog(GUIView.this, "Invalid Input", "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void displayCustomText(String customText) {
    JOptionPane.showMessageDialog(GUIView.this, customText, "Information",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void addFeatures(Features features) {
    savePortfolioButton.addActionListener(e -> displayArea.setText("<html><center><h1>"
        + features.savePortfolio() + "</h1></center></html>"));
    getPortfolioValueButton.addActionListener(e -> displayGetPortfolioValueWindow(features));
    getCostBasisButton.addActionListener(e -> displayGetPortfolioCostBasisWindow(features));
    sellStocksButton.addActionListener(e -> displaySellStocksWindow(features));
    buyStocksButton.addActionListener(e -> displayBuyStocksWindow(features));
    createFlexiblePortfolioButton.addActionListener(e -> displayCreateNewFlexibleWindow(features));
    fractionalInvestmentButton.addActionListener(e -> displayFractionInvestmentWindow(features));
    createDollarCostPortfolioButton.addActionListener(
        e -> displayCreateDollarCostAveragingWindow(features));
    retrievePortfolioButton.addActionListener(e -> displayArea.setText("<html><center><h1>"
        + features.retrievePortfolio() + "</h1></center></html>"));
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