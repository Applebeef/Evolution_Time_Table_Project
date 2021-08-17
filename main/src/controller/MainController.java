package controller;

import Generated.ETTDescriptor;
import controller.dynamic.schoolClassPaneController;
import descriptor.Descriptor;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainController {

    private Descriptor descriptor;
    private Stage PrimaryStage;

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
    private MenuButton timeTableDisplayMenu;

    @FXML
    private MenuButton engineDisplayMenu;

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
    private CheckBox fileLoadedIndicator;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;
    private boolean paused = false;

    @FXML
    private Button stopButton;

    @FXML
    private HBox engineDisplayPane;

    @FXML
    private HBox timeTableDisplayPane;

    @FXML
    private HBox resultsDisplayPane;

    @FXML
    private ProgressBar generationProgressBar;

    @FXML
    private ProgressBar fitnessProgressBar;

    @FXML
    private ProgressBar timeProgressBar;

    @FXML
    void displayAllSolutions(ActionEvent event) {

    }

    @FXML
    void displayBestSolutions(ActionEvent event) {

    }

    @FXML
    void displayClasses(ActionEvent event) {
        timeTableDisplayPane.getChildren().clear();

        //FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("../resources/dynamic_fxmls/schoolClass.fxml")));
        descriptor.getTimeTable().getSchoolClasses().getClassList().forEach(schoolClass -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/schoolClass.fxml")));
                Parent load = loader.load();
                schoolClassPaneController controller = loader.getController();
                controller.getId().setText(String.valueOf(schoolClass.getId()));
                controller.getName().setText(schoolClass.getName());
                schoolClass.getRequirements().getStudyList().forEach(study ->
                        controller.getRequirementsDisplayPane().getChildren().add(new Label(study.toString())));
                timeTableDisplayPane.getChildren().add(load);
            } catch (IOException ignored) {
            }
        });
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(getPrimaryStage());
        if (file != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                ETTDescriptor ettdescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);
                descriptor = new Descriptor(ettdescriptor);
                fileLoadedIndicator.setSelected(true);
                initUI();
            } catch (JAXBException ignored) {
            }
        }
    }

    private void initUI() {
        daysDisplayLabel.textProperty().bind(descriptor.getTimeTable().daysProperty().asString());
        hoursDisplayLabel.textProperty().bind(descriptor.getTimeTable().hoursProperty().asString());
        populationSizeDisplay.textProperty().bind(descriptor.getEngine().getInitialSolutionPopulation().sizeProperty().asString());
        startButton.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        pauseButton.disableProperty().bind(Bindings.not(descriptor.getEngine().engineStartedProperty()));
        stopButton.disableProperty().bind(Bindings.not(descriptor.getEngine().engineStartedProperty()));
        DisplayAllSolutionsButton.disableProperty().bind(Bindings.not(descriptor.getEngine().solutionsReadyProperty()));
        DisplayBestSolutionsButton.disableProperty().bind(Bindings.not(descriptor.getEngine().solutionsReadyProperty()));
        engineDisplayMenu.setDisable(false);
        timeTableDisplayMenu.setDisable(false);
    }

    @FXML
    void pause(ActionEvent event) {
        paused = !paused;
        if (paused) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
        //TODO make pause.
    }

    @FXML
    void start(ActionEvent event) {
        descriptor.getEngine().setEngineStarted(true);//TODO delete this and add real event
        descriptor.getEngine().setSolutionsReady(true);
    }

    @FXML
    void stop(ActionEvent event) {
        descriptor.getEngine().setEngineStarted(false);//TODO delete this and add real event
    }

    public Stage getPrimaryStage() {
        return PrimaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
    }
}
