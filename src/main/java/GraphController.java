import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;


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

    private CalculatorController calculatorController;

    private Fraction a;
    private Fraction b;
    private Fraction c;

    /*
     * The mathematical coordinates currently visible on the graph.
     */
    private double xMin = -10;
    private double xMax = 10;
    private double yMin = -10;
    private double yMax = 10;

    /*
     * Used for mouse dragging.
     */
    private double lastMouseX;
    private double lastMouseY;

    /**
     * Initializes the graph controller.
     *
     * <p>Obtains the graphics context from the canvas and registers
     * mouse event handlers for zooming and panning.</p>
     */
    @FXML
    private void initialize() {

        graphicsContext = canvas.getGraphicsContext2D();

        setupMouseHandlers();

        draw();
    }

    /**
     * Sets the calculator controller associated with this graph.
     *
     * @param calculatorController the calculator controller
     */
    public void setCalculatorController(
            CalculatorController calculatorController) {

        this.calculatorController = calculatorController;
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

        this.a = a;
        this.b = b;
        this.c = c;

        /*
         * Calculate the vertex so the initial view can be centered
         * around the important part of the parabola.
         */
        double vertexX =
                -b.doubleValue()
                        / (2 * a.doubleValue());

        double vertexY = calculateY(vertexX);

        xMin = Math.min(vertexX - 10, -10);
        xMax = Math.max(vertexX + 10, 10);

        yMin = Math.min(vertexY - 10, -10);
        yMax = Math.max(vertexY + 10, 10);

        draw();
    }

    /**
     * Redraws the entire graph.
     *
     * <p>The graph consists of the background, grid lines, coordinate
     * axes, quadratic function, roots, and vertex.</p>
     */
    private void draw() {

        clearCanvas();

        drawGrid();

        drawAxes();

        drawAxisTicks();

        drawAxisLabels();

        if (a != null && b != null && c != null) {

            drawQuadratic();

            drawRoots();

            drawVertex();
        }
    }

    /**
     * Clears the entire canvas before a new graph is drawn.
     */
    private void clearCanvas() {

        graphicsContext.clearRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );
    }

    /**
     * Draws the coordinate grid.
     *
     * <p>The grid spacing is determined dynamically based on the
     * current zoom level.</p>
     */
    private void drawGrid() {

        double gridSpacing = calculateGridSpacing();

        double firstX =
                Math.floor(xMin / gridSpacing)
                        * gridSpacing;

        for (double x = firstX; x <= xMax; x += gridSpacing) {

            double screenX = toScreenX(x);

            graphicsContext.strokeLine(
                    screenX,
                    0,
                    screenX,
                    canvas.getHeight()
            );
        }

        double firstY =
                Math.floor(yMin / gridSpacing)
                        * gridSpacing;

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

    /**
     * Draws the x-axis and y-axis if they are currently visible.
     */
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

        double firstLabel =
                Math.ceil(xMin / gridSpacing)
                        * gridSpacing;

        for (
                double x = firstLabel;
                x <= xMax;
                x += gridSpacing
        ) {

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

        double firstLabel =
                Math.ceil(yMin / gridSpacing)
                        * gridSpacing;

        for (
                double y = firstLabel;
                y <= yMax;
                y += gridSpacing
        ) {

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

        double firstTick =
                Math.ceil(xMin / spacing)
                        * spacing;

        for (
                double x = firstTick;
                x <= xMax;
                x += spacing
        ) {

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

        double firstTick =
                Math.ceil(yMin / spacing)
                        * spacing;

        for (
                double y = firstTick;
                y <= yMax;
                y += spacing
        ) {

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

    /**
     * Draws the quadratic function.
     *
     * <p>The function is sampled across the visible x-coordinate range
     * and connected with line segments.</p>
     */
    private void drawQuadratic() {

        double step =
                (xMax - xMin)
                        / canvas.getWidth();

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

    /**
     * Draws the real roots of the quadratic function.
     */
    private void drawRoots() {

        Double root1 =
                QuadraticSolver
                        .getDecimalRepresentations(a, b, c)
                        .root1();

        Double root2 =
                QuadraticSolver
                        .getDecimalRepresentations(a, b, c)
                        .root2();

        if (root1 != null) {
            drawPoint(root1, 0);
        }

        if (root2 != null && !root2.equals(root1)) {
            drawPoint(root2, 0);
        }
    }

    /**
     * Draws the vertex of the quadratic function.
     */
    private void drawVertex() {

        double vertexX =
                -b.doubleValue()
                        / (2 * a.doubleValue());

        double vertexY = calculateY(vertexX);

        drawPoint(vertexX, vertexY);
    }

    /**
     * Draws a point at a specified mathematical coordinate.
     *
     * @param x the mathematical x-coordinate
     * @param y the mathematical y-coordinate
     */
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

    /**
     * Calculates the y-coordinate of the quadratic function for a
     * specified x-coordinate.
     *
     * @param x the x-coordinate
     * @return the corresponding y-coordinate
     */
    private double calculateY(double x) {

        return a.doubleValue() * x * x
                + b.doubleValue() * x
                + c.doubleValue();
    }

    /**
     * Converts a mathematical x-coordinate into a canvas x-coordinate.
     *
     * @param x the mathematical x-coordinate
     * @return the corresponding canvas x-coordinate
     */
    private double toScreenX(double x) {

        return (x - xMin)
                / (xMax - xMin)
                * canvas.getWidth();
    }

    /**
     * Converts a mathematical y-coordinate into a canvas y-coordinate.
     *
     * <p>The y-axis is inverted in screen coordinates, so larger
     * mathematical y-values correspond to smaller canvas y-values.</p>
     *
     * @param y the mathematical y-coordinate
     * @return the corresponding canvas y-coordinate
     */
    private double toScreenY(double y) {

        return canvas.getHeight()
                - (y - yMin)
                / (yMax - yMin)
                * canvas.getHeight();
    }

    /**
     * Converts a canvas x-coordinate into a mathematical x-coordinate.
     *
     * @param screenX the canvas x-coordinate
     * @return the corresponding mathematical x-coordinate
     */
    private double toMathX(double screenX) {

        return xMin
                + screenX / canvas.getWidth()
                * (xMax - xMin);
    }

    /**
     * Converts a canvas y-coordinate into a mathematical y-coordinate.
     *
     * @param screenY the canvas y-coordinate
     * @return the corresponding mathematical y-coordinate
     */
    private double toMathY(double screenY) {

        return yMin
                + (canvas.getHeight() - screenY)
                / canvas.getHeight()
                * (yMax - yMin);
    }

    /**
     * Sets up mouse handlers for zooming and panning.
     */
    private void setupMouseHandlers() {

        /*
         * Zoom using the mouse wheel.
         */
        canvas.setOnScroll(event -> {

            double mouseX =
                    toMathX(event.getX());

            double mouseY =
                    toMathY(event.getY());

            double zoomFactor;

            if (event.getDeltaY() > 0) {
                zoomFactor = 0.9;
            } else {
                zoomFactor = 1.1;
            }

            zoom(
                    mouseX,
                    mouseY,
                    zoomFactor
            );

            event.consume();
        });

        /*
         * Begin panning.
         */
        canvas.setOnMousePressed(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {

                lastMouseX = event.getX();
                lastMouseY = event.getY();
            }
        });

        /*
         * Pan while dragging.
         */
        canvas.setOnMouseDragged(event -> {

            if (event.getButton() == MouseButton.PRIMARY) {

                double dx =
                        event.getX() - lastMouseX;

                double dy =
                        event.getY() - lastMouseY;

                pan(dx, dy);

                lastMouseX = event.getX();
                lastMouseY = event.getY();
            }
        });
    }

    /**
     * Zooms the graph around the point beneath the mouse cursor.
     *
     * <p>The point beneath the cursor remains fixed while the graph
     * zooms in or out, providing behavior similar to interactive
     * graphing applications.</p>
     *
     * @param centerX the mathematical x-coordinate at the cursor
     * @param centerY the mathematical y-coordinate at the cursor
     * @param factor the zoom factor
     */
    private void zoom(
            double centerX,
            double centerY,
            double factor) {

        xMin =
                centerX
                        + (xMin - centerX) * factor;

        xMax =
                centerX
                        + (xMax - centerX) * factor;

        yMin =
                centerY
                        + (yMin - centerY) * factor;

        yMax =
                centerY
                        + (yMax - centerY) * factor;

        draw();
    }

    /**
     * Pans the graph by a specified number of screen pixels.
     *
     * @param dx the horizontal pixel displacement
     * @param dy the vertical pixel displacement
     */
    private void pan(double dx, double dy) {

        double xUnitsPerPixel =
                (xMax - xMin)
                        / canvas.getWidth();

        double yUnitsPerPixel =
                (yMax - yMin)
                        / canvas.getHeight();

        double xShift =
                dx * xUnitsPerPixel;

        double yShift =
                dy * yUnitsPerPixel;

        xMin -= xShift;
        xMax -= xShift;

        yMin += yShift;
        yMax += yShift;

        draw();
    }

    /**
     * Calculates an appropriate grid spacing based on the current
     * zoom level.
     *
     * @return the spacing between grid lines
     */
    private double calculateGridSpacing() {

        double range = xMax - xMin;

        double rawSpacing = range / 10;

        double magnitude =
                Math.pow(
                        10,
                        Math.floor(Math.log10(rawSpacing))
                );

        double normalized =
                rawSpacing / magnitude;

        if (normalized < 2) {
            return 1 * magnitude;
        } else if (normalized < 5) {
            return 2 * magnitude;
        } else {
            return 5 * magnitude;
        }
    }
}

