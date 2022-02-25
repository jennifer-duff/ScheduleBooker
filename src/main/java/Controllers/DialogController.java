package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class DialogController implements Initializable {
    @FXML private Label appIdLabel;
    @FXML private Label startDateLabel;
    @FXML private Label startTimeLabel;
    @FXML private Button cancelBtn;
    @FXML private Button deleteBtn;

    public static Boolean wasDeleted = false;
    public static Boolean notificationShown = false;

    public static String appId;
    public static String startdate;
    public static String startTime;

    public void setAppInfo(int appIdIncoming, LocalDateTime start){
//        System.out.println("AppID: " + appIdIncoming + "  " + String.valueOf(start.toLocalDate()) + " " + start.toLocalTime());
        appId = String.valueOf(appIdIncoming);
        startdate = String.valueOf(start.toLocalDate());
        startTime = String.valueOf(start.toLocalTime());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(appId != null && startdate != null && startTime != null){
            appIdLabel.setText(appId);
            startDateLabel.setText(startdate);
            startTimeLabel.setText(startTime);
        }
    }
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
