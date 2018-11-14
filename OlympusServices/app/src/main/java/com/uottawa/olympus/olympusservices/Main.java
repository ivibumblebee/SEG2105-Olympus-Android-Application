package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

/**
 * The starting page of the app which contains two buttons
 * of either registering for the app or logging into an existing
 * account.
 *
 */

public class Main extends AppCompatActivity {

    /**
     * On creation of the object the app loads up the xml page
     * for the class and creates dbHelper object and Admin object
     * and then add the admin into the database.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserType admin = new Admin();
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addUser(admin);


    }

    /**
     * On click of the sign up button loads up the sign up activity
     *
     * @param view View object containing the buttons.
     */
    public void onClickSignUp(View view){
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
        finish();

    }

    /**
     * On click of the Login button loads up the login activity.
     *
     * @param view View object containing the buttons.
     */
    public void onClickLogIn(View view){
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        startActivity(intent);
        finish();
    }

}