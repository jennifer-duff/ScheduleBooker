package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DialogController {
    @FXML
    private Button cancelBtn;

    @FXML
    private Button deleteBtn;

    public static Boolean wasDeleted = false;

    public void returnToHome(ActionEvent actionEvent) {
        ((Button)actionEvent.getTarget()).getScene().getWindow().hide();
    }

    public void delete(ActionEvent actionEvent) {
        wasDeleted = true;
        returnToHome(actionEvent);
    }
}
