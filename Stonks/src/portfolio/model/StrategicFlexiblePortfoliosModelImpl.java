package portfolio.model;

import java.time.LocalDate;
import java.util.Map;

public class StrategicFlexiblePortfoliosModelImpl extends FlexiblePortfoliosModel
    implements IStrategicFlexiblePortfolioModel {

  @Override
  public void createStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
      LocalDate date, LocalDate endDate) {

  }

  @Override
  public void investStrategicPortfolio(Map<String, Double> stockProportions, Double totalAmount,
    int portfolioId, LocalDate date) {

  }
}
