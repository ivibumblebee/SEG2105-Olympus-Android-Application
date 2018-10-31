package com.uottawa.olympus.olympusservices;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private String name;
    private double rate;
    private List<ServiceProvider> serviceProviders;

    Service(String name, double rate) {
        this.name = name;
        this.rate = rate;
        serviceProviders = new ArrayList<ServiceProvider>();
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

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
