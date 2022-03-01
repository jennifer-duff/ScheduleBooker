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



    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initializtion
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
     * @param cust          The Customer entity whose information will be pre-loaded into the "Modify" screen
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
        int custId = Integer.parseInt(custIdField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        String divisionName = divisionComboBox.getValue();
        int divisionId = LocationDAO.getDivisionId(divisionName);
        String zip = zipField.getText();
        String phone = phoneField.getText();

        Customer customer = new Customer(custId, name, address, divisionId, zip, phone);

        if(titleLabel.getText().equals("Add Customer")){
            CustomerDAO.addCustomer(customer);
        }
        else if(titleLabel.getText().equals("Modify Customer")){
            CustomerDAO.modifyCustomer(customer);
            //if customer has any apps, update them:
            ObservableList<Appointment> allapps = AppointmentDAO.getAllAppointments();
            for(Appointment app : allapps){
                if(app.getCustId() == customer.getCustId()){
                    app.setCustName(customer.getName());
                    app.setLocation(customer.getFullAddress());
                    AppointmentDAO.modifyAppointment(app);
                }
            }
        }
        returnToCustomers(actionEvent);
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
