package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

       /*
        Booking[] bookings;
        //get bookings here
        mRecyclerView = (RecyclerView) findViewById(R.id.Bookings);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdminServicesList.MyAdapter(booking, this);
        mRecyclerView.setAdapter(mAdapter);
        */
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
            //holder.name.setText(booking.getServiceprovider().getFirstname()+" "+booking.getServiceprovider().getLastname());



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookings.length;
        }

        class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView homeowner;
            TextView serviceprovider;
            TextView date;
            TextView start;
            TextView end;
            TextView status;

            public BookingHolder(View row){
                super(row);
                homeowner = row.findViewById(R.id.HomeOwnerName);
                serviceprovider = row.findViewById(R.id.ServiceProviderName);
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
                            alertDialogBuilder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            //confirm booking
                                            Toast.makeText(Bookings.this,"Booking is confirmed",Toast.LENGTH_LONG).show();
                                            Bookings.this.recreate();
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //delete booking
                            Toast.makeText(Bookings.this,"Booking is deleted",Toast.LENGTH_LONG).show();
                            Bookings.this.recreate();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Bookings.this);
                    alertDialogBuilder.setMessage("Are you sure you want to cancel your booking");
                    alertDialogBuilder.setPositiveButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //cancel booking
                                    Toast.makeText(Bookings.this,"Booking is cancelled",Toast.LENGTH_LONG).show();
                                    Bookings.this.recreate();
                                }
                            });

                    alertDialogBuilder.setNegativeButton("Nevermind",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }


            }


        }


    }
}
