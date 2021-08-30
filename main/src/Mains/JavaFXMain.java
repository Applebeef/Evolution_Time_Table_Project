package Mains;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class JavaFXMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Evolutionary Time Table");

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../resources/main.fxml")));
        Parent load = loader.load();
        MainController controller = loader.getController();
        controller.setTextBoundaries();
        controller.setPrimaryStage(primaryStage);

        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
