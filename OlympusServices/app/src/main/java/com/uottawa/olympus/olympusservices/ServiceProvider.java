package com.uottawa.olympus.olympusservices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The service provider is the child userType class. The service provider
 * class provides services which the homeowner can buy in the app.
 *
 */

public class ServiceProvider extends UserType {

    //Field for list of services that service provider offers.
    private List<Service> services;
    //Field for array of availabilities
    /*
    DO NOT CHANGE THIS ARRAY. The 2D array size is hard coded such that the first array has a size
    of 7 and the array inside has an array size of 4. The array is setup such that the index represent a day of the
    week. Monday is 0, Tuesday is 1, Wednesday is 2, Thursday is 3, Friday is 4, Saturday is 5,
    Sunday is 6. so, [Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday]. Inside each of
    the arrays there is a another array containing int values such that index 0,1,2,3 is startHour,
    startMin, endHour, endMin respectively.
     */
    private int[][] availabilities;

    private String address;
    private String phonenumber;
    private String companyname;
    private boolean licensed;
    private String description;
    private int rating




    /**
     * Constructor for the service object which takes the parameters to
     * fill out the service providers field.
     *
     * @param username String of the username.
     * @param password String of the password.
     * @param firstname String of the firstname.
     * @param lastname String of the lastname.
     */
    ServiceProvider(String username, String password, String firstname, String lastname, String address,
                    String phonenumber, String companyname, boolean licensed){
        super(username, password, firstname, lastname);
        services = new ArrayList<>();
        availabilities = new int[7][4];
        this.address = address;
        this.phonenumber = phonenumber;
        this.companyname = companyname;
        this.licensed = licensed;
        this.description = "";
    }

    ServiceProvider(String username, String password, String firstname, String lastname, String address,
                    String phonenumber, String companyname, boolean licensed, String description){
        super(username, password, firstname, lastname);
        services = new ArrayList<>();
        availabilities = new int[7][4];
        this.address = address;
        this.phonenumber = phonenumber;
        this.companyname = companyname;
        this.licensed = licensed;
        this.description = description;
    }

    /**
     * gets the role of the UserType.
     *
     * @return String "Service Provider"
     */
    public String getRole(){ return "ServiceProvider"; }

    /**
     * adds service to the to service list if the service is in the
     * database list of services.
     *
     * @param service Service object which is added to the services field.
     * @return boolean whether service is added.
     */
    public boolean addService(Service service){
        for (Service listService : services){
            if (service.getName().equals(listService.getName())){
                return false;
            }
        }
        services.add(service);
        return true;
    }

    /**
     * Gets the services list field of service provider.
     *
     * @return arrayList of Services
     */



    public void setAvailabilities(int day, int startHour, int startMin, int endHour, int endMin){
        availabilities[day][0] = startHour;
        availabilities[day][1] = startMin;
        availabilities[day][2] = endHour;
        availabilities[day][3] = endMin;
    }

    public int[][] getAvailabilities(){
        return availabilities;
    }

    public void setAvailabilities(int[][] availabilities) {
        this.availabilities = availabilities;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServices(){
        return services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}