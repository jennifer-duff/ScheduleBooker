package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AppointmentDAO {
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
            String values = "NULL, \"" + title + "\" , \"" + description + "\" , \"" + location + "\", \"" + type + "\", \"" + localStart + "\", \""  + localEnd + "\" , \"" + LocalDateTime.now() + "\" , \"User_" + userId + "\" , \"" + LocalDateTime.now()  + "\" , \"User_" + userId + "\" , \"" +  custId + "\" , \""  + userId + "\" , \""  + contactID + "\"";
//            System.out.println(values);
            String query = "INSERT INTO appointments VALUES (" + values + ")";
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
            //String query = "SELECT Appointment_ID FROM appointments ORDER BY Appointment_ID DESC LIMIT 1";
            //PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
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
}
