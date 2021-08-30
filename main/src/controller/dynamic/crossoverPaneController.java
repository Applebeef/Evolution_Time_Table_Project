package controller.dynamic;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class crossoverPaneController {

    @FXML
    private Label type;

    @FXML
    private Label orientationNameLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Slider cuttingPointsSlider;

    @FXML
    private TextField cuttingPointsTextField;

    @FXML
    private CheckBox activeCheckbox;

    @FXML
    private ChoiceBox<String> orientationChoiceBox;

    public Label getType() {
        return type;
    }

    public void setType(Label type) {
        this.type = type;
    }

    public Label getOrientationNameLabel() {
        return orientationNameLabel;
    }

    public void setOrientationNameLabel(Label orientationNameLabel) {
        this.orientationNameLabel = orientationNameLabel;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    public Slider getCuttingPointsSlider() {
        return cuttingPointsSlider;
    }

    public void setCuttingPointsSlider(Slider cuttingPointsSlider) {
        this.cuttingPointsSlider = cuttingPointsSlider;
    }

    public TextField getCuttingPointsTextField() {
        return cuttingPointsTextField;
    }

    public void setCuttingPointsTextField(TextField cuttingPointsTextField) {
        this.cuttingPointsTextField = cuttingPointsTextField;
    }

    public CheckBox getActiveCheckbox() {
        return activeCheckbox;
    }

    public void setActiveCheckbox(CheckBox activeCheckbox) {
        this.activeCheckbox = activeCheckbox;
    }

    public ChoiceBox<String> getOrientationChoiceBox() {
        return orientationChoiceBox;
    }

    public void setOrientationChoiceBox(ChoiceBox<String> orientationChoiceBox) {
        this.orientationChoiceBox = orientationChoiceBox;
    }
}
