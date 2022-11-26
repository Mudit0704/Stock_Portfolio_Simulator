package portfolio.model;

public class MockSavePartialTxn extends MockForRetrieveFuture {
  @Override
  protected String getPath() {
    return "test/test_save_future_txn/";
  }
}
