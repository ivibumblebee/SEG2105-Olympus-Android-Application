package com.uottawa.olympus.olympusservices;

/**
 * The class Admin is a child of the class userType. The class gives
 * admin permission over the app such that the user can add and delete services
 * and can add and delete other users from the database of the app.
 *
 */

public class Admin extends UserType {

    /**
     * The constructor for the admin object with predefined fields.
     * There should only be one admin object for the entire app.
     */
    Admin(){
        super("admin", "admin", "Admin", "Admin");
    }

    Admin(String hash, String salt){
        super("admin", hash, salt, "Admin", "Admin");
    }

    /**
     * The getRole() method returns a string "Admin"
     * the app gets role of user type objects for access
     * app permission purposes.
     *
     * @return String object "Admin"
     */
    public String getRole(){ return "Admin"; }
}
