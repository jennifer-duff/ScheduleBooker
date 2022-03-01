package Models;

import Database.AppointmentDAO;
import Database.CustomerDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Appointment {
    private int appId;
    private String title;
    private String description;
    private String type;
    private int custId;
    private String custName;
    private String location;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int contactId;
    private String contactName;
    private int userId;

    /**
     * @param appId             the ID of the appointment
     * @param title             the title of the appointment
     * @param description       the description for the appointment
     * @param type              the appointment's type
     * @param custId            the ID of the appointment's customer
     * @param location          the location
     * @param startDateTime     the start date/time of the appointment
     * @param endDateTime       the end date/time of the appointment
     * @param contactId         the ID of the contact assigned to the appointment
     * @param userId            the ID of the user who created the appointment
     */
    public Appointment(int appId, String title, String description, String type, int custId, String location, LocalDateTime startDateTime, LocalDateTime endDateTime, int contactId, int userId) {
        this.appId = appId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.custId = custId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * @return the ID of the appointment
     */
    public int getAppId() {
        return appId;
    }


    /**
     * @param appId the Appointment ID to set for the appointment
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }


    /**
     * @return the title of the appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set for the appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * @return the description of the appointment
     */
    public String getDescription() {
        return description;
    }


    /**
     * @param description the description to set for the appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the appointment's type
     */
    public String getType(){
        return type;
    }


    /**
     * @param type the type to set for the appointment
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * @return the location of the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set for the appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the ID of the appointment's customer
     */
    public int getCustId() {
        return custId;
    }

    /**
     * @param custId the ID to set for the appointment's customer
     */
    public void setCustId(int custId) {
        this.custId = custId;
    }

    /**
     * @return the name of the appointment's customer
     */
    public String getCustName(){
        return CustomerDAO.getCustomerName(this.custId);
    }

    /**
     * @param custName the name to set for the appointment's customer
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the appointment's start date/time
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the start date/time to set for the appointment
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return a String version of the appointment's start date (used for easier table display)
     */
    public String getReadOnlyStartDate(){
        LocalDate startDate = this.startDateTime.toLocalDate();
        return String.valueOf(startDate);
    }

    /**
     * @return a String version of the appointment's start time (used for easier table display)
     */
    public String getReadOnlyStartTime(){
        LocalTime startTime = this.startDateTime.toLocalTime();
        return String.valueOf(startTime);
    }

    /**
     * @return the endd date/time of the appointment
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }


    /**
     * @param endDateTime the end date/time to set for the appointment
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return a String version of the appointment's end date (used for easier table display)
     */
    public String getReadOnlyEndDate(){
        LocalDate endDate = this.endDateTime.toLocalDate();
        String readOnlyEndDate = String.valueOf(endDate);
        return readOnlyEndDate;
    }

    /**
     * @return a String version of the appointment's end time (used for easier table display)
     */
    public String getReadOnlyEndTime(){
        LocalTime endTime = this.endDateTime.toLocalTime();
        String readOnlyEndTime = String.valueOf(endTime);
        return readOnlyEndTime;
    }

    /**
     * @return the ID of the user who created the appointment
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the ID of the user creating the appointment
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the ID of the contact assigned to the appointment
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId the ID of the contact to assign to the appointment
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return the name of the contact assigned to the appointment (used for easier table display)
     */
    public String getContactName(){
        return AppointmentDAO.getContactName(this.contactId);
    }
}
