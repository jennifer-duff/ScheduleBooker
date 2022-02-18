package Utilities;

import com.jbdev.schedulebooker.Main;
import Controllers.AddModifyAppointment;
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

public class StageChangeUtils {
    public static void changeStage(ActionEvent actionEvent, String view, String stylesheet, String taskbarTitle, String screenTitle, Appointment app, Customer cust) throws IOException {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
        Parent scene = fxmlLoader.load();
        if (screenTitle.equals("Add Appointment") || screenTitle.equals("Modify Appointment")){
            AddModifyAppointment controller = fxmlLoader.getController();
            controller.setTitle(screenTitle);
            if(screenTitle.equals("Modify Appointment")){
                controller.setModifyValues(app);
            }
        }
        else if (screenTitle.equals("Add Customer") || screenTitle.equals("Modify Customer")){
            AddModifyCustomerController controller = fxmlLoader.getController();
            controller.setTitle(screenTitle);
//            controller.setModifyValues(cust);
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

//    public static void changeStageModify(ActionEvent actionEvent, String view, String stylesheet, String taskbarTitle, String screenTitle, Appointment app) throws IOException {
//        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
//        Parent scene = fxmlLoader.load();
//            AddModifyAppointment controller = fxmlLoader.getController();
//            controller.setTitle(screenTitle);
//            controller.setModifyValues(app);
//        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/jbdev/schedulebooker/stylesheets/app.css")).toExternalForm());
//        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(stylesheet)).toExternalForm());
//        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/jbdev/schedulebooker/assets/scheduleBookerIcon.png")));
//        stage.getIcons().add(icon);
//        stage.setTitle(Main.APP_TITLE + "  |  " + taskbarTitle);
//        stage.setScene(new Scene(scene));
//        stage.setResizable(false);
//        stage.centerOnScreen();
//        stage.show();
//    }

//    public static void changeStageModify(ActionEvent actionEvent, String view, String stylesheet, String taskbarTitle, String screenTitle, Customer cust) throws IOException {
//        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(view));
//        Parent scene = fxmlLoader.load();
//        AddModifyCustomerController controller = fxmlLoader.getController();
//        controller.setTitle(screenTitle);
//        //controller.setModifyValues(cust);
//        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/com/jbdev/schedulebooker/stylesheets/app.css")).toExternalForm());
//        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource(stylesheet)).toExternalForm());
//        Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/com/jbdev/schedulebooker/assets/scheduleBookerIcon.png")));
//        stage.getIcons().add(icon);
//        stage.setTitle(Main.APP_TITLE + "  |  " + taskbarTitle);
//        stage.setScene(new Scene(scene));
//        stage.setResizable(false);
//        stage.centerOnScreen();
//        stage.show();
//    }

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
