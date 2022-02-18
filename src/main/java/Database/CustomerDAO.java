package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM Customers";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // 1) Gets the value from each column for one row at a time,
            // 2) then create a new Customer entity from the info,
            // 3) and finally add the new cust to the allCustomers list
            while (resultSet.next()) {
                int custId = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String streetAddress = resultSet.getString("Address");
                String zip = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_ID");
                Customer customer = new Customer(custId, name, streetAddress, divisionId, zip, phone);
                allCustomers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    public static String getCustomerName(int custId) {
        String customerName = null;
        try {
            String query = "SELECT Customer_Name FROM Customers WHERE Customer_ID = " + custId;
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerName = resultSet.getString("Customer_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerName;
    }

    public static int getCustomerId(String custName) {
//        System.out.print(custName + "   ID = ");
        int customerId = 0;
        try {
            String query = "SELECT Customer_ID FROM customers WHERE Customer_Name = \"" + custName + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerId = resultSet.getInt("Customer_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(customerId);
        return customerId;
    }

    public static Customer getCustomerById(int custId){
        Customer customer = null;
        try {
            String query = "SELECT * FROM customers WHERE Customer_ID = \"" + custId + "\"";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("Customer_Name");
                String streetAddress = resultSet.getString("Address");
                String zip = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_ID");
                customer = new Customer(custId, name, streetAddress, divisionId, zip, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(customerId);
        return customer;
    }

//    public static String getCustomerDivision(int divisionId){
//        String custDivision = "";
//            try {
//            String query = "SELECT Division FROM first_level_divisions WHERE Division_ID = " + divisionId;
//            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                custDivision = resultSet.getString("Division");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    //        System.out.println(customerId);
//        return custDivision;
//        }
//
//    public static String getCustomerCountry(int divisionId){
//        String custCountry = "";
//        int countryId = 0;
//        try {
//            String query = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = " + divisionId;
//            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                countryId = resultSet.getInt("Country_ID");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            String query = "SELECT Country FROM countries WHERE Country_ID = " + countryId;
//            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                custCountry = resultSet.getString("Country");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return custCountry;
//    }

}
