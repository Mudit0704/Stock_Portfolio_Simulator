package portfolio.model;

/**
 * Mock Strategic portfolio class for testing retrieval of the stocks involving partially executed
 * transactions.
 */
public class MockForRetrieveFuture extends
    MockForStrategicFlexiblePortfoliosModel {

  @Override
  protected String getPath() {
    return "test/test_future_txn/";
  }
}
