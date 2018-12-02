package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class HomeOwnerTest {

    /**
     * Initiates an Homeowner account and tests if the random strings match to the required inputs.
     */

    @Test
    public void testHomeOwner(){
        UserType user = new HomeOwner( "John123", "1234567890", "John", "Doe" );
        String role = user.getRole();
        String salt = user.getSalt();
        assertEquals("HomeOwner", role);
        assertEquals("John123", user.getUsername());
        assertEquals( PasswordEncryption.encrypt("1234567890", salt), user.getHash());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        user.setUsername( "username" );
        user.setPassword( "password" );
        user.setFirstname( "firstname" );
        user.setLastname( "lastname" );
        assertNotEquals("John123", user.getUsername());
        assertNotEquals("1234567890", user.getHash());
        assertNotEquals("password", user.getHash());
        assertNotEquals( PasswordEncryption.encrypt("1234567890", salt), user.getHash());
        assertNotEquals(PasswordEncryption.encrypt("password", salt), user.getHash());
        assertNotEquals("John", user.getFirstname());
        assertNotEquals("Doe", user.getLastname());
    }
}
