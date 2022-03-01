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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class Report_AppBreakdownsController implements Initializable {
    @FXML private TableView<AppType> typeTable;
    @FXML private TableColumn<AppType, String> appTypeCol;
    @FXML private TableColumn<AppType, Integer> appTypeCountCol;
    @FXML private TableView<AppMonth> monthTable;
    @FXML private TableColumn<AppMonth, String> appMonthCol;
    @FXML private TableColumn<AppMonth, Integer> appMonthCountCol;


    /**
     * Initializes the stage
     *
     * @param url               The URL to be used in the stage's initialization
     * @param resourceBundle    The ResourceBundle to be used in the stage's initialization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<AppType> appTypeCounts = FXCollections.observableArrayList();
        ObservableList<String> appTypes = AppointmentDAO.getAppointmentTypes();

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
     * Switches to the "Customers" tab
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Customers" tab
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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


    /**
     * Switches to the "Customer Schedules" screen
     *
     * @param actionEvent       The ActionEvent associated with the user pressing the "Customer Schedules" screen
     * @throws IOException      The Exception that is thrown if the stage change operation fails
     */
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
