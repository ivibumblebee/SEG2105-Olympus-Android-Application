package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



public class AdminWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

    }
    @Override
    public void onBackPressed(){
    }

    public void LogOut(View view){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }
    public void goToUsers(View view){
        Intent intent = new Intent(getApplicationContext(), UsersList.class);
        startActivity(intent);
    }
    public void goToServices(View view){
        Intent intent = new Intent(getApplicationContext(), ServicesList.class);
        startActivity(intent);
    }
}
