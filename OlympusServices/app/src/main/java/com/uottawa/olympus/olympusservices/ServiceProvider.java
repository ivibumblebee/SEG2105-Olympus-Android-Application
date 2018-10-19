package com.uottawa.olympus.olympusservices;

public class ServiceProvider extends UserType {

    ServiceProvider(String username, String password, String firstname, String lastname){
        super(username, password, firstname, lastname);
    }

    public String getRole(){ return "ServiceProvider"; }


}
