package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeOwnerBookings extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_bookings);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
    }

    /**
     * Override so that previous screen refreshes when pressing the
     * back button on this activity of the app.
     *
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
