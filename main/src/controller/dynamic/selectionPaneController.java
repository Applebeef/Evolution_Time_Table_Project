package controller.dynamic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class selectionPaneController {

    @FXML
    private Label type;

    @FXML
    private Slider topPercentSlider;

    @FXML
    private Label errorLabel;

    @FXML
    private Slider pteSlider;

    @FXML
    private TextField pteTextField;

    @FXML
    private Slider elitismSlider;

    @FXML
    private TextField elitismTextField;

    @FXML
    private TextField topPercentTextField;

    @FXML
    private Label topPercentNameLabel;

    @FXML
    private CheckBox activeCheckbox;

    @FXML
    private Label parameterLabel;

    public Label getTopPercentNameLabel() {
        return topPercentNameLabel;
    }

    public Label getType() {
        return type;
    }

    public Slider getTopPercentSlider() {
        return topPercentSlider;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Slider getElitismSlider() {
        return elitismSlider;
    }

    public TextField getElitismTextField() {
        return elitismTextField;
    }

    public TextField getTopPercentTextField() {
        return topPercentTextField;
    }

    public CheckBox getActiveCheckbox() {
        return activeCheckbox;
    }

    public Label getParameterLabel() {
        return parameterLabel;
    }

    public Slider getPteSlider() {
        return pteSlider;
    }

    public TextField getPteTextField() {
        return pteTextField;
    }

    public void setActiveCheckbox(CheckBox activeCheckbox) {
        this.activeCheckbox = activeCheckbox;
    }
}
