package controller;

import Generated.ETTDescriptor;
import controller.dynamic.mutationPaneController;
import controller.dynamic.subjectPaneController;
import controller.dynamic.teacherSchoolClassPaneController;
import descriptor.Descriptor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
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
    private BooleanProperty paused = new SimpleBooleanProperty(true);

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

        descriptor.getTimeTable().getSchoolClasses().getClassList().forEach(schoolClass -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/teacherSchoolClass.fxml")));
                Parent load = loader.load();
                teacherSchoolClassPaneController controller = loader.getController();
                controller.getId().setText(String.valueOf(schoolClass.getId()));
                controller.getName().setText(schoolClass.getName());
                schoolClass.getRequirements().getStudyList().forEach(study ->
                        controller.getDisplayPane().getChildren().add(new Label(study.toString())));
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
        engineDisplayPane.getChildren().clear();

        descriptor.getEngine().getMutations().getMutationList().forEach(mutation -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/mutation.fxml")));
                Parent load = loader.load();
                mutationPaneController controller = loader.getController();
                controller.getName().setText(mutation.getName());
                controller.getTupples().textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        controller.getTupples().setText(oldValue);
                        controller.getErrorLabel().setText("Must input a number.");
                    } else if (Integer.parseInt(newValue) > descriptor.getEngine().getInitialSolutionPopulation().getSize()) {
                        controller.getTupples().setText(oldValue);
                        controller.getErrorLabel().setText("Tupples cant be higher than population size.");
                    } else {
                        controller.getErrorLabel().setText("");
                    }
                });
                Bindings.bindBidirectional(controller.getTupples().textProperty(), mutation.tupplesProperty(), new NumberStringConverter());
                if (mutation.componentProperty() != null) {
                    controller.getComponentTextField().textProperty().addListener(((observable, oldValue, newValue) -> {
                        if (!newValue.equalsIgnoreCase("T") && !newValue.equalsIgnoreCase("C")
                                && !newValue.equalsIgnoreCase("D") && !newValue.equalsIgnoreCase("H")
                                && !newValue.equalsIgnoreCase("S")) {
                            controller.getComponentTextField().setText(oldValue);
                            controller.getErrorLabel().setText("Please choose a valid component (D,H,C,T,S).");
                        } else {
                            controller.getComponentTextField().setText(newValue.toUpperCase(Locale.ROOT));
                            controller.getErrorLabel().setText("");
                        }
                    }));
                    Bindings.bindBidirectional(controller.getComponentTextField().textProperty(), mutation.componentProperty());
                } else {
                    controller.getComponentTextLabel().setVisible(false);
                    controller.getComponentTextField().setVisible(false);
                }
                Bindings.bindBidirectional(controller.getProbabilitySlider().valueProperty(), mutation.probabilityProperty());
                controller.getProbabilityLabel().textProperty().bind(mutation.probabilityProperty().asString("%.1f"));
                load.disableProperty().bind(paused.not());
                engineDisplayPane.getChildren().add(load);
            } catch (IOException ignored) {
            }
        });

    }

    @FXML
    void displaySelection(ActionEvent event) {

    }

    @FXML
    void displaySubjects(ActionEvent event) {
        timeTableDisplayPane.getChildren().clear();

        descriptor.getTimeTable().getSubjects().getSubjectList().forEach(subject -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/subjects.fxml")));
                Parent load = loader.load();
                subjectPaneController controller = loader.getController();
                controller.getIdLabel().setText(String.valueOf(subject.getId()));
                controller.getNameLabel().setText(subject.getName());
                timeTableDisplayPane.getChildren().add(load);
            } catch (IOException ignored) {
            }
        });
    }

    @FXML
    void displayTeachers(ActionEvent event) {
        timeTableDisplayPane.getChildren().clear();

        descriptor.getTimeTable().getTeachers().getTeacherList().forEach(teacher -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/teacherSchoolClass.fxml")));
                Parent load = loader.load();
                teacherSchoolClassPaneController controller = loader.getController();
                controller.getId().setText(String.valueOf(teacher.getId()));
                controller.getNameLabel().setText("Name: ");
                controller.getName().setText(teacher.getName());
                controller.getDisplayPaneNameLabel().setText("Teaching: ");
                teacher.getTeaching().getTeachesList().forEach(teaches ->
                        controller.getDisplayPane().getChildren().add(new Label(teaches.toString())));
                timeTableDisplayPane.getChildren().add(load);
            } catch (IOException ignored) {
            }
        });
    }

    @FXML
    void loadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(getPrimaryStage());
        if (file != null) {
            timeTableDisplayPane.getChildren().clear();
            engineDisplayPane.getChildren().clear();
            resultsDisplayPane.getChildren().clear();
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
        descriptor.getEngine().enginePausedProperty().bind(paused);
        stopButton.disableProperty().bind(Bindings.not(descriptor.getEngine().engineStartedProperty()));
        DisplayAllSolutionsButton.disableProperty().bind(Bindings.not(descriptor.getEngine().solutionsReadyProperty()));
        DisplayBestSolutionsButton.disableProperty().bind(Bindings.not(descriptor.getEngine().solutionsReadyProperty()));
        engineDisplayMenu.setDisable(false);
        timeTableDisplayMenu.setDisable(false);
    }

    @FXML
    void pause(ActionEvent event) {
        paused.set(!paused.get());
        if (paused.get()) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
        //TODO make pause.
    }

    @FXML
    void start(ActionEvent event) {
        descriptor.getEngine().setEngineStarted(true);//TODO delete this and add real event
        paused.set(false);
        pauseButton.setText("Pause");
        descriptor.getEngine().setSolutionsReady(true);
    }

    @FXML
    void stop(ActionEvent event) {
        descriptor.getEngine().setEngineStarted(false);//TODO delete this and add real event
        paused.set(true);
    }

    public Stage getPrimaryStage() {
        return PrimaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
    }
}
