package Database;

import Controllers.LoginController;
import Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerDAO {
    /**
     * @return  A list of all customers in the database
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM Customers";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

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


    /**
     * @param custId    The ID of the customer to look up
     * @return          The name of the given customer
     */
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


    /**
     * @param custName  The name of the customer to look up
     * @return          The ID of the given customer
     */
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


    /**
     * @param custId    The ID of the customer to look up
     * @return          The Customer entity of the given customer
     */
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
        return customer;
    }


    /**
     * @return          The ID that will be used for the next customer added to the database
     */
    public static int getNextId(){
        int currId = 0;
        try{
            ResultSet resultSet = getLastResultSet();
            while (resultSet.next()) {
                currId = resultSet.getInt("Customer_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (currId + 1);
    }


    /**
     * @return          The Customer that was most recently added to the databse
     */
    public static ResultSet getLastResultSet(){
        ResultSet resultSet = null;
        try{
            String query = "SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 1";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    /**
     * @param cust      The customer to be added to the database
     */
    public static void addCustomer(Customer cust) {
        String name = cust.getName();
        String address = cust.getStreetAddress();
        int divisionId = cust.getDivisionId();
        String zip = cust.getZip();
        String phone = cust.getPhone();

        try {
            String values =
                    "NULL, \"" +
                            name + "\", \"" +
                            address + "\", \"" +
                            zip + "\", \"" +
                            phone + "\", \"" +
                            LocalDateTime.now() + "\", \"" +
                            "User_1\", \"" +
                            LocalDateTime.now() + "\", \"" +
                            "User_1\", " +
                            divisionId;

//            System.out.println(values);
            String query = "INSERT INTO customers VALUES (" + values + ")";
//            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param cust      The customer that will be modified
     */
    public static void modifyCustomer(Customer cust) {
        int custId = cust.getCustId();
        String name = cust.getName();
        String address = cust.getStreetAddress();
        int divisionId = cust.getDivisionId();
        String zip = cust.getZip();
        String phone = cust.getPhone();

        try {
            String values =
                    "Customer_Name = \"" + name + "\", " +
                    "Address = \"" + address + "\", " +
                    "Postal_Code = \"" + zip + "\", " +
                    "Phone = \"" + phone + "\", " +
                    "Last_Update = \"" + LocalDateTime.now() + "\", " +
                    "Last_Updated_By = \""  + LoginController.USERNAME + "\", " +
                    "Division_ID = " + divisionId;
            String query = "UPDATE customers SET " + values + " WHERE Customer_ID = " + custId;
//            System.out.println(query);
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param customer  The customer that will be deleted
     */
    public static void deleteCustomer(Customer customer) {
        //first, check if customer has any appointments and delete them
        ObservableList<Appointment> allapps = AppointmentDAO.getAllAppointments();
        for(Appointment app : allapps){
            if(app.getCustId() == customer.getCustId()) {
                AppointmentDAO.deleteAppointment(app);
            }
        }

        try {
            String query = "DELETE FROM customers WHERE Customer_ID = " + customer.getCustId();
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
