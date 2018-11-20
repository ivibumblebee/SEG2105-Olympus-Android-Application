package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.GregorianCalendar;

import com.uottawa.olympus.olympusservices.Booking.Status;

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
    private static final int DB_VERSION = 5;
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
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_LICENSED = "licensed";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_SALT = "salt";

    //name of table containing services and rates
    private static final String TABLE_SERVICES = "services";
    //columns of TABLE_SERVICES
    private static final String COLUMN_SERVICE = "service";
    private static final String COLUMN_RATE = "rate";

    //name of table containing service provider information
    private static final String TABLE_SERVICEPROVIDERS = "serviceProviders";
    //columns of TABLE_SERVICEPROVIDERS
    private static final String COLUMN_SERVICEPROVIDERNAME = "username";
    private static final String COLUMN_SERVICEPROVIDERSERVICE = "service";
    private static final String COLUMN_AVERAGERATING = "averageRating";
    private static final String COLUMN_RATERS = "raters";
    private static final String COLUMN_ACTIVE = "active";

    //name of table containing service provider availability
    //availability is stored as number of minutes from 00:00
    private static final String TABLE_AVAILABILITY = "availability";
    //columns of TABLE_AVAILABILITY
    private static final String COLUMN_AVAILABILITYNAME = "username";
    private static final String COLUMN_MONSTART = "mondaystart";
    private static final String COLUMN_MONEND = "mondayend";
    private static final String COLUMN_TUESTART = "tuesdaystart";
    private static final String COLUMN_TUEEND = "tuesdayend";
    private static final String COLUMN_WEDSTART = "wednesdaystart";
    private static final String COLUMN_WEDEND = "wednesdayend";
    private static final String COLUMN_THUSTART = "thursdaystart";
    private static final String COLUMN_THUEND = "thursdayend";
    private static final String COLUMN_FRISTART = "fridaystart";
    private static final String COLUMN_FRIEND = "fridayend";
    private static final String COLUMN_SATSTART = "saturdaystart";
    private static final String COLUMN_SATEND = "saturdayend";
    private static final String COLUMN_SUNSTART = "sundaystart";
    private static final String COLUMN_SUNEND = "sundayend";

    //name of table containing booking
    private static final String TABLE_BOOKINGS = "bookings";
    //columns of TABLE_BOOKING
    private static final String COLUMN_BOOKINGSERVICEPROVIDER = "serviceProvider";
    private static final String COLUMN_BOOKINGHOMEOWNER = "homeOwner";
    private static final String COLUMN_BOOKINGSERVICE = "service";
    private static final String COLUMN_BOOKINGYEAR = "year";
    private static final String COLUMN_BOOKINGMONTH = "month";
    private static final String COLUMN_BOOKINGDATE = "day";
    private static final String COLUMN_BOOKINGSTART = "starttime";
    private static final String COLUMN_BOOKINGEND = "endtime";
    private static final String COLUMN_BOOKINGSTATUS = "status";
    private static final String COLUMN_RATING = "rating";

    /**
     * Creates an instance of DBHelper to allow activities to access and
     * perform CRUD operations on the database via DBHelper's methods
     *
     * @param context current activity calling DBHelper
     */
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        //since these methods take a while we will call them once and store the returned dbs
        readDB = this.getReadableDatabase();
        writeDB = this.getWritableDatabase();
        //pre-add the admin user
        addUser(new Admin());
        addUser(new ServiceProvider("testing", "testing", "TestFirst", "TestLast",
                                    "1234 Test Street, Testinaro, Timor-Teste", "6136136163",
                                    "Test Company", false,
                                    "Tested 10 years"));
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //making the table containing user login information
        String CREATE_LOGIN_TABLE = "CREATE TABLE "+ TABLE_LOGIN + "("
                + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_FIRSTNAME + " TEXT DEFAULT 'FirstName',"
                + COLUMN_LASTNAME + " TEXT DEFAULT 'LastName',"
                + COLUMN_USERTYPE + " TEXT NOT NULL, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_COMPANY + " TEXT, "
                + COLUMN_LICENSED + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_SALT + " TEXT "
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        //making the table containing services and their rates
        String CREATE_SERVICES_TABLE = "CREATE TABLE "+ TABLE_SERVICES + "("
                + COLUMN_SERVICE + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                + COLUMN_RATE + " REAL DEFAULT 0.0" + ")";
        db.execSQL(CREATE_SERVICES_TABLE);

        //making the table containing service providers and offered services
        String CREATE_SERVICEPROVIDERS_TABLE = "CREATE TABLE "+ TABLE_SERVICEPROVIDERS + "("
                + COLUMN_SERVICEPROVIDERNAME + " TEXT, "
                + COLUMN_SERVICEPROVIDERSERVICE + " TEXT, "
                + COLUMN_AVERAGERATING + " REAL DEFAULT 0, "
                + COLUMN_RATERS + " REAL DEFAULT 0, "
                + COLUMN_ACTIVE + " TEXT DEFAULT 'active', "
                //service provider name is foreign key
                + " FOREIGN KEY(" + COLUMN_SERVICEPROVIDERNAME
                + ") REFERENCES " + TABLE_LOGIN + "(" + COLUMN_USERNAME +"), "
                //service is also foreign key
                + " FOREIGN KEY(" + COLUMN_SERVICEPROVIDERSERVICE
                + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE +") "
                + ")";
        db.execSQL(CREATE_SERVICEPROVIDERS_TABLE);

        //making the table containing services and their rates
        String CREATE_AVAILABILITY_TABLE = "CREATE TABLE "+ TABLE_AVAILABILITY + "("
                + COLUMN_AVAILABILITYNAME + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK, "
                + COLUMN_MONSTART + " REAL, "
                + COLUMN_MONEND + " REAL, "
                + COLUMN_TUESTART + " REAL, "
                + COLUMN_TUEEND + " REAL, "
                + COLUMN_WEDSTART + " REAL, "
                + COLUMN_WEDEND + " REAL, "
                + COLUMN_THUSTART + " REAL, "
                + COLUMN_THUEND + " REAL, "
                + COLUMN_FRISTART + " REAL, "
                + COLUMN_FRIEND + " REAL, "
                + COLUMN_SATSTART + " REAL, "
                + COLUMN_SATEND + " REAL, "
                + COLUMN_SUNSTART + " REAL, "
                + COLUMN_SUNEND + " REAL)";
        db.execSQL(CREATE_AVAILABILITY_TABLE);

        //making the table containing bookings
        String CREATE_BOOKING_TABLE = "CREATE TABLE "+ TABLE_BOOKINGS + "("
                + COLUMN_BOOKINGSERVICEPROVIDER + " TEXT,"
                + COLUMN_BOOKINGHOMEOWNER + " TEXT,"
                + COLUMN_BOOKINGSERVICE + " TEXT, "
                + COLUMN_BOOKINGYEAR + " REAL,"
                + COLUMN_BOOKINGMONTH + " REAL, "
                + COLUMN_BOOKINGDATE + " REAL, "
                + COLUMN_BOOKINGSTART + " REAL, "
                + COLUMN_BOOKINGEND + " REAL, "
                + COLUMN_BOOKINGSTATUS + " TEXT, "
                + COLUMN_RATING + " REAL DEFAULT 0 "
                + ")";
        db.execSQL(CREATE_BOOKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        switch(oldVersion){
            case 1: //going from db version 1 to 2
                //change usertype of Users to Homeowner
                ContentValues values = new ContentValues();
                values.put(COLUMN_USERTYPE, "HomeOwner");
                db.update(TABLE_LOGIN, values, COLUMN_USERTYPE + " = ?", new String[]{"User"});

                //if services table is not created, create it
                db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_SERVICES + "("
                        + COLUMN_SERVICE + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK,"
                        + COLUMN_RATE + " REAL DEFAULT 0.0" + ")");

            case 2: //going from db versions 1-2 to 3
                db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_SERVICEPROVIDERS + "("
                        + COLUMN_SERVICEPROVIDERNAME + " TEXT NOT NULL, "
                        + COLUMN_SERVICEPROVIDERSERVICE + " TEXT NOT NULL, "
                        //service provider name is foreign key
                        + " FOREIGN KEY(" + COLUMN_SERVICEPROVIDERNAME
                        + ") REFERENCES " + TABLE_LOGIN + "(" + COLUMN_USERNAME +"), "
                        //service is also foreign key
                        + " FOREIGN KEY(" + COLUMN_SERVICEPROVIDERSERVICE
                        + ") REFERENCES " + TABLE_SERVICES + "(" + COLUMN_SERVICE +") "
                        + ")");
                db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_AVAILABILITY + "("
                        + COLUMN_AVAILABILITYNAME + " TEXT UNIQUE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK, "
                        + COLUMN_MONSTART + " REAL, "
                        + COLUMN_MONEND + " REAL, "
                        + COLUMN_TUESTART + " REAL, "
                        + COLUMN_TUEEND + " REAL, "
                        + COLUMN_WEDSTART + " REAL, "
                        + COLUMN_WEDEND + " REAL, "
                        + COLUMN_THUSTART + " REAL, "
                        + COLUMN_THUEND + " REAL, "
                        + COLUMN_FRISTART + " REAL, "
                        + COLUMN_FRIEND + " REAL, "
                        + COLUMN_SATSTART + " REAL, "
                        + COLUMN_SATEND + " REAL, "
                        + COLUMN_SUNSTART + " REAL, "
                        + COLUMN_SUNEND + " REAL)");

                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_ADDRESS + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_PHONE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_COMPANY + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_LICENSED + " TEXT");
            case 3:
                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_DESCRIPTION + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_LOGIN + " ADD COLUMN " + COLUMN_SALT + " TEXT");
            case 4:
                db.execSQL("ALTER TABLE " + TABLE_SERVICEPROVIDERS + " ADD COLUMN " + COLUMN_AVERAGERATING + " REAL DEFAULT 0");
                db.execSQL("ALTER TABLE " + TABLE_SERVICEPROVIDERS + " ADD COLUMN " + COLUMN_RATERS + " REAL DEFAULT 0");
                db.execSQL("ALTER TABLE " + TABLE_SERVICEPROVIDERS + " ADD COLUMN " + COLUMN_ACTIVE + " TEXT DEFAULT 'active'");
                db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_BOOKINGS + "("
                        + COLUMN_BOOKINGSERVICEPROVIDER + " TEXT,"
                        + COLUMN_BOOKINGHOMEOWNER + " TEXT,"
                        + COLUMN_BOOKINGSERVICE + " TEXT, "
                        + COLUMN_BOOKINGYEAR + " REAL,"
                        + COLUMN_BOOKINGMONTH + " REAL, "
                        + COLUMN_BOOKINGDATE + " REAL, "
                        + COLUMN_BOOKINGSTART + " REAL, "
                        + COLUMN_BOOKINGEND + " REAL, "
                        + COLUMN_BOOKINGSTATUS + " TEXT, "
                        + COLUMN_RATING + " REAL DEFAULT 0 "
                        + ")");
        }
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
        if (userType == null) return false;
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
        if (userType instanceof ServiceProvider){
            ServiceProvider serviceProvider = (ServiceProvider)userType;

            String address = serviceProvider.getAddress();
            if (address != null){
                values.put(COLUMN_ADDRESS, address);
            }

            String phone = serviceProvider.getPhonenumber();
            if (phone != null){
                values.put(COLUMN_PHONE, phone);
            }

            String company = serviceProvider.getCompanyname();
            if (company != null){
                values.put(COLUMN_COMPANY, company);
            }

            String licensed = String.valueOf(serviceProvider.isLicensed());
            values.put(COLUMN_LICENSED, licensed);

            String description = serviceProvider.getDescription();
            if (description != null){
                values.put(COLUMN_DESCRIPTION, description);
            }

        }

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
        if (username == null) return null;
        UserType usertype = null;
        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_LOGIN
                        + " WHERE " + COLUMN_USERNAME + " = ?",
                new String[]{username});

        if (cursor.moveToFirst()){
            String password = cursor.getString(1);
            String firstname = cursor.getString(2);
            String lastname = cursor.getString(3);
            String address = cursor.getString(5);
            String phonenumber = cursor.getString(6);
            String companyname = cursor.getString(7);
            boolean licensed = Boolean.parseBoolean(cursor.getString(8));
            String description = cursor.getString(9);
            if (cursor.getString(4)
                    .equals("Admin")){
                usertype = new Admin();
            } else if (cursor.getString(4)
                    .equals("ServiceProvider")){
                ServiceProvider serviceProvider = new ServiceProvider(username, password, firstname, lastname, address, phonenumber, companyname, licensed, description);
                getAllServicesProvidedByUser(serviceProvider);
                getAvailabilities(serviceProvider);
                usertype = serviceProvider;
            } else {
                usertype = new HomeOwner(username, password, firstname, lastname);
            }
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
        return updateUserInfo(username, password, firstname, lastname,
                null, null, null, null, null);
    }



    public boolean updateUserInfo(String username, String password, String firstname, String lastname,
                                  String address, String phonenumber, String companyname, Boolean licensed,
                                  String description){
        ContentValues values = new ContentValues();
        if (password != null && !password.equals("")) values.put(COLUMN_PASSWORD, password);
        if (firstname != null && !firstname.equals("")) values.put(COLUMN_FIRSTNAME, firstname);
        if (lastname != null && !lastname.equals(""))values.put(COLUMN_LASTNAME, lastname);
        if (address != null && !address.equals(""))values.put(COLUMN_ADDRESS, address);
        if (phonenumber != null && !phonenumber.equals(""))values.put(COLUMN_PHONE, phonenumber);
        if (companyname != null && !companyname.equals(""))values.put(COLUMN_COMPANY, companyname);
        if (licensed != null) values.put(COLUMN_LICENSED, String.valueOf(licensed));
        if (description != null )values.put(COLUMN_DESCRIPTION, description);


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
        if (username == null) return false;

        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_LOGIN
                        + " WHERE " + COLUMN_USERNAME + " = ?",
                new String[]{username});

        if (!cursor.moveToFirst()) return false;

        boolean deleted;
        if (cursor.getString(4).equals("ServiceProvider")) {
            deleted = writeDB.delete(TABLE_LOGIN,  COLUMN_USERNAME+" = ?",
                    new String[]{username}) > 0;

            if (deleted) {
                writeDB.delete(TABLE_SERVICEPROVIDERS, COLUMN_SERVICEPROVIDERNAME + " = ?",
                        new String[]{username});
                writeDB.delete(TABLE_AVAILABILITY, COLUMN_AVAILABILITYNAME + " = ?",
                        new String[]{username});
            }
            cursor.close();
            return deleted;
        } else {
            cursor.close();
            return writeDB.delete(TABLE_LOGIN,  COLUMN_USERNAME+" = ?",
                    new String[]{username}) > 0;
        }
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
        if (service == null) return false;
        //Check for duplicate username by querying services table
        Cursor cursor = writeDB.query(TABLE_SERVICES,
                new String[] {COLUMN_SERVICE},
                COLUMN_SERVICE + " = ?",
                new String[]{service.getName().toLowerCase().trim()},
                null, null, null,
                "1");
        //If cursor has 1+ elements in it, username already exists in table
        if (cursor != null && cursor.getCount() > 0){
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICE, service.getName().toLowerCase().trim());
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
        if (serviceName == null) return null;

        Service service;
        serviceName = serviceName.toLowerCase().trim();
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
        if (service == null) return false;
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATE, service.getRate());

        return writeDB.update(TABLE_SERVICES, values, COLUMN_SERVICE+" = ?",
                new String[]{service.getName().toLowerCase().trim()}) > 0;
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
        if (name == null) return false;

        name = name.toLowerCase().trim();
        ContentValues values = new ContentValues();
        if (rate > 0)
            values.put(COLUMN_RATE, rate);

        return writeDB.update(TABLE_SERVICES, values, COLUMN_SERVICE+" = ?",
                new String[]{name}) > 0;
    }

    /**
     * Looks in database for a service, and deletes the corresponding
     * entry. Returns true if a service was deleted, false otherwise.
     *
     * @param service service of entry to delete
     * @return whether the service was deleted
     */
    public boolean deleteService(String service) {
        if (service == null) return false;

        boolean deleted;
        String nullify = null;
        service = service.toLowerCase().trim();

        deleted = writeDB.delete(TABLE_SERVICES,  COLUMN_SERVICE+" = ?",
                new String[]{service}) > 0;

        if (deleted) {
            writeDB.delete(TABLE_SERVICEPROVIDERS, COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                    new String[]{service});
        }
        return deleted;
    }


    /**
     * Returns a list of String arrays containing the service categories,
     * names and hourly rates.
     *
     * @return list of arrays of [service, rate]
     */
    public List<String[]> getAllServices(){
        return getAll("SELECT * FROM " + TABLE_SERVICES + " ORDER BY " + COLUMN_SERVICE);
    }

    public boolean addServiceProvidedByUser(ServiceProvider serviceProvider, Service service){
        if (serviceProvider == null || service == null) return false;
        return addServiceProvidedByUser(serviceProvider.getUsername(), service.getName());
    }

    public boolean addServiceProvidedByUser(ServiceProvider serviceProvider, String serviceName){
        if (serviceProvider == null || serviceName == null) return false;
        return addServiceProvidedByUser(serviceProvider.getUsername(), serviceName);
    }

    public boolean addServiceProvidedByUser(String serviceProviderUsername, String serviceName){
        if (serviceProviderUsername == null || serviceName == null) return false;

        //TODO: Check if serviceProviderUsername and serviceName are in db before adding

        serviceName = serviceName.toLowerCase().trim();

        //Check for duplicate username/service combination by querying login table
        Cursor cursor = writeDB.query(TABLE_SERVICEPROVIDERS,
                new String[] {COLUMN_ACTIVE},
                COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                        + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                new String[]{serviceProviderUsername, serviceName},
                null, null, null,
                "1");
        //If cursor has 1+ elements in it, username/service combination already exists in table
        if (cursor != null && cursor.getCount() > 0){
            if (cursor.getString(0).equals("active")){
                cursor.close();
                return false;
            } else {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ACTIVE, "active");
                return writeDB.update(TABLE_SERVICEPROVIDERS, values,
                        COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                                + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                        new String[]{serviceProviderUsername, serviceName})>0;
            }

        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVICEPROVIDERNAME, serviceProviderUsername);
        values.put(COLUMN_SERVICEPROVIDERSERVICE, serviceName);
        writeDB.insert(TABLE_SERVICEPROVIDERS, null, values);
        return true;
    }

    public boolean deleteServiceProvidedByUser(ServiceProvider serviceProvider, Service service){
        if (serviceProvider == null || service == null) return false;
        return deleteServiceProvidedByUser(serviceProvider.getUsername(), service.getName());
    }

    public boolean deleteServiceProvidedByUser(ServiceProvider serviceProvider, String serviceName){
        if (serviceProvider == null || serviceName == null) return false;
        return deleteServiceProvidedByUser(serviceProvider.getUsername(), serviceName);
    }

    public boolean deleteServiceProvidedByUser(String serviceProviderUsername, String serviceName){
        if (serviceProviderUsername == null || serviceName == null) return false;
        serviceName = serviceName.toLowerCase().trim();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVE, "inactive");

        return writeDB.update(TABLE_SERVICEPROVIDERS, values,
                COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                        + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                new String[]{serviceProviderUsername, serviceName}) > 0;
    }

    public List<String[]> getAllServicesProvidedByUser(ServiceProvider serviceProvider){
        if (serviceProvider == null) return new ArrayList<>();

        return getAllServicesProvidedByUser(serviceProvider.getUsername());
    }

    public List<String[]> getAllServicesProvidedByUser(String serviceProviderName){
        if (serviceProviderName == null) return new ArrayList<>();

        return getAll("SELECT " + TABLE_SERVICES + "." + COLUMN_SERVICE + ", "
                + TABLE_SERVICES + "." + COLUMN_RATE
                + " FROM " + TABLE_SERVICES
                + " JOIN " + TABLE_SERVICEPROVIDERS
                + " ON " + TABLE_SERVICEPROVIDERS + "." + COLUMN_SERVICEPROVIDERSERVICE + " = "
                + TABLE_SERVICES + "." + COLUMN_SERVICE
                + " AND " + TABLE_SERVICEPROVIDERS + "." + COLUMN_SERVICEPROVIDERNAME
                + "= '" + serviceProviderName + "'"
                + " AND " + TABLE_SERVICEPROVIDERS + "." + COLUMN_ACTIVE  + " = 'active'");
    }

    public List<String[]> getAllProvidersByService(Service service){
        if (service == null) return new ArrayList<>();

        return getAllProvidersByService(service.getName());
    }

    public List<String[]> getAllProvidersByService(String serviceName){
        if (serviceName == null) return new ArrayList<>();

        serviceName = serviceName.toLowerCase().trim();
        return getAll("SELECT " + COLUMN_SERVICEPROVIDERNAME
                + " FROM " + TABLE_SERVICEPROVIDERS
                + " WHERE " + COLUMN_SERVICEPROVIDERSERVICE + " = '"
                + serviceName + "'"
                + " AND " + TABLE_SERVICEPROVIDERS + "." + COLUMN_ACTIVE  + " = 'active'");
    }

    public boolean updateAvailability(ServiceProvider serviceProvider){
        //availability is stored as number of minutes from 00:00
        if (serviceProvider == null) return false;
        int[][] availabilities = serviceProvider.getAvailabilities();
        if (availabilities == null) return false;

        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_AVAILABILITY
                        + " WHERE " + COLUMN_AVAILABILITYNAME + " = ?",
                new String[]{serviceProvider.getUsername()});

        ContentValues contentValues = new ContentValues();
        addAvailabilityToContentValues(contentValues, COLUMN_MONSTART, COLUMN_MONEND, availabilities[0]);
        addAvailabilityToContentValues(contentValues, COLUMN_TUESTART, COLUMN_TUEEND, availabilities[1]);
        addAvailabilityToContentValues(contentValues, COLUMN_WEDSTART, COLUMN_WEDEND, availabilities[2]);
        addAvailabilityToContentValues(contentValues, COLUMN_THUSTART, COLUMN_THUEND, availabilities[3]);
        addAvailabilityToContentValues(contentValues, COLUMN_FRISTART, COLUMN_FRIEND, availabilities[4]);
        addAvailabilityToContentValues(contentValues, COLUMN_SATSTART, COLUMN_SATEND, availabilities[5]);
        addAvailabilityToContentValues(contentValues, COLUMN_SUNSTART, COLUMN_SUNEND, availabilities[6]);

        if (!cursor.moveToFirst()){
            contentValues.put(COLUMN_AVAILABILITYNAME, serviceProvider.getUsername());
            writeDB.insert(TABLE_AVAILABILITY, null, contentValues);
        } else {
            writeDB.update(TABLE_AVAILABILITY, contentValues,
                    COLUMN_AVAILABILITYNAME + " = ?", new String[]{serviceProvider.getUsername()});
        }
        return true;
    }

    private void addAvailabilityToContentValues(ContentValues contentValues,
                                                String startColumn, String endColumn,
                                                int[] startAndEndTimes){
        if (startAndEndTimes == null){
            contentValues.put(startColumn, 0);
            contentValues.put(endColumn, 0);
        } else {
            int startTime = startAndEndTimes[0]*60+startAndEndTimes[1];
            int endTime = startAndEndTimes[2]*60+startAndEndTimes[3];
            if (endTime - startTime <=0 || startTime > 1439 || startTime < 0
                    || endTime > 1439 || endTime <= 0) {
                contentValues.put(startColumn, 0);
                contentValues.put(endColumn, 0);
            } else {
                contentValues.put(startColumn, startTime);
                contentValues.put(endColumn, endTime);
            }
        }

    }


    //note that this method overwrites serviceProvider's availability if it exists
    public int[][] getAvailabilities(ServiceProvider serviceProvider){
        if (serviceProvider==null) return new int[7][4];
        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_AVAILABILITY
                        + " WHERE " + COLUMN_AVAILABILITYNAME + " = ?",
                new String[]{serviceProvider.getUsername()});
        if (cursor.moveToFirst()){
            for (int i = 0; i < 7; i++) {
                int start = cursor.getInt(i*2+1);
                int end = cursor.getInt(i*2+2);
                serviceProvider.setAvailabilities(i, start/60, start%60,
                        end/60, end%60);
            }
        } else {
            return new int[7][4];
        }
        return serviceProvider.getAvailabilities();
    }

    public int[][] getAvailabilities(String serviceProviderName){
        int[][] availabilities = new int[7][4];
        if (serviceProviderName==null) return availabilities;

        Cursor cursor = readDB.rawQuery("SELECT * FROM " + TABLE_AVAILABILITY
                        + " WHERE " + COLUMN_AVAILABILITYNAME + " = ?",
                new String[]{serviceProviderName});
        if (cursor.moveToFirst()){
            for (int i = 0; i < 7; i++) {
                int start = cursor.getInt(i*2+1);
                int end = cursor.getInt(i*2+2);
                availabilities[i] = new int[]{start/60, start%60, end/60, end%60};
            }
        }
        return availabilities;
    }


    public boolean addBooking(Booking booking){
        if (booking == null) return false;

        GregorianCalendar current = new GregorianCalendar();
        current.setTimeInMillis(System.currentTimeMillis());
        GregorianCalendar bookDate = new GregorianCalendar(booking.getYear(), booking.getMonth(),
                booking.getDay(), booking.getStarth(), booking.getStartmin());

        //check if time of booking is after this time
        if (current.compareTo(bookDate) > 0) return false;


        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKINGSTART, booking.getStarth()*60 + booking.getStartmin());
        contentValues.put(COLUMN_BOOKINGEND, booking.getEndh()*60 + booking.getEndmin());
        contentValues.put(COLUMN_BOOKINGDATE, booking.getDay());
        contentValues.put(COLUMN_BOOKINGMONTH, booking.getMonth());
        contentValues.put(COLUMN_BOOKINGYEAR, booking.getYear());
        contentValues.put(COLUMN_BOOKINGSERVICEPROVIDER, booking.getServiceprovider().getUsername());
        contentValues.put(COLUMN_BOOKINGHOMEOWNER, booking.getHomeowner().getUsername());
        contentValues.put(COLUMN_BOOKINGSERVICE, booking.getService().getName());
        contentValues.put(COLUMN_BOOKINGSTATUS, Status.PENDING.toString());
        contentValues.put(COLUMN_RATING, 0);

        writeDB.insert(TABLE_BOOKINGS, null, contentValues);

        return true;
    }

    public List<Booking> findBookings(String username){
        List<Booking> bookingList = new ArrayList<>();
        ServiceProvider serviceProvider = null;
        HomeOwner homeOwner = null;
        Cursor cursor = writeDB.rawQuery("SELECT * FROM " + TABLE_LOGIN + " WHERE "
                        + COLUMN_BOOKINGSERVICEPROVIDER + " = ?",
                        new String[] {username});
        if (!cursor.moveToFirst() || cursor.getString(5).equals("Admin")) return bookingList;

        if (cursor.getString(5).equals("ServiceProvider")) {
            serviceProvider = (ServiceProvider)findUserByUsername(username);
            cursor = writeDB.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " WHERE "
                    + COLUMN_BOOKINGSERVICEPROVIDER + " = ? AND "
                    + COLUMN_BOOKINGSTATUS + " != ?", new String[]{username, Status.CANCELLED.toString()});
        } else {
            homeOwner = (HomeOwner)findUserByUsername(username);
            cursor = writeDB.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " WHERE "
                    + COLUMN_BOOKINGHOMEOWNER + " = ? AND "
                    + COLUMN_BOOKINGSTATUS + " != ?", new String[]{username, Status.CANCELLED.toString()});
        }

        if (cursor.moveToFirst()){
            for (int i=0; i<cursor.getCount(); i++){
                int startTime = cursor.getInt(6);
                int endTime = cursor.getInt(7);
                int starth = startTime / 60;
                int startmin = startTime % 60;
                int endh = endTime / 60;
                int endmin = endTime % 60;
                int day = cursor.getInt(5);
                int month = cursor.getInt(4);
                int year = cursor.getInt(3);
                String stat = cursor.getString(8);
                Status status = (stat.equals("Pending")? Status.PENDING : Status.CONFIRMED);
                ServiceProvider serviceprovider = (serviceProvider == null?
                        (ServiceProvider)findUserByUsername(cursor.getString(0)):serviceProvider);
                HomeOwner homeowner = (homeOwner == null?
                        (HomeOwner) findUserByUsername(cursor.getString(1)):homeOwner);
                Service service = findService(cursor.getString(3));
                Booking booking = new Booking(starth, startmin, endh, endmin, day, month, year,
                        serviceprovider, homeowner, service);
                booking.setStatus(status);
                bookingList.add(booking);

                cursor.moveToNext();
            }
        }
        return bookingList;
    }


