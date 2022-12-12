package view;

import controller.Features;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * This class extends the JFrame class and implements the ViewGUI interface and ActionListener
 * class. It has implementations for printing various stock operation menus on the GUI. These
 * include creation and modification using earlier methods, investment strategy and dollar cost
 * averaging, uploading an existing portfolio, retrieving composition, total, cost basis and
 * performance graph of a portfolio.
 */
public class ViewGUIImpl extends JFrame implements ViewGUI, ActionListener {

  Features fObj;
  private JButton addStocksButton;
  private JButton buttonOk;
  private JButton doneButton;
  private JButton backButton;
  private JLabel welcomeLabel;
  private JLabel getPFName;
  private JLabel getTickerSymbol;
  private JLabel getDate;
  private JLabel getNumOfShares;
  private JLabel errorLabel;
  private JLabel getInvtAmt;
  private JLabel getPerc;
  private JLabel getEndDate;
  private JLabel getCommFee;
  private JTextField inPFName;
  private JTextField inDate;
  private JTextField inNumOfShares;
  private JTextField inInvtAmt;
  private JTextField inPerc;
  private JTextField inEndDate;
  private JTextField inInterval;
  private JTextField inCommFee;
  private JFrame getPFDetailsFrame;
  private JFrame createErrorFrame;
  private JFrame modifyPFDetailsFrame;
  private JFrame getSuccessFrame;
  private JFrame portfolioDetailsFrame;
  private JFrame createOptionFrame;
  private JFrame uploadFileFrame;
  private JComboBox tsComboBox;

  /**
   * Constructor for the ViewGUIImpl class which takes the title of the first invisible frame as a
   * parameter.
   *
   * @param caption The title of the frame
   */
  public ViewGUIImpl(String caption) {
    super(caption);
    UIManager.put("Button.font", new Font("Times New Roman", Font.PLAIN, 15));
    UIManager.put("Label.font", new Font("Times New Roman", Font.PLAIN, 15));
    UIManager.put("TextArea.font", new Font("Times New Roman", Font.PLAIN, 15));
  }

  @Override
  public void addFeatures(Features features) {
    fObj = features;
  }

