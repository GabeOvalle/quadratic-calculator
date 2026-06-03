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
        try{
            double a = Fraction.getFraction(aValue.getText()).doubleValue();
            double b = Fraction.getFraction(bValue.getText()).doubleValue();
            double c = Fraction.getFraction(cValue.getText()).doubleValue();

            answer1.setText(QuadraticSolver.getSolutions(a, b, c).root1());
            answer2.setText(QuadraticSolver.getSolutions(a, b, c).root2());
        } catch(NumberFormatException e){
            alert("Enter only numbers or fractions");
        } catch(ArithmeticException e){
            alert("Your equation isn't quadratic");
        }
    }

    private static void alert(String headerText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.show();
    }
}

