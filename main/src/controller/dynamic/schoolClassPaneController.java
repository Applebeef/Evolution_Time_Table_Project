package controller.dynamic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class schoolClassPaneController {

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private VBox requirementsDisplayPane;

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public Label getId() {
        return id;
    }

    public void setId(Label id) {
        this.id = id;
    }

    public VBox getRequirementsDisplayPane() {
        return requirementsDisplayPane;
    }
}
