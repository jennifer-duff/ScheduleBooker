package Models;

import Database.LocationDAO;

public class Customer {
    private int custId;
    private String name;
    private String streetAddress;
    private int divisionId;
    private String zip;
    private String country;
    private String fullAddress;
    private String phone;

    /**
     * @param custId            the ID of the customer
     * @param name              the customer's first+last name
     * @param streetAddress     the customer's street address + city (e.g., "346 Evergreen Drive, Seattle")
     * @param divisionId        the ID of the customer's first-level-division
     * @param zip               the customer's zip
     * @param phone             the customer's phone number
     */
    public Customer(int custId, String name, String streetAddress, int divisionId, String zip, String phone) {
        this.custId = custId;
        this.name = name;
        this.streetAddress = streetAddress;
        this.divisionId = divisionId;
        this.zip = zip;
        this.phone = phone;
    }

    /**
     * @return the ID of the customer
     */
    public int getCustId() {
        return custId;
    }

    /**
     * @param custId the ID to set for the customer
     */
    public void setCustId(int custId) {
        this.custId = custId;
    }

    /**
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set for the customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the customer's street address + city
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * @param streetAddress the street address + city to set for the customer
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * @return the customer's first-level-division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the first-level-division ID to set for the customer
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the name of the customer's first-level-division ID (used for Table displays)
     */
    public String getDivisionName(){
        return LocationDAO.getDivisionName(this.divisionId);
    }

    /**
     * @return the customer's zip code
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip code to set for the customer
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the customer's country (used for Table displays)
     */
    public String getCountry(){
        return LocationDAO.getCountryName(this.divisionId);
    }

    /**
     * @return A String representation of the customer's full address (street address + division name + country name) - used for Table displays
     */
    public String getFullAddress(){
        String divisionName = LocationDAO.getDivisionName(this.divisionId);
        return this.streetAddress + ", " + divisionName + ", " + this.zip +  ", " + this.getCountry();
    }

    /**
     * @return the customer's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set for the customer
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
