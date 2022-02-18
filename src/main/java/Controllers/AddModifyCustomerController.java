package Controllers;

import Utilities.StageChangeUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AddModifyCustomerController {
    @FXML private Label titleLabel;

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

    public void saveCustomer(ActionEvent actionEvent) throws IOException {
        returnToCustomers(actionEvent);
    }
}