  @Override
  public void getFlexibleMenu() {
    welcomeLabel = new JLabel("<HTML><U>Welcome to Stock Application</U></HTML>");
    welcomeLabel.setForeground(Color.blue);
    welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));

    JLabel labelMainMenu1 = clickableLabelMainMenu(
        " 1. Create flexible portfolio ", "1", this);
    JLabel labelMainMenu2 = clickableLabelMainMenu(
        " 2. Modify portfolio ", "2", this);
    JLabel labelMainMenu3 = clickableLabelMainMenu(
        " 3. Upload a portfolio ", "3", this);
    JLabel labelMainMenu4 = clickableLabelMainMenu(
        " 4. Get composition ", "4", this);
    JLabel labelMainMenu5 = clickableLabelMainMenu(
        " 5. Get cost basis ", "5", this);
    JLabel labelMainMenu6 = clickableLabelMainMenu(
        " 6. Get value of portfolio ", "6", this);
    JLabel labelMainMenu7 = clickableLabelMainMenu(
        " 7. View performance graph ", "7", this);
    JLabel labelMainMenu8 = clickableLabelMainMenu(
        " 8. Rebalance a portfolio ", "8", this);
    JLabel labelMainMenu9 = clickableLabelMainMenu(
        " 9. Exit ", "9", this);
    labelMainMenu9.setForeground(Color.RED);

    Box menuBox = Box.createVerticalBox();
    menuBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(welcomeLabel);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu1);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu2);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu3);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu4);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu5);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu6);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu7);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu8);
    menuBox.add(Box.createRigidArea(new Dimension(120, 20)));
    menuBox.add(labelMainMenu9);

    this.setResizable(false);
    this.add(menuBox);
    this.pack();
    this.setSize(400, 500);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  @Override
  public void createOptions() {

    welcomeLabel = new JLabel("<HTML><U>Portfolio Creation Page</U></HTML>");
    welcomeLabel.setForeground(Color.blue);
    welcomeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));

    JLabel labelCreateMenu1 = clickableLabelSubMenu(
        " 1. Create portfolio by manually selecting the quantity. ", "1");
    JLabel labelCreateMenu2 = clickableLabelSubMenu(
        " 2. Create portfolio by investing money on a specific date. ", "2");
    JLabel labelCreateMenu3 = clickableLabelSubMenu(
        " 3. Create portfolio by dollar cost average strategy. ", "3");

    backButton = new JButton("Back");

    Box menuBox = Box.createVerticalBox();
    menuBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    menuBox.add(welcomeLabel);
    menuBox.add(Box.createRigidArea(new Dimension(0, 20)));
    menuBox.add(labelCreateMenu1);
    menuBox.add(Box.createRigidArea(new Dimension(0, 20)));
    menuBox.add(labelCreateMenu2);
    menuBox.add(Box.createRigidArea(new Dimension(0, 20)));
    menuBox.add(labelCreateMenu3);
    menuBox.add(Box.createRigidArea(new Dimension(0, 20)));
    menuBox.add(backButton);

    createOptionFrame = frameSetter("Create page", menuBox);

    backButton(backButton, createOptionFrame, this);

    //    backButton.addActionListener(evt -> {
    //      createOptionFrame.dispose();
    //      this.setVisible(true);
    //    });
  }

  @Override
  public void getCreatePage(ArrayList<String> tickerSymbolList) {

    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   "); //new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    pfNameBox.add(getPFName);
    pfNameBox.add(inPFName);

    Box tsBox = Box.createHorizontalBox();
    tsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getTickerSymbol = new JLabel("Ticker symbol   ");
    tsComboBox = new JComboBox(tickerSymbolList.toArray());

    tsBox.add(getTickerSymbol);
    tsBox.add(tsComboBox);

    Box nsBox = Box.createHorizontalBox();
    nsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getNumOfShares = new JLabel("Number of shares   ");
    inNumOfShares = new JTextField(10);
    nsBox.add(getNumOfShares);
    nsBox.add(inNumOfShares);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Date (yyyy-MM-dd)   ");
    inDate = new JTextField(10);
    dateBox.add(getDate);
    dateBox.add(inDate);

    Box commFeeBox = Box.createHorizontalBox();
    commFeeBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getCommFee = new JLabel("Commission Fee    ");
    inCommFee = new JTextField(10);
    commFeeBox.add(getCommFee);
    commFeeBox.add(inCommFee);

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    addStocksButton = new JButton("Add stocks");
    doneButton = doneButton("Done");

    submitExitBox.add(addStocksButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(30, 0)));
    submitExitBox.add(doneButton);

    Box createBox = Box.createVerticalBox();
    createBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    createBox.add(pfNameBox);
    createBox.add(tsBox);
    createBox.add(nsBox);
    createBox.add(dateBox);
    createBox.add(commFeeBox);
    createBox.add(submitExitBox);

    getPFDetailsFrame = frameSetter("Enter details", createBox);

    addStocksButton.addActionListener(evt ->
        fObj.echoStockDetails(inPFName.getText(), (String) tsComboBox.getSelectedItem(),
            inNumOfShares.getText(), inCommFee.getText(), inDate.getText(),
            inPFName.isEditable()));

    backButton(doneButton, getPFDetailsFrame, createOptionFrame);

  }

  @Override
  public void stockModifiedSuccess() {

    Box successBox = Box.createVerticalBox();

    Box doneBox = Box.createHorizontalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Portfolio modified successfully. Click 'Back' if you want to "
        + "buy/ sell more stocks or 'Done' to save the portfolio.");
    doneBox.add(errorLabel);

    Box submitExitBox = Box.createHorizontalBox();
    backButton = new JButton("Back   ");
    doneButton = doneButton("Done");
    submitExitBox.add(backButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 20)));
    submitExitBox.add(doneButton);

    successBox.add(doneBox);
    successBox.add(submitExitBox);

    getSuccessFrame = frameSetter("Success", successBox);

    backButton.addActionListener(evt -> {
      getSuccessFrame.dispose();
      inPFName.setEditable(false);
      inNumOfShares.setText("");
      inDate.setText("");
      inCommFee.setText("");
    });

    doneButton.addActionListener(evt -> {
      getSuccessFrame.dispose();
      getPFDetailsFrame.dispose();
    });

  }

  @Override
  public void errorPfName() {
    createErrorFrame = errorFrameSetter("Invalid portfolio name. "
        + "If you want to modify an existing"
        + " portfolio please select option 2 from the menu.");
    clearErrorFields(inPFName);
  }

  @Override
  public void errorPfName2() {
    createErrorFrame = errorFrameSetter("Invalid portfolio name. Portfolio doesn't exist.");
    clearErrorFields(inPFName);
  }


  @Override
  public void errorNs() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Number of shares entered is not a "
        + "valid input. It should be a positive integer only");
    clearErrorFields(inNumOfShares);
  }

  @Override
  public void errorCommFee() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Invalid commission fee.");
    clearErrorFields(inCommFee);
  }

  @Override
  public void errorInvtAmt() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Investment amount is invalid. ");
    clearErrorFields(inInvtAmt);
  }

  @Override
  public void errorDate() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Either market is closed or date "
        + "format is incorrect. System accepts yyyy-MM-dd format only.");
    clearErrorFields(inDate);
    clearErrorFields(inEndDate);
  }

  @Override
  public void errorDateMarketClosed() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Either market is closed or "
        + "date format is incorrect. System accepts yyyy-MM-dd format only.");
    clearErrorFields(inDate);
  }

  @Override
  public void getGraphPage() {

    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    inPFName.setSize(10, 10);
    pfNameBox.add(getPFName);
    pfNameBox.add(inPFName);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Start Date   ");
    inDate = new JTextField(10);
    dateBox.add(getDate);
    dateBox.add(inDate);

    Box dateEndBox = Box.createHorizontalBox();
    dateEndBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getEndDate = new JLabel("End Date   ");
    inEndDate = new JTextField(10);
    dateEndBox.add(getEndDate);
    dateEndBox.add(inEndDate);

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    doneButton = doneButton("Done");
    backButton = new JButton("Back");
    submitExitBox.add(doneButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
    submitExitBox.add(backButton);

    Box graphBox = Box.createVerticalBox();
    graphBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    graphBox.add(pfNameBox);
    graphBox.add(dateBox);
    graphBox.add(dateEndBox);
    graphBox.add(submitExitBox);

    JFrame getGraphDetailsFrame = frameSetter("Graph details Page", graphBox);

    doneButton.addActionListener(evt ->
        fObj.getGraph(inPFName.getText(), inDate.getText(), inEndDate.getText()));

    backButton(backButton, getGraphDetailsFrame, this);

  }

  @Override
  public void getDcaPage(ArrayList<String> tickerSymbolList) {

    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    pfNameBox.add(getPFName);
    pfNameBox.add(inPFName);

    Box amtBox = Box.createHorizontalBox();
    amtBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getInvtAmt = new JLabel("Investment Amount ($)   ");
    inInvtAmt = new JTextField(10);
    amtBox.add(getInvtAmt);
    amtBox.add(inInvtAmt);

    Box commFeeBox = Box.createHorizontalBox();
    commFeeBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getCommFee = new JLabel("Commission Fee    ");
    inCommFee = new JTextField(10);
    commFeeBox.add(getCommFee);
    commFeeBox.add(inCommFee);

    ///////////
    Box tsBox = Box.createHorizontalBox();
    tsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getTickerSymbol = new JLabel("Ticker symbol   ");
    tsComboBox = new JComboBox(tickerSymbolList.toArray());

    tsBox.add(getTickerSymbol);
    tsBox.add(tsComboBox);

    Box percBox = Box.createHorizontalBox();
    percBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPerc = new JLabel("Percentage   ");
    inPerc = new JTextField(10);
    percBox.add(getPerc);
    percBox.add(inPerc);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Start Date   ");
    inDate = new JTextField(10);
    inDate.setEditable(false);
    getEndDate = new JLabel("End Date   ");
    inEndDate = new JTextField(10);
    inEndDate.setEditable(false);

    dateBox.add(getDate);
    dateBox.add(Box.createRigidArea(new Dimension(10, 20)));
    dateBox.add(inDate);
    dateBox.add(Box.createRigidArea(new Dimension(10, 20)));
    dateBox.add(getEndDate);
    dateBox.add(Box.createRigidArea(new Dimension(20, 20)));
    dateBox.add(inEndDate);

    Box intervalBox = Box.createHorizontalBox();
    intervalBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    JLabel getInterval = new JLabel("Interval (in days)   ");
    inInterval = new JTextField(10);
    inInterval.setEditable(false);
    intervalBox.add(getInterval);
    intervalBox.add(inInterval);

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    addStocksButton = new JButton("Add Stocks");
    doneButton = doneButton("Done");
    doneButton.setEnabled(false);
    backButton = new JButton("Back");
    submitExitBox.add(addStocksButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
    submitExitBox.add(doneButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
    submitExitBox.add(backButton);

    Box createDCABox = Box.createVerticalBox();
    createDCABox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    createDCABox.add(pfNameBox);
    createDCABox.add(amtBox);
    createDCABox.add(commFeeBox);
    createDCABox.add(tsBox);
    createDCABox.add(percBox);
    createDCABox.add(dateBox);
    createDCABox.add(intervalBox);
    createDCABox.add(submitExitBox);

    JFrame getDCADetailsFrame = frameSetter("DCA Page", createDCABox);
    addStocksButton.addActionListener(evt ->
        fObj.createDcaPortfolio(inPFName.getText(), inInvtAmt.getText(),
            inCommFee.getText(), (String) tsComboBox.getSelectedItem(), inPerc.getText(),
            inDate.getText(), inEndDate.getText(), inInterval.getText(),
            inPFName.isEditable(), tsComboBox.isEnabled()));

    doneButton.addActionListener(evt ->
        fObj.createDcaPortfolio(inPFName.getText(), inInvtAmt.getText(),
            inCommFee.getText(), (String) tsComboBox.getSelectedItem(), inPerc.getText(),
            inDate.getText(), inEndDate.getText(), inInterval.getText(),
            inPFName.isEditable(), tsComboBox.isEnabled()));

    backButton.addActionListener(evt -> getDCADetailsFrame.dispose());

  }


  @Override
  public void dcaStockAdded() {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Stock added successfully.");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      inPFName.setEditable(false);
      inInvtAmt.setEditable(false);
      inCommFee.setEditable(false);
      backButton.setEnabled(false);
      //      inTickerSymbol.setText("");
      inPerc.setText("");
    });

  }

  @Override
  public void dcaInvested100() {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Stock added successfully. You've invested 100% of your money. "
        + "You can't invest in more stocks hereafter.");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      inPFName.setEditable(false);
      inInvtAmt.setEditable(false);
      inCommFee.setEditable(false);
      //      inTickerSymbol.setText("");
      inPerc.setText("");
      tsComboBox.setEnabled(false);
      //      inTickerSymbol.setEditable(false);
      inPerc.setEditable(false);
      inDate.setEditable(true);
      inEndDate.setEditable(true);
      inInterval.setEditable(true);
      addStocksButton.setEnabled(false);
      doneButton.setEnabled(true);
      backButton.setEnabled(false);
    });

  }

  @Override
  public void errorPerc() {
    createErrorFrame = errorFrameSetter("" + "<HTML>" + "Invalid percentage. "
        + "The reasons could be one of these <br />"
        + "1. Percentage is not a number (Ex. 50) <br />"
        + "2. Sum of percentages exceed 100." + "</HTML>");
    clearErrorFields(inPerc);
  }

  @Override
  public void getPfNameAndDate() {

    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    pfNameBox.add(getPFName);
    pfNameBox.add(Box.createRigidArea(new Dimension(20, 20)));
    pfNameBox.add(inPFName);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Date (yyyy-MM-dd)   ");
    inDate = new JTextField(10);
    dateBox.add(getDate);
    dateBox.add(Box.createRigidArea(new Dimension(20, 20)));
    dateBox.add(inDate);

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    doneButton = doneButton("Submit");

    backButton = new JButton("Back ");
    submitExitBox.add(doneButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 20)));
    submitExitBox.add(backButton);

    Box pfBox = Box.createVerticalBox();
    pfBox.add(pfNameBox);
    pfBox.add(dateBox);
    pfBox.add(submitExitBox);

    JFrame createPageFrame = frameSetter("Output", pfBox);

    backButton(backButton, createPageFrame, this);

    doneButton.addActionListener(evt -> {
      fObj.echoPfNameWithDate(inPFName.getText(), inDate.getText());
    });

  }

  @Override
  public void outputPage(String output) {

    Box portfolioDetailsBox = Box.createVerticalBox();
    portfolioDetailsBox.setBorder(BorderFactory.createEmptyBorder(
        10, 10, 20, 20));

    JTextArea result = new JTextArea("Result : \n" + output);
    result.setEditable(false);
    portfolioDetailsBox.add(result);
    buttonOk = new JButton("Ok");
    portfolioDetailsBox.add(Box.createRigidArea(new Dimension(0, 20)));
    portfolioDetailsBox.add(buttonOk);

    portfolioDetailsFrame = frameSetter("Output", portfolioDetailsBox);

    buttonOk.addActionListener(evt -> {
      portfolioDetailsFrame.dispose();
      inPFName.setText("");
      inDate.setText("");
    });
  }

  @Override
  public void errorInterval() {
    createErrorFrame = errorFrameSetter("!!! ERROR. Interval entered is invalid. "
        + "It should be in number of days. (Ex. 50)");
    clearErrorFields(inInterval);
  }

  @Override
  public void portfolioSuccess(String action) {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Portfolio " + action + " successfully");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      inDate.setEditable(false);
      inEndDate.setEditable(false);
      inInterval.setEditable(false);
      doneButton.setEnabled(false);
      backButton.setEnabled(true);
    });
  }

  @Override
  public void uploadFile() {

    Box uploadBox = Box.createHorizontalBox();
    uploadBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    JLabel getFilePath = new JLabel("Upload your portfolio ");
    JButton uploadButton = new JButton("Choose file");

    JButton backButton = new JButton("Back");

    uploadBox.add(getFilePath);
    uploadBox.add(Box.createRigidArea(new Dimension(10, 20)));
    uploadBox.add(uploadButton);
    uploadBox.add(Box.createRigidArea(new Dimension(10, 20)));
    uploadBox.add(backButton);

    uploadFileFrame = frameSetter("Upload", uploadBox);

    uploadButton.addActionListener(evt -> {
      fObj.getFilePath(getFilePath());
    });

    backButton(backButton, uploadFileFrame, this);
  }

  @Override
  public void fileUploadSuccess() {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("File uploaded successfully. You can perform operations on this.");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      uploadFileFrame.dispose();
    });
  }

  @Override
  public void errorFileUpload() {

    Box errorBox = Box.createVerticalBox();
    errorBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("!!! ERROR. File uploaded is of invalid format. "
        + "System accepts .xml files only");
    errorBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    errorBox.add(Box.createRigidArea(new Dimension(0, 20)));
    errorBox.add(buttonOk);

    createErrorFrame = frameSetter("Error", errorBox);

    buttonOk.addActionListener(evt -> createErrorFrame.dispose());
  }

  @Override
  public void getModifyPage() {

    JLabel labelSubMenu1 = clickableLabelSubMenu2(
        " 1. Buy stocks by entering quantity of stocks. ", "1");
    JLabel labelSubMenu2 = clickableLabelSubMenu2(
        " 2. Sell stocks. ", "2");
    JLabel labelSubMenu3 = clickableLabelSubMenu2(
        " 3. Buy stocks by investing money on a specific date. ", "3");
    JLabel labelSubMenu4 = clickableLabelSubMenu2(
        " 4. Buy stocks with dollar cost average strategy. ", "4");
    backButton = new JButton("Back");

    Box modifyBox = Box.createVerticalBox();
    modifyBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    modifyBox.add(labelSubMenu1);
    modifyBox.add(Box.createRigidArea(new Dimension(0, 20)));
    modifyBox.add(labelSubMenu2);
    modifyBox.add(Box.createRigidArea(new Dimension(0, 20)));
    modifyBox.add(labelSubMenu3);
    modifyBox.add(Box.createRigidArea(new Dimension(0, 20)));
    modifyBox.add(labelSubMenu4);
    modifyBox.add(Box.createRigidArea(new Dimension(0, 20)));
    modifyBox.add(backButton);

    modifyPFDetailsFrame = frameSetter("Modify Page", modifyBox);

    backButton(backButton, modifyPFDetailsFrame, this);

  }


  @Override
  public void modifyStocks(ArrayList<String> tickerSymbolList) {
    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    pfNameBox.add(getPFName);
    pfNameBox.add(inPFName);

    Box tsBox = Box.createHorizontalBox();
    tsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getTickerSymbol = new JLabel("Ticker symbol   ");
    tsComboBox = new JComboBox(tickerSymbolList.toArray());
    tsBox.add(getTickerSymbol);
    tsBox.add(tsComboBox);

    Box nsBox = Box.createHorizontalBox();
    nsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getNumOfShares = new JLabel("Number of shares   ");
    inNumOfShares = new JTextField(10);
    nsBox.add(getNumOfShares);
    nsBox.add(inNumOfShares);

    Box commFeeBox = Box.createHorizontalBox();
    commFeeBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getCommFee = new JLabel("Commission Fee    ");
    inCommFee = new JTextField(10);
    commFeeBox.add(getCommFee);
    commFeeBox.add(inCommFee);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Date (yyyy-MM-dd)   ");
    inDate = new JTextField(10);
    dateBox.add(getDate);
    dateBox.add(inDate);

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    addStocksButton = new JButton("Modify");
    doneButton = doneButton("Done");

    submitExitBox.add(addStocksButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(30, 0)));
    submitExitBox.add(doneButton);

    Box createBox = Box.createVerticalBox();
    createBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    createBox.add(pfNameBox);
    createBox.add(tsBox);
    createBox.add(nsBox);
    createBox.add(commFeeBox);
    createBox.add(dateBox);
    createBox.add(submitExitBox);

    getPFDetailsFrame = frameSetter("Modify Stocks", createBox);

    addStocksButton.addActionListener(evt -> fObj.buyOrSellStocks(inPFName.getText(),
        (String) tsComboBox.getSelectedItem(), inNumOfShares.getText(), inCommFee.getText(),
        inDate.getText()));

    backButton(doneButton, getPFDetailsFrame, modifyPFDetailsFrame);
    doneButton.addActionListener(evt -> {
      getPFDetailsFrame.dispose();
      modifyPFDetailsFrame.setVisible(true);
    });
  }

  @Override
  public void errorSell() {
    createErrorFrame = errorFrameSetter("!!! ERROR : You are trying to sell more shares "
        + "of this stock than you own. ");
    clearErrorFields(inNumOfShares);
  }

  @Override
  public void getCreateByInvtOncePage(ArrayList<String> tickerSymbolList) {

    Box pfNameBox = Box.createHorizontalBox();
    pfNameBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPFName = new JLabel("Portfolio name   ");
    inPFName = new JTextField(10);
    pfNameBox.add(getPFName);
    pfNameBox.add(inPFName);

    Box amtBox = Box.createHorizontalBox();
    amtBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getInvtAmt = new JLabel("Investment Amount ($)   ");
    inInvtAmt = new JTextField(10);
    amtBox.add(getInvtAmt);
    amtBox.add(inInvtAmt);

    Box commFeeBox = Box.createHorizontalBox();
    commFeeBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getCommFee = new JLabel("Commission fee   ");
    inCommFee = new JTextField(10);
    commFeeBox.add(getCommFee);
    commFeeBox.add(inCommFee);

    Box tsBox = Box.createHorizontalBox();
    tsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getTickerSymbol = new JLabel("Ticker symbol   ");
    tsComboBox = new JComboBox(tickerSymbolList.toArray());

    tsBox.add(getTickerSymbol);
    tsBox.add(tsComboBox);

    Box percBox = Box.createHorizontalBox();
    percBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getPerc = new JLabel("Percentage   ");
    inPerc = new JTextField(10);
    percBox.add(getPerc);
    percBox.add(inPerc);

    Box dateBox = Box.createHorizontalBox();
    dateBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    getDate = new JLabel("Date(yyyy-MM-dd)   ");
    inDate = new JTextField(10);
    inDate.setEditable(false);
    dateBox.add(getDate);
    dateBox.add(Box.createRigidArea(new Dimension(10, 20)));
    dateBox.add(inDate);

    inEndDate = new JTextField("");     //
    inInterval = new JTextField("");    //

    Box submitExitBox = Box.createHorizontalBox();
    submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    addStocksButton = new JButton("Add Stocks");
    doneButton = doneButton("Done");
    doneButton.setEnabled(false);
    backButton = new JButton("Back");
    submitExitBox.add(addStocksButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
    submitExitBox.add(doneButton);
    submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
    submitExitBox.add(backButton);

    Box createDCABox = Box.createVerticalBox();
    createDCABox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    createDCABox.add(pfNameBox);
    createDCABox.add(amtBox);
    createDCABox.add(commFeeBox);
    createDCABox.add(tsBox);
    createDCABox.add(percBox);
    createDCABox.add(dateBox);
    createDCABox.add(submitExitBox);

    JFrame getInvtOnceFrame = frameSetter("Enter details", createDCABox);

    addStocksButton.addActionListener(evt ->
        fObj.createInvtPortfolio(inPFName.getText(), inInvtAmt.getText(),
            inCommFee.getText(), (String) tsComboBox.getSelectedItem(), inPerc.getText(),
            inDate.getText(), inPFName.isEditable(), tsComboBox.isEnabled()));

    doneButton.addActionListener(evt -> {
      fObj.createInvtPortfolio(inPFName.getText(), inInvtAmt.getText(),
          inCommFee.getText(), (String) tsComboBox.getSelectedItem(), inPerc.getText(),
          inDate.getText(), inPFName.isEditable(), tsComboBox.isEnabled());
    });

    backButton.addActionListener(evt -> getInvtOnceFrame.dispose());

  }

  @Override
  public void drawGraph(HashMap<String, Integer> axisForGraph) {

    LineChartView chart = new LineChartView("Performance Graph");

    JFrame graphFrame = new JFrame("Graph");
    graphFrame.add(chart.lineChartCreate(
        "Performance graph of portfolio (Time v/s Value($))", axisForGraph));
    graphFrame.pack();
    graphFrame.setLocationRelativeTo(null);
    graphFrame.setDefaultCloseOperation(graphFrame.DISPOSE_ON_CLOSE);
    graphFrame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == tsComboBox) {
      System.out.println(tsComboBox);
    }
  }

  /**
   * This method sets the error pop up frame for all types of errors.
   *
   * @param errorMsg error message to be displayed.
   * @return a frame with the error message.
   */
  private JFrame errorFrameSetter(String errorMsg) {

    Box errorBox = Box.createVerticalBox();
    errorBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));

    errorLabel = new JLabel(errorMsg);
    errorBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    errorBox.add(Box.createRigidArea(new Dimension(0, 20)));
    errorBox.add(buttonOk);

    return frameSetter("Error", errorBox);
  }

  /**
   * This method defines what the 'ok' button on each error frame should do. The ok button should
   * dispose the error frame and reset the value of the corresponding text field.
   *
   * @param clearField the jtext field to be created.
   */
  private void clearErrorFields(JTextField clearField) {
    buttonOk.addActionListener(evt -> {
      createErrorFrame.dispose();
      clearField.setText("");
    });
  }

  /**
   * This method sets the frame size and the location on the screen of all the frames that would be
   * visible to the user.
   *
   * @param frameTitle The title of the frame
   * @param content    The components to be added to the frame
   * @return a frame with the content to be displayed.
   */
  private JFrame frameSetter(String frameTitle, Box content) {
    JFrame frameName = new JFrame(frameTitle);
    frameName.setResizable(false);
    frameName.add(content);
    frameName.pack();
    //frameName.setSize(400, 500);
    frameName.setLocationRelativeTo(null);
    frameName.setDefaultCloseOperation(frameName.DISPOSE_ON_CLOSE);
    frameName.setVisible(true);
    return frameName;
  }

  /**
   * Sets the display type of 'done' button used on almost every page.
   *
   * @param buttonText the text in the button.
   * @return a button
   */
  private JButton doneButton(String buttonText) {
    JButton done = new JButton(buttonText);
    done.setBackground(Color.black);
    done.setForeground(Color.white);
    return done;
  }

  /**
   * Sets the display type of 'back' button used on almost every page.
   *
   * @param button          the back button.
   * @param disposeFrame    frame to be disposed on clicking the frame
   * @param setVisibleFrame the frame to be visible
   * @return a button
   */
  private void backButton(JButton button, JFrame disposeFrame, JFrame setVisibleFrame) {
    button.addActionListener(evt -> {
      disposeFrame.dispose();
      setVisibleFrame.setVisible(true);
    });
  }

  /**
   * Get the file path from the user local system.
   *
   * @return file path in String format
   */
  private String getFilePath() {
    JFileChooser j = new JFileChooser();
    j.setMultiSelectionEnabled(false);
    j.setFileSelectionMode(JFileChooser.FILES_ONLY);
    String filePath = "";
    if (j.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      java.io.File f = j.getSelectedFile();
      filePath = f.getPath();
    }
    return filePath;
  }

  /**
   * Implementation method to create hyperlinks for the labels inside the main menu.
   *
   * @param labelText text for which hyperlinks to be created
   * @param menuNum   number for the menu options
   * @param mainFrame the current frame
   * @return JLabel as hyperlinks
   */
  private JLabel clickableLabelMainMenu(String labelText, String menuNum, JFrame mainFrame) {
    JLabel label = new JLabel(labelText);
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    label.setFont(new Font("Times New Roman", Font.PLAIN, 17));
    label.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        label.setText("<html><a href=''>" + labelText + "</a></html>");
      }

      public void mouseClicked(MouseEvent e) {
        fObj.echoTextTyped(menuNum);
        mainFrame.setVisible(false);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        label.setText(labelText);
      }
    });
    return label;
  }

  /**
   * Implementation method to create hyperlinks for the labels inside the sub menu.
   *
   * @param labelText text for which hyperlinks to be created
   * @param menuNum   number for the menu options
   * @return JLabel as hyperlinks
   */
  private JLabel clickableLabelSubMenu(String labelText, String menuNum) {
    JLabel label = new JLabel(labelText);
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    label.setFont(new Font("Times New Roman", Font.PLAIN, 17));
    label.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        label.setText("<html><a href=''>" + labelText + "</a></html>");
      }

      public void mouseClicked(MouseEvent e) {
        if (Objects.equals(menuNum, "1")) {
          fObj.createBySelf();
        } else if (Objects.equals(menuNum, "2")) {
          fObj.createByInvtOnce();
        } else if (Objects.equals(menuNum, "3")) {
          fObj.createByDca();
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
        label.setText(labelText);
      }
    });
    return label;
  }

  /**
   * This method handles the clickable labels present in the sub menu for 'Modifying' a portfolio.
   *
   * @param labelText The text of the label
   * @param menuNum   The serial number of the menu
   * @return a clickable label which will redirect the user to appropriate screens.
   */
  private JLabel clickableLabelSubMenu2(String labelText, String menuNum) {
    JLabel label = new JLabel(labelText);
    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    label.setFont(new Font("Times New Roman", Font.PLAIN, 17));
    label.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        label.setText("<html><a href=''>" + labelText + "</a></html>");
      }

      public void mouseClicked(MouseEvent e) {
        if (Objects.equals(menuNum, "1")) {
          fObj.modifyStockBuy();
        } else if (Objects.equals(menuNum, "2")) {
          fObj.modifyStockSell();
          modifyPFDetailsFrame.setVisible(false);
        } else if (Objects.equals(menuNum, "3")) {
          fObj.modifyInvtOnce();
        } else if (Objects.equals(menuNum, "4")) {
          fObj.modifyStocksDca();
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
        label.setText(labelText);
      }
    });
    return label;
  }

  @Override
  public void rebalancePortfolio(String pfName, String date) {
    List<String> portfolioStocks = fObj.getPortfolioStocks(pfName, date);

    if (!portfolioStocks.isEmpty()) {
      Box tsBox = Box.createHorizontalBox();
      tsBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
      getTickerSymbol = new JLabel("Ticker symbol   ");
      tsComboBox = new JComboBox(portfolioStocks.toArray());

      tsBox.add(getTickerSymbol);
      tsBox.add(tsComboBox);

      Box percBox = Box.createHorizontalBox();
      percBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
      getPerc = new JLabel("Percentage   ");
      inPerc = new JTextField(10);
      percBox.add(getPerc);
      percBox.add(inPerc);

      Box submitExitBox = Box.createHorizontalBox();
      submitExitBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
      addStocksButton = new JButton("Add Stocks");
      doneButton = doneButton("Done");
      doneButton.setEnabled(false);
      backButton = new JButton("Back");
      submitExitBox.add(addStocksButton);
      submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
      submitExitBox.add(doneButton);
      submitExitBox.add(Box.createRigidArea(new Dimension(20, 0)));
      submitExitBox.add(backButton);

      Box createRebalanceBox = Box.createVerticalBox();
      createRebalanceBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
      createRebalanceBox.add(tsBox);
      createRebalanceBox.add(percBox);
      createRebalanceBox.add(submitExitBox);

      addStocksButton.addActionListener(evt -> {
        try {
          if (portfolioStocks.size() != 1 && tsComboBox.getItemCount() != 1
              && Double.parseDouble(inPerc.getText()) >= 100d) {
            errorPerc();
          } else {
            fObj.addStockWeightsForRebalance((String) tsComboBox.getSelectedItem(),
                inPerc.getText());
            tsComboBox.removeItem(tsComboBox.getSelectedItem());
          }
        } catch (NumberFormatException e) {
          errorPerc();
        }

      });

      JFrame createPageFrame = frameSetter("Rebalance a portfolio", createRebalanceBox);

      doneButton.addActionListener(evt -> {
        fObj.rebalancePortfolio(pfName, date);
        createPageFrame.dispose();
        inPFName.setText("");
        inDate.setText("");
      });

      backButton.addActionListener(evt -> {
        createPageFrame.dispose();
      });
    }
  }

  @Override
  public void rebalanceDone100() {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("You've added 100% of weightage"
        + "for stocks rebalancing. You can't add more stock weightage hereafter.");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      inPerc.setText("");
      tsComboBox.setEnabled(false);
      inPerc.setEditable(false);
      addStocksButton.setEnabled(false);
      doneButton.setEnabled(true);
      backButton.setEnabled(false);
    });

  }

  @Override
  public void rebalanceStockAdded() {

    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Stock added successfully.");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
      backButton.setEnabled(false);
      inPerc.setText("");
    });

  }

  @Override
  public void rebalanceSuccess() {
    Box successBox = Box.createVerticalBox();
    successBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
    errorLabel = new JLabel("Portfolio rebalanced successfully");
    successBox.add(errorLabel);

    buttonOk = new JButton("Ok");
    successBox.add(Box.createRigidArea(new Dimension(0, 20)));
    successBox.add(buttonOk);

    getSuccessFrame = frameSetter("Success", successBox);

    buttonOk.addActionListener(evt -> {
      getSuccessFrame.dispose();
    });
  }

  @Override
  public void displayNoStockAvailableWindow() {
    createErrorFrame = errorFrameSetter("No stocks available at this date.");
    clearErrorFields(inPFName);
    clearErrorFields(inDate);
  }
}

