package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void onClickLogIn(View view){
        String username = ((EditText) findViewById(R.id.UsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordInput)).getText().toString();
        DBHelper dbHelper = new DBHelper(this);
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        if(dbHelper.findUserByUsername(username)!=null) {
            if (dbHelper.findUserByUsername(username).getUsername().equals(username) &&
                    dbHelper.findUserByUsername(username).getPassword().equals(password)) {
                intent.putExtra("username", username);
                startActivity(intent);


            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Account does not exist", Toast.LENGTH_LONG).show();
        }



    }




}
