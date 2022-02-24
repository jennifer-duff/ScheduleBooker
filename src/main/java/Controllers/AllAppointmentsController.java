package Controllers;

import Database.AppointmentDAO;
import Utilities.StageChangeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class AllAppointmentsController implements Initializable {
    @FXML private Label errorMsg;
    @FXML private TableView <Appointment> appTable;
    @FXML private TableColumn <Appointment, Integer> appIdCol;
    @FXML private TableColumn <Appointment, String> titleCol;
    @FXML private TableColumn <Appointment, String> descriptionCol;
    @FXML private TableColumn <Appointment, String> typeCol;
    @FXML private TableColumn <Appointment, Integer> custIdCol;
    @FXML private TableColumn <Appointment, String> custNameCol;
    @FXML private TableColumn <Appointment, String> locationCol;
    @FXML private TableColumn <Appointment, String> startDateCol;
    @FXML private TableColumn <Appointment, String> startTimeCol;
    @FXML private TableColumn <Appointment, String> endDateCol;
    @FXML private TableColumn <Appointment, String> endTimeCol;
    @FXML private TableColumn <Appointment, Integer> contactCol;
    @FXML private TableColumn <Appointment, String> contactNameCol;
    @FXML private TableColumn <Appointment, Integer> userIdCol;
    ObservableList<Appointment> allAppointments;
    Stage stage;

    public void checkForApps() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15 = now.plusMinutes(15);
        for(Appointment app : allAppointments){
            LocalDateTime start = app.getStartDateTime();
            if(start.isEqual(now) || ( start.isAfter(now) && start.isBefore(nowPlus15) || start.isEqual(nowPlus15))){
//                System.out.println("UPCOMING APPOINTMENT!");
                if(!DialogController.notificationShown){
                    StageChangeUtils.showNotificationDialog(stage, app.getAppId(), app.getStartDateTime());
                }
            }
        }
    }

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
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyStartDate"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyStartTime"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyEndDate"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyEndTime"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        contactNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        startDateCol.setSortType(TableColumn.SortType.ASCENDING);
        startTimeCol.setSortType(TableColumn.SortType.ASCENDING);
        appTable.getSortOrder().setAll(startDateCol, startTimeCol);
    }

    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
        try {
            checkForApps();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            "Reports | Contact Schedules",
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
        try{
            errorMsg.setText("");
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
        catch(Exception error){
            errorMsg.setText("Whoops! Please select a row.");
        }

    }

    public void deleteApp(ActionEvent actionEvent) throws IOException {
        try {
            errorMsg.setText("");
            ObservableList<Appointment> selectedItems = appTable.getSelectionModel().getSelectedItems();
            Appointment appointment = selectedItems.get(0);
            String appTitle = appointment.getTitle();
            int appId = appointment.getAppId();
            String appType = appointment.getType();
            StageChangeUtils.showDeleteDialog(stage);
            if (DialogController.wasDeleted){
                AppointmentDAO.deleteAppointment(appointment);
                updateTable();
                errorMsg.setText(appType + " appointment \"" + appTitle + "\" (#" +  appId + ") was deleted.");
            }
            else{
                errorMsg.setText("");
            }
            DialogController.wasDeleted = false;
        }
        catch(Exception error){
            errorMsg.setText("Whoops! Please select a row.");
        }
    }

    public void viewApps(ActionEvent actionEvent) throws IOException {
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

    public void viewAppsByWeek(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_AllAppointmentsByWeek.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "All Appointments",
                "",
                null,
                null
        );
    }

    public void viewAppsByMonth(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_AllAppointmentsByMonth.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "All Appointments",
                "",
                null,
                null
        );
    }
}