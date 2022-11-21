package portfolio.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.Features;

public class GUIView extends JFrame implements IGUIView {
  private JPanel mainPanel;
  private JScrollPane mainScrollPane;

  public GUIView(String caption)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    super(caption);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(10, 10));
    mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    setSize(800, 800);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    displayMenu();

    pack();
    setVisible(true);
    toFront();
  }

  @Override
  public void displayMenu() {
    JPanel toolBar = new JPanel();
    toolBar.setLayout(new FlowLayout());

    toolBar.add(new JButton("Button 1"));
    toolBar.add(new JButton("Button 2"));
    toolBar.add(new JButton("Button 3"));
    toolBar.add(new JButton("Long-Named Button 4"));
    toolBar.add(new JButton("5"));
    JButton button;
    mainPanel.add(toolBar, BorderLayout.PAGE_START);

    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(10, 20);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    displayPanel.add(scrollPane);
    mainPanel.add(displayPanel, BorderLayout.CENTER);

    button = new JButton("Button 3 (LINE_START)");
    mainPanel.add(button, BorderLayout.LINE_START);

    button = new JButton("Long-Named Button 4 (PAGE_END)");
    mainPanel.add(button, BorderLayout.PAGE_END);

    button = new JButton("5 (LINE_END)");
    mainPanel.add(button, BorderLayout.LINE_END);
  }

  @Override
  public void displayInvalidInput() {

  }

  @Override
  public void displayCustomText(String customText) {

  }

  @Override
  public void addFeatures(Features features) {

  }
}
