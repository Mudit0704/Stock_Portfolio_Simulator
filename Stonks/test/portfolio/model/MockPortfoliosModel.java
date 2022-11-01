package portfolio.model;

/**
 * This class represents a mock portfolio to test the model. This passes on a mock stock service
 * object to the model and inherits the methods of model as it is for the purpose of testing.
 */
public class MockPortfoliosModel extends PortfoliosModel {

  /**
   * Constructor that takes a mocked stock service object for the purpose of setting mock service
   * to test the model.
   *
   * @param stockService the stock service object to be set for testing model.
   */
  public MockPortfoliosModel(IStockService stockService) {
    super(stockService);
  }


}