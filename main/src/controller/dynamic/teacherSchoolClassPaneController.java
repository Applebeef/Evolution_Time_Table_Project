package controller.dynamic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class teacherSchoolClassPaneController {

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private VBox displayPane;

    @FXML
    private Label displayPaneNameLabel;

    @FXML
    private Label nameLabel;

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

    public VBox getDisplayPane() {
        return displayPane;
    }

    public Label getDisplayPaneNameLabel() {
        return displayPaneNameLabel;
    }

    public Label getNameLabel() {
        return nameLabel;
    }
}
