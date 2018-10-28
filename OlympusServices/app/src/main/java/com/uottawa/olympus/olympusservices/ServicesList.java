package com.uottawa.olympus.olympusservices;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class ServicesList extends AppCompatActivity implements NewServiceDialogFragment.NoticeDialogListener, EditServiceDialogFragment.NoticeDialogListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        DBHelper dbHelper = new DBHelper(this);
        List<String[]> users = dbHelper.getAllServices();
        String[] services = new String[(users.size()+1)*2];
        services[0] = "Name";
        services[1] = "Rate";
        Iterator iter = users.iterator();
        for (int i=0; i<users.size();i++){
            String[] current = (String[])iter.next();
            services[(i+1)*2] = current[0];
            services[(i+1)*2+1] = current[1];
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
        GridView gridView = findViewById(R.id.Services);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editService(view);
                }
            });
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