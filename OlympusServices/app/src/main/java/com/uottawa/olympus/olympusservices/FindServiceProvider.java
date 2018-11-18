package com.uottawa.olympus.olympusservices;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class FindServiceProvider extends AppCompatActivity {
    String username;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service_provider);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        MaterialSpinner spinner = findViewById(R.id.RatingInput);
        spinner.setItems(1, 2, 3, 4, 5);

        dbHelper = new DBHelper(this);
        MaterialSpinner spinner2 = findViewById(R.id.ServicesInput);

        List<String[]> serviceslist = dbHelper.getAllServices();
        String[] services = new String[(serviceslist.size())];
        Iterator iter = serviceslist.iterator();
        for (int i=0; i<serviceslist.size();i++){
            String[] current = (String[])iter.next();
            services[i] = current[0];
        }
        spinner2.setItems(services);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void Search(View view){

    }

    public void onClickDate(View view){

        final Button button = (Button)view;
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, day);
                        button.setText(month + " / " + (day) + " / "
                                + year);
                    }

                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    public void onClickTime(View view){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        final Button button = (Button)view;

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

    private int[] parseTime(String startTime, String endTime){
        int[] times = new int[4];
        if(startTime.equals("START")){
            times[0]=0;
            times[1]=0;
        }else{
            times[0] = Integer.parseInt(startTime.substring(0,2));
            times[1] = Integer.parseInt(startTime.substring(3));
        }
        if(endTime.equals("END")){
            times[2]=0;
            times[3]=0;
        }else{
            times[2] = Integer.parseInt(endTime.substring(0,2));
            times[3] = Integer.parseInt(endTime.substring(3));
        }
        return times;
    }

    private boolean validateTime(int[] time){
        if(time[0]==0&&time[1]==0&&time[2]==0&&time[3]==0){
            return true;
        }
        if(time[2]>time[0]){
            return true;
        }else{
            if(time[2]==time[0]&&time[3]>time[1]){
                return true;
            }else{
                return false;
            }
        }
    }
}
