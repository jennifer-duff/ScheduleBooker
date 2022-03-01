package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    /**
     * The protocol used to connect to the database
     */
    private static final String protocol = "jdbc";

    /**
     * The type of database we're connecting to (i.e., the vendor)
     */
    private static final String vendor = ":mysql:";

    /**
     * The location of the database
     */
    private static final String location = "//localhost/";

    /**
     * The name of the database
     */
    private static final String databaseName = "client_schedule";

    /**
     * The URL that will be used to connect to the database
     */
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL

    /**
     * Reference to the database driver
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * Username for connecting to the database
     */
    private static final String userName = "sqlUser";

    /**
     * Password for connecting to the database
     */
    private static final String password = "Passw0rd!";

    /**
     * The Connection Interface
     */
    public static Connection connection;



    /**
     * A method to connect to the MySQL database
     */
    public static void openConnection(){
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception exception)
        {
            System.out.println("Error:" + exception.getMessage());
        }
    }


    /**
     * @return  The database connection
     */
    public static Connection getConnection(){
        return connection;
    }


    /**
     * A method to disconnect from the MySQL database
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception exception)
        {
            System.out.println("Error:" + exception.getMessage());
        }
    }
}
