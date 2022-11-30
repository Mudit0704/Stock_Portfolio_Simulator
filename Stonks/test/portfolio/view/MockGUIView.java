package portfolio.view;

import javax.swing.UnsupportedLookAndFeelException;
import portfolio.controller.IFeatures;
import portfolio.view.guiview.GUIView;

/**
 * A Mock GUI view class that displays dummy outputs to help facilitate testing the controller.
 */
public class MockGUIView extends GUIView {

  /**
   * Constructs an object of MockGUIView and initializes its members.
   *
   * @param title the title to be used by mock view window
   * @throws UnsupportedLookAndFeelException if lnf.isSupportedLookAndFeel() is false
   * @throws ClassNotFoundException          if the LookAndFeel class could not be found
   * @throws InstantiationException          if a new instance of the class couldn't be created
   * @throws IllegalAccessException          if the class or initializer isn't accessible
   */
  public MockGUIView(String title) throws UnsupportedLookAndFeelException, ClassNotFoundException,
      InstantiationException, IllegalAccessException {
    super(title);
  }

  @Override
  public void addFeatures(IFeatures features) {
    //Overriden addFeatures method to avoid launching of UI while testing.
  }
}
