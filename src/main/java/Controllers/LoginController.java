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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.time.ZoneId.systemDefault;

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


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId systemZone = systemDefault();
        countryLabel.setText(systemZone.toString());

        Locale locale = Locale.getDefault();
        lang = locale.getDisplayLanguage();
        if(lang.equalsIgnoreCase("français")){
            loginLabel.setText("Se Connecter");
            usernameLabel.setText("Identifiant");
            passwordLabel.setText("Mot de Passe");
            enterBtn.setText(" E N T R É E ");
        }
    }


    /**
     * Records the login attempt to the "login_activity.txt" file
     *
     * @param isSuccessful  Used to determine whether the login was successful, so that the method can log the
     *                      attempt appropriately
     */
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
            String filename = "login_activity.txt";
            FileWriter fw = new FileWriter(filename,true);  //the true will append the new data
            fw.write(string);                                       //appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public Boolean checkCredentials(){
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
        return isValidLogin;
    }


    /**
     * Logs into the application, and presents the first screen ("All Appointments")
     *
     * @param actionEvent   The ActionEvent associated with the user hitting the "Enter" button
     * @throws IOException  The Exception that is thrown if the stage change is unsuccessful
     */
    @FXML
    public void enterApp(ActionEvent actionEvent) throws IOException {
        Boolean isValidLogin = checkCredentials();

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































