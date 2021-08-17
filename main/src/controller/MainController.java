package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class MainController {

    @FXML
    private TitledPane EnginePane;

    @FXML
    private Label populationSizeDisplay;

    @FXML
    private TitledPane TimeTablePane;

    @FXML
    private Label daysDisplayLabel;

    @FXML
    private Label hoursDisplayLabel;

    @FXML
    private TitledPane ResultsPane;

    @FXML
    private Button DisplayBestSolutionsButton;

    @FXML
    private Button DisplayAllSolutionsButton;

    @FXML
    private Label GenerationsLabel;

    @FXML
    private Label BestFitnessCurrent;

    @FXML
    private Label BestFitnessTotal;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;
    private boolean paused = false;

    @FXML
    private Button stopButton;

    @FXML
    void displayAllSolutions(ActionEvent event) {

    }

    @FXML
    void displayBestSolutions(ActionEvent event) {

    }

    @FXML
    void displayClasses(ActionEvent event) {

    }

    @FXML
    void displayCrossover(ActionEvent event) {

    }

    @FXML
    void displayMutations(ActionEvent event) {

    }

    @FXML
    void displaySelection(ActionEvent event) {

    }

    @FXML
    void displaySubjects(ActionEvent event) {

    }

    @FXML
    void displayTeachers(ActionEvent event) {

    }

    @FXML
    void loadFile(ActionEvent event) {

    }

    @FXML
    void pause(ActionEvent event) {
        paused=!paused;
        if(paused){
            pauseButton.setText("Resume");
        }
        else{
            pauseButton.setText("Pause");
        }
        //TODO make pause.
    }

    @FXML
    void start(ActionEvent event) {

    }

    @FXML
    void stop(ActionEvent event) {

    }

}
