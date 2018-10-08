package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(packageName = "com.uottawa.olympus.olympusservices")
public class DBUnitTest {
    private DBHelper dbHelper = new DBHelper(RuntimeEnvironment.application);

    @Test
    public void testAddAndDeleteUser(){
        UserType originalUser, dbUser;
        boolean deleted;


        originalUser = new User("mgarzon", "soccer", "Miguel", "Garzon");
        dbHelper.addUser(originalUser);
        dbUser = dbHelper.findUserByUsername("mgarzon");

        assertEquals("User", dbUser.getClass().getSimpleName());
        assertEquals("mgarzon", dbUser.getUsername());
        assertEquals("soccer", dbUser.getPassword());
        assertEquals("Miguel", dbUser.getFirstname());
        assertEquals("Garzon", dbUser.getLastname());


        originalUser = new ServiceProvider("jguzman", "seg2105", "Juan", "Guzman");
        dbHelper.addUser(originalUser);
        dbUser = dbHelper.findUserByUsername("jguzman");

        assertEquals("ServiceProvider", dbUser.getClass().getSimpleName());
        assertEquals("jguzman", dbUser.getUsername());
        assertEquals("seg2105", dbUser.getPassword());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());


        deleted = dbHelper.deleteUser("mgarzon");
        assertTrue(deleted);

        deleted = dbHelper.deleteUser("jguzman");
        assertTrue(deleted);

    }


    @Test
    public void testAddDuplicateUsers(){
        boolean added;

        added = dbHelper.addUser(new User("mgarzon", "soccer", "Miguel", "Garzon"));
        assertTrue(added);
        added = dbHelper.addUser(new User("mgarzon", "seg2105", "Miguel", "Garzon"));
        assertTrue(!added);
        added = dbHelper.addUser(new ServiceProvider("mgarzon", "seg2105", "Juan", "Guzman"));
        assertTrue(!added);

        dbHelper.deleteUser("mgarzon");
    }

    @Test
    public void testUpdateUserLogin(){
        boolean updated;
        UserType dbUser;

        dbHelper.addUser(new User("mgarzon", "soccer", "Miguel", "Garzon"));
        updated = dbHelper.updateUserInfo("mgarzon", "soccer", "Juan", "Guzman");
        assertTrue(updated);

        dbUser = dbHelper.findUserByUsername("mgarzon");

        assertEquals("mgarzon", dbUser.getUsername());
        assertEquals("soccer", dbUser.getPassword());
        assertEquals("Juan", dbUser.getFirstname());
        assertEquals("Guzman", dbUser.getLastname());

        updated = dbHelper.updateUserInfo("jguzman", "seg2105", "Juan", "Guzman");
        assertTrue(!updated);

        dbHelper.deleteUser("mgarzon");
    }

}
