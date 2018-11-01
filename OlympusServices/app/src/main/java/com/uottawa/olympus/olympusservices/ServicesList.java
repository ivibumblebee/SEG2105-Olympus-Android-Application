package com.uottawa.olympus.olympusservices;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class ServicesList extends AppCompatActivity implements NewServiceDialogFragment.NoticeDialogListener, EditServiceDialogFragment.NoticeDialogListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        DBHelper dbHelper = new DBHelper(this);
        List<String[]> serviceslist = dbHelper.getAllServices();
        Service[] services = new Service[(serviceslist.size())];
        Iterator iter = serviceslist.iterator();
        for (int i=0; i<serviceslist.size();i++){
            String[] current = (String[])iter.next();
            services[i] = new Service(current[0], Double.parseDouble(current[1]));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.Services);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(services);
        mRecyclerView.setAdapter(mAdapter);



    }
    public void addService(View view) {
        DialogFragment newFragment = new NewServiceDialogFragment();
        newFragment.show(getSupportFragmentManager(), "addService");
    }
    public void editService(View view) {
        DialogFragment newFragment = new EditServiceDialogFragment();
        newFragment.show(getSupportFragmentManager(), "editService");
    }
    //add new service
    @Override
    public void onDialogNew(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        Double rate = (Double)dialog.getArguments().get("rate");
        dbHelper.addService(new Service(name,rate));
        dialog.dismiss();
        this.recreate();
    }
    //user clicked cancel
    @Override
    public void onDialogNevermind(DialogFragment dialog) {

    }
    //edits service with info from dialog
    @Override
    public void onDialogEdit(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        Double rate = (Double)dialog.getArguments().get("rate");
        dbHelper.updateService(new Service(name,rate));
        dialog.dismiss();
        this.recreate();
    }
    //deletes service with info from dialog
    @Override
    public void onDialogDelete(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        dbHelper.deleteService(name);
        dialog.dismiss();
        this.recreate();
    }

}