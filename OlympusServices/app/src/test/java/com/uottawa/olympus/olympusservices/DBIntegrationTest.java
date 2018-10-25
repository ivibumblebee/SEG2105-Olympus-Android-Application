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


        originalUser = new User("mgarzon", "soccer", "Miguel", "Garzon");
        addedOne = dbHelper.addUser(originalUser);
        dbUser = dbHelper.findUserByUsername("mgarzon");

        assertEquals("User", dbUser.getClass().getSimpleName());
        assertEquals("mgarzon", dbUser.getUsername());
        assertEquals("soccer", dbUser.getPassword());
        assertEquals("Miguel", dbUser.getFirstname());
        assertEquals("Garzon", dbUser.getLastname());


        originalUser = new ServiceProvider("jbO4aBF4dC", "seg2105", "Juan", "Guzman");
        addedTwo = dbHelper.addUser(originalUser);
        dbUser = dbHelper.findUserByUsername("jbO4aBF4dC");

        assertEquals("ServiceProvider", dbUser.getClass().getSimpleName());
        assertEquals("jbO4aBF4dC", dbUser.getUsername());
        assertEquals("seg2105", dbUser.getPassword());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());

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
    public void testAddDuplicateUsers(){
        boolean added;

        added = dbHelper.addUser(new User("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));
        assertTrue(added);
        added = dbHelper.addUser(new User("jbO4aBF4dC", "seg2105", "Miguel", "Garzon"));
        assertTrue(!added);
        added = dbHelper.addUser(new ServiceProvider("jbO4aBF4dC", "seg2105", "Juan", "Guzman"));
        assertTrue(!added);

        dbHelper.deleteUser("jbO4aBF4dC");
    }

    @Test
    public void testUpdateUserLogin(){
        boolean updated;
        UserType dbUser;

        dbHelper.addUser(new User("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));
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
        dbHelper.addUser(new User("jbO4aBF4dC", "soccer", "Miguel", "Garzon"));

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

        assertEquals("Exterminating flatworms", dbService.getName());
        assertEquals(20.00, dbService.getRate(), 0.001);


        originalService = new Service("Cleaning shoes", 15.00);
        addedTwo = dbHelper.addService(originalService);
        dbService = dbHelper.findService("Cleaning shoes");

        assertEquals("Cleaning shoes", dbService.getName());
        assertEquals(15.00, dbService.getRate(), 0.001);

        if (addedOne) {
            deleted = dbHelper.deleteService("Exterminating flatworms");
            assertTrue(deleted);
        }

        if (addedTwo) {
            deleted = dbHelper.deleteService("Cleaning shoes");
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

        assertEquals("Exterminating flatworms", service.getName());
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

        dbHelper.deleteUser("jbO4aBF4dC");
    }

}

