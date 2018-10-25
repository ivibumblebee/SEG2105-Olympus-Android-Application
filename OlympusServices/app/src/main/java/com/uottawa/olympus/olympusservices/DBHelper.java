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

    //name of table containing services and rates
    private static final String TABLE_SERVICES = "services";
    //columns of TABLE_LOGIN
    private static final String COLUMN_SERVICE = "service";
    private static final String COLUMN_RATE = "rate";


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
                + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_FIRSTNAME + " TEXT DEFAULT 'FirstName',"
                + COLUMN_LASTNAME + " TEXT DEFAULT 'LastName',"
                + COLUMN_USERTYPE + " TEXT NOT NULL" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_SERVICES_TABLE = "CREATE TABLE "+ TABLE_SERVICES + "("
                + COLUMN_SERVICE + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                + COLUMN_RATE + " REAL DEFAULT 0.0" + ")";

        db.execSQL(CREATE_SERVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //methods for table of users

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
     * Returns a list of String arrays containing the username, first name,
     * last name, and user type of every user in TABLE_LOGIN.
     *
     * @return list of arrays of [username, first name, last name, user type]
     */
    public List<String[]> getAllUsers(){
        return getAll("SELECT " + COLUMN_USERNAME + ", "
                + COLUMN_FIRSTNAME + ", "
                + COLUMN_LASTNAME + ", "
                + COLUMN_USERTYPE
                + " FROM "+TABLE_LOGIN);
    }

    //methods for table of services

    /**
     * Adds a service to the database. Returns false if service already
     * exists in the database.
     * Returns true if successful in adding service to database.
     *
     * @param service service to be added
     * @return whether adding service was successful
     */
    public boolean addService(Service service){
        //Check for duplicate username by querying login table
        Cursor cursor = writeDB.query(TABLE_SERVICES,
                new String[] {COLUMN_SERVICE},
                COLUMN_SERVICE + " = ?",
                new String[]{service.getName()},
                null, null, null,
                "1");
        //If cursor has 1+ elements in it, username already exists in table
        if (cursor != null && cursor.getCount() > 0){
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE, service.getName());
        values.put(COLUMN_RATE, service.getRate());
        writeDB.insert(TABLE_SERVICES, null, values);
        return true;
    }

    /**
     * Looks in database for service with specified, and returns an
     * object of Service if found.
     * Returns null if no such service found.
     *
     * @param serviceName service to look up
     * @return object representing service found
     */
    public Service findService(String serviceName){
        Service service;
        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_SERVICES
                        + " WHERE " + COLUMN_SERVICE + " = ?",
                new String[]{serviceName});

        if (cursor.moveToFirst()){
            String servName = cursor.getString(0);
            double rate = cursor.getDouble(1);
            service = new Service(servName, rate);
        } else {
            service = null;
        }
        cursor.close();
        return service;
    }

    /**
     * Updates service rate using a Service object.
     * Returns true if a service was found and entry updated.
     * Returns false if no service was found.
     *
     *
     * @param service service object containing updated values
     *
     * @return whether updating service information was successful
     */
    public boolean updateService(Service service){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATE, service.getRate());

        return writeDB.update(TABLE_SERVICES, values, COLUMN_SERVICE+" = ?",
                new String[]{service.getName()}) > 0;
    }

    /**
     * Updates service rate using input of service name and rate.
     * Returns true if a service was found and entry updated.
     * Returns false if no service was found.
     *
     *
     * @param name name of service
     * @param rate rate of service
     *
     * @return whether updating service information was successful
     */
    public boolean updateService(String name, double rate){
        ContentValues values = new ContentValues();
        if (rate > 0)
            values.put(COLUMN_RATE, rate);

        return writeDB.update(TABLE_SERVICES, values, COLUMN_SERVICE+" = ?",
                new String[]{name}) > 0;
    }


    /**
     * Returns a list of String arrays containing the service categories,
     * names and hourly rates.
     *
     * @return list of arrays of [service, rate]
     */
    public List<String[]> getAllServices(){
        return getAll("SELECT * FROM " + TABLE_SERVICES);
    }

    /**
     * Prints all entries of table. One row is printed per line. Columns are
     * separated by spaces.
     *
     * @param tableName name of table to print
     */
    void printTable(String tableName){
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


    private List<String[]> getAll(String rawQuery){
        List<String[]> list = new LinkedList<>();
        String[] infoArray;
        Cursor cursor = readDB.rawQuery(rawQuery,null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                infoArray = new String[cursor.getColumnNames().length];
                for (int j = 0; j < cursor.getColumnNames().length; j++) {
                    infoArray[j] = cursor.getString(j);
                }
                list.add(infoArray);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

}
