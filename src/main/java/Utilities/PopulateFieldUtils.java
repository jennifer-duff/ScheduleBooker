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

public class PopulateFieldUtils {
    public static String populateHourPicker(ComboBox<String> comboBox){
        ObservableList<String> hourArray = FXCollections.observableArrayList();
        ArrayList<Integer> hourArrayInts = new ArrayList<Integer>();

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
        //(e.g, things like 22:15EST, 22:30EST, etc.) -> handled on the other end
        return hourArray.get(hourArray.size() - 1);
    }

    public static void populateMinutePicker(ComboBox<String> minutecomboBox, ComboBox<String> hourcomboBox, String finalHour){
        ObservableList<String> minuteArray = FXCollections.observableArrayList();
        minuteArray.add("00");
        minuteArray.add("15");
        minuteArray.add("30");
        minuteArray.add("45");

        if(hourcomboBox.getValue() == null){
            minutecomboBox.setItems(minuteArray);
        }
        else if(hourcomboBox.getValue().equals(finalHour)) {
            ObservableList<String> altMinuteArray = FXCollections.observableArrayList();
            altMinuteArray.add("00");
            minutecomboBox.setItems(altMinuteArray);
            minutecomboBox.setValue(altMinuteArray.get(0));
        }
        else {
            minutecomboBox.setItems(minuteArray);
        }
    }

//    public static void populateAmPmPicker(ComboBox<String> comboBox){
//        ObservableList<String> amPmArray = FXCollections.observableArrayList();
//        amPmArray.add("am");
//        amPmArray.add("pm");
//        comboBox.setItems(amPmArray);
//    }
//    public static String convertTo24Hr(String hour, String amPm){
//        if(amPm.equalsIgnoreCase("PM") && !hour.equals("12")){
//            int hourInt = Integer.parseInt(hour);
//            hourInt = hourInt + 12;
//            hour = String.valueOf(hourInt);
//        }
//        if(amPm.equalsIgnoreCase("AM") && hour.equals("12")){
//            hour = "00";
//        }
//        return hour;
//    }

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

//    public static void setModifyTimeValues(String dateTime, Appointment app, ComboBox<String> hourPicker, ComboBox<String> minutePicker, ComboBox<String> amPmPicker){
//        String hour = dateTime.substring(0, 2);
//        int intHour = Integer.parseInt(hour);
//        //00:00 -> 12:00am
//        if(intHour == 0){
////            System.out.println(intHour + " is in the MORNING");
//            hour = "12";
//            amPmPicker.setValue("am");
//        }
//
//        // 01:00, 02:00, 03:00, 04:00, 05:00, 06:00, 07:00, 08:00, 09:00 -> stays the same
//        if(intHour <= 11) {
////            System.out.println(intHour + " is in the MORNING");
//            amPmPicker.setValue("am");
//        }
//
//        //12:00pm -> stays the same
//        else if(intHour == 12){
////            System.out.println(intHour + " is in the EVENING");
//            amPmPicker.setValue("pm");
//        }
//
//        // 13:00,    14:00,    15:00,    16:00,    17:00,    18:00,    19:00,    20:00,    21:00 ->
//        // 01:00 pm, 02:00 pm, 03:00 pm, 04:00 pm, 05:00 pm, 06:00 pm, 07:00 pm, 08:00 pm, 09:00pm
//        else if (intHour > 12 && intHour <= 21) {
////            System.out.println(intHour + " is in the EVENING");
//            intHour = intHour - 12;
//            hour = "0" + String.valueOf(intHour);
//            amPmPicker.setValue("pm");
//        }
//        // 22:00,    23:00 ->
//        // 10:00 pm, 11:00 pm
//        else if (intHour > 21) {
////            System.out.println(intHour + " is in the EVENING");
//            intHour = intHour - 12;
//            hour = String.valueOf(intHour);
//            amPmPicker.setValue("pm");
//        }
//        hourPicker.setValue(hour);
//        String minute = dateTime.substring(3);
////        System.out.println(minute);
//        minutePicker.setValue(minute);
//    }

}
