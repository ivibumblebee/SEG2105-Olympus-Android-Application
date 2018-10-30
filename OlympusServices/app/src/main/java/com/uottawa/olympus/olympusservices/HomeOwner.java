package com.uottawa.olympus.olympusservices;


public class HomeOwner extends UserType {

    HomeOwner(String username, String password, String firstname, String lastname){
        super(username, password, firstname, lastname);
    }

    public String getRole(){ return "HomeOwner"; }

}
