package Controllers;

import Database.AppointmentDAO;
import Database.ContactDAO;
import Database.CustomerDAO;
import Utilities.StageChangeUtils;
import Utilities.TimeUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import Models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class AddModifyAppointment implements Initializable {
    @FXML private Label titleLabel;
    @FXML private Label addModifyErrorMsg;
    @FXML private Label titleErrorMsg;
    @FXML private Label typeErrorMsg;
    @FXML private Label descriptionErrorMsg;
    @FXML private Label startErrorMsg;
    @FXML private Label endErrorMsg;
    @FXML private Label customerErrorMsg;
//    @FXML private Label locationErrorMsg;
    @FXML private Label userErrorMsg;
    @FXML private Label contactErrorMsg;

    @FXML private TextField appIdField;
    @FXML private TextField titleField;
    @FXML private TextField typeField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> startHourPicker;
    @FXML private ComboBox<String> startMinutePicker;
    @FXML private ComboBox<String> startAmPmPicker;
    @FXML private ComboBox<String> endHourPicker;
    @FXML private ComboBox<String> endMinutePicker;
    @FXML private ComboBox<String> endAmPmPicker;
    @FXML private ComboBox<String> customerNameComboBox;
    @FXML private TextField locationField;
    @FXML private TextField userIdField;
    @FXML private ComboBox<String> contactNameComboBox;

    String startDate;
    String startHour;
    String startMinute;
    String startAmPm;

    String endDate;
    String endHour;
    String endMinute;
    String endAmPm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeUtils.populateHourPicker(startHourPicker);
        TimeUtils.populateMinutePicker(startMinutePicker);
        TimeUtils.populateAmPmPicker(startAmPmPicker);
        TimeUtils.populateHourPicker(endHourPicker);
        TimeUtils.populateMinutePicker(endMinutePicker);
        TimeUtils.populateAmPmPicker(endAmPmPicker);

        TimeUtils.populateCustomerPicker(customerNameComboBox);
        TimeUtils.populateContactPicker(contactNameComboBox);

        if(titleLabel.getText().equals("Add Appointment")){
            // set appId to next #
            int nextId = AppointmentDAO.getNextId();
            appIdField.setText(String.valueOf(nextId));
        }

        // shift focus from appId (since it's non-editable) to next field ("Title")
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                titleField.requestFocus();
            }
        });
    }

    public void setTitle(String title){
        titleLabel.setText(title);
    }

    public void setModifyValues(Appointment app){
        appIdField.setText(String.valueOf(app.getAppId()));
        titleField.setText(app.getTitle());
        typeField.setText(app.getType());
        descriptionField.setText(app.getDescription());
        startDatePicker.setValue(app.getStartDateTime().toLocalDate());
        String startTime = app.getStartDateTime().toLocalTime().toString();
        TimeUtils.setModifyTimeValues(startTime, app, startHourPicker, startMinutePicker, startAmPmPicker);
        endDatePicker.setValue(app.getEndDateTime().toLocalDate());
        String endTime = app.getEndDateTime().toLocalTime().toString();
        TimeUtils.setModifyTimeValues(endTime, app, endHourPicker, endMinutePicker, endAmPmPicker);
        int custId = app.getCustId();
        customerNameComboBox.setValue(CustomerDAO.getCustomerName(custId));
        locationField.setText(app.getLocation());
        userIdField.setText(String.valueOf(app.getUserId()));
        contactNameComboBox.setValue(ContactDAO.getContactName(app.getContactId()));
    }

    public void returnToApps(ActionEvent actionEvent) throws IOException {
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

    public void saveApp(ActionEvent actionEvent) throws IOException {
        //re-set error msg fields
        addModifyErrorMsg.setText("");
        titleErrorMsg.setText("");
        typeErrorMsg.setText("");
        descriptionErrorMsg.setText("");
        startErrorMsg.setText("");
        endErrorMsg.setText("");
        customerErrorMsg.setText("");
        userErrorMsg.setText("");
        contactErrorMsg.setText("");

        //Pull input values from fields
        int appId = 0;
        String title = "";
        String type = "";
        String description = "";
        String location = "";
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        int customerId = 0;
        int userId = 0;
        int contactId = 0;

        try {
            appId = Integer.parseInt(appIdField.getText());
            title = titleField.getText();
            type = typeField.getText();
            description = descriptionField.getText();
            location = locationField.getText();

            startDate = startDatePicker.getValue().toString();
            startHour = startHourPicker.getValue();
            startMinute = startMinutePicker.getValue();
            startAmPm = startAmPmPicker.getValue();
            startHour = TimeUtils.convertTo24Hr(startHour, startAmPm);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String startDateString = startDate + " " + startHour + ":" + startMinute;
            startDateTime = LocalDateTime.parse(startDateString, formatter);

            endDate = endDatePicker.getValue().toString();
            endHour = endHourPicker.getValue();
            endMinute = endMinutePicker.getValue();
            endAmPm = endAmPmPicker.getValue();
            endHour = TimeUtils.convertTo24Hr(endHour, endAmPm);
            String endDateString = endDate + " " + endHour + ":" + endMinute;
            endDateTime = LocalDateTime.parse(endDateString, formatter);

            String customerName = customerNameComboBox.getValue();
            customerId = CustomerDAO.getCustomerId(customerName);

            userId = Integer.parseInt(userIdField.getText());
            String contactName = contactNameComboBox.getValue();
            contactId = ContactDAO.getContactId(contactName);
        }
        catch(Exception error){
            System.out.println("Something went wrong!");
        }

        String checkStatus = errorChecking();
        if(checkStatus.equals("FAIL")){
            return;
        }

        Appointment app = new Appointment(appId, title, description, type, customerId, location, startDateTime, endDateTime, contactId, userId);

        if(titleLabel.getText().equals("Add Appointment")){
            AppointmentDAO.addAppointment(app);
        }
        else if(titleLabel.getText().equals("Modify Appointment")) {
            AppointmentDAO.modifyAppointment(app);
        }
        returnToApps(actionEvent);
    }

    //Error-checking
    private String errorChecking(){
        boolean passesCheck = true;
        String requiredMsg = "* Required";
        addModifyErrorMsg.setText("Whoops! Don't forget to fill out all fields.");

        if(titleField.getText().equals("")){
            titleErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(typeField.getText().equals("")){
            typeErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(descriptionField.getText().equals("")){
            descriptionErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(startDatePicker.getValue() == null || startHourPicker.getValue() == null || startMinutePicker.getValue() == null || startAmPmPicker.getValue() == null){
            startErrorMsg.setText("* All fields required");
            passesCheck = false;
        }
        if(endDatePicker.getValue() == null || endHourPicker.getValue() == null || endMinutePicker.getValue() == null || endAmPmPicker.getValue() == null){
            endErrorMsg.setText("* All fields required");
            passesCheck = false;
        }
        if(customerNameComboBox.getValue()== null){
            customerErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(!(userIdField.getText().equals("1"))){
            userErrorMsg.setText("* User ID must be \"1\"");
            passesCheck = false;
        }
        if(contactNameComboBox.getValue() == null){
            contactErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }

        if(passesCheck){
            return "PASS";
        }
        else {
            return "FAIL";
        }
    }
}
