package portfolio.view;

import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.Features;
import portfolio.view.guiview.GUIView;

public class MockGUIView extends GUIView {

  public MockGUIView(String caption)
      throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    super(caption);
  }

  @Override
  public void addFeatures(Features features) {

  }
}
