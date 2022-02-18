module com.jbdev.schedulebooker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.jbdev.schedulebooker to javafx.fxml, javafx.base, javafx.graphics;
    exports Controllers;
    exports Models;
    exports com.jbdev.schedulebooker;
    opens Controllers to javafx.fxml;
}