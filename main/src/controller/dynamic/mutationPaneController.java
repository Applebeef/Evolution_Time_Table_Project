package controller.dynamic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class mutationPaneController {

    @FXML
    private Label name;

    @FXML
    private TextField tupples;

    @FXML
    private Slider probabilitySlider;

    @FXML
    private Label probabilityLabel;

    @FXML
    private Label componentTextLabel;

    @FXML
    private TextField componentTextField;

    @FXML
    private Label errorLabel;

    @FXML
    void componentTextFieldChanged(ActionEvent event) {

    }

    @FXML
    void tupplesTextFieldChanged(ActionEvent event) {

    }

    public Label getName() {
        return name;
    }

    public void setComponentTextField(TextField componentTextField) {
        this.componentTextField = componentTextField;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    public Label getProbabilityLabel() {
        return probabilityLabel;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public TextField getTupples() {
        return tupples;
    }

    public TextField getComponentTextField() {
        return componentTextField;
    }

    public void setTupples(TextField tupples) {
        this.tupples = tupples;
    }

    public Slider getProbabilitySlider() {
        return probabilitySlider;
    }

    public void setProbabilitySlider(Slider probabilitySlider) {
        this.probabilitySlider = probabilitySlider;
    }

    public Label getComponentTextLabel() {
        return componentTextLabel;
    }

    public void setComponentTextLabel(Label componentTextLabel) {
        this.componentTextLabel = componentTextLabel;
    }
}
