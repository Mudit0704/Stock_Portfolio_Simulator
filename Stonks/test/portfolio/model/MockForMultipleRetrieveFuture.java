package portfolio.model;

public class MockForMultipleRetrieveFuture extends MockForRetrieveFuture {
  @Override
  protected String getPath() {
    return "test/test_multiple_future_txn/";
  }
}
