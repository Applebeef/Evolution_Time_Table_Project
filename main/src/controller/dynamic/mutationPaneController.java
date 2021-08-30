package controller.dynamic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> componentChoiceBox;

    @FXML
    void componentTextFieldChanged(ActionEvent event) {

    }

    @FXML
    void tupplesTextFieldChanged(ActionEvent event) {

    }

    public Label getName() {
        return name;
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


    public ChoiceBox<String> getComponentChoiceBox() {
        return componentChoiceBox;
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
