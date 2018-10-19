package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserType admin = new Admin();
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addUser(admin);


    }

    public void onClickSignUp(View view){
        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        startActivityForResult(intent,0);

    }

    public void onClickLogIn(View view){
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        startActivityForResult(intent,0);
    }

}