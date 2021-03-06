package com.jbdev.schedulebooker;

import Database.AppointmentDAO;
import Database.DatabaseConnection;
import Database.LocationDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static final String APP_TITLE = "ScheduleBooker";

    /**
     * Launches the program and opens the database connection
     *
     * @param args  Command-line arguments
     */
    public static void main(String[] args) {
        DatabaseConnection.openConnection();
        launch();
        DatabaseConnection.closeConnection();
    }


    /**
     * Sets up any universal variables / settings, and displays the login screen
     *
     * @param stage         The login stage
     * @throws IOException  The Exception that is thrown if the stage change operation fails
     */
    @Override
    public void start(Stage stage) throws IOException {
        LocationDAO.changeLocationCol();
        AppointmentDAO.changeDescriptionCol();
        AppointmentDAO.changeStartEndToTimestamp();
        AppointmentDAO.updateEntries();

        System.setProperty("prism.lcdtext", "false");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Font.loadFont(getClass().getResourceAsStream("/com/jbdev/schedulebooker/fonts/Manrope-ExtraBold.ttf"), 82);
        Font.loadFont(getClass().getResourceAsStream("/com/jbdev/schedulebooker/fonts/Manrope-SemiBold.ttf"), 82);
        Font.loadFont(getClass().getResourceAsStream("/com/jbdev/schedulebooker/fonts/Manrope-Regular.ttf"), 82);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/jbdev/schedulebooker/stylesheets/app.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/jbdev/schedulebooker/stylesheets/login.css")).toExternalForm());
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/jbdev/schedulebooker/assets/scheduleBookerIcon.png")));
        stage.getIcons().add(icon);
        stage.setTitle(APP_TITLE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
































