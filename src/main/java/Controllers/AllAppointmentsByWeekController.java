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
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
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

    // TODO: javadoc this lambda expression
    private Callback<DatePicker, DateCell> disableNotFirstOfWeek(){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setShowWeekNumbers(true);
        Callback<DatePicker, DateCell> dayCellFactory = this.disableNotFirstOfWeek();
        datePicker.setDayCellFactory(dayCellFactory);

        updateTable();
//        try{
//            updateTable();
//        }
//        catch(Exception error) {
//            error.printStackTrace();
//        }
    }
    
    public void updateTable(){
        Locale locale = new Locale("en");
        LocalDate chosenDate = datePicker.getValue();

        if(chosenDate == null){
            return;
        }

        LocalDate endWeekDate = chosenDate.plusDays(6);

//        int chosenWeekNum = chosenDate.get(WeekFields.of(locale).weekOfYear());
//        int chosenWeekNum = chosenDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
//        int chosenWeekNum = chosenDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

//        Date weekStart = Date.from(weekStartLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        ObservableList<Appointment> appsByWeek = FXCollections.observableArrayList();

        //get weeknum of weekStart
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(weekStart);
//        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        allAppointments = AppointmentDAO.getAllAppointments();

//        allAppointments.forEach(app -> {
//            LocalDateTime startDate = app.getStartDateTime();
//            int currWeekNum = startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
//
//            if(currWeekNum == chosenWeekNum){
//                appsByWeek.add(app);
//            }
//        });

        for(Appointment app : allAppointments){
            LocalDate startDate = app.getStartDateTime().toLocalDate();
//            int currWeekNum = startDate.get(WeekFields.of(locale).weekOfYear());
//            int currWeekNum = startDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
//            int currWeekNum = startDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
//            if(currWeekNum == chosenWeekNum){
//                appsByWeek.add(app);
//            }
            if(startDate.isEqual(chosenDate) || startDate.isEqual(endWeekDate) || startDate.isAfter(chosenDate) && startDate.isBefore(endWeekDate)){
                appsByWeek.add(app);
            }
        }

//        for(Appointment app : allAppointments){
//            LocalDateTime startDate = app.getStartDateTime();
//            WeekFields weekFields = WeekFields.of(Locale.getDefault());
//            int currWeekNum = startDate.get(weekFields.weekOfWeekBasedYear());
//            if(currWeekNum == weekOfYear){
//                appsByWeek.add(app);
//            }
//        }

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
                "/com/jbdev/schedulebooker/view_Report_ContactSchedules.fxml",
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
