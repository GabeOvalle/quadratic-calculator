import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    public void initialize() {
        UserHistoryUtil.initializeDatabase();

        aValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();
        });

        bValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();
        });

        cValue.textProperty().addListener((observable, oldValue, newValue) -> {
            answer1.clear();
            answer2.clear();
        });
    }

    private Fraction a;
    private Fraction b;
    private Fraction c;

    private Stage graphStage;

    private GraphController graphController;

    private Stage historyStage;

    private HistoryViewController historyViewController;

    public void setGraphStage(Stage stage) {
        this.graphStage = stage;
    }

    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
    }

    public void setHistoryStage(Stage stage) {
        this.historyStage = stage;
    }

    public void setHistoryViewController(HistoryViewController historyViewController) {
        this.historyViewController = historyViewController;
    }

    @FXML
    private void handleCalculate(){
        calculateAndDisplay();

        UserHistoryUtil.addEquation(
                a + "x² + " + b + "x +" + c,
                new UserHistoryUtil.Coefficients(a, b, c)
        );

        historyViewController.refreshHistory();
    }

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
        try{
            //Calculates and displays exact solutions and decimal approximations based on coefficients entered
            this.a = Fraction.getFraction(aValue.getText());
            this.b = Fraction.getFraction(bValue.getText());
            this.c = Fraction.getFraction(cValue.getText());

            QuadraticSolver.DecimalRepresentations approximations = QuadraticSolver.getDecimalRepresentations(a, b, c);

            answer1.setText(
                    QuadraticSolver.getSolutions(a, b, c).root1()
                            + formatDecimalApproximation(approximations.root1())
            );
            answer2.setText(
                    QuadraticSolver.getSolutions(a, b, c).root2()
                            + formatDecimalApproximation(approximations.root2())
            );

            graphController.drawGraph(a, b, c);

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
        }
    }

    private static String formatDecimalApproximation(Double approx) {

        if(approx == null) {
            return "";
        }

        String formattedApprox = "";

        if(Math.floor(approx) != approx) {
            if(String.valueOf(approx).length() > String.format("%.6f", approx).length()) {
                formattedApprox = "; ≈" + String.format("%.6f", approx);
            } else {
                formattedApprox = "; " + approx;
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

