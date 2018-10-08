package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "UsersDB.db";
    private static String DB_PATH = "";
    private static SQLiteDatabase readDB;
    private static SQLiteDatabase writeDB;


    private static final String TABLE_LOGIN = "userInfo";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FIRSTNAME = "firstName";
    private static final String COLUMN_LASTNAME = "lastName";
    private static final String COLUMN_USERTYPE = "userType";

//    private static final String TABLE_SERVICEPROVIDERS = "serviceProviders";


//    private static final String TABLE_SERVICES = "services";


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        readDB = this.getReadableDatabase();
        writeDB = this.getWritableDatabase();
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


    public boolean addUser(UserType userType){
        //Check for duplicate username by querying login table
        Cursor cursor = writeDB.query(TABLE_LOGIN,
                                new String[] {COLUMN_USERNAME},
                                COLUMN_USERNAME + " = ?",
                                new String[]{userType.getUsername()},
                                null, null, null,
                                "1");
        //If cursor has 1+ elements in it, username already exists in table
        if (cursor != null && cursor.getCount() > 0)
            return false;
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


    public boolean updateUser(String username, String password, String firstname, String lastname){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FIRSTNAME, firstname);
        values.put(COLUMN_LASTNAME, lastname);

        return writeDB.update(TABLE_LOGIN, values, COLUMN_USERNAME+" = ?",
                new String[]{username}) > 0;

    }


    public boolean deleteUser(String username) {
        return writeDB.delete(TABLE_LOGIN,  COLUMN_USERNAME+" = ?",
                new String[]{username}) > 0;
    }

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

}