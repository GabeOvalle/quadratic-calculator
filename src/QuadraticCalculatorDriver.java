import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QuadraticCalculatorDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

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
