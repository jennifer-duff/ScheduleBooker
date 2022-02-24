package Database;

import Controllers.LoginController;
import Models.AppMonth;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointment;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;

import static java.time.ZoneId.systemDefault;

public class AppointmentDAO {

    //initial db operations
    public static void changeDescriptionCol(){
        try {
            String query = "ALTER TABLE appointments CHANGE COLUMN `Description` `Description` LONGTEXT";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeStartEndToTimestamp(){
        //OG type was 'datetime
        try {
            String query = "ALTER TABLE appointments CHANGE COLUMN `Start` `Start` timestamp;";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "ALTER TABLE appointments CHANGE COLUMN `End` `End` timestamp;";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateEntries(){
        ObservableList<Appointment> allApps =  getAllAppointments();
        for(Appointment app : allApps){
            modifyAppointment(app);
        }
    }

    public static LocalDateTime convertToUTC(LocalDateTime systemLocalDateTime){
        //convert system LocalDateTime to ZonedLocalDateTime
        ZoneId systemZone = systemDefault();
        ZonedDateTime systemZonedTime = systemLocalDateTime.atZone(systemZone);

        //convert system Zonedtime to UTC zonedtime
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZonedDateTime utcZonedTime = systemZonedTime.withZoneSameInstant(utcZone);

        //get utc LocalDateTime from utzZonedTime
        LocalDateTime utcLocalDateTime = utcZonedTime.toLocalDateTime();

        return utcLocalDateTime;
    }

    public static LocalDateTime convertFromUTC(LocalDateTime utcLocalDateTime){
        //convert utcLocalDateTime to ZonedDateTime
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZonedDateTime utcZonedTime = utcLocalDateTime.atZone(utcZone);

        //convert utcZonedTime to systemLocalTime
        ZoneId systemZone = systemDefault();
        ZonedDateTime systemZonedTime = utcZonedTime.withZoneSameInstant(systemZone);

        //get system LocalDateTime from system ZonedTime
        LocalDateTime systemLocalDateTime = systemZonedTime.toLocalDateTime();

        return systemLocalDateTime;
    }

    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try{
            String query = "SELECT * FROM appointments";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // 1) Gets the value from each column for one row at a time,
            // 2) then create a new appointment entity from the info,
            // 3) and finally add the new app to the allAppointments list
            while(resultSet.next()){
                int appId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                int custId = resultSet.getInt("Customer_ID");
                String location = resultSet.getString("Location");
                Timestamp systemStartTimestamp = resultSet.getTimestamp("Start");
                LocalDateTime systemStartLocalDateTime = systemStartTimestamp.toLocalDateTime();
//                Timestamp utcStartDateTimestamp = resultSet.getTimestamp("Start");
//                LocalDateTime utcStartLocalDateTime = utcStartDateTimestamp.toLocalDateTime();
//                LocalDateTime systemStartLocalDateTime = convertFromUTC(utcStartLocalDateTime);

                Timestamp systemEndDateTimestamp = resultSet.getTimestamp("End");
                LocalDateTime systemEndLocalDateTime = systemEndDateTimestamp.toLocalDateTime();
//                Timestamp utcEndDateTimestamp = resultSet.getTimestamp("End");
//                LocalDateTime utcEndLocalDateTime = utcEndDateTimestamp.toLocalDateTime();
//                LocalDateTime systemEndLocalDateTime = convertFromUTC(utcEndLocalDateTime);

                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(appId, title, description, type, custId, location, systemStartLocalDateTime, systemEndLocalDateTime, contactId, userId);
                allAppointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    public static void addAppointment(Appointment app){
        String title = app.getTitle();
        String description = app.getDescription();
        String type = app.getType();
        int custId = app.getCustId();
        String location = app.getLocation();

        LocalDateTime localStart = app.getStartDateTime();
        LocalDateTime utcStart = convertToUTC(localStart);            //convert to UTC for storage
        Timestamp timestampedStart = Timestamp.valueOf(utcStart);     //convert to TimeStamp for saving in database
//        Timestamp timestampedStart = Timestamp.valueOf(localStart);

        LocalDateTime localEnd = app.getEndDateTime();
        LocalDateTime utcEnd = convertToUTC(localEnd);
        Timestamp timestampedEnd = Timestamp.valueOf(utcEnd);         //convert to TimeStamp for saving in database:
//        Timestamp timestampedEnd = Timestamp.valueOf(localEnd);

        int userId = app.getUserId();
        int contactID = app.getContactId();

        try {
            String values =
                    "NULL, \"" +                           //Appointment_ID
                    title + "\", \"" +                     //Title
                    description + "\", \"" +               //Description
                    location + "\", \"" +                  //Location
                    type + "\", \"" +                      //Type
                    timestampedStart + "\", \""  +         //Start
                    timestampedEnd + "\", \"" +            //End
                    LocalDateTime.now() + "\", \"" +       //Create_Date
                    LoginController.USERNAME + "\", \"" +  //Created_By
                    LocalDateTime.now()  + "\", \"" +      //Last_Update
                    LoginController.USERNAME + "\", \"" +  //Last_Updated_By
                    custId + "\", \"" +                    //Customer_ID
                    userId + "\", \"" +                    //User_ID
                    contactID + "\"";                      //Contact_ID
            String query = "INSERT INTO appointments VALUES (" + values + ")";
            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifyAppointment (Appointment app){
        int appId = app.getAppId();
        String title = app.getTitle();
        String description = app.getDescription();
        String type = app.getType();
        int custId = app.getCustId();
        String location = app.getLocation();

        LocalDateTime localStart = app.getStartDateTime();
        LocalDateTime utcStart = convertToUTC(localStart);            //convert to UTC for storage
        Timestamp timestampedStart = Timestamp.valueOf(utcStart);     //convert to TimeStamp for saving in database
//        Timestamp timestampedStart = Timestamp.valueOf(localStart);

        LocalDateTime localEnd = app.getEndDateTime();
        LocalDateTime utcEnd = convertToUTC(localEnd);
        Timestamp timestampedEnd = Timestamp.valueOf(utcEnd);         //convert to TimeStamp for saving in database
//        Timestamp timestampedEnd = Timestamp.valueOf(localEnd);

        int userId = app.getUserId();
        int contactID = app.getContactId();
        try {
            String values =
                    "Appointment_ID = \"" + appId + "\", " +
                    "Title = \"" + title + "\", " +
                    "Description = \"" + description + "\", " +
                    "Location = \"" + location + "\", " +
                    "Type = \"" + type + "\", " +
                    "Start = \"" + timestampedStart + "\", " +
                    "End = \"" + timestampedEnd + "\", " +
                    "Last_Update = \"" + LocalDateTime.now()  + "\", " +
                    "Last_Updated_By = \"User_" + userId + "\", " +
                    "Customer_ID = \"" + custId + "\", " +
                    "User_ID = \"" + userId + "\", " +
                    "Contact_ID = \"" + contactID + "\"";
            String query = "UPDATE appointments SET " + values + " WHERE APPOINTMENT_ID = " + appId;
//            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAppointment(Appointment app) {
        try {
            String query = "DELETE FROM appointments WHERE Appointment_ID = " + app.getAppId();
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getContactName(int contactId){
        String contactName = null;
        try {
            String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = " + contactId;
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                contactName = resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactName;
    }

    public static int getNextId(){
        int currId = 0;
        try{
            ResultSet resultSet = getLastResultSet();
            while (resultSet.next()) {
                currId = resultSet.getInt("Appointment_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (currId + 1);
    }

    public static ResultSet getLastResultSet(){
        ResultSet resultSet = null;
        try{
            String query = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID DESC LIMIT 1";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static String checkForOverlap(int appId, String customerName, LocalDateTime start, LocalDateTime end){
        try{
            String query = "SELECT * FROM appointments WHERE (timestamp(Start) BETWEEN \"" + start + "\" AND \"" + end + "\") OR (timestamp(End) BETWEEN \"" + start + "\" AND \"" + end + "\")";
//            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
               int custId = resultSet.getInt("Customer_ID");
               String custName = CustomerDAO.getCustomerName(custId);
               if(custName.equals(customerName)){
                   int currappId = resultSet.getInt("Appointment_ID");
                   if(appId != currappId){
                       return "OVERLAP";
                   }
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CLEAR";
    }

    //For app breakdown stats:
    public static ObservableList<String> getAppointmentTypes(){
        ObservableList<String> appTypes = FXCollections.observableArrayList();
        try{
            String query = "SELECT DISTINCT Type FROM appointments";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String type = resultSet.getString("Type");
                appTypes.add(type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appTypes;
    }

    public static int getAppTypeCount(String type){
        int typeCount = 0;
        try{
            String query = "SELECT Type, COUNT(*) FROM appointments WHERE Type = \"" + type + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                typeCount = resultSet.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeCount;
    }

    public static ArrayList<ArrayList<String>> getAppMonths(){
        ArrayList<ArrayList<String>> appMonthCounts = new ArrayList<ArrayList<String>>();
        ObservableList<AppMonth> appMonthObjs = FXCollections.observableArrayList();

        try{
            String query = "SELECT Start FROM appointments";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Timestamp startDate = resultSet.getTimestamp("Start");
                String yearMonth = new SimpleDateFormat("yyyy - MMMM").format(startDate);
                boolean wasFound = false;
                for (int i = 0; i < appMonthCounts.size(); i++){
                    if(!appMonthCounts.get(i).get(0).equalsIgnoreCase(yearMonth)){
                        continue;
                    }
                    else{
                        wasFound = true;
                        int currCount = Integer.parseInt(appMonthCounts.get(i).get(1));
                        currCount++;
                        appMonthCounts.get(i).set(1, String.valueOf(currCount));
                    }
                }
                if(!wasFound){
                    ArrayList<String> tempArray = new ArrayList<String>();
                    tempArray.add(yearMonth);
                    tempArray.add("1");
                    appMonthCounts.add(tempArray);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appMonthCounts;
    }
}
