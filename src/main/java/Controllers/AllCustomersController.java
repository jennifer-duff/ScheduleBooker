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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

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

    public void viewContacts(ActionEvent actionEvent) throws IOException {
       StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AllContacts.fxml",
            "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
            "Reports | Contact Schedules",
            "",
            null,
            null
       );
    }

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

    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
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

    public void deleteCustomer(ActionEvent actionEvent) throws IOException {
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
