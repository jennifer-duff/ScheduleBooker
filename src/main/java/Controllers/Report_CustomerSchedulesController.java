package Controllers;

import Database.AppointmentDAO;
import Database.CustomerDAO;
import Models.Appointment;
import Models.Customer;
import Utilities.StageChangeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class Report_CustomerSchedulesController implements Initializable {
    @FXML private Label msgLabel;
    @FXML private ComboBox<String> comboBox;
    @FXML private TableView<Appointment> contactAppsTable;
    @FXML private TableColumn<Appointment, Integer> appIdCol;
    @FXML private TableColumn <Appointment, String> titleCol;
    @FXML private TableColumn <Appointment, String> descriptionCol;
    @FXML private TableColumn <Appointment, String> typeCol;
    @FXML private TableColumn <Appointment, LocalDateTime> startDateCol;
    @FXML private TableColumn <Appointment, LocalDateTime> startTimeCol;
    @FXML private TableColumn <Appointment, LocalDateTime> endDateCol;
    @FXML private TableColumn <Appointment, LocalDateTime> endTimeCol;
    @FXML private TableColumn <Appointment, Integer> custIdCol;
    @FXML private TableColumn <Appointment, String> custNameCol;



    /**
     * Switches to the "Customers" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Customers" tab
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    @FXML
    public void viewCustomers(ActionEvent actionEvent) throws IOException {
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
     * Switches to the "Appointments" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Appointments" tab
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
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for(Customer customer : allCustomers){
            String custName = customer.getName();
            customerNames.add(custName);
        }
        comboBox.setItems(customerNames);
    }


    /**
     * Populates the table with only those appointments for the specified customer,
     * and sorts those appointments by Start Date and then Start Time
     */
    public void populateTable(){
        String customerName = comboBox.getValue();
        ObservableList<Appointment> allApps = AppointmentDAO.getAllAppointments();
        ObservableList<Appointment> contactApps = FXCollections.observableArrayList();
        for(Appointment app : allApps){
            if(app.getCustName().equals(customerName)){
                contactApps.add(app);
            }
        }
        contactAppsTable.setItems(contactApps);
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyStartDate"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyStartTime"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyEndDate"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("readOnlyEndTime"));

        startDateCol.setSortType(TableColumn.SortType.ASCENDING);
        startTimeCol.setSortType(TableColumn.SortType.ASCENDING);
        contactAppsTable.getSortOrder().setAll(startDateCol, startTimeCol);

        if(contactApps.size() == 0){
            msgLabel.setText("No appointments found for " + customerName);
        }
        else {
            msgLabel.setText("");
        }
    }


    /**
     * Switches to the "Appointment Breakdown" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Appointment Breakdown" screen
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    public void viewAppBreakdown(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_Report_AppBreakdowns.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "Reports | Appointment Breakdowns",
                "",
                null,
                null
        );
    }


    /**
     * Switches to the "Contact Schedules" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Contact Schedules" screen
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
    public void viewContactSchedules(ActionEvent actionEvent) throws IOException {
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
}
