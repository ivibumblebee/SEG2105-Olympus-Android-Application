package com.uottawa.olympus.olympusservices;

import org.junit.Test;

import static org.junit.Assert.*;

public class BookingTest {
    HomeOwner user = new HomeOwner( "John123", "1234567890", "John", "Doe" );
    ServiceProvider serviceprovider = new ServiceProvider("Jane123", "1234567890", "Jane", "Doe", "testaddress", "8888888888", "companydotcom", true);
    Service service = new Service( "FrenchMaid", 250 );
    Booking book = new Booking(13, 30, 16, 0, 0, 0, 2019,
    serviceprovider, user, service);

    @Test
    public void testStarth() {
        assertEquals(13, book.getStarth());
        book.setStarth(10);
        assertNotEquals(13, book.getStarth());
    }

    @Test
    public void testStartmin() {
        assertEquals(30, book.getStartmin());
        book.setStartmin(20);
        assertNotEquals(30, book.getStartmin());
    }

    @Test
    public void testEndh() {
        assertEquals(16, book.getEndh());
        book.setEndh(14);
        assertNotEquals(16, book.getEndh());
    }

    @Test
    public void testEndmin() {
        assertEquals(0, book.getEndmin());
        book.setEndmin(20);
        assertNotEquals(0, book.getEndmin());
    }

    @Test
    public void testDay() {
        assertEquals(0, book.getDay());
        book.setDay(1);
        assertNotEquals(0, book.getDay());
    }

    @Test
    public void testMonth() {
        assertEquals(0, book.getMonth());
        book.setMonth(2);
        assertNotEquals(0, book.getMonth());
    }

    @Test
    public void testYear() {
        assertEquals(2019, book.getYear());
        book.setYear(2020);
        assertNotEquals(2019, book.getYear());
    }

    @Test
    public void testServiceprovider() {
        assertEquals(serviceprovider, book.getServiceprovider() );
        ServiceProvider serviceprovider2 = new ServiceProvider("Joshua123", "1234567890", "Joshua", "Doe", "testoaddress", "8888888888", "whateverdotcom", true);
        book.setServiceprovider(serviceprovider2);
        assertNotEquals(serviceprovider, book.getServiceprovider());
    }

    @Test
    public void testHomeowner() {
        assertEquals(user, book.getHomeowner() );
        HomeOwner user2 = new HomeOwner( "Johnny123", "1234567890", "Johnny", "Dumas" );
        book.setHomeowner(user2);
        assertNotEquals(user, book.getHomeowner());
    }

    @Test
    public void testService() {
        assertEquals(service, book.getService() );
        Service service2 = new Service( "ItalianMafia", 300 );
        book.setService(service2);
        assertNotEquals(service, book.getService());
    }

    @Test
    public void testStatus() {
        book.setStatus(Booking.Status.CONFIRMED);
        assertEquals(Booking.Status.CONFIRMED, book.getStatus());
    }

    @Test
    public void testRating() {
        book.setRating(5);
        assertEquals(5, book.getRating());
    }
}