package Controllers;

import Database.AppointmentDAO;
import Models.AppMonth;
import Models.AppType;
import Utilities.StageChangeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Report_AppBreakdownsController implements Initializable {
    @FXML private Label errorMsg;
    @FXML private TableView<AppType> typeTable;
    @FXML private TableColumn<AppType, String> appTypeCol;
    @FXML private TableColumn<AppType, Integer> appTypeCountCol;
    @FXML private TableView<AppMonth> monthTable;
    @FXML private TableColumn<AppMonth, String> appMonthCol;
    @FXML private TableColumn<AppMonth, Integer> appMonthCountCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<AppType> appTypeCounts = FXCollections.observableArrayList();
        ObservableList<String> appTypes = AppointmentDAO.getAppointmentTypes();

//        if(appTypes.size() == 0){
//            errorMsg.setText("No appointments found");
//            return;
//        }
//        else{
//            errorMsg.setText("");
//        }

        for(String type : appTypes){
            int typeCount = AppointmentDAO.getAppTypeCount(type);
            AppType appType = new AppType(type, typeCount);
            appTypeCounts.add(appType);
        }
        typeTable.setItems(appTypeCounts);
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("typeValue"));
        appTypeCountCol.setCellValueFactory(new PropertyValueFactory<>("typeCount"));

        appTypeCol.setSortType(TableColumn.SortType.ASCENDING);
        typeTable.getSortOrder().setAll(appTypeCol);


        ObservableList<AppMonth> allAppMonthObjs = FXCollections.observableArrayList();
        ArrayList<ArrayList<String>> appMonthCounts = AppointmentDAO.getAppMonths();
        for(ArrayList<String> appMonth : appMonthCounts){
            String monthyear = appMonth.get(0);
//            monthyear = convertMonthToAbbrev(monthyear);
            int count = Integer.parseInt(appMonth.get(1));
            AppMonth appMonthObj = new AppMonth(monthyear, count);
            allAppMonthObjs.add(appMonthObj);
        }
        monthTable.setItems(allAppMonthObjs);
        appMonthCol.setCellValueFactory(new PropertyValueFactory<>("appMonthValue"));
        appMonthCountCol.setCellValueFactory(new PropertyValueFactory<>("appMonthCount"));

        appMonthCol.setSortType(TableColumn.SortType.ASCENDING);
        monthTable.getSortOrder().setAll(appMonthCol);
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

    public void viewContacts(ActionEvent actionEvent) throws IOException {
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

    public void viewCustomerSchedules(ActionEvent actionEvent) throws IOException {
        StageChangeUtils.changeStage(
                actionEvent,
                "/com/jbdev/schedulebooker/view_Report_CustomerSchedules.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "Reports | Customer Schedules",
                "",
                null,
                null
        );
    }

}
