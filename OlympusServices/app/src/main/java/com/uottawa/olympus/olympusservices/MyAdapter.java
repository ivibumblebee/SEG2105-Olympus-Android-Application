package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ServicesHolder> {

    private Service[] services;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Service[] services) {
        this.services = services;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ServicesHolder vh = new ServicesHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ServicesHolder holder, int position) {
        Service service = services[position];
        holder.name.setText(service.getName());
        holder.rate.setText(""+service.getRate());



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.length;
    }

    class ServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView rate;

        public ServicesHolder(View row){
            super(row);
            name = row.findViewById(R.id.Name);
            rate = row.findViewById(R.id.Rate);
            row.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
        }


    }


}
