package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        DBHelper dbHelper = new DBHelper(this);
        UserType user;
        user = dbHelper.findUserByUsername(username);
        TextView role = findViewById(R.id.Role);
        TextView name = findViewById(R.id.name);
        role.setText(user.getRole());
        name.setText(user.getFirstname());


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
