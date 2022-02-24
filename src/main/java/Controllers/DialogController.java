package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDateTime;

public class DialogController {
    @FXML private Label appIdLabel;
    @FXML private Label startDateLabel;
    @FXML private Label startTimeLabel;
    @FXML private Button cancelBtn;
    @FXML private Button deleteBtn;

    public static Boolean wasDeleted = false;
    public static Boolean notificationShown = false;

    public void setAppInfo(int appId, LocalDateTime start){
        System.out.println(appId + String.valueOf(start));
        appIdLabel.setText(String.valueOf(appId));
        startDateLabel.setText(String.valueOf(start.toLocalDate()));
        startTimeLabel.setText(String.valueOf(start.toLocalTime()));
    }

    //TODO: move above method contents into "initializable" method, and set String vars for all necessary parts

    public void showNotification(ActionEvent actionEvent){
        notificationShown = true;
        returnToHome(actionEvent);
    }

    public void returnToHome(ActionEvent actionEvent) {
        ((Button)actionEvent.getTarget()).getScene().getWindow().hide();
    }

    public void delete(ActionEvent actionEvent) {
        wasDeleted = true;
        returnToHome(actionEvent);
    }
}
