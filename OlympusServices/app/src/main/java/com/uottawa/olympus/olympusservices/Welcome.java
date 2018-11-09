package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

/**
 * Welcome class creates the welcome screen for the HomeOwners and ServiceProviders
 * as a temporary screen until full functionality of those two a classes are
 * implemented.
 *
 */
public class Welcome extends AppCompatActivity {

    /**
     * On creation of this object the app will display the xml file for
     * the welcome page which gets the role and username of the UserType
     * object and welcomes the user with a message.
     *
     * @param savedInstanceState Bundle to transfer data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        DBHelper dbHelper = new DBHelper(this);
        UserType user;
        user = dbHelper.findUserByUsername(username);
        TextView role = findViewById(R.id.Role);
        TextView name = findViewById(R.id.name);
        role.setText(user.getRole());
        name.setText(user.getFirstname());


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


}
