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
        DBHelper dbHelper = new DBHelper(this);
        ServiceProvider user = (ServiceProvider) dbHelper.findUserByUsername(username);
        //int[][] test = {{12,9,17,50},{0,0,0,0},{7,31,17,9},{12,9,17,50},{0,0,0,0},{7,31,17,9},{4,0,19,30}};
        //user.setAvailabilities(test);
        int[][] days  = user.getAvailabilities();
        String startTime;
        String endTime;
        int i = 0;
        for(int[] times: days){
            startTime = formatTime(times[0],times[1]);
            endTime = formatTime(times[2],times[3]);
            if(times[0]==0&&times[1]==0&&times[2]==0&&times[3]==0){
                startTime = "START";
                endTime = "END";
            }
            if(i==0){
                ((Button)findViewById(R.id.MondayStart)).setText(startTime);
                ((Button)findViewById(R.id.MondayEnd)).setText(endTime);
            }else if(i==1){
                ((Button)findViewById(R.id.TuesdayStart)).setText(startTime);
                ((Button)findViewById(R.id.TuesdayEnd)).setText(endTime);
            }else if(i==2){
                ((Button)findViewById(R.id.WednesdayStart)).setText(startTime);
                ((Button)findViewById(R.id.WednesdayEnd)).setText(endTime);
            }else if(i==3){
                ((Button)findViewById(R.id.ThursdayStart)).setText(startTime);
                ((Button)findViewById(R.id.ThursdayEnd)).setText(endTime);
            }else if(i==4){
                ((Button)findViewById(R.id.FridayStart)).setText(startTime);
                ((Button)findViewById(R.id.FridayEnd)).setText(endTime);
            }else if(i==5){
                ((Button)findViewById(R.id.SaturdayStart)).setText(startTime);
                ((Button)findViewById(R.id.SaturdayEnd)).setText(endTime);
            }else if(i==6){
                ((Button)findViewById(R.id.SundayStart)).setText(startTime);
                ((Button)findViewById(R.id.SundayEnd)).setText(endTime);
            }
            i++;
        }
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
                            String time = "";

                            button.setText(formatTime(hourOfDay,minute));
                            //set availibility for service provider and check start time is less than finish
                        }

                    }, hour, minute, false);
            timePickerDialog.show();
    }

    public void onRemove(View view){
        //set time to Start/End, set availibility for start and end to null
        Button start;
        Button end;
        if(view.getId()==R.id.DeleteMon){
            start = findViewById(R.id.MondayStart);
            end = findViewById(R.id.MondayEnd);
            start.setText("START");
            end.setText("END");
        }else if(view.getId()==R.id.DeleteTuesday){
            start = findViewById(R.id.TuesdayStart);
            end = findViewById(R.id.TuesdayEnd);
            start.setText("START");
            end.setText("END");
        }else if(view.getId()==R.id.DeleteWednesday){
            start = findViewById(R.id.WednesdayStart);
            end = findViewById(R.id.WednesdayEnd);
            start.setText("START");
            end.setText("END");
        }else if(view.getId()==R.id.DeleteThursday){
            start = findViewById(R.id.ThursdayStart);
            end = findViewById(R.id.ThursdayEnd);
            start.setText("START");
            end.setText("END");
        }else if(view.getId()==R.id.DeleteFriday){
            start = findViewById(R.id.FridayStart);
            end = findViewById(R.id.FridayEnd);
            start.setText("START");
            end.setText("END");
        }else if(view.getId()==R.id.DeleteSaturday){
            start = findViewById(R.id.SaturdayStart);
            end = findViewById(R.id.SaturdayEnd);
            start.setText("START");
            end.setText("END");
        }else{
            start = findViewById(R.id.SundayStart);
            end = findViewById(R.id.SundayEnd);
            start.setText("START");
            end.setText("END");
        }
    }

    public void onSetTimes(View view){
        String mondayStratTime = ((Button)findViewById(R.id.MondayStart)).getText().toString();
        String mondayEndTime = ((Button)findViewById(R.id.MondayEnd)).getText().toString();
        String tuesdayStratTime = ((Button)findViewById(R.id.TuesdayStart)).getText().toString();
        String tuesdayEndTime = ((Button)findViewById(R.id.TuesdayEnd)).getText().toString();
        String wednesdayStratTime = ((Button)findViewById(R.id.WednesdayStart)).getText().toString();
        String wednesdayEndTime = ((Button)findViewById(R.id.WednesdayEnd)).getText().toString();
        String thursdayStratTime = ((Button)findViewById(R.id.ThursdayStart)).getText().toString();
        String thursdayEndTime = ((Button)findViewById(R.id.ThursdayEnd)).getText().toString();
        String fridayStratTime = ((Button)findViewById(R.id.FridayStart)).getText().toString();
        String fridayEndTime = ((Button)findViewById(R.id.FridayEnd)).getText().toString();
        String saturdayStratTime = ((Button)findViewById(R.id.SaturdayStart)).getText().toString();
        String saturdayEndTime = ((Button)findViewById(R.id.SaturdayEnd)).getText().toString();
        String sundayStratTime = ((Button)findViewById(R.id.SundayStart)).getText().toString();
        String sundayEndTime = ((Button)findViewById(R.id.SundayEnd)).getText().toString();

        System.out.println(mondayEndTime);
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

    private String formatTime(int hours, int minutes){
        String time = "";
        if(hours<10){
            time = time+"0"+hours+":";
        }else{
            time = time+hours+":";
        }
        if (minutes<10){
            time = time+"0"+minutes;
        }
        else {
            time = time+minutes;
        }
        return time;
    }

    private int[] parseTime(String time){
        int[] apple = {1};
        return apple;
    }
}
