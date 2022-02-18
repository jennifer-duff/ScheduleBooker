package Utilities;

import Database.ContactDAO;
import Database.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import Models.Appointment;
import Models.Customer;

public class TimeUtils {
    public static void populateHourPicker(ComboBox<String> comboBox){
        ObservableList<String> hourArray = FXCollections.observableArrayList();
        hourArray.add("01");
        hourArray.add("02");
        hourArray.add("03");
        hourArray.add("04");
        hourArray.add("05");
        hourArray.add("06");
        hourArray.add("07");
        hourArray.add("08");
        hourArray.add("09");
        hourArray.add("10");
        hourArray.add("11");
        hourArray.add("12");
        comboBox.setItems(hourArray);
    }

    public static void populateMinutePicker(ComboBox<String> comboBox){
        ObservableList<String> minuteArray = FXCollections.observableArrayList();
        minuteArray.add("00");
        minuteArray.add("15");
        minuteArray.add("30");
        minuteArray.add("45");
        comboBox.setItems(minuteArray);
    }
    public static void populateAmPmPicker(ComboBox<String> comboBox){
        ObservableList<String> amPmArray = FXCollections.observableArrayList();
        amPmArray.add("am");
        amPmArray.add("pm");
        comboBox.setItems(amPmArray);
    }
    public static String convertTo24Hr(String hour, String amPm){
        if(amPm.equalsIgnoreCase("PM") && !hour.equals("12")){
            int hourInt = Integer.parseInt(hour);
            hourInt = hourInt + 12;
            hour = String.valueOf(hourInt);
        }
        if(amPm.equalsIgnoreCase("AM") && hour.equals("12")){
            hour = "00";
        }
        return hour;
    }

    public static void populateCustomerPicker(ComboBox<String> comboBox){
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();
        for(Customer cust : allCustomers){
            String custName = cust.getName();
            customerNames.add(custName);
        }
        comboBox.setItems(customerNames);
    }

    public static void populateContactPicker(ComboBox<String> comboBox){
        ObservableList<String> allContactNames = ContactDAO.getAllContactNames();
        comboBox.setItems(allContactNames);
    }

    public static void setModifyTimeValues(String dateTime, Appointment app, ComboBox<String> hourPicker, ComboBox<String> minutePicker, ComboBox<String> amPmPicker){
        String hour = dateTime.substring(0, 2);
        int intHour = Integer.parseInt(hour);
//        System.out.println(intHour);
        //00:00 -> 12:00am
        if(intHour == 0){
//            System.out.println(intHour + " is in the MORNING");
            hour = "12";
            amPmPicker.setValue("am");
        }

        // 01:00, 02:00, 03:00, 04:00, 05:00, 06:00, 07:00, 08:00, 09:00 -> stays the same
        if(intHour <= 11) {
//            System.out.println(intHour + " is in the MORNING");
            amPmPicker.setValue("am");
        }

        //12:00pm -> stays the same
        else if(intHour == 12){
//            System.out.println(intHour + " is in the EVENING");
            amPmPicker.setValue("pm");
        }

        // 13:00,    14:00,    15:00,    16:00,    17:00,    18:00,    19:00,    20:00,    21:00 ->
        // 01:00 pm, 02:00 pm, 03:00 pm, 04:00 pm, 05:00 pm, 06:00 pm, 07:00 pm, 08:00 pm, 09:00pm
        else if (intHour > 12 && intHour <= 21) {
//            System.out.println(intHour + " is in the EVENING");
            intHour = intHour - 12;
            hour = "0" + String.valueOf(intHour);
            amPmPicker.setValue("pm");
        }
        // 22:00,    23:00 ->
        // 10:00 pm, 11:00 pm
        else if (intHour > 21) {
//            System.out.println(intHour + " is in the EVENING");
            intHour = intHour - 12;
            hour = String.valueOf(intHour);
            amPmPicker.setValue("pm");
        }
        hourPicker.setValue(hour);
        String minute = dateTime.substring(3);
//        System.out.println(minute);
        minutePicker.setValue(minute);
    }

}
