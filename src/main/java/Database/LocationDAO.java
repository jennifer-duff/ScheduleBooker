package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDAO {

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

    public static String getCountry(int divisionId) {
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
}
