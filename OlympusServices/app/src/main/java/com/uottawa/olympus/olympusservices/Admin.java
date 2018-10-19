package com.uottawa.olympus.olympusservices;

public class Admin extends UserType {

    Admin(){
        super("admin", "admin", "Admin", "Admin");
    }

    public String getRole(){ return "Admin"; }
}
