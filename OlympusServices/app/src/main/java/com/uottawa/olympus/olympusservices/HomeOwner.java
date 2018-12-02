package com.uottawa.olympus.olympusservices;


/**
 * The class HomeOwner is a child of the class userType. The class permission
 * to the user of a standard homeowner hence users with homeowner permissions
 * are able to book service providers. Has not been implemented yet.
 *
 */

public class HomeOwner extends UserType {

    /**
     * Constructor of the HomeOwner object that takes the username, password,
     * lastname, and firstname as parameters to use for the creation of a
     * HomeOwner object.
     *
     * @param username String for username.
     * @param password String for password.
     * @param firstname String for firstname.
     * @param lastname String for lastname.
     */
    HomeOwner(String username, String password, String firstname, String lastname){
        super(username, password, firstname, lastname);
    }

    /**
     * Constructor of the HomeOwner object that takes the username, password,
     * lastname, and firstname as parameters to use for the creation of a
     * HomeOwner object.
     *
     * @param username String for username.
     * @param hash String for hash.
     * @param salt String for salt.
     * @param firstname String for firstname.
     * @param lastname String for lastname.
     */
    HomeOwner(String username, String hash, String salt, String firstname, String lastname){
        super(username, hash, salt, firstname, lastname);
    }

    /**
     * Returns the type of role the user is for this class.
     * will return the string "HomeOwner".
     *
     * @return "HomeOwner" String object.
     */
    public String getRole(){ return "HomeOwner"; }

}
