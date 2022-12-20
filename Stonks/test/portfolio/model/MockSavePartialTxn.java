package portfolio.model;

/**
 * Mock Strategic portfolio class for testing save and retrieve of the stocks involving future
 * transactions.
 */
public class MockSavePartialTxn extends MockForRetrieveFuture {
  @Override
  protected String getPath() {
    return "test/";
  }
}
