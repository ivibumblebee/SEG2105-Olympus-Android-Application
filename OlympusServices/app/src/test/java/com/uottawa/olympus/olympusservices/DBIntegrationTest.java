package com.uottawa.olympus.olympusservices;

//import android.content.Context;
//import android.database.DatabaseUtils;
//import android.database.sqlite.SQLiteDatabase;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;
import com.uottawa.olympus.olympusservices.Booking.Status;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(packageName = "com.uottawa.olympus.olympusservices")
public class DBIntegrationTest {

    /**
     * Enum for setting up before a test
     */
    private enum TestAfter{
        USER, SERVICE, LINK, AVAILABILITY, BOOKING, RATING;
    }


    private DBHelper dbHelper = new DBHelper(RuntimeEnvironment.application);

    //testing user login table
    @Test
    public void testAdminExists(){
        //Admin is automatically created when DB starts up, if admin does not already exist.
        //so findUserByUsername("admin") should always return an Admin object
        UserType dbUser = dbHelper.findUserByUsername("admin");
        assertEquals("Admin", dbUser.getClass().getSimpleName());
        assertEquals("admin", dbUser.getUsername());
        assertEquals("Admin", dbUser.getFirstname());
        assertEquals("Admin", dbUser.getLastname());
        assertEquals(PasswordEncryption.encrypt("admin", dbUser.getSalt()), dbUser.getHash());
    }

    @Test
    public void testAddAndDeleteUser(){
        UserType originalUser, dbUser;
        boolean deleted, addedOne, addedTwo;

        //add a HomeOwner to database
        originalUser = new HomeOwner("mgarzon", "soccer", "Miguel", "Garzon");
        addedOne = dbHelper.addUser(originalUser);

        //test retrieving HomeOwner, and confirm that user info is the same as that in object passed
        dbUser = dbHelper.findUserByUsername("mgarzon");
        assertEquals("HomeOwner", dbUser.getClass().getSimpleName());
        assertEquals("mgarzon", dbUser.getUsername());
        assertEquals(originalUser.getHash(), dbUser.getHash());
        assertEquals(originalUser.getSalt(), dbUser.getSalt());
        assertEquals("Miguel", dbUser.getFirstname());
        assertEquals("Garzon", dbUser.getLastname());

        //add a ServiceProvider to database
        originalUser = new ServiceProvider("jbO4aBF4dC", "seg2105", "Juan", "Guzman",
                "testaddress", "8888888888", "companydotcom", true);
        addedTwo = dbHelper.addUser(originalUser);

        //test retrieving ServiceProvider, and confirm that user info is the same as that in object passed
        dbUser = dbHelper.findUserByUsername("jbO4aBF4dC");
        assertEquals("ServiceProvider", dbUser.getClass().getSimpleName());
        assertEquals("jbO4aBF4dC", dbUser.getUsername());
        assertEquals(originalUser.getHash(), dbUser.getHash());
        assertEquals(originalUser.getSalt(), dbUser.getSalt());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());

        //if user exsists in database, delete and test that user has been deleted
        if (addedOne) {
            deleted = dbHelper.deleteUser("mgarzon");
            assertTrue(deleted);
        }

