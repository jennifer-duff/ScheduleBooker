package Controllers;

import Database.ContactDAO;
import Utilities.StageChangeUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AllContactsController implements Initializable {
    @FXML private Label msgLabel;
    @FXML private ComboBox<String> comboBox;
    @FXML private TableView<Appointment> contactAppsTable;
    @FXML private TableColumn<Appointment, Integer> appIdCol;
    @FXML private TableColumn <Appointment, String> titleCol;
    @FXML private TableColumn <Appointment, String> descriptionCol;
    @FXML private TableColumn <Appointment, String> typeCol;
    //    @FXML private TableColumn <Appointment, String> locationCol;
    @FXML private TableColumn <Appointment, LocalDateTime> startDateCol;
    @FXML private TableColumn <Appointment, LocalDateTime> startTimeCol;
    @FXML private TableColumn <Appointment, LocalDateTime> endDateCol;
    @FXML private TableColumn <Appointment, LocalDateTime> endTimeCol;
    @FXML private TableColumn <Appointment, Integer> custIdCol;
    @FXML private TableColumn <Appointment, String> custNameCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.setItems(ContactDAO.getAllContactNames());
    }

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

    public void populateTable(){
        String contactName = comboBox.getValue();
        ObservableList<Appointment> contactApps = ContactDAO.getContactApps(contactName);
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

        if(contactApps.size() == 0){
            msgLabel.setText("No appointments found for " + contactName);
        }
        else {
            msgLabel.setText("");
        }
    }

    public void viewAppBreakdown(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_AppBreakdowns.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "Reports | Appointment Breakdowns",
                "",
                null,
                null
        );
    }
}
