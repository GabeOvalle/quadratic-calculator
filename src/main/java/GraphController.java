import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Displays and manages the graph of a quadratic equation.
 *
 * <p>This controller is responsible for plotting quadratic functions on
 * a JavaFX {@link LineChart}, displaying the vertex and x-intercepts,
 * and allowing the user to zoom and pan the graph using the mouse.</p>
 *
 * @author Gabriel Ovalle
 */
public class GraphController {

    @FXML
    public LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private XYChart.Series<Number, Number> series;

    private Fraction a;
    private Fraction b;
    private Fraction c;

    private double lastMouseX;
    private double lastMouseY;

    private CalculatorController calculatorController;

    /**
     * Sets the calculator controller associated with this graph controller.
     *
     * <p>This reference allows the graph controller to communicate with the
     * main calculator controller when necessary.</p>
     *
     * @param calculatorController the calculator controller associated with
     *                             this graph controller
     */
    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    /**
     * Initializes the graph window.
     *
     * <p>Configures the graph axes and registers mouse event handlers for
     * zooming with the scroll wheel and panning by dragging the graph.</p>
     */
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

    /**
     * Draws the graph of a quadratic equation.
     *
     * <p>The graph is centered around the vertex of the parabola and
     * automatically adjusts the viewing window to display the function.
     * The graph also displays the x-intercepts, when they exist, and the
     * vertex of the parabola.</p>
     *
     * @param a the coefficient of the {@code x²} term
     * @param b the coefficient of the {@code x} term
     * @param c the constant term
     */
    public void drawGraph(Fraction a, Fraction b, Fraction c) {

        this.a = a;
        this.b = b;
        this.c = c;

        this.series = new XYChart.Series<>();
        series.setName("y = " + QuadraticSolver.formatEquation(a, b, c));

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

    /**
     * Plots the real x-intercepts of the quadratic function on the graph.
     *
     * <p>If the quadratic has no real roots, no intercepts are plotted.</p>
     *
     * @param root1 the first real root, or {@code null} if none exists
     * @param root2 the second real root, or {@code null} if none exists
     */
    private void graphRoots(Double root1, Double root2) {

        if(root1 != null && root2 != null) {
            XYChart.Series<Number, Number> roots = new XYChart.Series<>();

            roots.getData().add(new XYChart.Data<>(root1, 0));

            roots.getData().add(new XYChart.Data<>(root2, 0));

            lineChart.getData().add(roots);
        }
    }

    /**
     * Plots the vertex of the quadratic function on the graph.
     *
     * @param a the coefficient of the {@code x²} term
     * @param b the coefficient of the {@code x} term
     * @param c the constant term
     */
   private void graphVertex(Fraction a, Fraction b, Fraction c) {
       XYChart.Series<Number, Number> vertexSeries = new XYChart.Series<>();

       double vx = -b.doubleValue() / (2 * a.doubleValue());
       double vy = a.doubleValue()*vx*vx
               + b.doubleValue()*vx
               + c.doubleValue();

       vertexSeries.getData().add(new XYChart.Data<>(vx, vy));

       lineChart.getData().add(vertexSeries);
   }

    /**
     * Zooms the specified axis by the given zoom factor while keeping the
     * current center of the axis fixed.
     *
     * @param axis the axis to zoom
     * @param factor the zoom factor; values greater than {@code 1} zoom in,
     *               while values less than {@code 1} zoom out
     */
    private void zoomAxis(NumberAxis axis, double factor) {

        double lower = axis.getLowerBound();
        double upper = axis.getUpperBound();

        double center = (lower + upper) / 2;
        double halfRange = (upper - lower) / 2;

        halfRange /= factor;

        axis.setLowerBound(center - halfRange);
        axis.setUpperBound(center + halfRange);
    }

    /**
     * Pans the x-axis by the specified number of pixels.
     *
     * <p>The pixel distance is converted to graph units based on the
     * current width of the graph.</p>
     *
     * @param pixelDelta the horizontal mouse movement in pixels
     */
    private void panXAxis(double pixelDelta) {

        double range = xAxis.getUpperBound() - xAxis.getLowerBound();

        double graphDelta = pixelDelta / lineChart.getWidth() * range;

        xAxis.setLowerBound(xAxis.getLowerBound() - graphDelta);
        xAxis.setUpperBound(xAxis.getUpperBound() - graphDelta);
    }

    /**
     * Pans the y-axis by the specified number of pixels.
     *
     * <p>The pixel distance is converted to graph units based on the
     * current height of the graph.</p>
     *
     * @param pixelDelta the vertical mouse movement in pixels
     */
    private void panYAxis(double pixelDelta) {

        double range = yAxis.getUpperBound() - yAxis.getLowerBound();

        double graphDelta = pixelDelta / lineChart.getWidth() * range;

        yAxis.setLowerBound(yAxis.getLowerBound() - graphDelta);
        yAxis.setUpperBound(yAxis.getUpperBound() - graphDelta);
    }
}
