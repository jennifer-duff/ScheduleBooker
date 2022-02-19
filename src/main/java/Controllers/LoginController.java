package Controllers;

import Database.UserDAO;
import Utilities.StageChangeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
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
    public static String USERNAME;


    public void initialize(URL location, ResourceBundle resources) {
//        String systemDefaultZone = String.valueOf(systemDefault());
//        systemDefaultZone = systemDefaultZone.replace("_", " ");
//        systemDefaultZone = systemDefaultZone.replace("/", " / ");
//        countryLabel.setText(systemDefaultZone);

//        String userCountry = System.getProperty("user.country");
//        String userLang = System.getProperty("user.language");

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

    @FXML
    public void enterApp(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = usernameField.getText();
        boolean isValidLogin = false;
        int rowCount = UserDAO.getUserRowCount();
        ArrayList<ArrayList<String>> loginList = UserDAO.getUsernamesAndPasswords(rowCount);
//        System.out.println(loginList);
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
            if(lang.equalsIgnoreCase("english")){
                errorMsg.setText("Invalid username / password");
            }
            else if(lang.equalsIgnoreCase("français")){
                errorMsg.setText("Identifiant / mot de passe ne sont pas valide");
            }
        }


    }
}