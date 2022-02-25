package Models;

import Database.LocationDAO;

public class Customer {
    private int custId;
    private String name;
    private String streetAddress;
    private int divisionId;
//    private String divisionName;
    private String zip;
    private String country;
    private String fullAddress;
    private String phone;

    public Customer(int custId, String name, String streetAddress, int divisionId, String zip, String phone) {
        this.custId = custId;
        this.name = name;
        this.streetAddress = streetAddress;
        this.divisionId = divisionId;
        this.zip = zip;
        this.phone = phone;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName(){
        return LocationDAO.getDivisionName(this.divisionId);
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry(){
        return LocationDAO.getCountryName(this.divisionId);
    }

    public String getFullAddress(){
        String divisionName = LocationDAO.getDivisionName(this.divisionId);
        return this.streetAddress + ", " + divisionName + ", " + this.zip +  ", " + this.getCountry();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
