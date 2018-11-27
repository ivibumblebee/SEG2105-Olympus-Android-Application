package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class HomeOwnerEditProfile extends AppCompatActivity {
    String username;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner_edit_profile);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        dbHelper = new DBHelper(this);
        UserType user;
        user = dbHelper.findUserByUsername(username);
        TextView firstname = findViewById(R.id.FirstNameInput);
        TextView lastname = findViewById(R.id.LastNameInput);
        TextView password = findViewById(R.id.PasswordInput);


        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());
        password.setText(user.getPassword());

    }
    /**
     * Override so that previous screen refreshes when pressing the
     * back button on this activity of the app.
     *
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Welcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }


    /**
     * Saves updated user information to the database
     * @param view
     */
    public void Save(View view){
        TextView firstname = findViewById(R.id.FirstNameInput);
        TextView lastname = findViewById(R.id.LastNameInput);
        TextView password = findViewById(R.id.PasswordInput);

        //Checks for the fields
        if(password.getText().toString().length()>=5 && firstname.getText().toString().length()>0
                && lastname.getText().toString().length()>0
                && password.getText().toString().matches("[a-zA-Z0-9]*")
                && firstname.getText().toString().matches("[a-zA-Z]*")
                && lastname.getText().toString().matches("[a-zA-Z]*")
                ) {

            if(dbHelper.updateUserInfo(username, password.getText().toString(), firstname.getText().toString(), lastname.getText().toString()
                    )){
                //add comment method here
                Toast.makeText(this, "Profile has been updated", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Could not update profile ", Toast.LENGTH_LONG).show();

            }

        }
        else{
            Toast.makeText(this, "Fields cannot be empty and must be formatted correctly", Toast.LENGTH_LONG).show();
        }
    }

}
