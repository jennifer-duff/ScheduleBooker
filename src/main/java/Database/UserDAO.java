package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {

    /**
     * @param rowCount  The number of rows that will be present in the list to be returned
     * @return          A list of sublists, containing each username/password combination
     */
    public static ArrayList<ArrayList<String>> getUsernamesAndPasswords(int rowCount) {
        ArrayList<ArrayList<String>> loginList = new ArrayList<ArrayList<String> >(rowCount);
        try {
            String query = "SELECT * FROM users";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ArrayList<String> userNameAndPassword = new ArrayList<String>();
                String username = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                userNameAndPassword.add(username);
                userNameAndPassword.add(password);
                loginList.add(userNameAndPassword);

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return loginList;
    }

    /**
     * @return          The number of rows in the "users" table
     */
    public static int getUserRowCount(){
        int rowCount = 0;
        try {
            String query = "SELECT COUNT(*) FROM users";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rowCount = resultSet.getInt("COUNT(*)");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }


    /**
     *
     * @param userName  The name of the user to look up
     * @return          The ID of the given user
     */
    public static int getUserId(String userName){
        int userId = 0;
        try {
            String query = "SELECT User_ID FROM users WHERE User_Name = \"" + userName + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userId = resultSet.getInt("User_ID");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
