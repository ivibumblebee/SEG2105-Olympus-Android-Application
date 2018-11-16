package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceProviderTest2 {
    ServiceProvider serviceprovider = new ServiceProvider("John123", "1234567890", "John", "Doe",
            "testaddress", "8888888888", "companydotcom", true, "LOL");

    /**
     * sets availabilities and tests them.
     */
    @Test
    public void testAvailabilities() {
        int [][] availability = new int[7][4];
        assertEquals( availability, serviceprovider.getAvailabilities());
        serviceprovider.setAvailabilities(5, 8, 30, 12, 0);
        availability[5][0] = 8;
        availability[5][1] = 30;
        availability[5][2] = 12;
        availability[5][3] = 0;
        assertEquals( availability, serviceprovider.getAvailabilities());
    }

    /**
     * Tests other instances and checks if they are correct
     *
     */
    @Test
    public void testOtherInstances() {
        assertEquals( "testaddress", serviceprovider.getAddress() );
        assertEquals( "8888888888", serviceprovider.getPhonenumber() );
        assertEquals( "companydotcom", serviceprovider.getCompanyname() );
        assertEquals( true, serviceprovider.isLicensed() );
        assertEquals( "LOL", serviceprovider.getDescription() );
        serviceprovider.setAddress( "Canada, Ontario" );
        serviceprovider.setPhonenumber( "+1-416-555-0182" );
        serviceprovider.setCompanyname( "Google" );
        serviceprovider.setLicensed( false );
        serviceprovider.setDescription( "Nope" );
        assertNotEquals( "testaddress", serviceprovider.getAddress() );
        assertNotEquals( "8888888888", serviceprovider.getPhonenumber() );
        assertNotEquals( "companydotcom", serviceprovider.getCompanyname() );
        assertNotEquals( true, serviceprovider.isLicensed() );
        assertNotEquals( "LOL", serviceprovider.getDescription() );
    }
}