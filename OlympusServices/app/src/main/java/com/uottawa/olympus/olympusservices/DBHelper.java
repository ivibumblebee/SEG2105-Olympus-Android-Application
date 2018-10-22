package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.LinkedList;
import java.util.List;

/**
 * The class DBHelper allows the Android application to access and perform
 * CRUD (Create, Read, Update, Delete) operations on the tables of the SQLite database.
 * There is currently one table of all users' login information and names.
 * Table of service providers and services to come soon.
 *
 * To use, create an object of this class with the current activity as context.
 *
 */

public class DBHelper extends SQLiteOpenHelper {

    //version of db used for update method
    private static final int DB_VERSION = 1;
    //name of db in app data
    private static final String DB_NAME = "UsersDB.db";

    //SQLiteDatabase for reading
    private static SQLiteDatabase readDB;

    //SQLiteDatabase for writing
    private static SQLiteDatabase writeDB;

    //name of table containing user login information and names
    private static final String TABLE_LOGIN = "userInfo";
    //columns of TABLE_LOGIN
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FIRSTNAME = "firstName";
    private static final String COLUMN_LASTNAME = "lastName";
    private static final String COLUMN_USERTYPE = "userType";

//    private static final String TABLE_SERVICEPROVIDERS = "serviceProviders";


//    private static final String TABLE_SERVICES = "services";


    /**
     * Creates an instance of DBHelper to allow activities to access and
     * perform CRUD operations on the database via DBHelper's methods
     *
     * @param context current activity calling DBHelper
     */
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        readDB = this.getReadableDatabase();
        writeDB = this.getWritableDatabase();
        addUser(new Admin());
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String CREATE_LOGIN_TABLE = "CREATE TABLE "+ TABLE_LOGIN + "("
                + COLUMN_USERNAME + " STRING UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                + COLUMN_PASSWORD + " STRING NOT NULL,"
                + COLUMN_FIRSTNAME + " STRING DEFAULT 'FirstName',"
                + COLUMN_LASTNAME + " STRING DEFAULT 'LastName',"
                + COLUMN_USERTYPE + " STRING NOT NULL" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /**
     * Adds a user to the database. Returns false if there is a user already
     * existing in the database with the same username. Returns true if
     * successful in adding user to database.
     *
     * @param userType user to be added
     * @return whether adding user was successful
     */
    public boolean addUser(UserType userType){
        //Check for duplicate username by querying login table
        Cursor cursor = writeDB.query(TABLE_LOGIN,
                new String[] {COLUMN_USERNAME},
                COLUMN_USERNAME + " = ?",
                new String[]{userType.getUsername()},
                null, null, null,
                "1");
        //If cursor has 1+ elements in it, username already exists in table
        if (cursor != null && cursor.getCount() > 0){
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userType.getUsername());
        values.put(COLUMN_PASSWORD, userType.getPassword());
        values.put(COLUMN_FIRSTNAME, userType.getFirstname());
        values.put(COLUMN_LASTNAME, userType.getLastname());
        values.put(COLUMN_USERTYPE, userType.getClass().getSimpleName());
        writeDB.insert(TABLE_LOGIN, null, values);
        return true;
    }

    /**
     * Looks in database for user with requested username, and returns an
     * object of UserType corresponding to said user's role.
     * Returns null if no such user found.
     *
     * @param username username to look up
     * @return object representing user found
     */
    public UserType findUserByUsername(String username){
        UserType usertype;
        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_LOGIN
                        + " WHERE " + COLUMN_USERNAME + " = ?",
                new String[]{username});

        if (cursor.moveToFirst()){
            String password = cursor.getString(1);
            String firstname = cursor.getString(2);
            String lastname = cursor.getString(3);
            if (cursor.getString(4)
                    .equals("Admin")){
                usertype = new Admin();
            } else if (cursor.getString(4)
                    .equals("ServiceProvider")){
                usertype = new ServiceProvider(username, password, firstname, lastname);
            } else {
                usertype = new User(username, password, firstname, lastname);
            }
        } else {
            usertype = null;
        }
        cursor.close();
        return usertype;
    }

    /**
     * Updates user login information and name for user with requested username.
     * Returns true if a user of said username was found and entry updated.
     * Returns false if no user was found of said username.
     *
     *
     * @param username username of entry to update
     * @param password new password
     * @param firstname new first name
     * @param lastname new last name
     *
     * @return whether updating user information was successful
     */
    public boolean updateUserInfo(String username, String password, String firstname, String lastname){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);

        return writeDB.update(TABLE_LOGIN, values, COLUMN_USERNAME+" = ?",
                new String[]{username}) > 0;

    }

    /**
     * Looks in database for user with requested username, and deletes the corresponding
     * entry. Returns true if a user was deleted, false otherwise.
     *
     * @param username username of entry to delete
     * @return whether a user was deleted
     */
    public boolean deleteUser(String username) {
        return writeDB.delete(TABLE_LOGIN,  COLUMN_USERNAME+" = ?",
                new String[]{username}) > 0;
    }

    /**
     * Prints all entries of table. One row is printed per line. Columns are
     * separated by spaces.
     *
     * @param tableName name of table to print
     */
    public void printTable(String tableName){
        Cursor cursor = readDB.rawQuery("SELECT * FROM "+tableName, null);
        cursor.moveToFirst();
        for (int i = 0; i<cursor.getCount(); i++){
            String[] columns = cursor.getColumnNames();
            for (String name: columns) {
                System.out.print(cursor.getString(cursor.getColumnIndex(name))+" ");
            }
            System.out.println();
            cursor.moveToNext();
        }
        cursor.close();
    }

    /**
     * Returns a list of String arrays containing the username, first name,
     * last name, and user type of every user in TABLE_LOGIN.
     *
     * @return list of arrays of [username, first name, last name, user type]
     */
    public List<String[]> getAllUsers(){
        List<String[]> listOfUsers = new LinkedList<>();
        String[] user;
        Cursor cursor = readDB.rawQuery("SELECT " + COLUMN_USERNAME + ", "
                + COLUMN_FIRSTNAME + ", "
                + COLUMN_LASTNAME + ", "
                + COLUMN_USERTYPE
                + " FROM "+TABLE_LOGIN, null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                user = new String[cursor.getColumnNames().length];
                for (int j = 0; j < cursor.getColumnNames().length; j++) {
                    user[j] = cursor.getString(j);
                }
                listOfUsers.add(user);
            }
        }
        cursor.close();
        return listOfUsers;
    }

}
