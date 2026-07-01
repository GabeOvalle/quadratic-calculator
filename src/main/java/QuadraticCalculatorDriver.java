import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Launches the Quadratic Calculator JavaFX application.
 *
 * <p>This class serves as the entry point for the application and is
 * responsible for loading the primary user interface from the
 * quadraticCalculator.fxml file.</p>
 *
 * @author Gabriel Ovalle
 */
public class QuadraticCalculatorDriver extends Application {

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and displays the primary application window.
     *
     * <p>Loads the user interface from the
     * {@code quadraticCalculator.fxml} file and displays it in the
     * primary stage.</p>
     *
     * @param stage the primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage stage){
        try{
            FXMLLoader primaryLoader = new FXMLLoader(getClass().getResource("quadraticCalculator.fxml"));
            Parent primaryRoot = primaryLoader.load();
            stage.setTitle("Quadratic Calculator");
            stage.setScene(new Scene(primaryRoot));
            stage.show();
            CalculatorController calculatorController = primaryLoader.getController();

            FXMLLoader secondaryLoader = new FXMLLoader(getClass().getResource("graphWindow.fxml"));
            Stage secondaryStage = new Stage();
            Parent secondaryRoot = secondaryLoader.load();
            secondaryStage.setScene(new Scene(secondaryRoot));
            GraphController graphController = secondaryLoader.getController();

            FXMLLoader thirdLoader = new FXMLLoader(getClass().getResource("historyView.fxml"));
            Stage thirdStage = new Stage();
            Parent thirdRoot = thirdLoader.load();
            thirdStage.setScene(new Scene(thirdRoot));
            HistoryViewController historyViewController = thirdLoader.getController();

            calculatorController.setGraphController(graphController);
            calculatorController.setHistoryViewController(historyViewController);

            graphController.setCalculatorController(calculatorController);

            historyViewController.setCalculatorController(calculatorController);

            calculatorController.setGraphStage(secondaryStage);
            calculatorController.setHistoryStage(thirdStage);

            stage.setOnCloseRequest(event -> {
                secondaryStage.close();
                thirdStage.close();
            });
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
