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
    private int[][] availabilities;


    /**
     * Constructor for the service object which takes the parameters to
     * fill out the service providers field.
     *
     * @param username String of the username.
     * @param password String of the password.
     * @param firstname String of the firstname.
     * @param lastname String of the lastname.
     */
    ServiceProvider(String username, String password, String firstname, String lastname){
        super(username, password, firstname, lastname);
        services = new ArrayList<>();
        availabilities = new int[7][4];
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
    public List<Service> getServices(){
        return services;
    }


    public void setAvailabilities(int day, int startHour, int startMin, int endHour, int endMin){
        availabilities[day][0] = startHour;
        availabilities[day][1] = startMin;
        availabilities[day][2] = endHour;
        availabilities[day][3] = endMin;
    }

    public int[][] getAvailabilities(){
        return availabilities;
    }


}
