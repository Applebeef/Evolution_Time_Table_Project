package controller;

import Generated.ETTDescriptor;
import Mains.Util.ResultDisplay;
import Mains.Util.Row;
import Mains.Util.TimeTableSolutionDisplayer;
import controller.dynamic.*;
import descriptor.Descriptor;

import evolution.engine.problem_solution.Solution;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import settings.Crossovers;
import settings.Mutation;
import settings.Mutations;
import settings.Selections;
import solution.TimeTableSolution;
import time_table.Rule;
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
import java.util.stream.Collectors;

public class MainController {

    private Descriptor descriptor;
    private Stage PrimaryStage;
    private Thread thread;

    Instant pauseStart;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private TitledPane EnginePane;

    @FXML
    private MenuButton tableOrGraphMenu;

    @FXML
    private Label populationSizeDisplay;

    @FXML
    private TitledPane TimeTablePane;

    @FXML
    private Label daysDisplayLabel;

    @FXML
    private Label parameterLabel;

    @FXML
    private Label hoursDisplayLabel;

    @FXML
    private Label hardRulesWeightDisplayLabel;

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
    private RadioButton displayRawRadioButton;

    @FXML
    private RadioButton displayRulesRadioButton;

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

    @FXML
    private LineChart<String, Number> solutionsGraph;

    ResultDisplay resultDisplay = ResultDisplay.TEACHER;

    @FXML
    void displayAllSolutions(ActionEvent event) {

    }


    @FXML
    void displayBestSolution(ActionEvent event) {
        TimeTableSolution solution = (TimeTableSolution) descriptor.getEngine().getBestSolution().getV2();
        currentGenerationViewLabel.textProperty().set(String.valueOf(descriptor.getEngine().getBestSolution().getV1()));
        updateTable(descriptor.getEngine().getBestSolution().getV1());

    }

