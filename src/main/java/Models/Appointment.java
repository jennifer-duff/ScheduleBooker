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
    private String readOnlyStartDate;
    private String readOnlyStartTime;
    private LocalDateTime endDateTime;
    private String readOnlyEndDate;
    private String readOnlyEndTime;
    private int contactId;
    private String contactName;
    private int userId;

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

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName(){
        return CustomerDAO.getCustomerName(this.custId);
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getReadOnlyStartDate(){
        LocalDate startDate = this.startDateTime.toLocalDate();
        readOnlyStartDate = String.valueOf(startDate);
        return readOnlyStartDate;
    }

    public String getReadOnlyStartTime(){
        LocalTime startTime = this.startDateTime.toLocalTime();
        readOnlyStartTime = String.valueOf(startTime);
        return readOnlyStartTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getReadOnlyEndDate(){
        LocalDate endDate = this.endDateTime.toLocalDate();
        readOnlyEndDate = String.valueOf(endDate);
        return readOnlyEndDate;
    }

    public String getReadOnlyEndTime(){
        LocalTime endTime = this.endDateTime.toLocalTime();
        readOnlyEndTime = String.valueOf(endTime);
        return readOnlyEndTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName(){
        return AppointmentDAO.getContactName(this.contactId);
    }
}
