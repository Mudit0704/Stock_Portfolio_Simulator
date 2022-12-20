package portfolio.model;

/**
 * This class represents a mock flexible portfolio model to test the model.
 */
public class MockModelForFlexiPortfolio extends FlexiblePortfoliosModel {

  @Override
  protected String getPath() {
    return "test/test_model/";
  }

}
