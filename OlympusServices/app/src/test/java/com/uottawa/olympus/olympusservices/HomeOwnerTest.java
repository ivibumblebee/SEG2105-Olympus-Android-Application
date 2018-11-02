package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class HomeOwnerTest {

    @Test
    public void testHomeOwner(){
        UserType user = new HomeOwner( "John123", "1234567890", "John", "Doe" );
        String role = user.getRole();
        assertEquals("HomeOwner", role);
        assertEquals("John123", user.getUsername());
        assertEquals("1234567890", user.getPassword());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        user.setUsername( "username" );
        user.setPassword( "password" );
        user.setFirstname( "firstname" );
        user.setLastname( "lastname" );
        assertNotEquals("John123", user.getUsername());
        assertNotEquals("1234567890", user.getPassword());
        assertNotEquals("John", user.getFirstname());
        assertNotEquals("Doe", user.getLastname());
    }
}
