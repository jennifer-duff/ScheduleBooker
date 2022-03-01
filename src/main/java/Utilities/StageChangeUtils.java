package Utilities;

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
import java.util.Objects;

/**
 * Utilities to change stages within the app
 */
public class StageChangeUtils {
    /**
     * A method for switching between stages
     *
     * @param actionEvent   The ActionEvent for the stage change
     * @param view          The path of the FXML file for the new stage
     * @param stylesheet    The path of the CSS stylesheet for new stage
     * @param taskbarTitle  The title that should be set in the taskbar for the new stage
     * @param screenTitle   The H1 heading of the new stage
     * @param app           The Appointment entity associated with the new stage (if not applicable, st to "null")
     * @param cust          The Customer entity associated with the new stage (if not applicable, st to "null")
     * @throws IOException  The exception thrown if the stage change does not work
     */
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


    /**
     * A method for showing a custom dialog box used to confirm a "Delete" action
     *
     * @param stage         The stage for the dialog box
     * @throws IOException  The exception thrown if showing the stage does not work
     */
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
