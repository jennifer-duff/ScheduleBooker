package Utilities;

import Database.ContactDAO;
import Database.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import Models.Customer;

import java.time.*;
import java.util.*;

import static java.time.ZoneId.systemDefault;

/**
 * Utilities to pre-populate ComboBoxes on the Add/Modify Appointment screens
 */
public class PopulateFieldUtils {
    /**
     * @param   comboBox the ComboBox used to pick the start or end hour of an appointment on the "Add/Modify Appointment" screen
     * @return  The final hour in the list (e.g., closing time) - Used to prevent users from scheduling apps in that last hour
     *          (i.e., prevent scheduling for times like 22:15EST, 22:30EST, etc.). Value is passed to populateMinutePicker() to handle this.
     */
    public static String populateHourPicker(ComboBox<String> comboBox){
        ObservableList<String> hourArray = FXCollections.observableArrayList();
        ArrayList<Integer> hourArrayInts = new ArrayList<>();

        for(int i = 8; i <= 22; i++){
            //set time in EST
            LocalDate date = LocalDate.now();
            LocalTime easternTime = LocalTime.of(i, 0);
            LocalDateTime easternDateTime = LocalDateTime.of(date, easternTime);
            ZoneId easternZone = ZoneId.of("America/New_York");
            ZonedDateTime easternZonedTime = easternDateTime.atZone(easternZone);

            //convert from EST zonedtime to local zonedtime
            ZoneId localZone = systemDefault();
            ZonedDateTime localZonedTime = easternZonedTime.withZoneSameInstant(localZone);

            //get LocalDateTime from local zonedtime
            LocalDateTime localDateTime = localZonedTime.toLocalDateTime();

            //extract time from localDateTime
            LocalTime localTime = localDateTime.toLocalTime();
            int hour = Integer.parseInt(localTime.toString().substring(0, 2));
            hourArrayInts.add(hour);
        }

        //sort the hours
        Collections.sort(hourArrayInts);

        //convert the sorted hours back to Strings, and add to final hourArray ObservableList
        for(int hour : hourArrayInts){
            String hourString = String.valueOf(hour);
            if(hourString.equals("0") || hourString.equals("1") || hourString.equals("2") || hourString.equals("3") || hourString.equals("4") || hourString.equals("5") | hourString.equals("6") || hourString.equals("7") || hourString.equals("8") || hourString.equals("9")){
                hourString = "0" + hourString;
            }
            hourArray.add(hourString);
        }

        //set the comboBox with the hourArray list
        comboBox.setItems(hourArray);
        //return the final hour, so we can prevent users from scheduling apps in that last hour
        //(e.g, things like 22:15EST, 22:30EST, etc.) -> handled in populateMinutePicker()
        return hourArray.get(hourArray.size() - 1);
    }


    /**
     * @param minuteComboBox    the ComboBox used to pick the start or end minute value of an appointment on the "Add/Modify Appointment" screen
     * @param hourComboBox      the ComboBox used to pick the start or end hour of an appointment on the "Add/Modify Appointment" screen
     * @param finalHour         Closing time - Used to prevent users from scheduling apps in that last hour (i.e., prevent scheduling for times like 22:15EST, 22:30EST, etc.)
     */
    public static void populateMinutePicker(ComboBox<String> minuteComboBox, ComboBox<String> hourComboBox, String finalHour){
        ObservableList<String> minuteArray = FXCollections.observableArrayList();
        minuteArray.add("00");
        minuteArray.add("15");
        minuteArray.add("30");
        minuteArray.add("45");

        if(hourComboBox.getValue() == null){
            minuteComboBox.setItems(minuteArray);
        }
        else if(hourComboBox.getValue().equals(finalHour)) {
            ObservableList<String> altMinuteArray = FXCollections.observableArrayList();
            altMinuteArray.add("00");
            minuteComboBox.setItems(altMinuteArray);
            minuteComboBox.setValue(altMinuteArray.get(0));
        }
        else {
            minuteComboBox.setItems(minuteArray);
        }
    }


    /**
     * @param comboBox  The ComboBox used to pick the customer's name on the "Add/Modify Appointment" screen
     */
    public static void populateCustomerPicker(ComboBox<String> comboBox){
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();
        for(Customer cust : allCustomers){
            String custName = cust.getName();
            customerNames.add(custName);
        }
        comboBox.setItems(customerNames);
    }


    /**
     * @param comboBox  The ComboBox used to pick the contact's name on the "Add/Modify Appointment" screen
     */
    public static void populateContactPicker(ComboBox<String> comboBox){
        ObservableList<String> allContactNames = ContactDAO.getAllContactNames();
        comboBox.setItems(allContactNames);
    }
}
