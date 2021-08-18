package controller.dynamic;

import javafx.fxml.FXML;
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
    private Slider elitismSlider;

    @FXML
    private TextField elitismTextField;

    @FXML
    private TextField topPercentTextField;

    @FXML
    private Label topPercentNameLabel;

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
}
