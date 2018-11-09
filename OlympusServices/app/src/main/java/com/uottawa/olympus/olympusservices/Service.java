package com.uottawa.olympus.olympusservices;

import java.util.ArrayList;
import java.util.List;

/**
 * creates a service object that can be managed by admin and service providers
 * and be viewed by the homeowner for the sale of service which is the main
 * functionality of the app.
 *
 */

public class Service {
    //name of the service field.
    private String name;
    //rate of the service field.
    private double rate;
    //list of services the providers that offers this service.
    private List<ServiceProvider> serviceProviders;

    /**
     * Constructor for a new service which uses the parameters
     * to fill in the fields of the service and creates an arrayList
     * for all the providers that provide that service.
     *
     * @param name String of the name of service.
     * @param rate double of the price of service.
     */
    Service(String name, double rate) {
        this.name = name;
        this.rate = rate;
        serviceProviders = new ArrayList<ServiceProvider>();
    }

    /**
     * Gets the Name field of the service.
     *
     * @return String of the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Rate field of the service.
     *
     * @return double of the rate.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Changes the name field of the object to parameter given.
     *
     * @param name String object of new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the rate field of the object to parameter given.
     *
     * @param rate double of new rate.
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Adds a new service provider that offers this service into the list
     * of service providers that offer this service.
     *
     * @param user Service Provider object which is added to the service's list.
     * @return boolean of if the Service provider has been added.
     */
    public boolean addServiceProvider(ServiceProvider user){
        for (ServiceProvider listUser : serviceProviders){
            if (user.getUsername().equals(listUser.getUsername())){
                return false;
            }
        }
        serviceProviders.add(user);
        return true;
    }

    public List<ServiceProvider> getServiceProviders(){
        return serviceProviders;
    }
}
