import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GraphController {

    @FXML
    public LineChart<Number, Number> lineChart;

    private CalculatorController calculatorController;

    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    public void drawGraph(Fraction a, Fraction b, Fraction c) {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = " + a + "x² + " + b + "x +" + c);

        double vertex = -b.doubleValue() / (2 * a.doubleValue());
        Double root1 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();
        Double root2 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();

        for(double x = vertex - 10; x <= vertex + 10; x+=1) {
            double y = a.doubleValue() * x * x + b.doubleValue() * x + c.doubleValue();
            series.getData().add(new XYChart.Data<>(x, y));
        }

        NumberAxis xAxis = (NumberAxis)lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis)lineChart.getYAxis();

        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        xAxis.setLowerBound(vertex - 10);
        xAxis.setUpperBound(vertex + 10);

        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(10);
        if(a.doubleValue() < 0) {
            yAxis.setLowerBound(getYValue(a, b, c, vertex - 10) - 10);
            yAxis.setUpperBound(getYValue(a, b, c, vertex) + 10);
        } else {
            yAxis.setLowerBound(getYValue(a, b, c, vertex) - 10);
            yAxis.setUpperBound(getYValue(a, b, c, vertex - 10) + 10);
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
        graphRoots(root1, root2);
    }

    private void graphRoots(Double root1, Double root2) {

        if(root1 != null && root2 != null) {
            XYChart.Series<Number, Number> roots = new XYChart.Series<>();

            roots.getData().add(new XYChart.Data<>(root1, 0));

            roots.getData().add(new XYChart.Data<>(root2, 0));

            lineChart.getData().add(roots);
        }
    }

    private double getYValue(Fraction a, Fraction b, Fraction c, double x) {
        return a.doubleValue() * x * x + b.doubleValue() * x + c.doubleValue();
    }
}
