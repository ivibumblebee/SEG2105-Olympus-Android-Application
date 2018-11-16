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

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(packageName = "com.uottawa.olympus.olympusservices")
public class DBIntegrationTest {
    private DBHelper dbHelper = new DBHelper(RuntimeEnvironment.application);

    //testing user login table
    @Test
    public void testAdminExists(){
        //Admin is automatically created when DB starts up, if admin does not already exist.
        //so findUserByUsername("admin") should always return an Admin object
        UserType dbUser = dbHelper.findUserByUsername("admin");
        assertEquals("Admin", dbUser.getClass().getSimpleName());
        assertEquals("admin", dbUser.getUsername());
        assertEquals("admin", dbUser.getPassword());
        assertEquals("Admin", dbUser.getFirstname());
        assertEquals("Admin", dbUser.getLastname());
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
        assertEquals("soccer", dbUser.getPassword());
        assertEquals("Miguel", dbUser.getFirstname());
        assertEquals("Garzon", dbUser.getLastname());

        //add a ServiceProvider to database
        originalUser = new ServiceProvider("jbO4aBF4dC", "seg2105", "Juan", "Guzman",
                "testaddress", "8888888888", "companydotcom", true, null);
        addedTwo = dbHelper.addUser(originalUser);

        //test retrieving ServiceProvider, and confirm that user info is the same as that in object passed
        dbUser = dbHelper.findUserByUsername("jbO4aBF4dC");
        assertEquals("ServiceProvider", dbUser.getClass().getSimpleName());
        assertEquals("jbO4aBF4dC", dbUser.getUsername());
        assertEquals("seg2105", dbUser.getPassword());
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
                "testaddress", "8888888888", "companydotcom", true, "Not a penguin");
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
                "testaddress", "8888888888", "companydotcom", true, null);
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
                "testaddress", "8888888888", "companydotcom", true, null));
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
        assertEquals("soccer", dbUser.getPassword());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());

        //changed on character of username. Everything is case sensitive!
        updated = dbHelper.updateUserInfo("JbO4aBF4dC", "seg2105", "Juan", "Guzman");
        assertTrue(!updated);

        dbHelper.deleteUser("jbO4aBF4dC");
    }

    @Test
    public void testGetAllUsers(){
        dbHelper.addUser(new HomeOwner("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));

        List<String[]> allUsers = dbHelper.getAllUsers();

        for (String[] user : allUsers){
/*            for (String s : user){
                System.out.print(s + " ");
            }
            System.out.println();*/
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

        dbHelper.deleteService("Exterminating flatworms");
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

        dbHelper.deleteService("Exterminating flatworms");
    }

    @Test
    public void testGetAllServices(){
        dbHelper.addService(new Service("Exterminating flatworms", 20.00));

        List<String[]> allServices = dbHelper.getAllServices();

        for (String[] service : allServices){
/*            for (String s : user){
                System.out.print(s + " ");
            }
            System.out.println();*/
            Service dbService = dbHelper.findService(service[0]);
            assertEquals(dbService.getRate(), Double.parseDouble(service[1]), 0.001);
        }

        dbHelper.deleteService("Exterminating flatworms");
    }

    @Test
    public void testAddAndDeleteServiceProvidedByUser(){
        dbHelper.addUser(new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null));
        dbHelper.addService(new Service("Hitman", 12358));
        boolean added = dbHelper.addServiceProvidedByUser("jbO4aBF4dC", "hitman");
        assertTrue(added);
        boolean deleted = dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC", "Hitman");
        assertTrue(deleted);
        dbHelper.deleteUser("jbO4aBF4dC");
        dbHelper.deleteService("hitman");
    }

    @Test
    public void testGetAllServicesProvidedByUserAndDeleteService(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null);
        dbHelper.addUser(serviceProvider);

        Service service1 = new Service("Hitman", 12358);
        Service service2 = new Service("Exterminating flatworms", 392.457);
        dbHelper.addService(service1);
        dbHelper.addService(service2);

        dbHelper.addServiceProvidedByUser(serviceProvider, service1);
        dbHelper.addServiceProvidedByUser(serviceProvider, service2);

        List<String[]> servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser(serviceProvider);
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

        dbHelper.deleteUser("jbO4aBF4dC");
    }

    @Test
    public void testGetAllProvidersByService(){
        dbHelper.addService(new Service("Exterminating flatworms", 392.457));
        dbHelper.addUser(new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null));
        dbHelper.addUser(new ServiceProvider("7MuF1c59XP", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null));

        dbHelper.addServiceProvidedByUser("jbO4aBF4dC", "exterminating flatworms");
        dbHelper.addServiceProvidedByUser("7MuF1c59XP", "exterminating flatworms");

        List<String[]> providersByService = dbHelper.getAllProvidersByService("exterminating flatworms");

        assertEquals(2, providersByService.size());
        assertEquals("jbO4aBF4dC", providersByService.get(0)[0]);
        assertEquals("7MuF1c59XP", providersByService.get(1)[0]);

        dbHelper.deleteService("Exterminating flatworms");
        dbHelper.deleteUser("jbO4aBF4dC");
        dbHelper.deleteUser("7MuF1c59XP");
    }


    @Test
    public void testDeleteServiceProvidedByUser(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null);
        dbHelper.addUser(serviceProvider);

        Service service1 = new Service("Hitman", 12358);
        Service service2 = new Service("Exterminating flatworms", 392.457);
        dbHelper.addService(service1);
        dbHelper.addService(service2);

        dbHelper.addServiceProvidedByUser(serviceProvider, service1);
        dbHelper.addServiceProvidedByUser(serviceProvider, service2);

        List<String[]> servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser(serviceProvider);
        assertEquals(2, servicesProvidedByUser.size());

        dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC","hitman");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(1, servicesProvidedByUser.size());

        dbHelper.deleteServiceProvidedByUser("jbO4aBF4dC", "exterminating flatworms");
        servicesProvidedByUser = dbHelper.getAllServicesProvidedByUser("jbO4aBF4dC");
        assertEquals(0, servicesProvidedByUser.size());

        dbHelper.deleteUser("jbO4aBF4dC");
    }



    @Test
    public void testUpdateAndGetAvailability(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null);
        serviceProvider.setAvailabilities(0, 4, 18, 19, 30);
        serviceProvider.setAvailabilities(1, 5, 20, 21, 11);
        serviceProvider.setAvailabilities(3, 7, 12, 15, 14);
        serviceProvider.setAvailabilities(4, 0, 0, 23, 29);

        //TODO:Perhaps implement a deep clone function for UserType?
        ServiceProvider serviceProvider2 = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null);
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
        dbHelper.deleteUser("jbO4aBF4dC");
    }


    @Test
    public void testInvalidAvailability(){
        ServiceProvider serviceProvider = new ServiceProvider("jbO4aBF4dC", null, null, null,
                "testaddress", "8888888888", "companydotcom", true, null);
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
        dbHelper.deleteUser("jbO4aBF4dC");
    }
}

