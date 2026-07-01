import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HistoryViewController {
    @FXML
    public ListView<String> historyList;

    private CalculatorController calculatorController;

    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    @FXML
    private void initialize() {
        historyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue != null) {
                    UserHistoryUtil.Coefficients coefficients = UserHistoryUtil.getCoefficients(newValue);

                    if(coefficients != null) {
                        calculatorController.aValue.setText(coefficients.a().toString());
                        calculatorController.bValue.setText(coefficients.b().toString());
                        calculatorController.cValue.setText(coefficients.c().toString());

                        calculatorController.handleCalculate();
                    }
                }
            }
        });
    }

    @FXML
    private void clearHistory() {
        historyList.getItems().clear();
        UserHistoryUtil.clearHistory();
    }
}
