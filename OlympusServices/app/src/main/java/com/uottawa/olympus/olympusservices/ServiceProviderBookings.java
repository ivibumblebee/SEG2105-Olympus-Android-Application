package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ServiceProviderBookings extends AppCompatActivity {

    //field for RecyclerView
    private RecyclerView mRecyclerView;
    //field for adapter of Recycler view
    private RecyclerView.Adapter mAdapter;
    //field for layout manager of Recyler view.
    private RecyclerView.LayoutManager mLayoutManager;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_bookings);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

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
            holder.name.setText(booking.getServiceprovider());



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookings.length;
        }

        class BookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView name;
            TextView rate;

            public BookingHolder(View row){
                super(row);
                name = row.findViewById(R.id.Name);
                rate = row.findViewById(R.id.Rate);
                row.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                TextView nameview = (TextView)view.findViewById(R.id.Name);
                String name = nameview.getText().toString();

            }


        }


    }
}
