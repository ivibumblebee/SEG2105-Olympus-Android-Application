package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class AdminWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        DBHelper dbHelper = new DBHelper(this);
        List<String[]> users = dbHelper.getAllUsers();
        String[] usernames = new String[users.size()+1];
        String[] usertypes = new String[users.size()+1];
        usernames[0] = "Username";
        usertypes[0] = "User Type";
        Iterator iter = users.iterator();
        for (int i=0; i<users.size();i++){
            String[] current = (String[])iter.next();
            usernames[i+1] = current[0];
            usertypes[i+1] = current[3];
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);
        ListView listView = (ListView) findViewById(R.id.Users);
        listView.setAdapter(adapter);
        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usertypes);
        ListView listView2 = (ListView) findViewById(R.id.Types);
        listView2.setAdapter(adapter2);

    }
}
