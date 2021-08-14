package application_interface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppInterface extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Handler");
        Button btn = new Button();
        btn.setText("Load file");
        FileChooser fileChooser = new FileChooser();

        btn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            System.out.println("The file you have chosen is: " + selectedFile.getAbsolutePath());
        });

        VBox vBox = new VBox(btn);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
