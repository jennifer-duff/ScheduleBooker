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

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.*;

public class AllAppointmentsByMonthController implements Initializable {
    @FXML private Label errorMsg;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private ComboBox<String> yearComboBox;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> months = FXCollections.observableArrayList();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        monthComboBox.setItems(months);

        //populate year combobox based on years in database
        ObservableList<String> years = FXCollections.observableArrayList();
        allAppointments = AppointmentDAO.getAllAppointments();
        for(Appointment app : allAppointments){
            LocalDateTime startDate = app.getStartDateTime();
            String appYear = String.valueOf(startDate.getYear());
            if(!years.contains(appYear)) {
                years.add(appYear);
            }
        }
        yearComboBox.setItems(years);
        //set comboBox automatically if there's only one year:
        if(years.size() == 1){
            yearComboBox.setValue(years.get(0));
        }

        try{
            updateTable();
        }
        catch(Exception error){
            return;
        }
    }
    
    public void updateTable(){
        ObservableList<Appointment> appsInMonth = FXCollections.observableArrayList();
        String pickedMonth = monthComboBox.getValue();
        String pickedYear = yearComboBox.getValue();
        if(pickedMonth == null || pickedYear == null){
            return;
        }

        allAppointments.forEach(app -> {
            LocalDateTime startDate = app.getStartDateTime();
            String appMonth = String.valueOf(startDate.getMonth());
            String appYear = String.valueOf(startDate.getYear());
            if(appMonth.equalsIgnoreCase(pickedMonth) && appYear.equalsIgnoreCase(pickedYear)){
                appsInMonth.add(app);
            }
        });

//        for(Appointment app : allAppointments){
//            LocalDateTime startDate = app.getStartDateTime();
//            String appMonth = String.valueOf(startDate.getMonth());
//            String appYear = String.valueOf(startDate.getYear());
//            if(appMonth.equalsIgnoreCase(pickedMonth) && appYear.equalsIgnoreCase(pickedYear)){
//                appsInMonth.add(app);
//            }
//        }

        if(appsInMonth.size() == 0){
            errorMsg.setText("Whoops, no appointments that month!");
        }
        else{
            errorMsg.setText("");
            appTable.setItems(appsInMonth);
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

    public void deleteApp(ActionEvent actionEvent){
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
}
