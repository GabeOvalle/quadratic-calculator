import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GraphController {

    @FXML
    private LineChart<Number, Number> lineChart;

    private CalculatorController calculatorController;

    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    public void clearLineChart() {
        lineChart.getData().clear();
    }

    public void drawGraph(Fraction a, Fraction b, Fraction c) {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = " + a + "x² + " + b + "x +" + c);

        for(double x = -10; x <= 10; x+=0.1) {
            double y = a.doubleValue() * x * x + b.doubleValue() * x + c.doubleValue();
            series.getData().add(new XYChart.Data<>(x, y));
        }

        clearLineChart();
        graphRoots(a, b, c);
        lineChart.getData().add(series);
    }

    private void graphRoots(Fraction a, Fraction b, Fraction c) {
        Double root1 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();
        Double root2 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();

        if(root1 != null && root2 != null) {
            XYChart.Series<Number, Number> roots =
                    new XYChart.Series<>();

            roots.getData().add(
                    new XYChart.Data<>(root1, 0));

            roots.getData().add(
                    new XYChart.Data<>(root2, 0));

            lineChart.getData().add(roots);
        }
    }
}
