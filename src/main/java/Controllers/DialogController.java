package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class DialogController {

    public static Boolean wasDeleted = false;

    public void returnToHome(ActionEvent actionEvent) {
        ((Button)actionEvent.getTarget()).getScene().getWindow().hide();
    }

    public void delete(ActionEvent actionEvent) {
        wasDeleted = true;
        returnToHome(actionEvent);
    }
}
