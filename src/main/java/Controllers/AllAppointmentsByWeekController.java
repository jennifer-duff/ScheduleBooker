package Controllers;

import Database.AppointmentDAO;
import Models.Appointment;
import Utilities.StageChangeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

public class AllAppointmentsByWeekController implements Initializable {
    @FXML private Label errorMsg;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Appointment> appTable;
    @FXML private TableColumn<Appointment, Integer> appIdCol;
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


    /**
     * Disables all DateCells except those that represent the first day of the week.
     * The first day of the week is based on the user's current location, as determined by the system settings
     *
     * LAMBDA EXPRESSION
     * This method contains a lambda expression. The expression is used to simplify the process of getting the first day
     * of every week, and then disabling the selection of every DateCell that is not on the first day of the week.
     * This wil allow the user to effecitvely specify a week number by choosing the first day of the desired week, as
     * executed in the updateTable() method.
     *
     * @return      A Datepicker used during initialization to create a dayCellFactory
     */
    private Callback<DatePicker, DateCell> disableNotFirstOfWeek(){

        // LAMBDA EXPRESSION
        return (final DatePicker datePicker1) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                //get first day of week
                DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

                // Disable all except first day of week
                if (item.getDayOfWeek() != firstDayOfWeek) {
                    setDisable(true);
                }
            }
        };
    }


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initializtion
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setShowWeekNumbers(true);
        Callback<DatePicker, DateCell> dayCellFactory = this.disableNotFirstOfWeek();
        datePicker.setDayCellFactory(dayCellFactory);

        updateTable();
    }


    /**
     * Updates the TableView such that it is populated only with appointments that occur during the user-selected week
     */
    public void updateTable(){
        LocalDate chosenDate = datePicker.getValue();

        if(chosenDate == null){
            return;
        }

        LocalDate endWeekDate = chosenDate.plusDays(6);

        ObservableList<Appointment> appsByWeek = FXCollections.observableArrayList();
        allAppointments = AppointmentDAO.getAllAppointments();
        for(Appointment app : allAppointments){
            LocalDate startDate = app.getStartDateTime().toLocalDate();
            if(startDate.isEqual(chosenDate) || startDate.isEqual(endWeekDate) || startDate.isAfter(chosenDate) && startDate.isBefore(endWeekDate)){
                appsByWeek.add(app);
            }
        }

        appTable.setItems(appsByWeek);
        if(appsByWeek.size() == 0){
            errorMsg.setText("Whoops, no appointments that week!");
        }
        else{
            errorMsg.setText("");
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
     * Switches to the "All Appointments" view
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "View All Appointments" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    @FXML
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
