package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Iterator;
import java.util.List;

/**
 * Creates the view and dialog listener for List of services
 * which the admin can view and manipulate.
 *
 */
public class ServiceProviderServicesList extends AppCompatActivity implements DeleteServiceDialogFragment.NoticeDialogListener{

    //field for RecyclerView
    private RecyclerView mRecyclerView;
    //field for adapter of Recycler view
    private RecyclerView.Adapter mAdapter;
    //field for layout manager of Recyler view.
    private RecyclerView.LayoutManager mLayoutManager;

    private String username;

    /**
     * On creation loads up the xml, and generates the services list,
     * and fillsout the recylerView fields.
     *
     * @param savedInstanceState Bundle to transfer information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_services_list);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        DBHelper dbHelper = new DBHelper(this);

        //grid
        List<String[]> serviceslist2 = dbHelper.getAllServicesProvidedByUser(username);
        Service[] services2 = new Service[(serviceslist2.size())];
        Iterator iter2 = serviceslist2.iterator();
        for (int i=0; i<serviceslist2.size();i++){
            String[] current = (String[])iter2.next();
            services2[i] = new Service(current[0], Double.parseDouble(current[1]));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.Services);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(services2, this);
        mRecyclerView.setAdapter(mAdapter);

        //spinner
        MaterialSpinner spinner = findViewById(R.id.ServicesInput);

        List<String[]> serviceslist = dbHelper.getAllServices();
        String[] services = new String[(serviceslist.size())];
        Iterator iter = serviceslist.iterator();
        for (int i=0; i<serviceslist.size();i++){
            String[] current = (String[])iter.next();
            services[i] = current[0];
        }
        ArrayAdapter<String> servicesadapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, services);

        servicesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(servicesadapter);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

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

    /**
     * Deletes services from the list.
     *
     * @param view View object contains the generated list and buttons
     */
    public void deleteService(View view, String name) {
        DialogFragment newFragment = new DeleteServiceDialogFragment();
        newFragment.show(getSupportFragmentManager(), "addService");
        Bundle args = new Bundle();
        args.putString("name", name);
        newFragment.setArguments(args);
    }

    /**
     * Adds service to the list.
     *
     * @param view View object contains the generated list and buttons
     */
    public void addService(View view) {
        MaterialSpinner spinner = findViewById(R.id.ServicesInput);
        String servicename = spinner.getText().toString();
        DBHelper dbHelper = new DBHelper(this);
        if(dbHelper.addServiceProvidedByUser(username, servicename)){
            this.recreate();
        }
        else{
            Toast.makeText(this, "Could not add service", Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * Uses Dialog to delete a service from the serviceList.
     *
     * @param dialog DialogFragment that contains the delete service button.
     */

    @Override
    public void onDialogDelete(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        dbHelper.deleteServiceProvidedByUser(username, name);
        dialog.dismiss();
        this.recreate();
    }

    /**
     * Does nothing
     *
     * @param dialog DialogFragment that contains the cancel button.
     */
    @Override
    public void onDialogNevermind(DialogFragment dialog) {

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ServicesHolder> {

        private Service[] services;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Service[] services, Context context) {
            this.services = services;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.service_list_item, parent, false);
            ServicesHolder vh = new ServicesHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ServicesHolder holder, int position) {
            Service service = services[position];
            holder.name.setText(service.getName());
            holder.rate.setText(String.format("$%,.2f", service.getRate()));



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
                TextView nameview = (TextView)view.findViewById(R.id.Name);
                String name = nameview.getText().toString();
                deleteService(view, name);

            }


        }


    }

}