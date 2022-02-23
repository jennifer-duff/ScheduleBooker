package Controllers;

import Database.AppointmentDAO;
import Models.AppMonth;
import Models.Appointment;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppBreakdownsController implements Initializable {
    @FXML private TableView<AppType> typeTable;
    @FXML private TableColumn<Appointment, String> appTypeCol;
    @FXML private TableColumn<Appointment, Integer> appTypeCountCol;
    @FXML private TableView<AppMonth> monthTable;
    @FXML private TableColumn<Appointment, String> appMonthCol;
    @FXML private TableColumn<Appointment, Integer> appMonthCountCol;

    public static String convertMonthToAbbrev(String yearMonthString){
        String currYear = LocalDateTime.now().toString().substring(0, 4);
        System.out.println("Current year: " + currYear);
        String year = yearMonthString.substring(0, 4);
        String month = yearMonthString.substring(5, 7);

        switch(month) {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
            default:
                month = "Whoops! Error";
                break;
        }
        //account for apps next year
        if(!(year.equalsIgnoreCase(currYear))){
            month = month + " [" + year + "]";
        }
        return month;
    }

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

        ObservableList<AppMonth> allAppMonthObjs = FXCollections.observableArrayList();
        ArrayList<ArrayList<String>> appMonthCounts = AppointmentDAO.getAppMonths();
        for(ArrayList<String> appMonth : appMonthCounts){
            String monthyear = appMonth.get(0);
            monthyear = convertMonthToAbbrev(monthyear);
            int count = Integer.parseInt(appMonth.get(1));
            AppMonth appMonthObj = new AppMonth(monthyear, count);
            allAppMonthObjs.add(appMonthObj);
        }
        monthTable.setItems(allAppMonthObjs);
        appMonthCol.setCellValueFactory(new PropertyValueFactory<>("appMonthValue"));
        appMonthCountCol.setCellValueFactory(new PropertyValueFactory<>("appMonthCount"));
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
                "/com/jbdev/schedulebooker/view_AllContacts.fxml",
                "/com/jbdev/schedulebooker/stylesheets/mainTabPages.css",
                "Reports | Contact Schedules",
                "",
                null,
                null
        );
    }


}
