package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDAO {

    public static void changeLocationCol(){
        try {
            String query = "ALTER TABLE appointments CHANGE COLUMN `Location` `Location` LONGTEXT";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDivisionName(int divisionId) {
        String divisionName = null;
        try {
            String query = "SELECT Division FROM First_Level_Divisions WHERE Division_ID = " + divisionId;
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                divisionName = resultSet.getString("Division");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionName;
    }

    public static int getDivisionId(String divisionName) {
        int divisionId = 0;
        try {
            String query = "SELECT * FROM first_level_divisions WHERE Division = \"" + divisionName + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                divisionId = resultSet.getInt("Division_ID");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionId;
    }

    public static String getCountryName(int divisionId) {
        int countryId = 0;
        String countryName = null;
        try {
            String divQuery = "SELECT Country_ID FROM First_Level_Divisions WHERE Division_ID = " + divisionId;
            PreparedStatement divStatement = DatabaseConnection.getConnection().prepareStatement(divQuery);
            ResultSet divResultSet = divStatement.executeQuery();
            while(divResultSet.next()) {
                countryId = divResultSet.getInt("Country_ID");
            }

            String countryQuery = "SELECT Country FROM countries WHERE Country_ID = " + countryId;
            PreparedStatement countryStatement = DatabaseConnection.getConnection().prepareStatement(countryQuery);
            ResultSet countryResultSet = countryStatement.executeQuery();
            while(countryResultSet.next()) {
                countryName = countryResultSet.getString("Country");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countryName;
    }

    public static int getCountryId(String countryName) {
        int countryId = 0;
        try {
            String divQuery = "SELECT Country_ID FROM countries WHERE Country = \"" + countryName + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(divQuery);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                countryId = resultSet.getInt("Country_ID");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countryId;
    }

    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        try {
            String query = "SELECT Country FROM countries";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String countryName = resultSet.getString("Country");
                allCountries.add(countryName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCountries;
    }

    public static ObservableList<String> getAllDivisionNames() {
        ObservableList<String> allDivisions = FXCollections.observableArrayList();
        try {
            String query = "SELECT Division FROM first_level_divisions";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String divisionName = resultSet.getString("Division");
                allDivisions.add(divisionName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allDivisions;
    }

    public static ObservableList<String> getCountryDivNames(int countryId){
        ObservableList<String> countryDivs = FXCollections.observableArrayList();
        try {
            String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = " + countryId;
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String divisionName = resultSet.getString("Division");
                countryDivs.add(divisionName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryDivs;
    }
}
