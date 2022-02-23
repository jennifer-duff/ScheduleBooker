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
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private Callback<DatePicker, DateCell> disableNotSunday(){
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable all except Sun
                if (item.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    setDisable(true);
//                        setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
        return dayCellFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setShowWeekNumbers(true);
        Callback<DatePicker, DateCell> dayCellFacotry = this.disableNotSunday();
        datePicker.setDayCellFactory(dayCellFacotry);

        try{
            updateTable();
        }
        catch(Exception error){
            return;
        }
    }
    
    public void updateTable(){
        ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();
        LocalDate weekStartLocal = null;
        try{
            weekStartLocal = datePicker.getValue();
        }
        catch(Exception error){
            return;
        }
        Date weekStart = Date.from(weekStartLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        ObservableList<Appointment> appsByWeek = FXCollections.observableArrayList();

        //get weeknum of weekStart
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(weekStart);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        allAppointments = AppointmentDAO.getAllAppointments();
        for(Appointment app : allAppointments){
            LocalDateTime startDate = app.getStartDateTime();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int currWeekNum = startDate.get(weekFields.weekOfWeekBasedYear());
            if(currWeekNum == weekOfYear){
                appsByWeek.add(app);
            }
        }
        if(appsByWeek.size() == 0){
            errorMsg.setText("Whoops, no appointments that week!");
        }
        else{
            errorMsg.setText("");
            appTable.setItems(appsByWeek);
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
