package com.uottawa.olympus.olympusservices;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider extends UserType {

    private List<Service> services;

    ServiceProvider(String username, String password, String firstname, String lastname){
        super(username, password, firstname, lastname);
        services = new ArrayList<>();
    }

    public String getRole(){ return "ServiceProvider"; }

    public boolean addService(Service service){
        for (Service listService : services){
            if (service.getName().equals(listService.getName())){
                return false;
            }
        }
        services.add(service);
        return true;
    }

    public List<Service> getServices(){
        return services;
    }

}