        if (addedTwo) {
            deleted = dbHelper.deleteUser("jbO4aBF4dC");
            assertTrue(deleted);
        }
    }

    @Test
    public void testAddAndGetServiceProvider(){
        //ServiceProviders have extra fields that can be added to the DB
        ServiceProvider serviceProvider = new ServiceProvider("7MuF1c59XP", null, null, null,
                "testaddress", "8888888888", "companydotcom", true);
        dbHelper.addUser(serviceProvider);

        //retrieve ServiceProvider and test the newly added fields
        UserType userType = dbHelper.findUserByUsername("7MuF1c59XP");
        //UserType returned should be an instance of ServiceProvider.
        //Also serves as check before casting
        assertTrue(userType instanceof ServiceProvider);

        ServiceProvider dbServiceProvider = (ServiceProvider) userType;
        assertEquals(serviceProvider.getAddress(), dbServiceProvider.getAddress());
        assertEquals(serviceProvider.getPhonenumber(), dbServiceProvider.getPhonenumber());
        assertEquals(serviceProvider.getCompanyname(), dbServiceProvider.getCompanyname());
        assertEquals(serviceProvider.isLicensed(), dbServiceProvider.isLicensed());
        assertEquals(serviceProvider.getDescription(), dbServiceProvider.getDescription());

        dbHelper.deleteUser("7MuF1c59XP");
    }

    @Test
    public void testDeleteServiceProvider(){
        //make sure all the rows related to ServiceProvider in all tables are deleted
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true);
        dbHelper.addUser(serviceProvider);

        Service service1 = new Service("Hitman", 12358);
        Service service2 = new Service("Exterminating flatworms", 392.457);
        dbHelper.addService(service1);
        dbHelper.addService(service2);

        dbHelper.addServiceProvidedByUser(serviceProvider, service1);
        dbHelper.addServiceProvidedByUser(serviceProvider, service2);

        serviceProvider.setAvailabilities(0, 4, 18, 19, 30);
        serviceProvider.setAvailabilities(1, 5, 20, 21, 11);
        serviceProvider.setAvailabilities(3, 7, 12, 15, 14);
        serviceProvider.setAvailabilities(4, 0, 0, 23, 29);
        dbHelper.updateAvailability(serviceProvider);

        dbHelper.deleteUser("jbO4aBF4dC");

        List<String[]> providersList = dbHelper.getAllProvidersByService("hitman");
        assertEquals(0, providersList.size());
        providersList = dbHelper.getAllProvidersByService("hitman");
        assertEquals(0, providersList.size());

        int[][] availabilities = dbHelper.getAvailabilities(serviceProvider);
        for (int i = 0; i<7; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(0, availabilities[i][j]);
            }
        }
    }

    @Test
    public void testAddDuplicateUsers(){
        boolean added;
        added = dbHelper.addUser(new HomeOwner("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));
        assertTrue(added);
        //should not be able to add user of same username, regardless of user type
        added = dbHelper.addUser(new HomeOwner("jbO4aBF4dC", "seg2105", "Miguel", "Garzon"));
        assertTrue(!added);
        added = dbHelper.addUser(new ServiceProvider("jbO4aBF4dC", "seg2105", "Juan", "Guzman",
                "testaddress", "8888888888", "companydotcom", true));
        assertTrue(!added);

        dbHelper.deleteUser("jbO4aBF4dC");
    }

    @Test
    public void testUpdateUserLogin(){
        boolean updated;
        UserType dbUser;

        dbHelper.addUser(new HomeOwner("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));
        updated = dbHelper.updateUserInfo("jbO4aBF4dC", "soccer", "Juan", "Guzman");
        assertTrue(updated);

        dbUser = dbHelper.findUserByUsername("jbO4aBF4dC");

        assertEquals("jbO4aBF4dC", dbUser.getUsername());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());

        //changed on character of username. Everything is case sensitive!
        updated = dbHelper.updateUserInfo("JbO4aBF4dC", "seg2105", "Juan", "Guzman");
        assertTrue(!updated);

        dbHelper.deleteUser("jbO4aBF4dC");
    }

    @Test
    public void testGetAllUsers(){
        setUp(TestAfter.USER);

        List<String[]> allUsers = dbHelper.getAllUsers();
        assertEquals(allUsers.size(), 5);

        for (String[] user : allUsers){
            UserType usertype = dbHelper.findUserByUsername(user[0]);
            assertEquals(usertype.getFirstname(), user[1]);
            assertEquals(usertype.getLastname(), user[2]);
            assertEquals(usertype.getClass().getSimpleName(), user[3]);
        }

        dbHelper.deleteUser("jbO4aBF4dC");
    }


    //Testing services table
    @Test
    public void testAddAndDeleteServices(){
        Service originalService, dbService;
        boolean deleted, addedOne, addedTwo;


        originalService = new Service("Exterminating flatworms", 20.00);
        addedOne = dbHelper.addService(originalService);
        dbService = dbHelper.findService("Exterminating flatworms");

        assertEquals("exterminating flatworms", dbService.getName());
        assertEquals(20.00, dbService.getRate(), 0.001);


        originalService = new Service("Cleaning fishtanks", 15.00);
        addedTwo = dbHelper.addService(originalService);
        dbService = dbHelper.findService("Cleaning fishtanks");

        assertEquals("cleaning fishtanks", dbService.getName());
        assertEquals(15.00, dbService.getRate(), 0.001);

        if (addedOne) {
            deleted = dbHelper.deleteService("Exterminating flatworms");
            assertTrue(deleted);
        }

        if (addedTwo) {
            deleted = dbHelper.deleteService("Cleaning fishtanks");
            assertTrue(deleted);
        }
    }


    @Test
    public void testAddDuplicateService(){
        boolean added;

        added = dbHelper.addService(new Service("Exterminating flatworms", 20.00));
        assertTrue(added);
        added = dbHelper.addService(new Service("Exterminating flatworms", 25.00));
        assertTrue(!added);
        added = dbHelper.addService(new Service("extermiNating fLatworms", 25.00));
        assertTrue(!added);

        dbHelper.deleteAll();
    }

    @Test
    public void testUpdateService(){
        boolean updated;
        Service service;

        dbHelper.addService(new Service("Exterminating flatworms", 20.00));
        updated = dbHelper.updateService("Exterminating flatworms", 25.00);
        assertTrue(updated);

        service = dbHelper.findService("Exterminating flatworms");

        assertEquals("exterminating flatworms", service.getName());
        assertEquals(25.00, service.getRate(), 0.001);

        dbHelper.deleteAll();
    }

    @Test
    public void testGetAllServices(){
        setUp(TestAfter.SERVICE);

        List<String[]> allServices = dbHelper.getAllServices();
        assertTrue(allServices.size() == 3);

        for (String[] service : allServices){
            Service dbService = dbHelper.findService(service[0]);
            assertEquals(dbService.getRate(), Double.parseDouble(service[1]), 0.001);
        }

        dbHelper.deleteAll();
    }

    @Test
    public void testAddAndDeleteServiceProvidedByUser(){
        setUp(TestAfter.SERVICE);

        boolean added = dbHelper.addServiceProvidedByUser("jbO4aBF4dC", "hitman");
        assertTrue(added);
        boolean deleted = dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC", "Hitman");
        assertTrue(deleted);

        dbHelper.deleteAll();
    }

    @Test
    public void testGetAllServicesProvidedByUserAndDeleteService(){
        setUp(TestAfter.LINK);

        List<String[]> servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(2, servicesProvidedByUser.size());
        assertEquals("hitman", servicesProvidedByUser.get(0)[0]);
        assertEquals(12358, Double.parseDouble(servicesProvidedByUser.get(0)[1]), 0.00001);
        assertEquals("exterminating flatworms", servicesProvidedByUser.get(1)[0]);
        assertEquals(392.457, Double.parseDouble(servicesProvidedByUser.get(1)[1]), 0.00001);

        dbHelper.deleteService("hitman");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(1, servicesProvidedByUser.size());

        dbHelper.deleteService("exterminating flatworms");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(0, servicesProvidedByUser.size());

        dbHelper.deleteAll();
    }

    @Test
    public void testGetAllProvidersByService(){
        setUp(TestAfter.LINK);

        List<String[]> providersByService = dbHelper.getAllProvidersByService("exterminating flatworms");

        assertEquals(2, providersByService.size());
        assertEquals("jbO4aBF4dC", providersByService.get(0)[0]);
        assertEquals("DW44FkUsX7", providersByService.get(1)[0]);

        dbHelper.deleteAll();
    }


    @Test
    public void testDeleteServiceProvidedByUser(){
        setUp(TestAfter.LINK);

        List<String[]> servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(2, servicesProvidedByUser.size());

        dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC","hitman");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(1, servicesProvidedByUser.size());

        dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC", "exterminating flatworms");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(0, servicesProvidedByUser.size());

        dbHelper.deleteAll();
    }



    @Test
    public void testUpdateAndGetAvailability(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true);
        serviceProvider.setAvailabilities(0, 4, 18, 19, 30);
        serviceProvider.setAvailabilities(1, 5, 20, 21, 11);
        serviceProvider.setAvailabilities(3, 7, 12, 15, 14);
        serviceProvider.setAvailabilities(4, 0, 0, 23, 29);

        //TODO:Perhaps implement a deep clone function for UserType?
        ServiceProvider serviceProvider2 = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true);
        serviceProvider2.setAvailabilities(0, 4, 18, 19, 30);
        serviceProvider2.setAvailabilities(1, 5, 20, 21, 11);
        serviceProvider2.setAvailabilities(3, 7, 12, 15, 14);
        serviceProvider2.setAvailabilities(4, 0, 0, 23, 29);

        dbHelper.addUser(serviceProvider2);

        boolean updated = dbHelper.updateAvailability(serviceProvider2);
        assertTrue(updated);

        serviceProvider2.setAvailabilities(3, 8, 12, 15, 10);
        int[][] dbAvailabilities = dbHelper.getAvailabilities(serviceProvider2);
        int[][] availabilities = serviceProvider.getAvailabilities();

        assertEquals(14, serviceProvider2.getAvailabilities()[3][3]);

        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 4; j++){
                assertEquals(availabilities[i][j], dbAvailabilities[i][j]);
            }
        }
        dbHelper.deleteAll();
    }


    @Test
    public void testInvalidAvailability(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true);
        serviceProvider.setAvailabilities(2, 8, 14, 8, 14);
        serviceProvider.setAvailabilities(3, 15, 12, 8, 14);


        dbHelper.addUser(serviceProvider);

        boolean updated = dbHelper.updateAvailability(serviceProvider);
        assertTrue(updated);
        int[][] dbAvailabilities = dbHelper.getAvailabilities(serviceProvider);
        int[][] availabilities = serviceProvider.getAvailabilities();

        for (int i = 0; i<7; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(0, availabilities[i][j]);
                assertEquals(0, dbAvailabilities[i][j]);
            }
        }
        dbHelper.deleteAll();
    }

    @Test
    public void testGetByTime(){
        setUp(TestAfter.AVAILABILITY);

        //December 3 2020 is a Thursday
        List<String[]> providers = dbHelper.getProvidersByTime("Exterminating flatworms", 2020,
                12, 3, 6, 12, 10, 0);
        assertEquals(0, providers.size());

        providers = dbHelper.getProvidersByTime("Exterminating flatworms", 2020,
                12, 3, 7, 12, 10, 0);
        assertEquals(1, providers.size());
        String[] firstProvider = providers.get(0);
        assertEquals("jbO4aBF4dC", firstProvider[0]);
        assertEquals("Jack", firstProvider[1]);
        assertEquals("Black", firstProvider[2]);
        assertEquals(0, Double.parseDouble(firstProvider[3]), 0.0001);

        //December 4 2020 is a Friday
        providers = dbHelper.getProvidersByTime("Exterminating flatworms", 2020,
                12, 4, 10, 0, 13, 0);
        assertEquals(2, providers.size());
    }



    @Test
    public void testAddBooking(){
        setUp(TestAfter.AVAILABILITY);

        //December 1, 2020 is a Tuesday. Provider is available from 5:20 to 21:11
        boolean added = dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2020, 12, 1, 8, 12, 10, 0);
        assertTrue(added);

        //This booking has already been done so cannot be booked anymore
        added = dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2020, 12, 1, 8, 12, 10, 0);
        assertTrue(!added);

        //Provider is available from 5:20 to 21:11, but has a booking from 8:12 to 10:00
        added = dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2020, 12, 1, 9, 12, 12, 0);
        assertTrue(!added);

        //December 3, 2020 is a Thursday. Provider is available from 7:12 to 15:14
        added = dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2020, 12, 3, 6, 12, 7, 30);
        assertTrue(!added);

        //November 20, 2018 is in the past. Should not be able to add booking
        added = dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2018, 11, 20, 8, 12, 10, 0);
        assertTrue(!added);

        dbHelper.deleteAll();
    }

    @Test
    public void testViewListOfBookings(){
        setUp(TestAfter.AVAILABILITY);

        List<Booking> bookings = dbHelper.findBookings("jbO4aBF4dC");
        assertEquals(0, bookings.size());
        bookings = dbHelper.findBookings("7MuF1c59XP");
        assertEquals(0, bookings.size());
        bookings = dbHelper.findBookings("DW44FkUsX7");
        assertEquals(0, bookings.size());

        dbHelper.addBooking("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                2020, 12, 1, 8, 12, 10, 0);
        bookings = dbHelper.findBookings("jbO4aBF4dC");
        assertEquals(1, bookings.size());
        bookings = dbHelper.findBookings("7MuF1c59XP");
        assertEquals(1, bookings.size());
        bookings = dbHelper.findBookings("DW44FkUsX7");
        assertEquals(0, bookings.size());

        dbHelper.addBooking("DW44FkUsX7", "7MuF1c59XP", "petting cats",
                2020, 12, 4, 10, 0, 10, 30);
        bookings = dbHelper.findBookings("jbO4aBF4dC");
        assertEquals(1, bookings.size());
        bookings = dbHelper.findBookings("7MuF1c59XP");
        assertEquals(2, bookings.size());
        bookings = dbHelper.findBookings("DW44FkUsX7");
        assertEquals(1, bookings.size());

        dbHelper.deleteAll();
    }

    @Test
    public void testConfirmAndCancelBooking(){
        setUp(TestAfter.AVAILABILITY);

        Booking booking = new Booking(8, 12, 10, 0,1,
                12, 2020, (ServiceProvider)dbHelper.findUserByUsername("jbO4aBF4dC"),
                (HomeOwner)dbHelper.findUserByUsername("7MuF1c59XP"), dbHelper.findService("Hitman"));
        dbHelper.addBooking(booking);

        assertTrue(dbHelper.confirmBooking(booking));
        List<Booking> bookings = dbHelper.findBookings("jbO4aBF4dC");
        Booking dbBooking = bookings.get(0);
        assertEquals(Status.CONFIRMED, dbBooking.getStatus());

        assertTrue(dbHelper.cancelBooking(booking));
        bookings = dbHelper.findBookings("jbO4aBF4dC");
        dbBooking = bookings.get(0);
        assertEquals(Status.CANCELLED, dbBooking.getStatus());

        bookings = dbHelper.findNonCancelledBookings("jbO4aBF4dC");
        assertEquals(0, bookings.size());

        dbHelper.deleteAll();
    }

    @Test
    public void testRating(){
        setUp(TestAfter.BOOKING);

        ServiceProvider serviceProvider = (ServiceProvider)dbHelper.findUserByUsername("jbO4aBF4dC");
        HomeOwner homeOwner = (HomeOwner)dbHelper.findUserByUsername("7MuF1c59XP");
        Service service = dbHelper.findService("Hitman");

        Booking booking = new Booking(8, 12, 10, 0,
                2, 10, 2018,
                serviceProvider, homeOwner, service);
        boolean added = dbHelper.addRating(booking, 5, "100%");
        assertTrue(added);

        double rating = dbHelper.getAverageRating("jbO4aBF4dC","Hitman");
        assertEquals(5, rating, 0.0001);

        booking = new Booking(10, 01, 11, 0,
                2, 10, 2018,
                serviceProvider, homeOwner, service);
        added = dbHelper.addRating(booking, 1, "Wrong target");
        assertTrue(added);

        rating = dbHelper.getAverageRating("jbO4aBF4dC","Hitman");
        assertEquals(3, rating, 0.0001);

        rating = dbHelper.getAverageRating("jbO4aBF4dC","exterminating flatworms");
        assertEquals(0, rating, 0.0001);

        dbHelper.deleteAll();

    }

//    @Test
//    public void printUsersTable(){
//        dbHelper.printTable("userInfo");
//    }

    // Ever gotten tired of adding things at the start of a test just to delete it all again?
    // I have.
    // This is a work in progress
    private void setUp(TestAfter testAfter){
        dbHelper.deleteAll();

        dbHelper.addUser(new Admin());

        ServiceProvider serviceProvider1 = new ServiceProvider("jbO4aBF4dC", null, "Jack", "Black",
                "testaddress", "8888888888", "companydotcom", true);
        dbHelper.addUser(serviceProvider1);

        ServiceProvider serviceProvider2 = new ServiceProvider("DW44FkUsX7", null, "Dwayne", "Johnson",
                "testaddress", "1248921742", "companydotcom", false);
        dbHelper.addUser(serviceProvider2);

        HomeOwner homeOwner1 = new HomeOwner("7MuF1c59XP", null, "Mufasa", "Died");
        dbHelper.addUser(homeOwner1);

        HomeOwner homeOwner2 = new HomeOwner("wRV3phzpl5", null, "Wren", "Phillips");
        dbHelper.addUser(homeOwner2);

        Service service1 = new Service("Hitman", 12358);
        dbHelper.addService(service1);

        Service service2 = new Service("Exterminating flatworms", 392.457);
        dbHelper.addService(service2);

        Service service3 = new Service("Petting cats", 0);
        dbHelper.addService(service3);

        if (!(testAfter.equals(TestAfter.USER) || testAfter.equals(TestAfter.SERVICE))){
            dbHelper.addServiceProvidedByUser(serviceProvider1, service1);
            dbHelper.addServiceProvidedByUser(serviceProvider1, service2);

            dbHelper.addServiceProvidedByUser(serviceProvider2, service3);
            dbHelper.addServiceProvidedByUser(serviceProvider2, service2);

            if (!testAfter.equals(TestAfter.LINK)){
                //serviceProvider1 is available on Monday, Tuesday, Thursday, and Friday
                serviceProvider1.setAvailabilities(0, 4, 18, 19, 30);
                serviceProvider1.setAvailabilities(1, 5, 20, 21, 11);
                serviceProvider1.setAvailabilities(3, 7, 12, 15, 14);
                serviceProvider1.setAvailabilities(4, 0, 0, 23, 29);

                dbHelper.updateAvailability(serviceProvider1);

                //serviceProvider2 is only available on Friday
                serviceProvider2.setAvailabilities(4, 10, 0, 23, 29);

                dbHelper.updateAvailability(serviceProvider2);

                if (!testAfter.equals(TestAfter.AVAILABILITY)){
                    Booking booking1 = new Booking(8, 12, 10, 0,1,
                            12, 2020, serviceProvider1, homeOwner1, service1);
                    dbHelper.addBooking(booking1);
                }

                if (testAfter.equals(TestAfter.BOOKING)){
                    //October 2 2018 is a Tuesday
                    dbHelper.forceAddBookingDONTTOUCH("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                            2018, 10, 2, 8, 12, 10, 0, Status.PENDING);
                    dbHelper.forceAddBookingDONTTOUCH("jbO4aBF4dC", "7MuF1c59XP", "Hitman",
                            2018, 10, 2, 10, 01, 11, 0, Status.PENDING);
                }
            }
        }
    }
}

