package Utilities;

import Controllers.LoginController;
import Database.AppointmentDAO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tests {
    public static final LoginController loginController = new LoginController();
    public static final File file = new File("login_activity.txt");

    /**
     * Utility function to retrieve the latest login attempt record
     *
     * @param file - the file to read (i.e., login_attempt.txt)
     * @return lastLine - the last line of the file (i.e., the latest login attempt record)
     */
    public static String tail(File file) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if( filePointer == fileLength ) {
                        continue;
                    }
                    break;

                } else if( readByte == 0xD ) {
                    if( filePointer == fileLength - 1 ) {
                        continue;
                    }
                    break;
                }

                sb.append( ( char ) readByte );
            }

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch( IOException e ) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileHandler != null )
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    /* ignore */
                }
        }
    }

    public static class WriteToLogTests{
        /* Check login_activity.txt - should show most recent login attempt as initiated by
           by user "Test Username"
        */
        public static void shouldRecordUsername(String username, Boolean isSuccessful){
            loginController.USERNAME = username;
            if(loginController.USERNAME == null){
                loginController.USERNAME = "NULL";
            }
            loginController.writeToLog(isSuccessful);
            String lastLine = tail(file);
            String loggedUsername = "";
            try{
                String splitLastLine = Objects.requireNonNull(lastLine).substring(lastLine.indexOf("\"") + 1);
                loggedUsername = splitLastLine.substring(0, splitLastLine.indexOf('"'));
            }
            catch(Exception error){
                System.out.println("shouldRecordUsername = FAIL");
                return;
            }

            if(loginController.USERNAME.equals(loggedUsername)){
                System.out.println("shouldRecordUsername = PASS");
            }
            else{
                System.out.println("shouldRecordUsername = FAIL");
            }
        }

        public static void shouldRecordDate(Boolean isSuccessful){
            loginController.writeToLog(isSuccessful);
            String today = String.valueOf(LocalDate.now());
            String lastLine = tail(file);
            if(Objects.requireNonNull(lastLine).contains(today)){
                System.out.println("shouldRecordDate = PASS");
            }
            else{
                System.out.println("shouldRecordDate = FAIL");
            }
        }

        public static void shouldRecordTime(Boolean isSuccessful){
            loginController.writeToLog(isSuccessful);
            LocalDateTime now = LocalDateTime.now();
            String stringNow= String.valueOf(AppointmentDAO.convertToUTC(now).toLocalTime()).substring(0, 8);
            String lastLine = tail(file);
            if(Objects.requireNonNull(lastLine).contains(stringNow)){
                System.out.println("shouldRecordTime = PASS");
            }
            else{
                System.out.println("shouldRecordTime = FAIL");
            }
        }

        public static void shouldRecordSuccess(Boolean isSuccessful){
            loginController.writeToLog(isSuccessful);
            String lastLine = tail(file);
            if(isSuccessful && lastLine.contains(" SUCCESSFUL")){
                System.out.println("shouldRecordSuccess(successful) = PASS");
            }
            else if (!isSuccessful && lastLine.contains(" UNSUCCESSFUL")){
                System.out.println("shouldRecordSuccess(unsuccessful) = PASS");
            }
            else{
                System.out.println("shouldRecordSuccess = FAIL");
            }
        }
    }
}
