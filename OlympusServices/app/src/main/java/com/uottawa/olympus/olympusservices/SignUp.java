package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jaredrummler.materialspinner.MaterialSpinner;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        MaterialSpinner spinner = findViewById(R.id.RoleInput);
        spinner.setItems("User", "Service Provider");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void onClickSignUp(View view){
        UserType newUser;
        String username = ((EditText) findViewById(R.id.UsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordInput)).getText().toString();
        String firstname = ((EditText) findViewById(R.id.FirstNameInput)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.LastNameInput)).getText().toString();
        MaterialSpinner spinner = findViewById(R.id.RoleInput);
        //TODO add message conditional to check if every EditText is filled up to standards
        if(username.length()>=5 && password.length()>5 && firstname.length()>0 && lastname.length()>0 && username.matches("[a-zA-Z0-9]*") && password.matches("[a-zA-Z0-9]*")
                && firstname.matches("[a-zA-Z0-9]*") && lastname.matches("[a-zA-Z0-9]*")){
            switch(spinner.getText().toString()){
                case "User":
                    newUser = new User(username,password,firstname,lastname);
                    break;
                case "Service Provider":
                    newUser = new ServiceProvider(username,password,firstname,lastname);
                    break;
                default:
                    newUser = new User(username,password,firstname,lastname); //if nothing is enter then defaults to user role.
                    break;

            }

            DBHelper dbHelper = new DBHelper(this);
            Intent intent = new Intent(getApplicationContext(),LogIn.class); //TODO check if signup should take to the login page or automatically login
            if(dbHelper.addUser(newUser)){
                startActivityForResult(intent,0);
            }else{
                Toast.makeText(this,"Username is taken",Toast.LENGTH_LONG).show();
            }
        }
        else if(firstname.length()==0 || lastname.length()==0 || username.length()==0 || password.length()==0){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (username.length()<=5 || password.length()<=5 ){
            Toast.makeText(this, "Password and username must be longer than 5 characters", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Fields may only contain alphanumeric values", Toast.LENGTH_LONG).show();
        }


    }



}
