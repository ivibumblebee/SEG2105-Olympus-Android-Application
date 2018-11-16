package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTest {
    Service service = new Service( "FrenchMaid", 250 );

    /**
     * Initiates an Service account and tests if the random strings match to the required inputs.
     */

    @Test
    public void testService(){
        assertEquals( "FrenchMaid", service.getName() );
        assertEquals( 250, service.getRate(), 0);
        service.setName( "ItalianMafia" );
        service.setRate( 200 );
        assertNotEquals( "FrenchMaid", service.getName() );
        assertNotEquals( 250, service.getRate());
    }

    /**
     * adds service providers and tests if they match the requirements in order to function properly. Example: If a service has a same name it doesn't count as one.
     *
     */

    @Test
    public void addServiceProviderTest(){
        service.addServiceProvider( new ServiceProvider( "John123", "1234567890", "John", "Doe",
                "testaddress", "8888888888", "companydotcom", true, null) );
        service.addServiceProvider( new ServiceProvider( "Jane123", "1234567890", "Jane", "Doe",
                "testaddress", "8888888888", "companydotcom", true, null) );
        service.addServiceProvider( new ServiceProvider( "John123", "1234567890", "John", "Doe",
                "testaddress", "8888888888", "companydotcom", true, null) );
        service.addServiceProvider( new ServiceProvider( "Jane123", "1234567890", "Jane", "Doe",
                "testaddress", "8888888888", "companydotcom", true, null) );
        service.addServiceProvider( new ServiceProvider( "Jane123", "1234567890", "John", "Doe",
                "testaddress", "8888888888", "companydotcom", true, null) );
        int numOfSP = service.getServiceProviders().size();
        assertEquals( 2,numOfSP );
    }

}