//    public boolean isProviderAvailable(String serviceProvider, String monthDayYear,
//                                       int starth, int startmin, int endh, int endmin) {
//        String[] date = monthDayYear.split("/");
//        return isProviderAvailable(serviceProvider, Integer.parseInt(date[2]),
//                Integer.parseInt(date[0]), Integer.parseInt(date[1]),
//                starth, startmin, endh, endmin);
//    }

//    /**
//     * Returns false if end time before start time, or service provider not available on that day
//     *
//     * @param serviceProvider
//     * @param year
//     * @param month
//     * @param day
//     * @param starth
//     * @param startmin
//     * @param endh
//     * @param endmin
//     * @return
//     */
//    public boolean isProviderAvailable(String serviceProvider, int year, int month, int day,
//                                       int starth, int startmin, int endh, int endmin){
//
//        int bookingStart = starth*60 + startmin;
//        int bookingEnd = endh*60 + endmin;
//
//        if (bookingEnd < bookingStart) return false;
//
//        GregorianCalendar start = new GregorianCalendar(year, month-1, day);
//        //Calendar.DAY_OF_WEEK starts with 1 for Sunday, and onwards
//        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK)-1;
//        Cursor cursor = null;
//        int availabilityStart, availabilityEnd;
//
//        //Check availabilities on day of week
//        switch (dayOfWeek){
//            case 1: //Sunday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_SUNSTART, COLUMN_SUNEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 2: //Monday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_MONSTART, COLUMN_MONEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 3: //Tuesday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_TUESTART, COLUMN_TUEEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 4: //Wednesday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_WEDSTART, COLUMN_WEDEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 5: //Thursday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_THUSTART, COLUMN_THUEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 6: //Friday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_FRISTART, COLUMN_FRIEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//            case 7: //Saturday
//                cursor = writeDB.query(TABLE_AVAILABILITY, new String[] {COLUMN_SATSTART, COLUMN_SATEND},
//                        COLUMN_AVAILABILITYNAME + " = ?", new String[] {serviceProvider}, null,
//                        null, null, null);
//                break;
//        }
//
//        cursor.moveToFirst();
//        availabilityStart = cursor.getInt(0);
//        availabilityEnd = cursor.getInt(1);
//
//        //service provider not available if availability end is 0, if availability starts after booking start,
//        // or if availability ends before booking end
//        if (availabilityEnd == 0 || availabilityStart > bookingStart || availabilityEnd < bookingEnd) return false;
//
//
//        //now we know for sure that the service provider is available on said day of the week
//        //we check to see if any of the bookings overlap on this time slot
//        cursor = writeDB.query(TABLE_BOOKINGS, new String[] {COLUMN_BOOKINGSTART, COLUMN_BOOKINGEND},
//                COLUMN_BOOKINGSERVICEPROVIDER + " = ? AND "
//                + COLUMN_BOOKINGYEAR + " = ? AND "
//                + COLUMN_BOOKINGMONTH + " = ? AND "
//                + COLUMN_BOOKINGDATE + " = ? AND "
//                + COLUMN_BOOKINGSTATUS + " != ?",
//                new String[] {serviceProvider, String.valueOf(year),
//                    String.valueOf(month), String.valueOf(day), Status.CANCELLED.toString()},
//                null, null, COLUMN_BOOKINGSTART, null);
//        boolean found = cursor.moveToFirst();
//        cursor.close();
//        return found;
//    }



    public boolean confirmBooking(Booking booking){
        if (booking == null) return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKINGSTATUS, Status.CONFIRMED.toString());

        GregorianCalendar current = new GregorianCalendar();
        current.setTimeInMillis(System.currentTimeMillis());
        GregorianCalendar bookDate = new GregorianCalendar(booking.getYear(), booking.getMonth(),
                booking.getDay(), booking.getStarth(), booking.getStartmin());

        //check if time of booking is before this time
        if (current.compareTo(bookDate) > 0) return false;

        return writeDB.update(TABLE_BOOKINGS, contentValues,
                COLUMN_BOOKINGSERVICEPROVIDER + " = ? AND "
                + COLUMN_BOOKINGHOMEOWNER + " = ? AND "
                + COLUMN_BOOKINGYEAR + " = ? AND "
                + COLUMN_BOOKINGMONTH + " = ? AND "
                + COLUMN_BOOKINGDATE + " = ? AND "
                + COLUMN_BOOKINGSTART + " = ? AND "
                + COLUMN_BOOKINGSTATUS + " = ?)",
                new String[] {booking.getServiceprovider().getUsername(),
                        booking.getHomeowner().getUsername(),
                        String.valueOf(booking.getYear()),
                        String.valueOf(booking.getMonth()),
                        String.valueOf(booking.getDay()),
                        String.valueOf(booking.getStarth()*60 + booking.getStartmin()),
                        Status.PENDING.toString()}) > 0;
    }

    public boolean cancelBooking(Booking booking){
        if (booking == null) return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOKINGSTATUS, Status.CANCELLED.toString());

        GregorianCalendar current = new GregorianCalendar();
        current.setTimeInMillis(System.currentTimeMillis());
        GregorianCalendar bookDate = new GregorianCalendar(booking.getYear(), booking.getMonth(),
                booking.getDay(), booking.getStarth(), booking.getStartmin());

        //check if time of booking has passed
        if (current.compareTo(bookDate) > 0) return false;

        return writeDB.update(TABLE_BOOKINGS, contentValues,
                COLUMN_BOOKINGSERVICEPROVIDER + " = ? AND "
                + COLUMN_BOOKINGHOMEOWNER + " = ? AND "
                + COLUMN_BOOKINGYEAR + " = ? AND "
                + COLUMN_BOOKINGMONTH + " = ? AND "
                + COLUMN_BOOKINGDATE + " = ? AND "
                + COLUMN_BOOKINGSTART + " = ?)",
                new String[] {booking.getServiceprovider().getUsername(),
                        booking.getHomeowner().getUsername(),
                        String.valueOf(booking.getYear()),
                        String.valueOf(booking.getMonth()),
                        String.valueOf(booking.getDay()),
                        String.valueOf(booking.getStarth()*60 + booking.getStartmin())}) > 0;
    }

    public boolean addRating(Booking booking, double rating){
        if (booking == null) return false;

        GregorianCalendar current = new GregorianCalendar();
        current.setTimeInMillis(System.currentTimeMillis());
        GregorianCalendar bookDate = new GregorianCalendar(booking.getYear(), booking.getMonth(),
                booking.getDay(), booking.getEndh(), booking.getEndmin());

        //check if time of booking is before this time
        if (current.compareTo(bookDate) < 0) return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RATING, rating);


        boolean updated =  writeDB.update(TABLE_BOOKINGS, contentValues,
                COLUMN_BOOKINGSERVICEPROVIDER + " = ? AND "
                        + COLUMN_BOOKINGHOMEOWNER + " = ? AND "
                        + COLUMN_BOOKINGYEAR + " = ? AND "
                        + COLUMN_BOOKINGMONTH + " = ? AND "
                        + COLUMN_BOOKINGDATE + " = ? AND "
                        + COLUMN_BOOKINGSTART + " = ?)",
                new String[] {booking.getServiceprovider().getUsername(),
                        booking.getHomeowner().getUsername(),
                        String.valueOf(booking.getYear()),
                        String.valueOf(booking.getMonth()),
                        String.valueOf(booking.getDay()),
                        String.valueOf(booking.getStarth()*60 + booking.getStartmin())}) > 0;

        if (updated) {
            Cursor cursor = writeDB.query(TABLE_SERVICEPROVIDERS, new String[]{COLUMN_AVERAGERATING, COLUMN_RATERS},
                    COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                            + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                    new String[] {booking.getServiceprovider().getUsername(), booking.getService().getName()},
                    null, null, null, null);
            cursor.moveToFirst();

            int currentRaters = cursor.getInt(1) + 1;
            double currentAverageRatings = cursor.getDouble(0) + rating;
            cursor.close();

            contentValues = new ContentValues();
            contentValues.put(COLUMN_AVERAGERATING, currentAverageRatings/(double)currentRaters);
            contentValues.put(COLUMN_RATERS, currentRaters);

            writeDB.update(TABLE_BOOKINGS, contentValues,
                    COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                    + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                    new String[] {booking.getServiceprovider().getUsername(),
                            booking.getService().getName()});
        }
        return updated;

    }

    public double getRatings(ServiceProvider serviceProvider, Service service){
        return getRatings(serviceProvider.getUsername(), service.getName());
    }

    public double getRatings(String serviceProviderName, String serviceName){
        Cursor cursor = writeDB.query(TABLE_SERVICEPROVIDERS, new String[]{COLUMN_AVERAGERATING},
                COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                        + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                new String[] {serviceProviderName, serviceName},
                null, null, null, null);
        cursor.moveToFirst();
        cursor.close();

        return cursor.getDouble(0);
    }

    public int getRaters(ServiceProvider serviceProvider, Service service){
        return getRaters(serviceProvider.getUsername(), service.getName());

    }

    public int getRaters(String serviceProviderName, String serviceName){
        Cursor cursor = writeDB.query(TABLE_SERVICEPROVIDERS, new String[]{COLUMN_RATERS},
                COLUMN_SERVICEPROVIDERNAME + " = ? AND "
                        + COLUMN_SERVICEPROVIDERSERVICE + " = ?",
                new String[] {serviceProviderName, serviceName},
                null, null, null, null);
        cursor.moveToFirst();
        cursor.close();

        return cursor.getInt(0);
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


    /**
     * Gets all items in a table
     * @param rawQuery SELECT * query
     * @return list of array representing all items in raw query
     */
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

