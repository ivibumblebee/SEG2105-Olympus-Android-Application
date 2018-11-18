package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ServiceProviderBookings extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_bookings);
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
        Intent intent = new Intent(getApplicationContext(),ServiceProviderWelcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
