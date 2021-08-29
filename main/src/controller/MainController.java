package controller;

import Generated.ETTDescriptor;
import Mains.Util.ResultDisplay;
import Mains.Util.Row;
import Mains.Util.TimeTableSolutionDisplayer;
import controller.dynamic.*;
import descriptor.Descriptor;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import settings.Crossovers;
import settings.Mutations;
import settings.Selections;
import solution.Fifth;
import solution.TimeTableSolution;
import time_table.SchoolClass;
import time_table.Teacher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MainController {

    private Descriptor descriptor;
    private Stage PrimaryStage;
    private Thread thread;

    Instant pauseStart;

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
    private CheckBox DisplayAllSolutionsCheckBox;

    @FXML
    private CheckBox DisplayBestSolutionCheckBox;

    @FXML
    private Label GenerationsLabel;

    @FXML
    private Label BestFitnessCurrent;

    @FXML
    private Button loadFileButton;

    @FXML
    private CheckBox fileLoadedIndicator;

    @FXML
    private TextField frequencyTextField;

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
    private TableView<Row> resultsTimeTable;

    @FXML
    private TableColumn<Row, String> baseColumn;

    @FXML
    private TextArea rawDisplay;

    @FXML
    private Slider resultsGenerationSlider;

    @FXML
    private ProgressBar generationProgressBar;

    @FXML
    private ProgressBar fitnessProgressBar;

    @FXML
    private ProgressBar timeProgressBar;

    @FXML
    private TextField generationEndConditionTextField;

    @FXML
    private TextField fitnessEndConditionTextField;

    @FXML
    private TextField timeEndConditionTextField;

    @FXML
    private Label currentGenerationViewLabel;

    @FXML
    private MenuButton displayTeacher;

    @FXML
    private MenuButton displayClass;

    @FXML
    private Label currentDisplayFitness;


    ResultDisplay resultDisplay = ResultDisplay.TEACHER;

    @FXML
    void displayAllSolutions(ActionEvent event) {

    }

    @FXML
    void displayBestSolution(ActionEvent event) {
        TimeTableSolution solution = (TimeTableSolution) descriptor.getEngine().getBestSolution().getV2();
        currentGenerationViewLabel.textProperty().set(String.valueOf(descriptor.getEngine().getBestSolution().getV1()));

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
        engineDisplayPane.getChildren().clear();

        for (Crossovers crossover : descriptor.getTimeTable().getCrossoverList()) {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/crossover.fxml")));
                Parent load = loader.load();
                crossoverPaneController controller = loader.getController();

                controller.getType().setText(crossover.getName());

                controller.getCuttingPointsTextField().textProperty().addListener(((observable, oldValue, newValue) -> {
                    newValue = newValue.replace(",", "");
                    if (!newValue.matches("\\d*")) {
                        controller.getCuttingPointsTextField().setText(oldValue);
                        controller.getErrorLabel().setText("Must input a number.");
                    } else if (newValue.equals("")) {
                        controller.getCuttingPointsTextField().setText("0");
                    } else if (Integer.parseInt(newValue) >= descriptor.getTimeTable().getMaxListSize()) {
                        controller.getErrorLabel().setText("Too many cutting points.");
                        controller.getCuttingPointsTextField().setText(oldValue);
                    } else {
                        controller.getErrorLabel().setText("");
                    }
                }));
                Bindings.bindBidirectional(controller.getCuttingPointsTextField().textProperty(), crossover.cuttingPointsProperty(), new NumberStringConverter());
                controller.getCuttingPointsSlider().maxProperty().set(descriptor.getTimeTable().getMaxListSize() - 1);
                Bindings.bindBidirectional(controller.getCuttingPointsSlider().valueProperty(), crossover.cuttingPointsProperty());

                if (!crossover.getOrientation().equals("")) {
                    controller.getOrientationChoiceBox().getItems().add("CLASS");
                    controller.getOrientationChoiceBox().getItems().add("TEACHER");
                    Bindings.bindBidirectional(controller.getOrientationChoiceBox().valueProperty(), crossover.orientationProperty());
                } else {
                    controller.getOrientationChoiceBox().setVisible(false);
                    controller.getOrientationNameLabel().setVisible(false);
                }

                Bindings.bindBidirectional(controller.getActiveCheckbox().selectedProperty(), crossover.activeProperty());
                controller.getActiveCheckbox().disableProperty().bind(crossover.activeProperty());
                load.disableProperty().bind(paused.not());

                engineDisplayPane.getChildren().add(load);

            } catch (IOException ignored) {

            }
        }
    }

    @FXML
    void displayMutations(ActionEvent event) {
        engineDisplayPane.getChildren().clear();

        Mutations mutations = descriptor.getTimeTable().getMutations();
        mutations.getMutationList().forEach(mutation -> {
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
                    } else if (newValue.equals("")) {
                        controller.getTupples().setText("0");
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
                                && !newValue.equalsIgnoreCase("S") && !newValue.equalsIgnoreCase("")) {
                            controller.getComponentTextField().setText(oldValue);
                            controller.getErrorLabel().setText("Please choose a valid component (D,H,C,T,S).");
                        } else {
                            controller.getComponentTextField().setText(newValue.toUpperCase());
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
        engineDisplayPane.getChildren().clear();

        for (Selections selection : descriptor.getTimeTable().getSelectionsList()) {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("../resources/dynamic_fxmls/selection.fxml")));
                Parent load = loader.load();
                selectionPaneController controller = loader.getController();

                controller.getType().setText(selection.getType());
                controller.getElitismTextField().textProperty().addListener(((observable, oldValue, newValue) -> {
                    newValue = newValue.replace(",", "");
                    if (!newValue.matches("\\d*")) {
                        controller.getElitismTextField().setText(oldValue);
                        controller.getErrorLabel().setText("Must input a number.");
                    } else if (newValue.equals("")) {
                        controller.getElitismTextField().setText("0");
                    } else if (Integer.parseInt(newValue) > descriptor.getEngine().getInitialSolutionPopulation().getSize()) {
                        controller.getElitismTextField().setText(oldValue);
                        controller.getErrorLabel().setText("Elitism must be lower than population size.");
                    } else {
                        controller.getErrorLabel().setText("");
                    }
                }));
                Bindings.bindBidirectional(controller.getElitismTextField().textProperty(), selection.elitismProperty(), new NumberStringConverter());
                controller.getElitismSlider().maxProperty().bind(descriptor.getEngine().getInitialSolutionPopulation().sizeProperty().subtract(1));
                Bindings.bindBidirectional(controller.getElitismSlider().valueProperty(), selection.elitismProperty());
                if (selection.topPercentProperty().get() != -1) {
                    controller.getTopPercentTextField().textProperty().addListener(((observable, oldValue, newValue) -> {
                        newValue = newValue.replace(",", "");
                        if (!newValue.matches("\\d*")) {
                            controller.getTopPercentTextField().setText(oldValue);
                            controller.getErrorLabel().setText("Must input a number.");
                        } else if (newValue.equals("")) {
                            controller.getTopPercentTextField().setText("0");
                        } else if (Integer.parseInt(newValue) > 100) {
                            controller.getTopPercentTextField().setText(oldValue);
                            controller.getErrorLabel().setText("Cant choose over 100%.");
                        } else {
                            controller.getErrorLabel().setText("");
                        }
                    }));
                    Bindings.bindBidirectional(controller.getTopPercentTextField().textProperty(), selection.topPercentProperty(), new NumberStringConverter());
                    Bindings.bindBidirectional(controller.getTopPercentSlider().valueProperty(), selection.topPercentProperty());
                } else {
                    controller.getTopPercentSlider().setVisible(false);
                    controller.getTopPercentTextField().setVisible(false);
                    controller.getTopPercentNameLabel().setVisible(false);
                }
                Bindings.bindBidirectional(controller.getActiveCheckbox().selectedProperty(), selection.activeProperty());
                controller.getActiveCheckbox().disableProperty().bind(selection.activeProperty());
                load.disableProperty().bind(paused.not());
                engineDisplayPane.getChildren().add(load);

            } catch (IOException ignored) {

            }
        }
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
            resultsTimeTable.getItems().clear();
            resultsTimeTable.getColumns().clear();
            displayClass.getItems().clear();
            displayTeacher.getItems().clear();
            paused.set(true);
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
        displayTeacher.disableProperty().bind(fileLoadedIndicator.selectedProperty().not());
        displayClass.disableProperty().bind(fileLoadedIndicator.selectedProperty().not());
        daysDisplayLabel.textProperty().bind(descriptor.getTimeTable().daysProperty().asString());
        hoursDisplayLabel.textProperty().bind(descriptor.getTimeTable().hoursProperty().asString());
        populationSizeDisplay.textProperty().bind(descriptor.getEngine().getInitialSolutionPopulation().sizeProperty().asString());
        startButton.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        pauseButton.disableProperty().bind(Bindings.not(descriptor.getEngine().engineStartedProperty()));
        Bindings.bindBidirectional(paused, descriptor.getEngine().enginePausedProperty());
        stopButton.disableProperty().bind(Bindings.not(descriptor.getEngine().engineStartedProperty()));
        engineDisplayMenu.setDisable(false);
        timeTableDisplayMenu.setDisable(false);
        generationEndConditionTextField.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        fitnessEndConditionTextField.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        timeEndConditionTextField.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        frequencyTextField.disableProperty().bind(descriptor.getEngine().engineStartedProperty());
        resultsGenerationSlider.maxProperty().bind(descriptor.getEngine().currentGenerationProperty());
        resultsGenerationSlider.blockIncrementProperty().bind(Bindings.createDoubleBinding(
                () -> Double.parseDouble(frequencyTextField.textProperty().get()), frequencyTextField.textProperty()));
        resultsGenerationSlider.majorTickUnitProperty().bind(Bindings.createDoubleBinding(
                () -> Double.parseDouble(frequencyTextField.textProperty().get()), frequencyTextField.textProperty()));
        resultsGenerationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue) && !resultsGenerationSlider.isValueChanging()) {
                currentGenerationViewLabel.textProperty().set(String.format("%.0f", newValue.doubleValue()));
                updateTable(newValue.intValue());
            }
        });
        resultsGenerationSlider.visibleProperty().bind(DisplayAllSolutionsCheckBox.selectedProperty());
        DisplayAllSolutionsCheckBox.visibleProperty().bind(descriptor.getEngine().solutionsReadyProperty().and(descriptor.getEngine().engineStartedProperty().not()));
        initResultsMenu();
        initTable();
        DisplayAllSolutionsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                DisplayAllSolutionsCheckBox.disableProperty().set(true);
                DisplayBestSolutionCheckBox.disableProperty().set(false);
                DisplayBestSolutionCheckBox.selectedProperty().set(false);
                resultsGenerationSlider.setValue(0);
                currentGenerationViewLabel.setText("0");
            }
        });
        DisplayBestSolutionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                DisplayBestSolutionCheckBox.disableProperty().set(true);
                DisplayAllSolutionsCheckBox.selectedProperty().set(false);
                DisplayAllSolutionsCheckBox.disableProperty().set(false);
            }
        });
        descriptor.getEngine().engineStartedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Platform.runLater(() -> currentGenerationViewLabel.textProperty()
                        .set(String.valueOf(descriptor.getEngine().getBestSolution().getV1())));
            }
        });
        descriptor.getEngine().newBestSolutionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> {
                    currentGenerationViewLabel.textProperty()
                            .set(String.valueOf(descriptor.getEngine().getBestSolution().getV1()));
                    updateTable(descriptor.getEngine().getBestSolution().getV1());
                });
            }
        });
        descriptor.getEngine().currentGenerationProperty().addListener((observable, oldValue, newValue) -> {
            double fitness = descriptor.getEngine().getSolutionList().get(0).getFitness();
            Platform.runLater(() -> {
                GenerationsLabel.setText(newValue.toString());
                BestFitnessCurrent.setText(String.format("%.2f", fitness));
            });
        });
    }

    private void initResultsMenu() {
        for (SchoolClass schoolClass : descriptor.getTimeTable().getSchoolClasses().getClassList()) {
            MenuItem menuItem = new MenuItem(schoolClass.getName() + " - " + schoolClass.getId());
            menuItem.setOnAction(event -> {
                resultDisplay = ResultDisplay.CLASS;
                resultDisplay.setId(schoolClass.getId());
                updateTable(Integer.parseInt(currentGenerationViewLabel.getText()));
            });
            displayClass.getItems().add(menuItem);
        }
        for (Teacher teacher : descriptor.getTimeTable().getTeachers().getTeacherList()) {
            MenuItem menuItem = new MenuItem(teacher.getName() + " - " + teacher.getId());
            menuItem.setOnAction(event -> {
                resultDisplay = ResultDisplay.TEACHER;
                resultDisplay.setId(teacher.getId());
                updateTable(Integer.parseInt(currentGenerationViewLabel.getText()));
            });
            displayTeacher.getItems().add(menuItem);
        }

        displayClass.disableProperty().bind(descriptor.getEngine().solutionsReadyProperty().not());
        displayTeacher.disableProperty().bind(descriptor.getEngine().solutionsReadyProperty().not());
    }

    private void initTable() {
        resultsTimeTable.getColumns().clear();
        baseColumn.setCellValueFactory(f -> new SimpleStringProperty(String.valueOf(f.getValue().getHour())));
        resultsTimeTable.getColumns().add(baseColumn);

        for (int i = 1; i <= descriptor.getTimeTable().getDays(); i++) {
            TableColumn<Row, String> tableColumn = new TableColumn<>(String.valueOf(i));
            tableColumn.setSortable(false);
            tableColumn.setResizable(false);
            tableColumn.setPrefWidth(baseColumn.getPrefWidth());
            resultsTimeTable.getColumns().add(tableColumn);
            int finalI = i;
            tableColumn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getDisplay(finalI)));
        }
    }

    private void updateTable(int solutionGeneration) {
        resultsTimeTable.getItems().clear();
        TimeTableSolutionDisplayer solutionDisplayer;
        TimeTableSolution solution;
        if (DisplayBestSolutionCheckBox.isSelected()) {
            solution = (TimeTableSolution) descriptor.getEngine().getBestSolution().getV2();
            if (solution == null) {
                return;
            }
            solutionDisplayer =
                    new TimeTableSolutionDisplayer(solution, resultDisplay);
        } else {
            solution = (TimeTableSolution) descriptor.getEngine().getBestSolutionsPerFrequency().get(solutionGeneration);
            if (solution == null) {
                return;
            }
            solutionDisplayer = new TimeTableSolutionDisplayer(solution, resultDisplay);
        }
        currentDisplayFitness.setText(String.format("Current fitness: %.2f", solution.getFitness()));
        for (int i = 1; i <= descriptor.getTimeTable().getHours(); i++) {
            resultsTimeTable.getItems().add(solutionDisplayer.getDisplay(i));
        }
        rawDisplay.setText(solution.toString());
    }

    @FXML
    void pause(ActionEvent event) {
        paused.set(!paused.get());
        if (paused.get()) {
            pauseButton.setText("Resume");
            pauseStart = Instant.now();
        } else {
            long timeOffset = ChronoUnit.SECONDS.between(pauseStart, Instant.now());
            synchronized (thread) {
                thread.notify();
            }
            descriptor.getEngine().addToMaxTime(timeOffset);
            timeProgressBar.progressProperty().unbind();
            timeProgressBar.progressProperty().bind(descriptor.getEngine().currentTimeProperty().divide((double) descriptor.getEngine().getMaxTime()));
            pauseButton.setText("Pause");
        }
    }

    @FXML
    void start(ActionEvent event) {
        descriptor.getEngine().reset();
        paused.set(false);
        pauseButton.setText("Pause");
        resultsGenerationSlider.setValue(0);
        currentGenerationViewLabel.textProperty().set("0");
        DisplayBestSolutionCheckBox.setSelected(true);

        descriptor.getEngine().initSolutionPopulation(descriptor.getTimeTable(), Integer.parseInt(generationEndConditionTextField.getText()));
        descriptor.getEngine().initThreadParameters(Integer.parseInt(frequencyTextField.getText()),
                Double.parseDouble(fitnessEndConditionTextField.getText()),
                Long.parseLong(timeEndConditionTextField.getText()));
        thread = new Thread(descriptor.getEngine());
        thread.setName("Engine");

        generationProgressBar.progressProperty().bind(descriptor.getEngine().currentGenerationProperty().divide((double) descriptor.getEngine().getNumber_of_generations()));
        fitnessProgressBar.progressProperty().bind(descriptor.getEngine().bestSolutionFitnessProperty().divide(descriptor.getEngine().getMaxFitness()));
        timeProgressBar.progressProperty().bind(descriptor.getEngine().currentTimeProperty().divide((double) descriptor.getEngine().getMaxTime()));

        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void stop(ActionEvent event) {
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
        descriptor.getEngine().setEngineStarted(false);
        paused.set(true);
    }

    public Stage getPrimaryStage() {
        return PrimaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
    }

    public void setTextBoundaries() {
        generationEndConditionTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("")) {
                generationEndConditionTextField.setText(oldValue);
            } else {
                BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
                BigInteger value = new BigInteger(newValue);

                if (value.compareTo(maxInt) > 0) {
                    generationEndConditionTextField.setText(oldValue);
                } else if (Integer.parseInt(newValue) < 0) {
                    generationEndConditionTextField.setText(oldValue);
                } else if (Integer.parseInt(newValue) == 0 &&
                        (fitnessEndConditionTextField.getText().equals("0") && timeEndConditionTextField.getText().equals("0"))) {
                    generationEndConditionTextField.setText(oldValue);
                }
            }
        }));
        fitnessEndConditionTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("")) {
                fitnessEndConditionTextField.setText(oldValue);
            } else if (Integer.parseInt(newValue) > 100 || Integer.parseInt(newValue) < 0) {
                fitnessEndConditionTextField.setText(oldValue);
            } else if (Integer.parseInt(newValue) == 0 &&
                    (generationEndConditionTextField.getText().equals("0") && timeEndConditionTextField.getText().equals("0"))) {
                fitnessEndConditionTextField.setText(oldValue);
            }
        }));
        timeEndConditionTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("")) {
                timeEndConditionTextField.setText(oldValue);
            } else {
                BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
                BigInteger value = new BigInteger(newValue);

                if (value.compareTo(maxInt) > 0) {
                    timeEndConditionTextField.setText(oldValue);
                } else if (Integer.parseInt(newValue) < 0) {
                    timeEndConditionTextField.setText(oldValue);
                } else if (Integer.parseInt(newValue) == 0 &&
                        (fitnessEndConditionTextField.getText().equals("0") && generationEndConditionTextField.getText().equals("0"))) {
                    timeEndConditionTextField.setText(oldValue);
                }
            }
        }));
        frequencyTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("")) {
                frequencyTextField.setText(oldValue);
            } else {
                BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
                BigInteger value = new BigInteger(newValue);

                if (value.compareTo(maxInt) > 0) {
                    frequencyTextField.setText(oldValue);
                } else if (Integer.parseInt(newValue) <= 0) {
                    frequencyTextField.setText(oldValue);
                }
            }
        }));
    }
}
