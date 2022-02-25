package Utilities;

import Controllers.DialogController;
import com.jbdev.schedulebooker.Main;
import Controllers.AddModifyAppointmentController;
import Controllers.AddModifyCustomerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Models.Appointment;
import Models.Customer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class StageChangeUtils {
    public static void changeStage(ActionEvent actionEvent, String view, String stylesheet, String taskbarTitle, String screenTitle, Appointment app, Customer cust) throws IOException {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
        Parent scene = fxmlLoader.load();
        if (screenTitle.equals("Add Appointment") || screenTitle.equals("Modify Appointment")){
            AddModifyAppointmentController controller = fxmlLoader.getController();
            controller.setTitle(screenTitle);
            if(screenTitle.equals("Modify Appointment")){
                controller.setModifyValues(app);
            }
        }
        else if (screenTitle.equals("Add Customer") || screenTitle.equals("Modify Customer")){
            AddModifyCustomerController controller = fxmlLoader.getController();
            controller.setTitle(screenTitle);
            if(screenTitle.equals("Modify Customer")){
                controller.setModifyValues(cust);
                controller.populateDivisionBox(cust.getCountry());
            }
        }
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/jbdev/schedulebooker/stylesheets/app.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(stylesheet)).toExternalForm());
        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/jbdev/schedulebooker/assets/scheduleBookerIcon.png")));
        stage.getIcons().add(icon);
        stage.setTitle(Main.APP_TITLE + "  |  " + taskbarTitle);
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }


    public static void showDeleteDialog(Stage stage) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view_dialog.fxml"));
        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/jbdev/schedulebooker/assets/scheduleBookerIcon.png")));
        newStage.getIcons().add(icon);
        newStage.setTitle("Delete Confirmation");
        newStage.setResizable(false);
        newStage.centerOnScreen();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(stage);
        newStage.setAlwaysOnTop(true);
        Parent scene = fxmlLoader.load();
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/jbdev/schedulebooker/stylesheets/app.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/jbdev/schedulebooker/stylesheets/dialog.css")).toExternalForm());
        newStage.setScene(new Scene(scene));
        newStage.showAndWait();
    }
}
