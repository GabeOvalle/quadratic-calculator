import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/**
 * Controls the graphing window of the Quadratic Calculator.
 *
 * <p>This controller uses a JavaFX {@link Canvas} to render quadratic
 * functions, coordinate axes, grid lines, roots, and the vertex of
 * the quadratic function.</p>
 *
 * <p>The graph can be zoomed using the mouse scroll wheel and panned
 * by dragging with the primary mouse button. The graph is dynamically
 * redrawn whenever the visible coordinate range changes.</p>
 */
public class GraphController {

    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    @FXML
    private Label equationLabel;

    @FXML
    private Label xLabel;

    @FXML
    private TextField xValue;

    @FXML
    private Label yLabel;

    @FXML
    private TextField yValue;

    @FXML
    private Button toggleAOSButton;
    private boolean aosIsShowing;

    @FXML
    private Button resetButton;

    @FXML
    private ListView<String> pointsList;
    private List<Double> xVals = new ArrayList<>();
    private List<Double> yVals = new ArrayList<>();

    @FXML
    private Button undoButton;

    @FXML
    private Button clearPointsButton;

    private Fraction a;
    private Fraction b;
    private Fraction c;

    private double xMin = -10;
    private double xMax = 10;
    private double yMin = -10;
    private double yMax = 10;

    private double lastMouseX;
    private double lastMouseY;

    @FXML
    private void initialize() {

        graphicsContext = canvas.getGraphicsContext2D();

        setupMouseHandlers();

        xValue.textProperty().addListener((observable, oldValue, newValue) -> {
            yLabel.setVisible(false);
            yValue.clear();
            yValue.setVisible(false);
        });

        draw();
    }

    @FXML
    private void plotPoint() {
        if(!xValue.getText().isEmpty()) {
            try {
                Fraction x = Fraction.getFraction(xValue.getText());
                double xPoint = x.doubleValue();
                double yPoint = calculateY(x.doubleValue());

                xVals.add(xPoint);
                yVals.add(yPoint);
                pointsList.getItems().add("(" + xValue.getText() + ", " + yPoint + ")");

                yLabel.setVisible(true);
                yValue.setVisible(true);
                yValue.setText(String.valueOf(yPoint));

                panToPoint(xPoint, yPoint);

                graphicsContext.setFill(Color.RED);
                drawPoint(xPoint, yPoint);

            } catch(NumberFormatException e){
                CalculatorController.alert("Enter only whole numbers, fractions, or decimals", "");
            } catch(ArithmeticException e){
                CalculatorController.alert("Invalid fraction input", e.getMessage());
            }
        }
    }

    @FXML
    private void resetGraph() {
        drawGraph(a, b, c);
    }

    @FXML
    private void toggleAOS() {
        if(!aosIsShowing) {
            aosIsShowing = true;
            toggleAOSButton.setText("Hide Axis of Symmetry");
            draw();
        } else {
            aosIsShowing = false;
            toggleAOSButton.setText("Show Axis of Symmetry");
            draw();
        }
    }

    @FXML
    private void undoLastPoint() {
        if(!pointsList.getItems().isEmpty()) {
            xVals.removeLast();
            yVals.removeLast();
            pointsList.getItems().removeLast();

            draw();
        }
    }

    @FXML
    private void clearPoints() {
        if(!pointsList.getItems().isEmpty()) {
            xVals.clear();
            yVals.clear();
            pointsList.getItems().clear();

            yLabel.setVisible(false);
            yValue.clear();
            yValue.setVisible(false);

            draw();
        }
    }

    /**
     * Draws the specified quadratic function on the graph.
     *
     * <p>The function's coefficients are stored and the graph is
     * redrawn using the current coordinate range.</p>
     *
     * @param a the coefficient of the x² term
     * @param b the coefficient of the x term
     * @param c the constant term
     */
    public void drawGraph(Fraction a, Fraction b, Fraction c) {

        equationLabel.setVisible(true);
        xLabel.setVisible(true);
        xValue.setVisible(true);
        toggleAOSButton.setVisible(true);
        resetButton.setVisible(true);

        pointsList.setVisible(true);
        pointsList.getItems().clear();
        undoButton.setVisible(true);
        clearPointsButton.setVisible(true);

        xValue.clear();
        yLabel.setVisible(false);
        yValue.clear();
        yValue.setVisible(false);

        aosIsShowing = false;
        toggleAOSButton.setText("Show Axis of Symmetry");

        xVals.clear();
        yVals.clear();

        this.a = a;
        this.b = b;
        this.c = c;

        equationLabel.setText("y = " + QuadraticSolver.formatEquation(a.toString(), b.toString(), c.toString()));

        double vertexX = -b.doubleValue() / (2 * a.doubleValue());

        double vertexY = calculateY(vertexX);

        xMin = Math.min(vertexX - 10, -10);
        xMax = Math.max(vertexX + 10, 10);

        yMin = Math.min(vertexY - 10, -10);
        yMax = Math.max(vertexY + 10, 10);

        draw();
    }

