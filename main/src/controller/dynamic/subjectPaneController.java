package controller.dynamic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class subjectPaneController {

    @FXML
    private Label subjectNameLabel;

    @FXML
    private Label idNameLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    public Label getSubjectNameLabel() {
        return subjectNameLabel;
    }

    public Label getIdNameLabel() {
        return idNameLabel;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getIdLabel() {
        return idLabel;
    }
}
