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

            double discriminant = (b*b) - 4*a*c;

            if(discriminant < 0){
                double firstPart = (0 - b) / (2*a);
                double complexNumerator = Math.sqrt(0 - discriminant);
                String secondPart = "i" + String.format("%.3f", complexNumerator) + "/" + 2*a;
                answer1.setText(firstPart + " + " + secondPart);
                answer2.setText(firstPart + " - " + secondPart);
            } else{
                double ans1 = ((0 - b) + Math.sqrt(discriminant)) / (2*a);
                double ans2 = ((0 - b) - Math.sqrt(discriminant)) / (2*a);
                answer1.setText(String.format("%.3f", ans1));
                answer2.setText(String.format("%.3f", ans2));
            }
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

