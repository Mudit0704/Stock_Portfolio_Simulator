package portfolio.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents the operations for any service making API calls.
 */
public interface IService {

  /**
   * Method to make API query calls.
   *
   * @param queryString the API query to be passed.
   * @return InputStream object for the result returned.
   * @throws IOException
   */
  InputStream queryAPI(String queryString) throws IOException;
}