    @FXML
    void displayClasses(ActionEvent event) {
        timeTableDisplayPane.getChildren().clear();

        descriptor.getTimeTable().getSchoolClasses().getClassList().forEach(schoolClass -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("/resources/dynamic_fxmls/teacherSchoolClass.fxml")));
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
                        .getResource("/resources/dynamic_fxmls/crossover.fxml")));
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
                        .getResource("/resources/dynamic_fxmls/mutation.fxml")));
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

                controller.getComponentChoiceBox().getItems().add("Days");
                controller.getComponentChoiceBox().getItems().add("Hours");
                controller.getComponentChoiceBox().getItems().add("Teacher");
                controller.getComponentChoiceBox().getItems().add("Class");
                controller.getComponentChoiceBox().getItems().add("Subject");

                String value = mutation.componentProperty().getValue();
                switch (value) {
                    case "D":
                        value = "Days";
                        break;
                    case "H":
                        value = "Hours";
                        break;
                    case "T":
                        value = "Teacher";
                        break;
                    case "C":
                        value = "Class";
                        break;
                    case "S":
                        value = "Subject";
                        break;
                }
                controller.getComponentChoiceBox().setValue(value);
                mutation.componentProperty().bind(Bindings.createStringBinding(() -> controller.getComponentChoiceBox().valueProperty().get().substring(0, 1)
                        , controller.getComponentChoiceBox().valueProperty()));

                if (mutation != Mutation.Flipping) {
                    controller.getComponentTextLabel().setVisible(false);
                    controller.getComponentChoiceBox().setVisible(false);
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
                        .getResource("/resources/dynamic_fxmls/selection.fxml")));
                Parent load = loader.load();
                selectionPaneController controller = loader.getController();

                controller.getType().setText(selection.getType());

                // Set the label name:
                if (selection.getType().equals("Tournament")) {
                    controller.getParameterLabel().setText("PTE:");
                    controller.getPteSlider().setVisible(true);
                    controller.getPteTextField().setVisible(true);
                } else if (selection.getType().equals("Truncation")) {
                    controller.getParameterLabel().setText("Top Percent:");
                    controller.getTopPercentSlider().setVisible(true);
                    controller.getTopPercentTextField().setVisible(true);
                }

                controller.getPteTextField().textProperty().addListener(((observable, oldValue, newValue) -> {
                    if (!newValue.matches("^(0(\\.\\d+)?|1(\\.0+)?)$")) {
                        controller.getPteTextField().setText("0.5");
                        controller.getErrorLabel().setText("Must input a number between 0 and 1.");
                    } else if (newValue.equals("")) {
                        controller.getPteTextField().setText("0.5");
                    } else if (Double.parseDouble(newValue) > 1.0) {
                        controller.getPteTextField().setText(oldValue);
                        controller.getErrorLabel().setText("PTE must be between 0 and 1.");
                    } else {
                        controller.getErrorLabel().setText("");
                    }
                }));
                Bindings.bindBidirectional(controller.getPteTextField().textProperty(),
                        selection.pteProperty(),
                        new NumberStringConverter());
                Bindings.bindBidirectional(controller.getPteSlider().valueProperty(),
                        selection.pteProperty());

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
                        .getResource("/resources/dynamic_fxmls/subjects.fxml")));
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
                        .getResource("/resources/dynamic_fxmls/teacherSchoolClass.fxml")));
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
    void displayRules(ActionEvent event) {
        timeTableDisplayPane.getChildren().clear();

        descriptor.getTimeTable().getRules().getRuleList().forEach(rule -> {
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass()
                        .getResource("/resources/dynamic_fxmls/subjects.fxml")));
                Parent load = loader.load();
                subjectPaneController controller = loader.getController();
                controller.getSubjectNameLabel().setText("Rule Name: ");
                controller.getNameLabel().setText(rule.getRuleId());
                controller.getIdNameLabel().setText("Type: ");
                controller.getIdLabel().setText(rule.getType().toString());

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
                Descriptor tempDescriptor = new Descriptor(ettdescriptor);
                Set<String> errorSet = tempDescriptor.checkValidity();
                if (errorSet.size() == 1 && errorSet.contains("")) {
                    descriptor = tempDescriptor;
                    fileLoadedIndicator.setSelected(true);
                    initUI();
                } else {
                    fileLoadedIndicator.setSelected(false);
                    timeTableDisplayMenu.disableProperty().unbind();
                    timeTableDisplayMenu.disableProperty().set(true);
                    engineDisplayMenu.disableProperty().unbind();
                    engineDisplayMenu.disableProperty().set(true);
                    startButton.disableProperty().unbind();
                    startButton.disableProperty().set(true);
                    DisplayBestSolutionCheckBox.disableProperty().unbind();
                    DisplayBestSolutionCheckBox.disableProperty().set(true);
                    Stage errorStage = new Stage();
                    HBox errors = new HBox();
                    errorSet.forEach(error -> {
                        Label errorLabel = new Label(error);
                        errors.getChildren().add(errorLabel);
                    });
                    errorStage.setScene(new Scene(errors, 400, 400));
                    errorStage.setTitle("Errors");
                    errorStage.show();
                }
            } catch (JAXBException ignored) {
            }
        }
    }

    private void initUI() {

        displayTeacher.disableProperty().bind(fileLoadedIndicator.selectedProperty().not());
        displayClass.disableProperty().bind(fileLoadedIndicator.selectedProperty().not());
        daysDisplayLabel.textProperty().bind(descriptor.getTimeTable().daysProperty().asString());
        hoursDisplayLabel.textProperty().bind(descriptor.getTimeTable().hoursProperty().asString());
        hardRulesWeightDisplayLabel.setText(String.valueOf(descriptor.getTimeTable().getRules().getHardRulesWeight()));
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
                tableDisplay(new ActionEvent());
            }
        });
        DisplayBestSolutionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                DisplayBestSolutionCheckBox.disableProperty().set(true);
                DisplayAllSolutionsCheckBox.selectedProperty().set(false);
                DisplayAllSolutionsCheckBox.disableProperty().set(false);
                tableDisplay(new ActionEvent());
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
            tableColumn.setEditable(false);
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
        if (displayRawRadioButton.isSelected()) {
            rawDisplay.setText(solution.toString());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Rule, Double> entry : solution.getRuleGradeMap().entrySet()) {
                stringBuilder.append(entry.getKey().getRuleId()).append(" - ").append(String.format("%.2f", Math.abs(entry.getValue()))).append(System.lineSeparator());
            }
            rawDisplay.setText(stringBuilder.toString());
        }
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
        GenerationsLabel.setText("0");

        descriptor.getEngine().initSolutionPopulation(descriptor.getTimeTable(), Integer.parseInt(generationEndConditionTextField.getText()));
        displayRawRadioButton.setDisable(false);
        displayRulesRadioButton.setDisable(false);

        BestFitnessCurrent.setText(String.format("%.2f", descriptor.getEngine().getBestSolution().getV2().getFitness()));
        descriptor.getEngine().initThreadParameters(Integer.parseInt(frequencyTextField.getText()),
                Double.parseDouble(fitnessEndConditionTextField.getText()),
                Long.parseLong(timeEndConditionTextField.getText())*60);
        thread = new Thread(descriptor.getEngine());
        thread.setName("Engine");

        generationProgressBar.progressProperty().unbind();
        generationProgressBar.progressProperty().set(0);
        fitnessProgressBar.progressProperty().unbind();
        fitnessProgressBar.progressProperty().set(0);
        timeProgressBar.progressProperty().unbind();
        timeProgressBar.progressProperty().set(0);


        if (!generationEndConditionTextField.getText().equals("0")) {
            generationProgressBar.progressProperty().bind(descriptor.getEngine().currentGenerationProperty().divide((double) descriptor.getEngine().getNumber_of_generations()));
        }
        if (!fitnessEndConditionTextField.getText().equals("0")) {
            fitnessProgressBar.progressProperty().bind(descriptor.getEngine().bestSolutionFitnessProperty().divide(descriptor.getEngine().getMaxFitness()));
        }
        if (!timeEndConditionTextField.getText().equals("0")) {
            timeProgressBar.progressProperty().bind(descriptor.getEngine().currentTimeProperty().divide((double) descriptor.getEngine().getMaxTime()));
        }

        descriptor.getEngine().engineStartedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Platform.runLater(() -> {
                    solutionsGraph.setAnimated(true);
                    solutionsGraph.getXAxis().setTickLength(Double.parseDouble(frequencyTextField.getText()));
                    Map<Integer, Solution> map = descriptor.getEngine().getBestSolutionsPerFrequency();
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    for (Map.Entry<Integer, Solution> entry : map.entrySet().stream().sorted(Comparator.comparingInt((Map.Entry::getKey))).collect(Collectors.toList())) {
                        series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue().getFitness()));
                    }
                    solutionsGraph.getData().clear();
                    solutionsGraph.getData().add(series);
                    solutionsGraph.setAnimated(false);
                });
            }
        });

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
            if (!newValue.matches("\\d*")) {
                generationEndConditionTextField.setText(oldValue);
            } else if (newValue.equals("")) {
                if ((fitnessEndConditionTextField.getText().equals("0") && timeEndConditionTextField.getText().equals("0"))) {
                    generationEndConditionTextField.setText(oldValue);
                } else {
                    generationEndConditionTextField.setText("0");
                }
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
            if (!newValue.matches("\\d*")) {
                fitnessEndConditionTextField.setText(oldValue);
            } else if (newValue.equals("")) {
                if ((generationEndConditionTextField.getText().equals("0") && timeEndConditionTextField.getText().equals("0"))) {
                    fitnessEndConditionTextField.setText(oldValue);
                } else {
                    fitnessEndConditionTextField.setText("0");
                }
            } else if (Integer.parseInt(newValue) > 100 || Integer.parseInt(newValue) < 0) {
                fitnessEndConditionTextField.setText(oldValue);
            } else if (Integer.parseInt(newValue) == 0 &&
                    (generationEndConditionTextField.getText().equals("0") && timeEndConditionTextField.getText().equals("0"))) {
                fitnessEndConditionTextField.setText(oldValue);
            }
        }));
        timeEndConditionTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timeEndConditionTextField.setText(oldValue);
            } else if (newValue.equals("")) {
                if ((fitnessEndConditionTextField.getText().equals("0") && generationEndConditionTextField.getText().equals("0"))) {
                    timeEndConditionTextField.setText(oldValue);
                } else {
                    timeEndConditionTextField.setText("0");
                }
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


    @FXML
    void displayRaw(ActionEvent event) {
        displayRulesRadioButton.selectedProperty().set(false);
        rawDisplay.promptTextProperty().set("Raw display");
        if (descriptor.getEngine().isSolutionsReady()) {
            updateTable(Integer.parseInt(currentGenerationViewLabel.getText()));
        }
    }

    @FXML
    void displayRulesInResult(ActionEvent event) {
        displayRawRadioButton.selectedProperty().set(false);
        rawDisplay.promptTextProperty().set("Rules display");
        if (descriptor.getEngine().isSolutionsReady()) {
            updateTable(Integer.parseInt(currentGenerationViewLabel.getText()));
        }
    }


    @FXML
    void displayCSS1(ActionEvent event) {
        Scene scene = mainScrollPane.getScene();
        scene.getStylesheets().clear();
        String css = Objects.requireNonNull(getClass().getResource("/resources/css1.css")).toExternalForm();
        scene.getStylesheets().add(css);
    }

    @FXML
    void displayCSS2(ActionEvent event) {
        Scene scene = mainScrollPane.getScene();
        scene.getStylesheets().clear();
        String css = Objects.requireNonNull(getClass().getResource("/resources/css3.css")).toExternalForm();
        scene.getStylesheets().add(css);
    }

    @FXML
    void displayDefaultCSS(ActionEvent event) {
        Scene scene = mainScrollPane.getScene();
        scene.getStylesheets().clear();
    }

    @FXML
    void tableDisplay(ActionEvent event) {
        solutionsGraph.setVisible(false);
        resultsTimeTable.setVisible(true);
    }

    @FXML
    void graphDisplay(ActionEvent event) {
        resultsTimeTable.setVisible(false);
        solutionsGraph.setVisible(true);
    }
}
