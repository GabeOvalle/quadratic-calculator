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
     * Initializes and displays the application windows.
     *
     * <p>Loads the main calculator interface, graph window, and equation
     * history window from their respective FXML files. The controllers are
     * connected so they can communicate with one another, and the graph and
     * history stages are associated with the calculator controller.</p>
     *
     * <p>When the main application window is closed, the graph and history
     * windows are also closed automatically.</p>
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage){
        try{
            FXMLLoader primaryLoader = new FXMLLoader(getClass().getResource("quadraticCalculator.fxml"));
            Parent primaryRoot = primaryLoader.load();
            primaryStage.setTitle("Quadratic Calculator");
            primaryStage.setScene(new Scene(primaryRoot));
            primaryStage.show();
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

            primaryStage.setOnCloseRequest(event -> {
                secondaryStage.close();
                thirdStage.close();
            });
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
