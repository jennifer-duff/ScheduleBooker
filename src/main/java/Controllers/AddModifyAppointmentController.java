package Controllers;

import Database.AppointmentDAO;
import Database.ContactDAO;
import Database.CustomerDAO;
import Models.Customer;
import Utilities.StageChangeUtils;
import Utilities.PopulateFieldUtils;
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


public class AddModifyAppointmentController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private Label addModifyErrorMsg;
    @FXML private Label titleErrorMsg;
    @FXML private Label typeErrorMsg;
    @FXML private Label descriptionErrorMsg;
    @FXML private Label startErrorMsg;
    @FXML private Label endErrorMsg;
    @FXML private Label customerErrorMsg;
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
    @FXML private ComboBox<String> endHourPicker;
    @FXML private ComboBox<String> endMinutePicker;
    @FXML private ComboBox<String> customerNameComboBox;
    @FXML private TextField locationField;
    @FXML private TextField userIdField;
    @FXML private ComboBox<String> contactNameComboBox;

    String startDate;
    String startHour;
    String startMinute;

    String endDate;
    String endHour;
    String endMinute;

    String finalHour;
    int appId;


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        finalHour = PopulateFieldUtils.populateHourPicker(startHourPicker);
        PopulateFieldUtils.populateMinutePicker(startMinutePicker, startHourPicker, finalHour);
        PopulateFieldUtils.populateHourPicker(endHourPicker);
        PopulateFieldUtils.populateMinutePicker(endMinutePicker, endMinutePicker, finalHour);

        PopulateFieldUtils.populateCustomerPicker(customerNameComboBox);
        PopulateFieldUtils.populateContactPicker(contactNameComboBox);

        userIdField.setText(String.valueOf(LoginController.USER_ID));

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


    /**
     * Sets the H1 heading for the screen
     *
     * @param title         The H1 heading to be used as the title for the screen
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }


    /**
     * Populates the "hour" and "minute" ComboBoxes w/ the appropriate options.
     * Options are based on the company's opening/closing times, and are translated into the user's local time.
     * This prevents users from choosing appointment times outside the company's operating hours
     */
    public void updateComboBoxes(){
        PopulateFieldUtils.populateMinutePicker(startMinutePicker, startHourPicker, finalHour);
        PopulateFieldUtils.populateMinutePicker(endMinutePicker, endHourPicker, finalHour);
    }


    /**
     * Pre-populates the "Modify" screen with the appropriate values for the selected appointment
     *
     * @param app           The Appointment entity whose information will be preloaded into the "Modify" screen
     */
    public void setModifyValues(Appointment app){
        appIdField.setText(String.valueOf(app.getAppId()));
        titleField.setText(app.getTitle());
        typeField.setText(app.getType());
        descriptionField.setText(app.getDescription());
        startDatePicker.setValue(app.getStartDateTime().toLocalDate());
        String startTime = app.getStartDateTime().toLocalTime().toString();
        String startHour = startTime.substring(0, 2);
        String startMinute = startTime.substring(3);
        startHourPicker.setValue(startHour);
        startMinutePicker.setValue(startMinute);
//        TimeUtils.setModifyTimeValues(startTime, app, startHourPicker, startMinutePicker, startAmPmPicker);
        endDatePicker.setValue(app.getEndDateTime().toLocalDate());
        String endTime = app.getEndDateTime().toLocalTime().toString();
        String endHour = endTime.substring(0, 2);
        String endMinute = endTime.substring(3);
        endHourPicker.setValue(endHour);
        endMinutePicker.setValue(endMinute);
//        TimeUtils.setModifyTimeValues(endTime, app, endHourPicker, endMinutePicker, endAmPmPicker);
        int custId = app.getCustId();
        customerNameComboBox.setValue(CustomerDAO.getCustomerName(custId));
        locationField.setText(app.getLocation());
        userIdField.setText(String.valueOf(app.getUserId()));
        contactNameComboBox.setValue(ContactDAO.getContactName(app.getContactId()));
    }


    /**
     * Returns to the "All Appointments" screen
     *
     * @param actionEvent   The ActionEvent associated with the user hitting a button to return to the "All Appointments" screen
     * @throws IOException  The Exception associated with the stage change that is thrown if the operation fails
     */
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


    /**
     * Saves the appointment to the database
     *
     * @param actionEvent   The ActionEvent associated with the user hitting the "Save" button
     * @throws IOException  The Exception associated with saving the Appointment to the database that is thrown if the operation fails
     */
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
        appId = 0;
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String startDateString = startDate + " " + startHour + ":" + startMinute;
            startDateTime = LocalDateTime.parse(startDateString, formatter);

            endDate = endDatePicker.getValue().toString();
            endHour = endHourPicker.getValue();
            endMinute = endMinutePicker.getValue();
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


    /**
     * Validates user input and check for errors
     * @return              A String indicating whether the input is valid ("PASS") or not ("FAIL")
     */
    private String errorChecking(){
        boolean passesCheck = true;
        String requiredMsg = "* Required";

        //check to ensure all fields are filled out
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
        if(startDatePicker.getValue() == null || startHourPicker.getValue() == null || startMinutePicker.getValue() == null){
            startErrorMsg.setText("* All fields required");
            passesCheck = false;
        }
        if(endDatePicker.getValue() == null || endHourPicker.getValue() == null || endMinutePicker.getValue() == null){
            endErrorMsg.setText("* All fields required");
            passesCheck = false;
        }
        if(customerNameComboBox.getValue()== null){
            customerErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(contactNameComboBox.getValue() == null){
            contactErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        // check - if any of the above triggered, stop now & return an error msg
        if(!passesCheck){
            addModifyErrorMsg.setText("Whoops! Don't forget to fill out all fields.");
            return "FAIL";
        }

        //check to ensure customer doesn't already have an app scheduled for that DateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startString = startDate + " " + startHour + ":" + startMinute;
        LocalDateTime startLocalDateTime = LocalDateTime.parse(startString, formatter);
        LocalDateTime utcStart = AppointmentDAO.convertToUTC(startLocalDateTime);

        String endString = endDate + " " + endHour + ":" + endMinute;
        LocalDateTime endLocalDateTime= LocalDateTime.parse(endString, formatter);
        LocalDateTime utcEnd = AppointmentDAO.convertToUTC(endLocalDateTime);

        String customerName = customerNameComboBox.getValue();

        String result = AppointmentDAO.checkForOverlap(appId, customerName, utcStart, utcEnd);
        if(result.equals("CLEAR")){
            passesCheck = true;
        }
        else if (result.equals("OVERLAP")){
            addModifyErrorMsg.setText("Whoops! " + customerName + " already has an appointment scheduled at that time.");
            startErrorMsg.setText("* Double-check start date/time");
            endErrorMsg.setText("* Double-check end date/time");
            passesCheck = false;
            return "FAIL";
        }

        //Ensure end time is after start time
        if(endLocalDateTime.isAfter(startLocalDateTime)){
            passesCheck = true;
        }
        else if(endLocalDateTime.isEqual(startLocalDateTime)){
            addModifyErrorMsg.setText("Whoops! Appointment start and end times can't be the same");
            startErrorMsg.setText("* Double-check start date/time");
            endErrorMsg.setText("* Double-check end date/time");
            passesCheck = false;
            return "FAIL";
        }
        else if(endLocalDateTime.isBefore(startLocalDateTime)){
            addModifyErrorMsg.setText("Whoops! Appointments can't end before they start.");
            startErrorMsg.setText("* Double-check start date/time");
            endErrorMsg.setText("* Double-check end date/time");
            passesCheck = false;
            return "FAIL";
        }

        if(passesCheck){
            return "PASS";
        }
        else {
            return "FAIL";
        }
    }


    /**
     * Sets the "location" field of the "Add/Modify Appointment" screen to be the selected customer's full address
     */
    public void selectLocation(){
        String customerName = customerNameComboBox.getValue();
        int customerId = CustomerDAO.getCustomerId(customerName);
        Customer customer = CustomerDAO.getCustomerById(customerId);
        String address = customer.getFullAddress();
        locationField.setText(address);
    }
}
