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


    /**
     * Hides the dialog and returns focus to the parent screen
     *
     * @param actionEvent   The ActionEvent associated with the user hitting a button to return to parent screen
     */
    public void returnToHome(ActionEvent actionEvent) {
        ((Button)actionEvent.getTarget()).getScene().getWindow().hide();
    }


    /**
     * Hides the dialog and returns focus to the parent screen, and sets wasDeleted to TRUE.
     * This allows the parent screen detect that the user has chosen to delete the selected item (deletion handled by
     * parent screen).
     *
     * @param actionEvent   The ActionEvent associated with the user hitting a button to return to parent screen
     */
    public void delete(ActionEvent actionEvent) {
        wasDeleted = true;
        returnToHome(actionEvent);
    }
}
