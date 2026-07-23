import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the Quadratic Calculator user interface.
 *
 * Handles user input, validates coefficients, and displays
 * exact and approximate solutions to quadratic equations.
 *
 * @author Gabriel Ovalle
 */
public class CalculatorController {
    @FXML
    public TextField aValue;

    @FXML
    public TextField bValue;

    @FXML
    public TextField cValue;

    @FXML
    private Button calculate;

    @FXML
    public TextField answer1;

    @FXML
    public TextField answer2;

    @FXML
    private Button graphToggle;

    @FXML
    private Button historyToggle;

    @FXML
    private TextArea additionalInfo;

    /**
     * Initializes the calculator window.
     *
     * <p>Initializes the equation history database and registers listeners
     * that clear the displayed solutions whenever any coefficient input is
     * modified.</p>
     */
    @FXML
    public void initialize() {
        UserHistoryUtil.initializeDatabase();

        additionalInfo.setText(
                "Factored Form: \n\n" +
                        "Vertex: \n\n" +
                        "Axis of Symmetry: "

        );

        aValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();

            additionalInfo.setText(
                    "Factored Form: \n\n" +
                            "Vertex: \n\n" +
                            "Axis of Symmetry: "

            );
        });

        bValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();

            additionalInfo.setText(
                    "Factored Form: \n\n" +
                            "Vertex: \n\n" +
                            "Axis of Symmetry: "

            );
        });

        cValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();

            additionalInfo.setText(
                    "Factored Form: \n\n" +
                            "Vertex: \n\n" +
                            "Axis of Symmetry: "

            );
        });
    }

    private Fraction a;
    private Fraction b;
    private Fraction c;

    private Stage graphStage;

    private GraphController graphController;

    private Stage historyStage;

    private HistoryViewController historyViewController;

    /**
     * Sets the stage used to display the graph window.
     *
     * @param stage the graph window stage
     */
    public void setGraphStage(Stage stage) {
        this.graphStage = stage;
    }

    /**
     * Sets the graph controller associated with this calculator.
     *
     * @param graphController the graph controller
     */
    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
    }

    /**
     * Sets the stage used to display the equation history window.
     *
     * @param stage the history window stage
     */
    public void setHistoryStage(Stage stage) {
        this.historyStage = stage;
    }

    /**
     * Sets the history view controller associated with this calculator.
     *
     * @param historyViewController the history view controller
     */
    public void setHistoryViewController(HistoryViewController historyViewController) {
        this.historyViewController = historyViewController;
    }

    /**
     * Calculates the solutions of the quadratic equation entered by the user.
     *
     * <p>The calculated equation is displayed, graphed, and added to the
     * equation history. If an invalid coefficient is entered or the equation
     * is not quadratic, an error dialog is displayed.</p>
     */
    @FXML
    private void handleCalculate(){
        try {
            calculateAndDisplay();

            UserHistoryUtil.addEquation(
                    QuadraticSolver.formatEquation(aValue.getText(), bValue.getText(), cValue.getText()),
                    new UserHistoryUtil.Coefficients(a, b, c)
            );

            historyViewController.refreshHistory();

        } catch(NumberFormatException e){
            //Alerts the user if an input is invalid
            alert("Enter only whole numbers, fractions, or decimals", "");
        } catch(ArithmeticException e){
            if(e.getMessage() != null && e.getMessage().equals("The denominator must not be zero")) {
                //Alerts the user if they enter a fraction with a denominator of zero
                alert("Invalid fraction input", e.getMessage());
            } else {
                //Alerts the user if the A coefficient is zero
                alert("Your equation isn't quadratic", "This equation appears to be linear");
            }
        } catch (Exception e) {
            alert("Error", e.getMessage());
        }
    }

    /**
     * Shows or hides the graph window.
     *
     * <p>If the graph window is shown, it is positioned directly below the
     * main calculator window.</p>
     */
    @FXML
    private void toggleGraph() {
        if(graphStage.isShowing()){
            graphStage.hide();
            graphToggle.setText("Show Graph");
        } else{
            Stage myStage = (Stage)graphToggle.getScene().getWindow();
            graphStage.setX(myStage.getX());
            graphStage.setY(myStage.getY() + myStage.getHeight());
            graphStage.setTitle("Graph");
            graphStage.show();
            graphToggle.setText("Hide Graph");

            graphStage.setOnHidden(event -> {
                graphToggle.setText("Show Graph");
            });
        }
    }

    /**
     * Shows or hides the equation history window.
     *
     * <p>If the history window is shown, it is positioned beside the main
     * calculator window and refreshed with the most recently stored
     * equations.</p>
     */
    @FXML
    private void toggleHistory() {
        if(historyStage.isShowing()) {
            historyStage.hide();
        } else {
            Stage myStage = (Stage)historyToggle.getScene().getWindow();
            historyStage.setX(myStage.getX() - 270);
            historyStage.setY(myStage.getY());
            historyStage.setTitle("Equation History");
            historyStage.show();

            historyViewController.refreshHistory();
        }
    }

    /**
     * Calculates and displays the solutions to the current quadratic equation.
     *
     * <p>Empty coefficient fields are treated as zero. The exact solutions,
     * decimal approximations, and graph of the quadratic function are then
     * displayed. Unlike {@code handleCalculate()}, this method does not add
     * the equation to the history database.</p>
     */
    public void calculateAndDisplay() {
        //Changes coefficients to zero if TextFields are empty
        if(aValue.getText().isEmpty()) {
            aValue.setText("0");
        }
        if(bValue.getText().isEmpty()) {
            bValue.setText("0");
        }
        if(cValue.getText().isEmpty()) {
            cValue.setText("0");
        }
        //Calculates and displays exact solutions and decimal approximations based on coefficients entered
        this.a = Fraction.getFraction(aValue.getText());
        this.b = Fraction.getFraction(bValue.getText());
        this.c = Fraction.getFraction(cValue.getText());

        QuadraticSolver.DecimalRepresentations approximations = QuadraticSolver.getDecimalRepresentations(a, b, c);

        if(!formatDecimalApproximation(approximations.root1()).isEmpty()) {
            answer1.setText(
                    QuadraticSolver.getSolutions(a, b, c).root1() + "; "
                            + formatDecimalApproximation(approximations.root1())
            );
        } else {
            answer1.setText(QuadraticSolver.getSolutions(a, b, c).root1());
        }

        if(!formatDecimalApproximation(approximations.root2()).isEmpty()) {
            answer2.setText(
                    QuadraticSolver.getSolutions(a, b, c).root2() + "; "
                            + formatDecimalApproximation(approximations.root2())
            );
        } else {
            answer2.setText(QuadraticSolver.getSolutions(a, b, c).root2());
        }

        String factoredForm = "Factored Form: " + QuadraticSolver.factoredForm(a, b, c);

        QuadraticSolver.Vertex vert = QuadraticSolver.getVertex(a, b, c);
        String vertex = "Vertex: (" + vert.x() + ", " + vert.y() + ")";

        String aos = "Axis of Symmetry: x = " + QuadraticSolver.getAxisOfSymmetry(a, b);

        additionalInfo.setText(
                factoredForm + "\n\n" +
                        vertex + "\n\n" +
                        aos

        );

        graphController.drawGraph(a, b, c);
    }

    private static String formatDecimalApproximation(Double approx) {

        if(approx == null) {
            return "";
        }

        String formattedApprox = "";

        if(Math.floor(approx) != approx) {
            if(String.valueOf(approx).length() > String.format("%.6f", approx).length()) {
                formattedApprox = "≈" + String.format("%.6f", approx);
            } else {
                formattedApprox = String.valueOf(approx);
            }
        }

        return formattedApprox;
    }

    private static void alert(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

}

