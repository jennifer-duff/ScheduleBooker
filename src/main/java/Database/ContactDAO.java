package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ContactDAO {
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> allContacts = FXCollections.observableArrayList();
        try {
            String query = "SELECT Contact_Name FROM Contacts";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // 1) Gets the value from each column for one row at a time,
            // 2) then create a new Contact entity from the info,
            // 3) and finally add the new app to the allContacts list
            while (resultSet.next()) {
                String contactName = resultSet.getString("Contact_Name");
                allContacts.add(contactName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    public static int getContactId(String contactName){
        int contactId = 0;
        try{
            String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name = \"" + contactName + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                contactId = resultSet.getInt("Contact_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactId;
    }

    public static String getContactName(int contactId){
        String contactName = null;
        try{
            String query = "SELECT Contact_Name FROM contacts WHERE Contact_ID = \"" + contactId + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                contactName = resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactName;
    }

    public static ObservableList<Appointment> getContactApps(String contactName){
        ObservableList<Appointment> contactApps = FXCollections.observableArrayList();

        // get contactID first:
        int contactId = getContactId(contactName);

        //look up apps by contactID
        try{
            String query = "SELECT * FROM appointments WHERE Contact_ID = " + contactId;
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

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

                Appointment appointment = new Appointment(appId, title, description, type, custId, location,startDateTime, endDateTime, contactId, userId);
                contactApps.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactApps;
    }
}
