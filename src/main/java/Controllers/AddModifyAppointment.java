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
        int appId = Integer.parseInt(appIdField.getText());
        String title = titleField.getText();
        String type = typeField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();

        String startDate = startDatePicker.getValue().toString();
        String startHour = startHourPicker.getValue();
        String startMinute = startMinutePicker.getValue();
        String startAmPm = startAmPmPicker.getValue();
        startHour = TimeUtils.convertTo24Hr(startHour, startAmPm);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startDateString = startDate+" "+startHour+":"+startMinute;
//        System.out.println(startDateString);
        LocalDateTime startDateTime = LocalDateTime.parse(startDateString, formatter);

        String endDate = endDatePicker.getValue().toString();
        String endHour = endHourPicker.getValue();
        String endMinute = endMinutePicker.getValue();
        String endAmPm = endAmPmPicker.getValue();
        endHour = TimeUtils.convertTo24Hr(endHour, endAmPm);
        String endDateString = endDate+" "+endHour+":"+endMinute;
        LocalDateTime endDateTime = LocalDateTime.parse(endDateString, formatter);

        String customerName = customerNameComboBox.getValue();
        int customerId = CustomerDAO.getCustomerId(customerName);

        int userID = Integer.parseInt(userIdField.getText());
        String contactName = contactNameComboBox.getValue();
        int contactId = ContactDAO.getContactId(contactName);

        Appointment app = new Appointment(appId, title, description, type, customerId, location, startDateTime, endDateTime, contactId, userID);

        if(titleLabel.getText().equals("Add Appointment")){
            AppointmentDAO.addAppointment(app);
        }
        else if(titleLabel.getText().equals("Modify Appointment")) {
            AppointmentDAO.modifyAppointment(app);
        }
        returnToApps(actionEvent);
    }


}
