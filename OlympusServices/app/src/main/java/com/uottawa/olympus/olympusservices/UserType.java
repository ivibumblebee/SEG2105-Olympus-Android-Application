package com.uottawa.olympus.olympusservices;


import java.io.Serializable;

public abstract class UserType {

    String username;
    String password;
    String firstname;
    String lastname;




    UserType(String username, String password, String firstname, String lastname){
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public abstract String getRole();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }


    public void setUsername(String username) {
        //remember to call updateUser(String username, String password, String firstname, String lastname)
        //in activity whenever a setter is called. DBHelper requires a Context (Activity) to be initialized
        //so cannot be initialized in this class
        this.username = username;

    }

    public void setPassword(String password) {
        //remember to call updateUser(String username, String password, String firstname, String lastname)
        //in activity whenever a setter is called. DBHelper requires a Context (Activity) to be initialized
        //so cannot be initialized in this class
        this.password = password;
    }

    public void setFirstname(String firstname) {
        //remember to call updateUser(String username, String password, String firstname, String lastname)
        //in activity whenever a setter is called. DBHelper requires a Context (Activity) to be initialized
        //so cannot be initialized in this class
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        //remember to call updateUser(String username, String password, String firstname, String lastname)
        //in activity whenever a setter is called. DBHelper requires a Context (Activity) to be initialized
        //so cannot be initialized in this class
        this.lastname = lastname;
    }




    public boolean equals(UserType other){
        if(this.username.equals(other.username)&&this.password.equals(other.password)&&
                this.firstname.equals(other.firstname)&&this.lastname.equals(other.lastname)){
            return true;
        }
        return false;
    }
}
