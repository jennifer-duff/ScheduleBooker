package Controllers;

import Utilities.StageChangeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Label loginLabel;
    @FXML private Label countryLabel;
    @FXML private Label errorMsg;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Button enterBtn;

    String lang;


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
            passwordLabel.setText("Mot de Passe");
            usernameLabel.setText("Identifiant");
            enterBtn.setText(" E n t r é e ");
        }
    }

    @FXML
    public void enterApp(ActionEvent actionEvent) throws IOException {
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
}