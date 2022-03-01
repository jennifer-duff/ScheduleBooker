package Controllers;

import Database.CustomerDAO;
import Utilities.StageChangeUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Models.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AllCustomersController implements Initializable {
    @FXML private Label errorMsg;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> custIdCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> addressCol;
    @FXML private TableColumn<Customer, String> divisionCol;
    @FXML private TableColumn<Customer, String> zipCol;
    @FXML private TableColumn<Customer, String> countryCol;
    @FXML private TableColumn<Customer, String> phoneCol;

    Stage stage;


    /**
     * Updates the TableView with data for all customers in the database,
     */
    public void updateTable(){
        ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();
        customerTable.setItems(allCustomers);
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }


    /**
     * Switches to the "All Appointments" view
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "View All Appointments" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    @FXML
    public void viewApps(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AllAppointments.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "All Appointments",
            "",
            null,
            null
        );
    }


    /**
     * Switches to the "Reports" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Reports" tab
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    public void viewReports(ActionEvent actionEvent) throws IOException {
       StageChangeUtils.changeStage(
            actionEvent,
               "/com/jbdev/schedulebooker/view_Report_ContactSchedules.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "Reports | Contact Schedules",
            "",
            null,
            null
       );
    }


    /**
     * Switches to the "Add Customer" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Add Customer" button
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AddModifyCustomer.fxml",
            "/com/jbdev/schedulebooker/stylesheets/addModify.css",
            "Add Customer",
            "Add Customer",
            null,
            null
        );
    }


    /**
     * Switches to the "Modify Customer" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Modify Customer" button
     */
    public void modifyCustomer(ActionEvent actionEvent){
        try {
            errorMsg.setText("");
            ObservableList<Customer> selectedItems = customerTable.getSelectionModel().getSelectedItems();
            Customer customer = selectedItems.get(0);
            StageChangeUtils.changeStage(
                    actionEvent,
                    "/com/jbdev/schedulebooker/view_AddModifyCustomer.fxml",
                    "/com/jbdev/schedulebooker/stylesheets/addModify.css",
                    "Modify Customer",
                    "Modify Customer",
                    null,
                    customer
            );
        }
        catch (Exception error){
            errorMsg.setText("Whoops! Please select a row.");
        }
    }


    /**
     * Displays the "Delete Confirmation" dialog box.
     * If the user confirms the delete action, deletes the customer from the database*
     */
    public void deleteCustomer(){
        try{
            errorMsg.setText("");
            ObservableList<Customer> selectedItems = customerTable.getSelectionModel().getSelectedItems();
            Customer customer = selectedItems.get(0);
            String custName = customer.getName();

            StageChangeUtils.showDeleteDialog(stage);
            if (DialogController.wasDeleted){
                CustomerDAO.deleteCustomer(customer);
                updateTable();
                errorMsg.setText("Customer \"" + custName + "\" was deleted.");
            }
            else{
                errorMsg.setText("");
            }
            DialogController.wasDeleted = false;
        }
        catch(Exception error){
            errorMsg.setText("Whoops! Please select a row.");
        }

    }
}
