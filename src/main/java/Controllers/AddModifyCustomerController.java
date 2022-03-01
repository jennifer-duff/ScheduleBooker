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

import javax.xml.stream.Location;
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

    public void setTitle(String title){
        titleLabel.setText(title);
    }

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

    public void setModifyValues(Customer cust) {
        custIdField.setText(String.valueOf(cust.getCustId()));
        nameField.setText(cust.getName());
        addressField.setText(cust.getStreetAddress());
        countryComboBox.setValue(cust.getCountry());
        divisionComboBox.setValue(cust.getDivisionName());
        zipField.setText(cust.getZip());
        phoneField.setText(cust.getPhone());
    }

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

    public void populateDivisionBox(String countryName){
        int countryId = LocationDAO.getCountryId(countryName);
        ObservableList<String> countryDivisions = LocationDAO.getCountryDivNames(countryId);
        divisionComboBox.setItems(countryDivisions);
    }

    public void pullCountryName(){
        String countryName= countryComboBox.getValue();
        populateDivisionBox(countryName);
    }
}
