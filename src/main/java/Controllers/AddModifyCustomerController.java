package Controllers;

import Database.AppointmentDAO;
import Database.CustomerDAO;
import Database.LocationDAO;
import Models.Appointment;
import Models.Customer;
import Utilities.StageChangeUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddModifyCustomerController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private TextField custIdField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> countryComboBox;
    @FXML private ComboBox<String> divisionComboBox;
    @FXML private TextField zipField;
    @FXML private TextField phoneField;

    @FXML private Label addModifyErrorMsg;
    @FXML private Label nameErrorMsg;
    @FXML private Label addressErrorMsg;
    @FXML private Label countryErrorMsg;
    @FXML private Label divisionErrorMsg;
    @FXML private Label zipErrorMsg;
    @FXML private Label phoneErrorMsg;



    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(titleLabel.getText().equals("Add Customer")){
            // set custId to next #
            int nextId = CustomerDAO.getNextId();
            custIdField.setText(String.valueOf(nextId));
        }

        ObservableList<String> allCountries = LocationDAO.getAllCountryNames();
        countryComboBox.setItems(allCountries);

        // shift focus from custId (since it's non-editable) to next field ("Name")
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nameField.requestFocus();
            }
        });
    }


    /**
     * Sets the H1 heading of the screen
     *
     * @param title         The H1 heading to be used as the title for the screen
     */
    public void setTitle(String title){
        titleLabel.setText(title);
    }


    /**
     * Returns the user to the "All Customers" screen
     *
     * @param actionEvent   The ActionEvent associated with the user hitting a button to return to the "All Customers" screen
     * @throws IOException  The Exception associated with the stage change that is thrown if the operation fails
     */
    public void returnToCustomers(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AllCustomers.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "All Customers",
            "",
            null,
            null
        );
    }


    /**
     * Pre-populates the "Modify" screen with the appropriate values for the selected customer
     *
     * @param cust          The Customer entity whose information will be preloaded into the "Modify" screen
     */
    public void setModifyValues(Customer cust) {
        custIdField.setText(String.valueOf(cust.getCustId()));
        nameField.setText(cust.getName());
        addressField.setText(cust.getStreetAddress());
        countryComboBox.setValue(cust.getCountry());
        divisionComboBox.setValue(cust.getDivisionName());
        zipField.setText(cust.getZip());
        phoneField.setText(cust.getPhone());
    }


    /**
     * Saves the customer to the database
     *
     * @param actionEvent   The ActionEvent associated with the user hitting the "Save" button
     * @throws IOException  The Exception associated with the Save event that is thrown if the operation fails
     */
    public void saveCustomer(ActionEvent actionEvent) throws IOException {
        //clear all error msgs
        addModifyErrorMsg.setText("");
        nameErrorMsg.setText("");
        addressErrorMsg.setText("");
        countryErrorMsg.setText("");
        divisionErrorMsg.setText("");
        zipErrorMsg.setText("");
        phoneErrorMsg.setText("");

        // pull input from fields
        int custId = 0;
        String name = null;
        String address = null;
        int divisionId = 0;
        String zip = null;
        String phone = null;
        try{
            custId = Integer.parseInt(custIdField.getText());
            name = nameField.getText();
            address = addressField.getText();
            String divisionName = divisionComboBox.getValue();
            divisionId = LocationDAO.getDivisionId(divisionName);
            zip = zipField.getText();
            phone = phoneField.getText();
        }
        catch (Exception error) {
            error.printStackTrace();
        }

        boolean passesCheck = errorChecking();

        if(passesCheck){
            Customer customer = new Customer(custId, name, address, divisionId, zip, phone);
            if(titleLabel.getText().equals("Add Customer")){
                CustomerDAO.addCustomer(customer);
            }
            else if(titleLabel.getText().equals("Modify Customer")){
                CustomerDAO.modifyCustomer(customer);
                //if customer has any apps, update them:
                ObservableList<Appointment> allApps = AppointmentDAO.getAllAppointments();
                for(Appointment app : allApps){
                    if(app.getCustId() == customer.getCustId()){
                        app.setCustName(customer.getName());
                        app.setLocation(customer.getFullAddress());
                        AppointmentDAO.modifyAppointment(app);
                    }
                }
            }
            returnToCustomers(actionEvent);
        }

    }


    /**
     * Validates user input, and displays error messages if any info is invalid and/or missing
     */
    public boolean errorChecking(){
        boolean passesCheck = true;
        String requiredMsg = "* Required";

        //check to ensure all fields are filled out
        if(nameField.getText().equals("")) {
            nameErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(addressField.getText().equals("")){
            addressErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(countryComboBox.getValue() == null){
            countryErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(divisionComboBox.getValue() == null){
            divisionErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(zipField.getText().equals("")){
            zipErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }
        if(phoneField.getText().equals("")){
            phoneErrorMsg.setText(requiredMsg);
            passesCheck = false;
        }

        if(!passesCheck){
            addModifyErrorMsg.setText("Whoops! Please fill out all fields");
            return passesCheck;
        }
        else{
            return passesCheck;
        }
    }


    /**
     * Populates the first-level-division ComboBox with the correct division names, based on the
     * country the users chooses
     *
     * @param countryName   The name of the country in which the customer resides
     */
    public void populateDivisionBox(String countryName){
        int countryId = LocationDAO.getCountryId(countryName);
        ObservableList<String> countryDivisions = LocationDAO.getCountryDivNames(countryId);
        divisionComboBox.setItems(countryDivisions);
    }


    /**
     * Pulls the country name from the input ComboBox, to be fed to the populateDivisionBox() method
     */
    public void pullCountryName(){
        String countryName= countryComboBox.getValue();
        populateDivisionBox(countryName);
    }
}
