package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The Admin Welcome class is the welcome
 * screen for admin users when they have logged into the
 * app. The admin welcome screen has features such as the
 * user list and service list which it can only access.
 *
 */

public class AdminWelcome extends AppCompatActivity {

    /**
     * Creates the xml pages for the class object
     * on creation of the object.
     *
     * @param savedInstanceState Bundle for transfer of data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

    }

    /**
     * Override so that nothing occurs when pressing the
     * back button on this activity of the app.
     *
     */
    @Override
    public void onBackPressed(){
    }

    /**
     * Logs out the user and returns them back to
     * main activity. End all current user activity
     * for security purposes.
     *
     * @param view View object of current activity.
     */
    public void LogOut(View view){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }

    /**
     * On click of list of user button that goes to
     * UserList screen for the admin to edit the user list
     * of the app.
     *
     * @param view View object of current activity
     */
    public void goToUsers(View view){
        Intent intent = new Intent(getApplicationContext(), UsersList.class);
        startActivity(intent);
    }

    /**
     * On click of list of services button goes to
     * ServiceList screen for the admin to edit the
     * service list of the app.
     *
     * @param view
     */
    public void goToServices(View view){
        Intent intent = new Intent(getApplicationContext(), AdminServicesList.class);
        startActivity(intent);
    }
}
