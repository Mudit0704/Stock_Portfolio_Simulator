package portfolio.model;

/**
 * Mock Strategic portfolio class for testing save and retrieve of the strategic portfolio class.
 */
public class MockForStrategicFlexiblePortfoliosModel extends
    StrategicFlexiblePortfoliosModel {
  @Override
  protected String getPath() {
    return "test/";
  }

}
