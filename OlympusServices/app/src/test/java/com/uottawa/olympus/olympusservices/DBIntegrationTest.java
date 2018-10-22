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
            UserType usertype = dbHelper.findUserByUsername(user[0]);
            assertEquals(usertype.getFirstname(), user[1]);
            assertEquals(usertype.getLastname(), user[2]);
            assertEquals(usertype.getClass().getSimpleName(), user[3]);
        }

        dbHelper.deleteUser("jbO4aBF4dC");
    }
    
}

