package Controllers;

import Database.AppointmentDAO;
import Database.UserDAO;
import Utilities.StageChangeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private Label loginLabel;
    @FXML private Label countryLabel;
    @FXML private Label errorMsg;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button enterBtn;

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    private String lang;
    public static int USER_ID;
    public static String USERNAME = "UNRECOGNIZED USER";


    public void initialize(URL location, ResourceBundle resources) {
        Locale locale = Locale.getDefault();
        lang = locale.getDisplayLanguage();
        String country = locale.getDisplayCountry();
        countryLabel.setText(country);

        if(lang.equalsIgnoreCase("français")){
            loginLabel.setText("Se Connecter");
            usernameLabel.setText("Identifiant");
            passwordLabel.setText("Mot de Passe");
            enterBtn.setText(" E n t r é e ");
        }
    }

    public void writeToLog(Boolean isSuccessful){
        LocalDateTime now = LocalDateTime.now();
        String date = String.valueOf( AppointmentDAO.convertToUTC(now).toLocalDate());
        String time = String.valueOf( AppointmentDAO.convertToUTC(now).toLocalTime()).substring(0, 8);
        String success;
        if(isSuccessful){
            success = "SUCCESSFUL";
        }
        else{
            success = "UNSUCCESSFUL";
        }
        String string = "LOGIN ATTEMPT: User \"" + USERNAME + "\" attempted login on " + date + " at " + time + " and was " + success + "\n";

        try{
            String filename= "login_activity.txt";
            FileWriter fw = new FileWriter(filename,true);  //the true will append the new data
            fw.write(string);                                       //appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    @FXML
    public void enterApp(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isValidLogin = false;
        int rowCount = UserDAO.getUserRowCount();
        ArrayList<ArrayList<String>> loginList = UserDAO.getUsernamesAndPasswords(rowCount);
        for(ArrayList<String> userPassCombo : loginList ){
            String currUser = userPassCombo.get(0);
            String currPass = userPassCombo.get(1);
            if (username.equals(currUser) && password.equals(currPass)) {
                isValidLogin = true;
                USER_ID = UserDAO.getUserId(username);
                USERNAME = username;
                break;
            }
        }

        if(isValidLogin){
            writeToLog(true);
            errorMsg.setText("");
            StageChangeUtils.changeStage(
                    actionEvent,
                    "/com/jbdev/schedulebooker/view_AllAppointments.fxml",
                    "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                    "All Appointments",
                    "",
                    null,
                    null
            );
        }
        else{
            writeToLog(false);
            if(lang.equalsIgnoreCase("english")){
                errorMsg.setText("Invalid username / password");
            }
            else if(lang.equalsIgnoreCase("français")){
                errorMsg.setText("Identifiant / mot de passe ne sont pas valide");
            }
        }
    }
}