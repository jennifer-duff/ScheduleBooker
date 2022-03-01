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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class AllAppointmentsController implements Initializable {
    @FXML private AnchorPane noUpcomingAppsPopup;
    @FXML private AnchorPane upcomingAppPopup;
    @FXML private Label appIdLabel;
    @FXML private Label startDateLabel;
    @FXML private Label startTimeLabel;

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
    public static Boolean checkedForApps = false;


    /**
     * Checks for upcoming appointments upon login.
     * If there are any appointments due to begin with the next 15 minutes, a message is shown displaying the
     * appointment ID, start date, and start time.
     * Else, a message is shown stating that there are no upcoming appointments
     */
    public void checkForApps(){
        if(checkedForApps){
            noUpcomingAppsPopup.setVisible(false);
            upcomingAppPopup.setVisible(false);
        }
        else{
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowPlus15 = now.plusMinutes(15);
            for(Appointment app : allAppointments){
                LocalDateTime start = app.getStartDateTime();
                if(start.isEqual(now) || ( start.isAfter(now) && start.isBefore(nowPlus15) || start.isEqual(nowPlus15))){
                    appIdLabel.setText(String.valueOf(app.getAppId()));
                    startDateLabel.setText(app.getReadOnlyStartDate());
                    startTimeLabel.setText(app.getReadOnlyStartTime());
                    upcomingAppPopup.setVisible(true);
                    checkedForApps = true;
                    return;
                }
                else{
                    noUpcomingAppsPopup.setVisible(true);
                }
            }
        }
        checkedForApps = true;
    }


    /**
     * Updates the TableView with data for all appointments in the database,
     * and sorts the table by appointment Start Date and then Start Time
     */
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


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initializtion
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
        checkForApps();
    }


    /**
     * Switches to the "Customers" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Customers" tab
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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


    /**
     * Switches to the "Reports" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Reports" tab
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    public void viewReports(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
                "/com/jbdev/schedulebooker/view_Report_ContactSchedules.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "Reports | Contact Schedules",
            "",
            null,
            null
        );
    }


    /**
     * Switches to the "Add Appointment" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Add Appointment" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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


    /**
     * Switches to the "Modify Appointment" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Modify Appointment" button
     */
    public void modifyApp(ActionEvent actionEvent){
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


    /**
     * Displays the Delete Confirmation dialog box.
     * If the user confirms the delete action, deletes the appointment from the database*
     */
    public void deleteApp(){
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


    /**
     * Switches to the "Appointments By Week" view
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "View Appointments By Week" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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


    /**
     * Switches to the "Appointments By Month" view
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "View Appointments By Month" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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