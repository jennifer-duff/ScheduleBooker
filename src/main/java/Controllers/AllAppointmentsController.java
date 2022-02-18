package Controllers;

import Database.AppointmentDAO;
import Utilities.StageChangeUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AllAppointmentsController implements Initializable {
    @FXML private Label msgLabel;
    @FXML private TableView <Appointment> appTable;
    @FXML private TableColumn <Appointment, Integer> appIdCol;
    @FXML private TableColumn <Appointment, String> titleCol;
    @FXML private TableColumn <Appointment, String> descriptionCol;
    @FXML private TableColumn <Appointment, String> typeCol;
    @FXML private TableColumn <Appointment, Integer> custIdCol;
    @FXML private TableColumn <Appointment, String> custNameCol;
    @FXML private TableColumn <Appointment, String> locationCol;
    @FXML private TableColumn <Appointment, LocalDateTime> startCol;
    @FXML private TableColumn <Appointment, LocalDateTime> endCol;
    @FXML private TableColumn <Appointment, Integer> contactCol;
    @FXML private TableColumn <Appointment, String> contactNameCol;
    @FXML private TableColumn <Appointment, Integer> userIdCol;
    ObservableList<Appointment> allAppointments;
    Stage stage;

    public void updateTable(){
        allAppointments = AppointmentDAO.getAllAppointments();
        appTable.setItems(allAppointments);
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        contactNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
    }

    public void viewCustomers(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_AllCustomers.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "All Customers",
                "",
                null,
                null
        );
    }

    public void viewContacts(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AllContacts.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "All Contacts",
            "",
            null,
            null
        );
    }

    public void addApp(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AddModifyAppointment.fxml",
            "/com/jbdev/schedulebooker/stylesheets/addModify.css",
            "Add Appointment",
            "Add Appointment",
            null,
            null
        );
    }

    public void modifyApp(ActionEvent actionEvent) throws IOException {
        ObservableList<Appointment> selectedItems = appTable.getSelectionModel().getSelectedItems();
        Appointment appointment = selectedItems.get(0);
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AddModifyAppointment.fxml",
            "/com/jbdev/schedulebooker/stylesheets/addModify.css",
            "Modify Appointment",
            "Modify Appointment",
            appointment,
            null
        );
    }

    public void deleteApp(ActionEvent actionEvent) throws IOException {
        ObservableList<Appointment> selectedItems = appTable.getSelectionModel().getSelectedItems();
        Appointment appointment = selectedItems.get(0);
        String appTitle = appointment.getTitle();

        StageChangeUtils.showDeleteDialog(stage);
        if (DialogController.wasDeleted){
            AppointmentDAO.deleteAppointment(appointment);
            updateTable();
            msgLabel.setText("Appointment \"" + appTitle + "\" was deleted");
        }
        else{
            msgLabel.setText("");
        }
        DialogController.wasDeleted = false;
    }
}
