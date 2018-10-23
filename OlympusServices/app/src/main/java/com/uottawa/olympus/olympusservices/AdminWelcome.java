package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
        String[] usernames = new String[(users.size()+1)*2];
        usernames[0] = "Username";
        usernames[1] = "User Type";
        Iterator iter = users.iterator();
        for (int i=0; i<users.size();i++){
            String[] current = (String[])iter.next();
            usernames[(i+1)*2] = current[0];
            usernames[(i+1)*2+1] = current[3];
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);
        GridView gridView = (GridView) findViewById(R.id.Users);
        gridView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed(){
    }

    public void LogOut(View view){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }
}
