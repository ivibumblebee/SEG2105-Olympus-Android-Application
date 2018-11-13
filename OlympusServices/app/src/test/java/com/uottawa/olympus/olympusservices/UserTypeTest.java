package com.uottawa.olympus.olympusservices;

import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTypeTest {

    /**
     * Tests if the every user that is created is equal to each other or not.
     */

    @Test
    public void userTypeComparaison() {
        UserType user = new HomeOwner("John123", "1234567890", "John", "Doe");
        UserType admin = new Admin();
        UserType serviceprovider = new ServiceProvider( "Jane123", "1234567890", "Jane", "Doe",
                "testaddress", "8888888888", "companydotcom", true);
        boolean userservice = user.equals(serviceprovider);
        boolean useradmin = user.equals(admin);
        boolean serviceadmin = serviceprovider.equals(admin);
        assertNotEquals( true, useradmin );
        assertNotEquals( true, serviceadmin );
        assertNotEquals( true, userservice );
        user.setFirstname(serviceprovider.getFirstname());
        user.setUsername(serviceprovider.getUsername());
        userservice = user.equals(serviceprovider);
        assertEquals( true, userservice );
    }
}