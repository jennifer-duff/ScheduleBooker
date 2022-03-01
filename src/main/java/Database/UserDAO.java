package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
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
