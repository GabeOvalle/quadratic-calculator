import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class GraphController {

    @FXML
    private LineChart lineChart;

    private CalculatorController calculatorController;

    public void setCalculatorController(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    public void clearLineChart() {
        lineChart.getData().clear();
    }

}
