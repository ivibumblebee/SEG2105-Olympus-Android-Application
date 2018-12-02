package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdminTest {

    /**
     * Initiates an Admin account and tests if the strings match to the required inputs.
     */

    @Test
    public void testAdmin() {
        Admin admin = new Admin();
        String username = admin.getUsername();
        String firstname = admin.getFirstname();
        String lastname = admin.getLastname();
        String role = admin.getRole();
        assertEquals("Admin", role);
        assertEquals("admin", username);
        assertEquals(PasswordEncryption.encrypt("admin", admin.getSalt()), admin.getHash());
        assertEquals("Admin", firstname);
        assertEquals("Admin", lastname);
    }
}