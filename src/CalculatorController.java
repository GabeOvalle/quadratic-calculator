import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorController {
    @FXML
    private TextField aValue;

    @FXML
    private TextField bValue;

    @FXML
    private TextField cValue;

    @FXML
    private Button calculate;

    @FXML
    private TextField answer1;

    @FXML
    private TextField answer2;

    @FXML
    private void handleCalculate(){
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
            Fraction a = Fraction.getFraction(aValue.getText());
            Fraction b = Fraction.getFraction(bValue.getText());
            Fraction c = Fraction.getFraction(cValue.getText());

            QuadraticSolver.DecimalApproximations approximations = QuadraticSolver.getDecimalApproximations(a, b, c);

            answer1.setText(
                    QuadraticSolver.getSolutions(a, b, c).root1()
                            + formatDecimalApproximation(approximations.root1())
            );
            answer2.setText(
                    QuadraticSolver.getSolutions(a, b, c).root2()
                            + formatDecimalApproximation(approximations.root2())
            );
        } catch(NumberFormatException e){
            alert("Enter only whole numbers, fractions, or decimals", "");
        } catch(ArithmeticException e){
            alert("Your equation isn't quadratic", "This equation appears to be linear");
        }
    }

    @FXML
    private void toggleGraph() {

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

