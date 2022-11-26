package portfolio.view.guiview;

import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JDialog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class PerformanceLineChart extends JDialog {

  private static final long serialVersionUID = 1L;

  public PerformanceLineChart(String title, Map<LocalDate, Double> data) {
    DefaultCategoryDataset dataset = createDataset(data);
    JFreeChart chart = ChartFactory.createLineChart(
        "Portfolio Performance",
        "Date",
        "Portfolio Value",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        false,
        false
    );

    CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
    axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
    chart.getCategoryPlot().setDomainGridlinesVisible(true);
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
  }

  private DefaultCategoryDataset createDataset(Map<LocalDate, Double> data) {
    String series1 = "Performance";
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    for(Entry<LocalDate, Double> entry : data.entrySet()) {
      dataset.addValue(entry.getValue(), series1, entry.getKey().toString());
    }

    return dataset;
  }
}