    private void draw() {

        clearCanvas();

        graphicsContext.setStroke(Color.LIGHTGRAY);
        graphicsContext.setFill(Color.LIGHTGRAY);

        drawGrid();

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setFill(Color.BLACK);

        drawAxes();

        drawAxisTicks();

        drawAxisLabels();

        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setFill(Color.BLUE);

        if(a != null && b != null && c != null && aosIsShowing) {
            drawAOS();
        }

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setFill(Color.RED);

        if (a != null && b != null && c != null) {

            drawQuadratic();

            drawRoots();

            drawVertex();

        }

        if(!xVals.isEmpty() && !yVals.isEmpty()) {
            drawPlottedPoints();
        }
    }

    private void clearCanvas() {
        graphicsContext.clearRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );
    }

    private void drawGrid() {

        double gridSpacing = calculateGridSpacing();

        double firstX = Math.floor(xMin / gridSpacing) * gridSpacing;

        for (double x = firstX; x <= xMax; x += gridSpacing) {

            double screenX = toScreenX(x);

            graphicsContext.strokeLine(
                    screenX,
                    0,
                    screenX,
                    canvas.getHeight()
            );
        }

        double firstY = Math.floor(yMin / gridSpacing) * gridSpacing;

        for (double y = firstY; y <= yMax; y += gridSpacing) {

            double screenY = toScreenY(y);

            graphicsContext.strokeLine(
                    0,
                    screenY,
                    canvas.getWidth(),
                    screenY
            );
        }
    }

    private void drawAxes() {
        drawXAxis();
        drawYAxis();
    }

    private void drawAxisLabels() {
        drawXAxisLabels();
        drawYAxisLabels();
    }

    private void drawAxisTicks() {
        drawXAxisTicks();
        drawYAxisTicks();
    }

    private void drawXAxis() {

        if (yMin <= 0 && yMax >= 0) {

            double screenY = toScreenY(0);

            graphicsContext.strokeLine(
                    0,
                    screenY,
                    canvas.getWidth() - 10,
                    screenY
            );

            graphicsContext.strokeLine(
                    canvas.getWidth() - 10,
                    screenY,
                    canvas.getWidth() - 20,
                    screenY - 5
            );

            graphicsContext.strokeLine(
                    canvas.getWidth() - 10,
                    screenY,
                    canvas.getWidth() - 20,
                    screenY + 5
            );
        }
    }

    private void drawYAxis() {

        if (xMin <= 0 && xMax >= 0) {

            double screenX = toScreenX(0);

            graphicsContext.strokeLine(
                    screenX,
                    canvas.getHeight(),
                    screenX,
                    10
            );

            graphicsContext.strokeLine(
                    screenX,
                    10,
                    screenX - 5,
                    20
            );

            graphicsContext.strokeLine(
                    screenX,
                    10,
                    screenX + 5,
                    20
            );
        }
    }

    private void drawXAxisLabels() {

        if (yMin > 0 || yMax < 0) {
            return;
        }

        double axisY = toScreenY(0);

        double gridSpacing = calculateGridSpacing();

        double firstLabel = Math.ceil(xMin / gridSpacing) * gridSpacing;

        for (double x = firstLabel; x <= xMax; x += gridSpacing) {

            double screenX = toScreenX(x);

            graphicsContext.fillText(
                    formatAxisValue(x),
                    screenX,
                    axisY + 20
            );
        }
    }

    private void drawYAxisLabels() {

        if (xMin > 0 || xMax < 0) {
            return;
        }

        double axisX = toScreenX(0);

        double gridSpacing = calculateGridSpacing();

        double firstLabel = Math.ceil(yMin / gridSpacing) * gridSpacing;

        for (double y = firstLabel; y <= yMax; y += gridSpacing) {

            double screenY = toScreenY(y);

            graphicsContext.fillText(
                    formatAxisValue(y),
                    axisX - 30,
                    screenY
            );
        }
    }

    private void drawXAxisTicks() {

        if (yMin > 0 || yMax < 0) {
            return;
        }

        double axisY = toScreenY(0);

        double spacing = calculateGridSpacing();

        double firstTick = Math.ceil(xMin / spacing) * spacing;

        for (double x = firstTick; x <= xMax; x += spacing) {

            double screenX = toScreenX(x);

            graphicsContext.strokeLine(
                    screenX,
                    axisY - 5,
                    screenX,
                    axisY + 5
            );
        }
    }

    private void drawYAxisTicks() {

        if (xMin > 0 || xMax < 0) {
            return;
        }

        double axisX = toScreenX(0);

        double spacing = calculateGridSpacing();

        double firstTick = Math.ceil(yMin / spacing) * spacing;

        for (double y = firstTick; y <= yMax; y += spacing) {

            double screenY = toScreenY(y);

            graphicsContext.strokeLine(
                    axisX - 5,
                    screenY,
                    axisX + 5,
                    screenY
            );
        }
    }

    private String formatAxisValue(double value) {

        if (Math.abs(value) < 1e-10) {
            value = 0;
        }

        if (value == Math.rint(value)) {
            return String.format("%.0f", value);
        }

        return String.format("%.6f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    private void drawQuadratic() {

        double step = (xMax - xMin) / canvas.getWidth();

        boolean firstPoint = true;

        double previousScreenX = 0;
        double previousScreenY = 0;

        for (double x = xMin; x <= xMax; x += step) {

            double y = calculateY(x);

            double screenX = toScreenX(x);
            double screenY = toScreenY(y);

            if (!firstPoint) {

                graphicsContext.strokeLine(
                        previousScreenX,
                        previousScreenY,
                        screenX,
                        screenY
                );
            }

            previousScreenX = screenX;
            previousScreenY = screenY;

            firstPoint = false;
        }
    }

    private void drawRoots() {

        Double root1 = QuadraticSolver.getDecimalRepresentations(a, b, c).root1();

        Double root2 = QuadraticSolver.getDecimalRepresentations(a, b, c).root2();

        if (root1 != null) {
            drawPoint(root1, 0);
        }

        if (root2 != null && !root2.equals(root1)) {
            drawPoint(root2, 0);
        }
    }

    private void drawVertex() {

        double vertexX = -b.doubleValue() / (2 * a.doubleValue());

        double vertexY = calculateY(vertexX);

        drawPoint(vertexX, vertexY);
    }

    private void drawAOS() {
        double aos = QuadraticSolver.getAxisOfSymmetry(a, b).doubleValue();

        if (xMin <= aos && xMax >= aos) {

            double screenX = toScreenX(aos);

            graphicsContext.strokeLine(
                    screenX,
                    canvas.getHeight(),
                    screenX,
                    10
            );

            graphicsContext.strokeLine(
                    screenX,
                    10,
                    screenX - 5,
                    20
            );

            graphicsContext.strokeLine(
                    screenX,
                    10,
                    screenX + 5,
                    20
            );
        }
    }

    private void drawPlottedPoints() {
        for(int i = 0; i < xVals.size(); i++) {
            drawPoint(xVals.get(i), yVals.get(i));
        }
    }

    private void drawPoint(double x, double y) {

        double screenX = toScreenX(x);
        double screenY = toScreenY(y);

        double radius = 5;

        graphicsContext.fillOval(
                screenX - radius,
                screenY - radius,
                radius * 2,
                radius * 2
        );
    }

    private double calculateY(double x) {
        return a.doubleValue() * x * x
                + b.doubleValue() * x
                + c.doubleValue();
    }

    private double toScreenX(double x) {
        return (x - xMin) / (xMax - xMin) * canvas.getWidth();
    }

    private double toScreenY(double y) {
        return canvas.getHeight() - (y - yMin) / (yMax - yMin) * canvas.getHeight();
    }

    private double toMathX(double screenX) {
        return xMin + screenX / canvas.getWidth() * (xMax - xMin);
    }

    private double toMathY(double screenY) {
        return yMin + (canvas.getHeight() - screenY) / canvas.getHeight() * (yMax - yMin);
    }

    private void setupMouseHandlers() {

        canvas.setOnScroll(event -> {

            double mouseX = toMathX(event.getX());

            double mouseY = toMathY(event.getY());

            double zoomFactor;

            if (event.getDeltaY() > 0) {
                zoomFactor = 0.9;
            } else {
                zoomFactor = 1.1;
            }

            zoom(mouseX, mouseY, zoomFactor);

            event.consume();
        });

        canvas.setOnMousePressed(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {

                lastMouseX = event.getX();
                lastMouseY = event.getY();
            }
        });

        canvas.setOnMouseDragged(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {

                double dx = event.getX() - lastMouseX;

                double dy = event.getY() - lastMouseY;

                pan(dx, dy);

                lastMouseX = event.getX();
                lastMouseY = event.getY();
            }
        });
    }

    private void zoom(double centerX, double centerY, double factor) {

        xMin = centerX + (xMin - centerX) * factor;

        xMax = centerX + (xMax - centerX) * factor;

        yMin = centerY + (yMin - centerY) * factor;

        yMax = centerY + (yMax - centerY) * factor;

        draw();
    }

    private void pan(double dx, double dy) {

        double xUnitsPerPixel = (xMax - xMin) / canvas.getWidth();

        double yUnitsPerPixel = (yMax - yMin) / canvas.getHeight();

        double xShift = dx * xUnitsPerPixel;

        double yShift = dy * yUnitsPerPixel;

        xMin -= xShift;
        xMax -= xShift;

        yMin += yShift;
        yMax += yShift;

        draw();
    }

    public void panToPoint(double x, double y) {

        double xRange = xMax - xMin;
        double yRange = yMax - yMin;

        xMin = x - xRange / 2;
        xMax = x + xRange / 2;

        yMin = y - yRange / 2;
        yMax = y + yRange / 2;

        draw();
    }

    private double calculateGridSpacing() {

        double range = xMax - xMin;

        double rawSpacing = range / 10;

        double magnitude = Math.pow(10, Math.floor(Math.log10(rawSpacing)));

        double normalized = rawSpacing / magnitude;

        if (normalized < 2) {
            return 1 * magnitude;
        } else if (normalized < 5) {
            return 2 * magnitude;
        } else {
            return 5 * magnitude;
        }
    }
}

