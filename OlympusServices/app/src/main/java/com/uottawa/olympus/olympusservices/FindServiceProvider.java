package com.uottawa.olympus.olympusservices;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class FindServiceProvider extends AppCompatActivity {
    String username;
    DBHelper dbHelper;

    //field for RecyclerView
    private RecyclerView mRecyclerView;
    //field for adapter of Recycler view
    private RecyclerView.Adapter mAdapter;
    //field for layout manager of Recyler view.
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service_provider);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");


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


        mRecyclerView = (RecyclerView) findViewById(R.id.ServiceProviders);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


         //

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void Search(View view){
        MaterialSpinner spinner = findViewById(R.id.ServicesInput);
        Button button = findViewById(R.id.Start);
        Button button2 = findViewById(R.id.End);
        Button button3 = findViewById(R.id.Date);
        RadioGroup ratingselect = findViewById(R.id.RatingSelect);
        int selectedId = ratingselect.getCheckedRadioButtonId();
        RadioButton ratingpicked;

        String service = spinner.getText().toString();
        String start;
        String end;
        String date;
        double rating;
        List<String[]> providers;
        if(!button.getText().toString().equals("Start") && !button2.getText().toString().equals("End")
                && !button3.getText().toString().equals("Date")){
            System.out.println(button.getText().toString()+":"+button2.getText().toString()+":"+button3.getText().toString());
            start = button.getText().toString();
            end = button2.getText().toString();
            date = button3.getText().toString();

            String[] dates = date.split("/");
            int month = Integer.parseInt(dates[0].replaceAll("\\s+",""));
            int day = Integer.parseInt(dates[1].replaceAll("\\s+",""));
            int year = Integer.parseInt(dates[2].replaceAll("\\s+",""));

            String[] starttimes = start.split(":");
            int starth = Integer.parseInt(starttimes[0].replaceAll("\\s+",""));
            int startmin = Integer.parseInt(starttimes[1].replaceAll("\\s+",""));

            String[] endtimes = end.split(":");
            int endh = Integer.parseInt(endtimes[0].replaceAll("\\s+",""));
            int endmin = Integer.parseInt(endtimes[1].replaceAll("\\s+",""));

            int[] times = {starth, startmin, endh, endmin};
            if(selectedId!=-1 && validateTime(times)){
                ratingpicked = (RadioButton) findViewById(selectedId);
                rating = Double.parseDouble(ratingpicked.getText().toString());
                providers = dbHelper.getProvidersByTimeAndRating(service, rating, year, month, day,
                        starth, startmin, endh, endmin);
            }
            else if(validateTime(times)){
                providers = dbHelper.getProvidersByTime(service, year, month, day,
                        starth, startmin, endh, endmin);
            }
            else if(selectedId!=-1){
                Toast.makeText(this, "Times are invalid, only searching by service and rating"+starth+";"+startmin+","+endh+";"+endmin, Toast.LENGTH_SHORT).show();
                ratingpicked = (RadioButton) findViewById(selectedId);
                rating = Double.parseDouble(ratingpicked.getText().toString());
                providers = dbHelper.getProvidersAboveRating(service, rating);

            }
            else{
                Toast.makeText(this, "Times are invalid, only searching by service", Toast.LENGTH_SHORT).show();
                providers = dbHelper.getAllProvidersByService(service);

            }
        }
        else{
            if(selectedId!=-1){
                ratingpicked = (RadioButton) findViewById(selectedId);
                rating = Double.parseDouble(ratingpicked.getText().toString());
                providers = dbHelper.getProvidersAboveRating(service, rating);
            }
            else{
                providers = dbHelper.getAllProvidersByService(service);
            }
        }

        //update recycler view
        String[][] providerslist = new String[providers.size()][];
        for(int i=0; i<providers.size(); i++){
            providerslist[i] = providers.get(i);
        }

        mAdapter = new MyAdapter(providerslist, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }

    public void makeBooking(String serviceprovider){
        MaterialSpinner spinner = findViewById(R.id.ServicesInput);
        String service = spinner.getText().toString();
        Intent intent = new Intent(getApplicationContext(),MakeBooking.class);
        intent.putExtra("homeowner", username);
        intent.putExtra("serviceprovider", serviceprovider);
        intent.putExtra("service", service);
        startActivity(intent);
        finish();



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

                        button.setTextSize(9);
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


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ProviderHolder> {

        private String[][] serviceProviders;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[][] serviceProviders, Context context) {
            this.serviceProviders = serviceProviders;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.service_list_item, parent, false);
            ProviderHolder vh = new ProviderHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ProviderHolder holder, int position) {
            String[] serviceprovider = serviceProviders[position];
            holder.name.setText(serviceprovider[1]+" "+serviceprovider[2]);
            holder.rate.setText(""+serviceprovider[3]);




        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return serviceProviders.length;
        }

        class ProviderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView name;
            TextView rate;

            public ProviderHolder(View row){
                super(row);
                name = row.findViewById(R.id.Name);
                rate = row.findViewById(R.id.Rate);
                row.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                TextView nameview = (TextView)view.findViewById(R.id.Name);
                String name = nameview.getText().toString();
                makeBooking(name);

            }


        }


    }
}
