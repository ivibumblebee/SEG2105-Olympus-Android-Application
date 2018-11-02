package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTest {
    Service service = new Service( "FrenchMaid", 250 );

    @Test
    public void testService(){
        assertEquals( "FrenchMaid", service.getName() );
        assertEquals( 250, service.getRate(), 0);
        service.setName( "ItalianMafia" );
        service.setRate( 200 );
        assertNotEquals( "FrenchMaid", service.getName() );
        assertNotEquals( 250, service.getRate());
    }

    @Test
    public void addServiceProviderTest(){
        service.addServiceProvider( new ServiceProvider( "John123", "1234567890", "John", "Doe" ) );
        service.addServiceProvider( new ServiceProvider( "Jane123", "1234567890", "Jane", "Doe" ) );
        service.addServiceProvider( new ServiceProvider( "John123", "1234567890", "John", "Doe" ) );
        service.addServiceProvider( new ServiceProvider( "Jane123", "1234567890", "Jane", "Doe" ) );
        int numOfSP = service.getServiceProviders().size();
        assertEquals( 2,numOfSP );
    }

}
