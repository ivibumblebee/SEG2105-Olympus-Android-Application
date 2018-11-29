package com.uottawa.olympus.olympusservices;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MakeBooking extends AppCompatActivity {
    String homeowner;
    String serviceprovider;
    String service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);
        Bundle bundle = getIntent().getExtras();
        homeowner = bundle.getString("homeowner");
        serviceprovider = bundle.getString("serviceprovider");
        service = bundle.getString("service");

        TextView homeo = findViewById(R.id.HomeOwner);
        TextView servicep = findViewById(R.id.ServiceProvider);
        TextView serv = findViewById(R.id.Service);

        homeo.setText("Home Owner: \n"+homeowner);
        servicep.setText("ServiceProvider: \n"+serviceprovider);
        serv.setText("Service: \n"+service);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        intent.putExtra("username", homeowner);
        startActivity(intent);
        finish();
    }
    public void Cancel(View view){
        onBackPressed();
    }

    public void Book(View view){
        Button button = findViewById(R.id.Start);
        Button button2 = findViewById(R.id.End);
        Button button3 = findViewById(R.id.Date);

        if(!button.getText().toString().equals("Start") && !button2.getText().toString().equals("End")
                && !button3.getText().toString().equals("Date")){
            String[] dates = button3.getText().toString().split("/");
            int month = Integer.parseInt(dates[0].replaceAll("\\s+",""));
            int day = Integer.parseInt(dates[1].replaceAll("\\s+",""));
            int year = Integer.parseInt(dates[2].replaceAll("\\s+",""));

            String[] starttimes = button.getText().toString().split(":");
            int starth = Integer.parseInt(starttimes[0].replaceAll("\\s+",""));
            int startmin = Integer.parseInt(starttimes[1].replaceAll("\\s+",""));

            String[] endtimes = button2.getText().toString().split(":");
            int endh = Integer.parseInt(endtimes[0].replaceAll("\\s+",""));
            int endmin = Integer.parseInt(endtimes[1].replaceAll("\\s+",""));

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date selecteddate = sdf.parse(dates[2].replaceAll("\\s+","") + "-" + dates[0].replaceAll("\\s+","") + "-" + dates[1].replaceAll("\\s+",""));
                if(selecteddate.compareTo(date)<0){
                    Toast.makeText(this, "Date must be a future date", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (starth<endh || (starth==endh && startmin<endmin)){
                        DBHelper dbHelper = new DBHelper(this);

                        //check if sp is availible not just true
                        if(true) {

                            if (dbHelper.addBooking(serviceprovider, homeowner, service, year, month, day,
                                    starth, startmin, endh, endmin)) {
                                Toast.makeText(this, "Booking Made", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                                intent.putExtra("username", homeowner);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(this, "Booking could not be made", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                    else{
                        Toast.makeText(this, "Time is invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(Exception e){
                Toast.makeText(this, "Date is invalid", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this, "Date and times must be selected", Toast.LENGTH_SHORT).show();
        }
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
                        month++;
                        String daystring;
                        String monthstring;
                        if((""+day).length()==1){
                            daystring = "0"+day;
                        }
                        else{
                            daystring = day+"";
                        }
                        if((""+month).length()==1){
                            monthstring = "0"+month;
                        }
                        else{
                            monthstring = ""+month;
                        }
                        button.setText(monthstring + " / " + daystring + " / "
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
