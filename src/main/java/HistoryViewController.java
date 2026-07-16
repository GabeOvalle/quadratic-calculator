import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Manages the equation history window.
 *
 * <p>This controller displays previously solved quadratic equations in a
 * {@link ListView}. Users can select an equation to reload its
 * coefficients into the calculator or clear the stored history.</p>
 *
 * @author Gabriel Ovalle
 */
public class HistoryViewController {
    @FXML
    public ListView<String> historyList;

    private CalculatorController calculatorController;

    private boolean updatingSelection = false;

    /**
     * Sets the calculator controller associated with this history view.
     *
     * <p>This reference allows the history view to populate the calculator
     * with the coefficients of a selected equation.</p>
     *
     * @param calculatorController the calculator controller associated with
     *                             this history view
     */
    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    /**
     * Initializes the history view.
     *
     * <p>Registers a listener that responds when the user selects an equation
     * from the history list. The selected equation's coefficients are loaded
     * into the calculator, and the corresponding solutions and graph are
     * displayed.</p>
     */
    @FXML
    private void initialize() {
        historyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue != null && !updatingSelection) {
                    UserHistoryUtil.Coefficients coefficients = UserHistoryUtil.getCoefficients(newValue);

                    if(coefficients != null) {
                        calculatorController.aValue.setText(coefficients.a().toString());
                        calculatorController.bValue.setText(coefficients.b().toString());
                        calculatorController.cValue.setText(coefficients.c().toString());

                        calculatorController.calculateAndDisplay();
                    }
                }
            }
        });
    }

    /**
     * Clears the displayed equation history and removes all stored equations
     * from the database.
     */
    @FXML
    private void clearHistory() {
        historyList.getItems().clear();
        UserHistoryUtil.clearHistory();
    }

    /**
     * Refreshes the history list with the most recently stored equations.
     *
     * <p>Selection events are temporarily disabled while the list is updated
     * to prevent the selection listener from responding to programmatic
     * changes.</p>
     */
    public void refreshHistory() {
        updatingSelection = true;
        historyList.setItems(UserHistoryUtil.getPastEquations());
        updatingSelection = false;
    }
}
