package portfolio.view.guiview;

import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import portfolio.controller.Features;

abstract class AbstractCommandHandlers implements CommandHandler {
  JTextArea resultArea;
  Features features;
  JProgressBar progressBar;
  JFrame mainFrame;

  AbstractCommandHandlers(JTextArea resultArea, Features features,
      JProgressBar progressBar, JFrame mainFrame) {
    this.resultArea = resultArea;
    this.features = features;
    this.progressBar = progressBar;
    this.mainFrame = mainFrame;
  }

  JDialog getUserInputDialog(String title) {
    JDialog userInputDialog = new JDialog(mainFrame, title);
    userInputDialog.setModalityType(ModalityType.APPLICATION_MODAL);
    userInputDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    return userInputDialog;
  }

  JPanel getAvailablePortfoliosDisplay(Features features) {
    JPanel displayPanel = new JPanel(new GridLayout(1, 1));
    JTextArea displayArea = new JTextArea(5, 20);
    displayArea.setEditable(false);
    displayArea.setLineWrap(true);
    JScrollPane scrollPane = new JScrollPane(displayArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    displayPanel.add(scrollPane);
    displayArea.setText(features.getAvailablePortfolios());
    return displayPanel;
  }
}
