package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains implementation to draw a performance graph (line chart).
 */
public class LineChartView extends ApplicationFrame {

  /**
   * Constructor for initialising the chart with a title.
   * @param applicationTitle the title of the chart.
   */
  protected LineChartView(String applicationTitle) {
    super(applicationTitle);
  }

  protected ChartPanel lineChartCreate(String chartTitle, HashMap<String, Integer> axisForGraph) {
    JFreeChart lineChart = ChartFactory.createLineChart(
            chartTitle,
            "Time ", "Value ($)",
            createDataset(axisForGraph),
            PlotOrientation.VERTICAL,
            true, true, false);

    ChartPanel chartPanel = new ChartPanel(lineChart);
    setContentPane(chartPanel);
    return chartPanel;
  }

  private DefaultCategoryDataset createDataset(HashMap<String, Integer> axisForGraph) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for (Map.Entry<String, Integer> i : axisForGraph.entrySet()) {
      String xVal = i.getKey();
      Integer yVal = i.getValue();
      dataset.addValue(yVal, "value", xVal);
    }
    return dataset;
  }
}
