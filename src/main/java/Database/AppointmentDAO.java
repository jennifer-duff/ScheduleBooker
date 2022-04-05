package Database;

import Controllers.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Appointment;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;

import static java.time.ZoneId.systemDefault;

public class AppointmentDAO {
    //initial DB operations:

    /**
     * SQL query to update the "Description" column of the database
     */
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


    /**
     * SQL query to update the "Start" and "End" columns of the database
     */
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


    /**
     * SQL query to update the default values in the database, so that customer's addresses are used as the location for all apps
     */
    public static void updateEntries(){
        ObservableList<Appointment> allApps =  getAllAppointments();
        for(Appointment app : allApps){
            int custId = app.getCustId();
            String fullLocation = CustomerDAO.getCustomerById(custId).getFullAddress();
            app.setLocation(fullLocation);
            modifyAppointment(app);
        }
    }


    /***
     * A method to convert from a DateTime from the local TimeZone to UTC TimeZone
     *
     * @param systemLocalDateTime   A DateTime in the user's local timezone, as determined by the system's settings
     * @return                      The DateTime of the user-inputted value, converted into UTC timezone
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static LocalDateTime convertToUTC(LocalDateTime systemLocalDateTime){
        //convert system LocalDateTime to ZonedLocalDateTime
        ZoneId systemZone = systemDefault();
        ZonedDateTime systemZonedTime = systemLocalDateTime.atZone(systemZone);

        //convert system Zonedtime to UTC zonedtime
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZonedDateTime utcZonedTime = systemZonedTime.withZoneSameInstant(utcZone);

        //get utc LocalDateTime from utcZonedTime
        LocalDateTime utcLocalDateTime = utcZonedTime.toLocalDateTime();

        return utcLocalDateTime;
    }

    /**
     * @return  A list of all appointments in the database
     */
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

                Timestamp systemEndDateTimestamp = resultSet.getTimestamp("End");
                LocalDateTime systemEndLocalDateTime = systemEndDateTimestamp.toLocalDateTime();

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


    /**
     * @param app   The appointment to add to the database
     */
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


    /**
     * @param app   The appointment to modify
     */
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


    /**
     * @param app   The appointment to delete from the database
     */
    public static void deleteAppointment(Appointment app) {
        try {
            String query = "DELETE FROM appointments WHERE Appointment_ID = " + app.getAppId();
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param contactId The ID of the contact to look up
     * @return          The name of the contact that has the inputted ID
     */
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


    /**
     * @return the ID that will be used for the next added Appointment (used to populate the ID field of the "Add Appointment" screen)
     */
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


    /**
     * @return  the last Appointment added to the database
     */
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


    /**
     * @param appId         The ID of the appointment that the user is attempting to Add/Modify
     * @param customerName  The name of the customer that the appointment is for
     * @param start         The start DateTime of the appointment
     * @param end           The end DateTime of the appointment
     * @return              A String indicating if the appointment overlaps w/ any others ("OVERLAP" or not "CLEAR)
     */
    public static String checkForOverlap(int appId, String customerName, LocalDateTime start, LocalDateTime end){
        try{
            String query = "SELECT * FROM appointments WHERE (timestamp(Start) BETWEEN \"" + start + "\" AND \"" + end + "\") OR (timestamp(End) BETWEEN \"" + start + "\" AND \"" + end + "\")";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
               int custId = resultSet.getInt("Customer_ID");
               String custName = CustomerDAO.getCustomerName(custId);
               if(custName.equals(customerName)){
                   int currAppId = resultSet.getInt("Appointment_ID");
                   if(appId != currAppId){
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

    /**
     * @return  A list of all appointment types
     */
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


    /**
     * @param type  An appointment type to look up
     * @return      The number of appointments in the database of the given type
     */
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


    /**
     * @return  An array containing sub-arrays that indicate the number of appointments in each month of the year(s)
     */
    public static ArrayList<ArrayList<String>> getAppMonths(){
        ArrayList<ArrayList<String>> appMonthCounts = new ArrayList<>();

        try{
            String query = "SELECT Start FROM appointments";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Timestamp startDate = resultSet.getTimestamp("Start");
                String yearMonth = new SimpleDateFormat("yyyy - MMMM").format(startDate);
                boolean wasFound = false;
                for (ArrayList<String> appMonthCount : appMonthCounts) {
                    if (appMonthCount.get(0).equalsIgnoreCase(yearMonth)) {
                        wasFound = true;
                        int currCount = Integer.parseInt(appMonthCount.get(1));
                        currCount++;
                        appMonthCount.set(1, String.valueOf(currCount));
                    }
                }
                if(!wasFound){
                    ArrayList<String> tempArray = new ArrayList<>();
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
