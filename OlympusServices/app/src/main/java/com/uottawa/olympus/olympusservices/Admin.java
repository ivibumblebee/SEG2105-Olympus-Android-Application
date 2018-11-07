package com.uottawa.olympus.olympusservices;

/**
 * This class allows the app to create admin object which is a child of the UserType class.
 * The admin object has administrator control over the app. A user logging in with
 * admin can delete and edit user profiles and delete and edit services.
 *
 */

public class Admin extends UserType {

    /**
     * Constructs an admin object such that the admin
     * has a constant username and password of admin and admin.
     * And the admin has a constant first name and last name of Admin.
     * there should only be one admin object.
     */
    Admin(){
        super("admin", "admin", "Admin", "Admin");
    }

    /**
     * This method is a method gets the string object describing the
     * role of the object.
     *
     * @return String "admin"
     */
    public String getRole(){ return "Admin"; }
}
