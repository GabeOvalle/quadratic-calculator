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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quadraticCalculator.fxml"));
            Parent root = loader.load();
            stage.setTitle("Quadratic Calculator");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
