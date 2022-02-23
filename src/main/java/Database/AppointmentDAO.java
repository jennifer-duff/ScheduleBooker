package Database;

import Controllers.LoginController;
import Models.AppMonth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentDAO {
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
                Timestamp startDateTimestamp = resultSet.getTimestamp("Start");
                Timestamp endDateTimestamp = resultSet.getTimestamp("End");
                LocalDateTime startDateTime = startDateTimestamp.toLocalDateTime();
                LocalDateTime endDateTime = endDateTimestamp.toLocalDateTime();
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(appId, title, description, type, custId, location,startDateTime, endDateTime, contactId, userId);
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
        LocalDateTime localEnd = app.getEndDateTime();
        int userId = app.getUserId();
        int contactID = app.getContactId();

        try {
            String values =
                    "NULL, \"" +                            //Appointment_ID
                    title + "\", \"" +                     //Title
                    description + "\", \"" +               //Description
                    location + "\", \"" +                   //Location
                    type + "\", \"" +                       //Type
                    localStart + "\", \""  +                //Start
                    localEnd + "\", \"" +                  //End
                    LocalDateTime.now() + "\", \"" +       //Create_Date
                    LoginController.USERNAME + "\", \"" +  //Created_By
                    LocalDateTime.now()  + "\", \"" +      //Last_Update
                    LoginController.USERNAME + "\", \"" +  //Last_Updated_By
                    custId + "\", \"" +                    //Customer_ID
                    userId + "\", \"" +                    //User_ID
                    contactID + "\"";                       //Contact_ID
            String query = "INSERT INTO appointments VALUES (" + values + ")";
            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifyAppointment (Appointment app){
//        String creationUser = "";
//        Timestamp creationDate = null;
//        try{
//            ResultSet resultSet = getLastResultSet();
//            while (resultSet.next()) {
//                creationDate = resultSet.getTimestamp("Create_Date");
//                creationUser = resultSet.getString("Created_By");
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
        int appId = app.getAppId();
        String title = app.getTitle();
        String description = app.getDescription();
        String type = app.getType();
        int custId = app.getCustId();
        String location = app.getLocation();
        LocalDateTime localStart = app.getStartDateTime();
        LocalDateTime localEnd = app.getEndDateTime();
        int userId = app.getUserId();
        int contactID = app.getContactId();
        try {
            String values =
                    "Appointment_ID = \"" + appId + "\", " +
                    "Title = \"" + title + "\", " +
                    "Description = \"" + description + "\", " +
                    "Location = \"" + location + "\", " +
                    "Type = \"" + type + "\", " +
                    "Start = \"" + localStart + "\", " +
                    "End = \"" + localEnd + "\", " +
//                            "Create_Date = \"" + creationDate + "\", " +
//                            "Created_By = \"User_" + creationUser + "\", " +
                    "Last_Update = \"" + LocalDateTime.now()  + "\", " +
                    "Last_Updated_By = \"User_" + userId + "\", " +
                    "Customer_ID = \"" + custId + "\", " +
                    "User_ID = \"" + userId + "\", " +
                    "Contact_ID = \"" + contactID + "\"";
//            System.out.println(values);
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
                String yearMonth = startDate.toString().substring(0,7);
//                String month = startDate.toString().substring(5,7);
//                String year = startDate.toString().substring(0, 4);
                //convert to abbrev.
//                month = convertMonthToAbbrev(month, year);
//                int count = 0;
//                if(!appMonthObjs.contains(month)){
//                    AppMonth appMonthObj = new AppMonth(month, count);
//                    appMonthObjs.add(appMonthObj);
//                }
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
