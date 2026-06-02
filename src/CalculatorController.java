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
            double a = Double.parseDouble(aValue.getText());
            double b = Double.parseDouble(bValue.getText());
            double c = Double.parseDouble(cValue.getText());

            answer1.setText(QuadraticSolver.getSolutions(a, b, c)[0]);
            answer2.setText(QuadraticSolver.getSolutions(a, b, c)[1]);
        } catch(NumberFormatException e){
            alert("Enter only numbers");
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

