package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Bookings extends AppCompatActivity {

    //field for RecyclerView
    private RecyclerView mRecyclerView;
    //field for adapter of Recycler view
    private RecyclerView.Adapter mAdapter;
    //field for layout manager of Recyler view.
    private RecyclerView.LayoutManager mLayoutManager;

    String username;
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        dbhelper = new DBHelper(this);

        List<Booking> booking = (List<Booking>)dbhelper.findBookings(username);
        Booking[] bookings = new Booking[booking.size()];
        bookings = booking.toArray(bookings);

        /* mock data
        Booking[] bookings = {new Booking(5, 5, 6, 6, 2, 3, 2019, (ServiceProvider)dbhelper.findUserByUsername("testing"),
                (HomeOwner)dbhelper.findUserByUsername("tester"), dbhelper.findService("service1"))};
        */
        mRecyclerView = (RecyclerView) findViewById(R.id.Bookings);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(bookings, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Override so that previous screen refreshes when pressing the
     * back button on this activity of the app.
     *
     */
    @Override
    public void onBackPressed(){
        Intent intent;
        if(dbhelper.findUserByUsername(username).getRole().equals("ServiceProvider")){
            intent = new Intent(getApplicationContext(),ServiceProviderWelcome.class);
        }
        else{
            intent = new Intent(getApplicationContext(),Welcome.class);
        }

        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BookingHolder> {

        private Booking[] bookings;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Booking[] bookings, Context context) {
            this.bookings = bookings;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_list_item, parent, false);
            BookingHolder vh = new BookingHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(BookingHolder holder, int position) {
            Booking booking = bookings[position];
            holder.serviceprovider.setText(booking.getServiceprovider().getFirstname()+" "+booking.getServiceprovider().getLastname()+", "+booking.getServiceprovider().getCompanyname());
            holder.homeowner.setText(booking.getHomeowner().getFirstname()+" "+booking.getHomeowner().getLastname());
            holder.service.setText(booking.getService().getName());
            String day;
            String month;
            String year = booking.getYear()+"";
            if(booking.getDay()<10){
                day = "0"+booking.getDay();
            }
            else{
                day = booking.getDay()+"";
            }
            if(booking.getMonth()<10){
                month = "0"+booking.getMonth();
            }
            else{
                month = booking.getMonth()+"";
            }

            holder.date.setText(month+"/"+day+"/"+year);
            holder.start.setText(formatTime(booking.getStarth(), booking.getStartmin()));
            holder.end.setText(formatTime(booking.getEndh(), booking.getEndmin()));
            holder.status.setText(booking.getStatus().toString());


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookings.length;
        }

        class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView homeowner;
            TextView serviceprovider;
            TextView service;
            TextView date;
            TextView start;
            TextView end;
            TextView status;
            int starth;
            int startmin;
            int endh;
            int endmin;
            int month;
            int day;
            int year;

            public BookingHolder(View row){
                super(row);
                homeowner = row.findViewById(R.id.HomeOwnerName);
                serviceprovider = row.findViewById(R.id.ServiceProviderName);
                service = row.findViewById(R.id.ServiceName);
                date = row.findViewById(R.id.DateName);
                start = row.findViewById(R.id.StartTime);
                end = row.findViewById(R.id.EndTime);
                status = row.findViewById(R.id.StatusName);

                row.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                if(dbhelper.findUserByUsername(username).getRole()=="ServiceProvider"){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Bookings.this);
                    alertDialogBuilder.setMessage("Cancel or Confirm your booking");
                            alertDialogBuilder.setPositiveButton("Confirm Booking",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            if(dbhelper.confirmBooking(new Booking(starth, startmin, endh, endmin, day, month,
                                                    year, (ServiceProvider)dbhelper.findUserByUsername(serviceprovider.getText().toString()),
                                                    (HomeOwner)dbhelper.findUserByUsername(homeowner.getText().toString()),
                                                    dbhelper.findService(service.getText().toString())))){

                                                Toast.makeText(Bookings.this,"Booking is confirmed",Toast.LENGTH_LONG).show();
                                                Bookings.this.recreate();
                                            }
                                            else{
                                                Toast.makeText(Bookings.this,"Booking could not be confirmed",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("Cancel Booking",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(dbhelper.cancelBooking(new Booking(starth, startmin, endh, endmin, day, month,
                                    year, (ServiceProvider)dbhelper.findUserByUsername(serviceprovider.getText().toString()),
                                    (HomeOwner)dbhelper.findUserByUsername(homeowner.getText().toString()),
                                    dbhelper.findService(service.getText().toString())))){
                                Toast.makeText(Bookings.this,"Booking is cancelled",Toast.LENGTH_LONG).show();
                                Bookings.this.recreate();
                            }
                            else{
                                Toast.makeText(Bookings.this,"Booking could not be cancelled",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Bookings.this);
                    alertDialogBuilder.setView(R.layout.rating_item);
                    alertDialogBuilder.setPositiveButton("Rate Booking",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    RadioGroup ratingselect = ((AlertDialog) arg0).findViewById(R.id.RatingSelect);
                                    int selectedId = ratingselect.getCheckedRadioButtonId();
                                    RadioButton ratingpicked;
                                    EditText comment = ((AlertDialog) arg0).findViewById(R.id.Comment);
                                    if(selectedId!=-1 && !comment.getText().toString().equals("")){
                                        ratingpicked = (RadioButton)((AlertDialog) arg0).findViewById(selectedId);
                                        double rating = Double.parseDouble(ratingpicked.getText().toString());
                                        /*Booking booking = new Booking(starth, startmin, endh, endmin, day, month,
                                                year, (ServiceProvider)dbhelper.findUserByUsername(serviceprovider.getText().toString()),
                                                (HomeOwner)dbhelper.findUserByUsername(homeowner.getText().toString()),
                                                dbhelper.findService(service.getText().toString()));
                                                */
                                        Booking booking = new Booking(starth, startmin, endh, endmin, day, month, year, new ServiceProvider(serviceprovider.getText().toString(),
                                        "", "", "", "", "", "", true),
                                                new HomeOwner(homeowner.getText().toString(), "", "", ""),
                                                new Service(service.getText().toString(), 5));
                                        if(!dbhelper.addRating(booking, rating, comment.getText().toString())){
                                            Toast.makeText(Bookings.this, "Rating could not be added", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Bookings.this.recreate();
                                        }

                                    }
                                    else{
                                        Toast.makeText(Bookings.this, "Rating and comment must be filled in", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                    alertDialogBuilder.setNegativeButton("Cancel Booking",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(dbhelper.cancelBooking(new Booking(starth, startmin, endh, endmin, day, month,
                                            year, (ServiceProvider)dbhelper.findUserByUsername(serviceprovider.getText().toString()),
                                            (HomeOwner)dbhelper.findUserByUsername(homeowner.getText().toString()),
                                            dbhelper.findService(service.getText().toString())))){
                                        Toast.makeText(Bookings.this,"Booking is cancelled",Toast.LENGTH_LONG).show();
                                        Bookings.this.recreate();
                                    }
                                    else{
                                        Toast.makeText(Bookings.this,"Booking could not be cancelled",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }


            }


        }


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
}
