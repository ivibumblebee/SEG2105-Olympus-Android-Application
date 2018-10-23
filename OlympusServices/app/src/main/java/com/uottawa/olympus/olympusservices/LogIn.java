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
        if(username.matches("[a-zA-Z0-9]*")&&password.matches("[a-zA-Z0-9]*")
                && password.length()>0 && username.length()>0) {
            if (dbHelper.findUserByUsername(username) != null) {
                UserType user = dbHelper.findUserByUsername(username);
                if (user.getUsername().equals(username) &&
                        user.getPassword().equals(password)) {
                    if(user.getRole()=="Admin"){
                        Intent intent = new Intent(getApplicationContext(),AdminWelcome.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(),Welcome.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Account does not exist", Toast.LENGTH_LONG).show();
            }
        }
        else if(username.length()==0 || password.length()==0){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Fields may only contain alphanumeric values", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }




}
