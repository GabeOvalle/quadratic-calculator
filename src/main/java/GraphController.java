import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GraphController {

    @FXML
    public LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private double lastMouseX;
    private double lastMouseY;

    private CalculatorController calculatorController;

    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    @FXML
    private void initialize() {
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        lineChart.setOnScroll(event -> {
            double zoomFactor = 1.1;

            if (event.getDeltaY() < 0) {
                zoomFactor = 1 / zoomFactor;   // zoom out
            }

            zoomAxis(xAxis, zoomFactor);
            zoomAxis(yAxis, zoomFactor);

            event.consume();
        });

        lineChart.setOnMousePressed(event -> {
            lastMouseX = event.getX();
            lastMouseY = event.getY();
        });

        lineChart.setOnMouseDragged(event -> {
            double dx = event.getX() - lastMouseX;
            double dy = event.getY() - lastMouseY;

            panXAxis(dx);
            panYAxis( 0 - dy);

            lastMouseX = event.getX();
            lastMouseY = event.getY();
        });
    }

    public void drawGraph(Fraction a, Fraction b, Fraction c) {

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = " + a + "x² + " + b + "x +" + c);

        double vertex = -b.doubleValue() / (2 * a.doubleValue());

        double minX = Math.min(vertex - 10, -10);
        double maxX = Math.max(vertex + 10, 10);
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double step = 0.05;

        for (double x = minX; x <= maxX; x += step) {
            double y = a.doubleValue() * x * x
                    + b.doubleValue() * x
                    + c.doubleValue();

            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);

            series.getData().add(new XYChart.Data<>(x, y));
        }

        xAxis.setLowerBound(minX);
        xAxis.setUpperBound(maxX);
        xAxis.setTickUnit(2);

        yAxis.setLowerBound(minY - 1);
        yAxis.setUpperBound(maxY + 1);

        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        xAxis.setForceZeroInRange(true);
        yAxis.setForceZeroInRange(true);

        lineChart.getData().clear();
        lineChart.getData().add(series);

        Double root1 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();
        Double root2 = QuadraticSolver.getDecimalRepresentations(a, b, c).root2();
        graphRoots(root1, root2);
        graphVertex(a, b, c);
    }

    private void graphRoots(Double root1, Double root2) {

        if(root1 != null && root2 != null) {
            XYChart.Series<Number, Number> roots = new XYChart.Series<>();

            roots.getData().add(new XYChart.Data<>(root1, 0));

            roots.getData().add(new XYChart.Data<>(root2, 0));

            lineChart.getData().add(roots);
        }
    }

   private void graphVertex(Fraction a, Fraction b, Fraction c) {
       XYChart.Series<Number, Number> vertexSeries = new XYChart.Series<>();

       double vx = -b.doubleValue() / (2 * a.doubleValue());
       double vy = a.doubleValue()*vx*vx
               + b.doubleValue()*vx
               + c.doubleValue();

       vertexSeries.getData().add(new XYChart.Data<>(vx, vy));

       lineChart.getData().add(vertexSeries);
   }

    private void zoomAxis(NumberAxis axis, double factor) {

        double lower = axis.getLowerBound();
        double upper = axis.getUpperBound();

        double center = (lower + upper) / 2;
        double halfRange = (upper - lower) / 2;

        halfRange /= factor;

        axis.setLowerBound(center - halfRange);
        axis.setUpperBound(center + halfRange);
    }

    private void panXAxis(double pixelDelta) {

        double range = xAxis.getUpperBound() - xAxis.getLowerBound();

        double graphDelta = pixelDelta / lineChart.getWidth() * range;

        xAxis.setLowerBound(xAxis.getLowerBound() - graphDelta);
        xAxis.setUpperBound(xAxis.getUpperBound() - graphDelta);
    }

    private void panYAxis(double pixelDelta) {

        double range = yAxis.getUpperBound() - yAxis.getLowerBound();

        double graphDelta = pixelDelta / lineChart.getWidth() * range;

        yAxis.setLowerBound(yAxis.getLowerBound() - graphDelta);
        yAxis.setUpperBound(yAxis.getUpperBound() - graphDelta);
    }
}
