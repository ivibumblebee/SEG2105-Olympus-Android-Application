package com.uottawa.olympus.olympusservices;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class ServiceProviderAvailabilities extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_availabilities);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
    }


    public void onClick(View v) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            final Button button = (Button)v;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (minute==0){
                                button.setText(hourOfDay + ":00");
                            }
                            else {
                                button.setText(hourOfDay + ":" + minute);
                            }

                            //set availibility for service provider and check start time is less than finish
                        }

                    }, hour, minute, false);
            timePickerDialog.show();
    }

    public void onRemove(View view){
        //set time to Start/End, set availibility for start and end to null
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
