package Controllers;

import Database.CustomerDAO;
import Utilities.StageChangeUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Models.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AllCustomersController implements Initializable {
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> custIdCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> addressCol;
    @FXML private TableColumn<Customer, String> divisionCol;
    @FXML private TableColumn<Customer, String> zipCol;
    @FXML private TableColumn<Customer, String> countryCol;
    @FXML private TableColumn<Customer, String> phoneCol;

    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            "All Contacts",
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
        StageChangeUtils.changeStage(
            actionEvent,
            "/com/jbdev/schedulebooker/view_AddModifyCustomer.fxml",
            "/com/jbdev/schedulebooker/stylesheets/addModify.css",
            "Modify Customer",
            "Modify Customer",
            null,
            null
        );
    }

    public void deleteCustomer(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.showDeleteDialog(stage);
    }


}
