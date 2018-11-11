package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {
    String username;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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
        Intent intent = new Intent(getApplicationContext(),ServiceProviderWelcome.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
    public void Save(View view){
        TextView firstname = findViewById(R.id.FirstNameInput);
        TextView lastname = findViewById(R.id.LastNameInput);
        TextView password = findViewById(R.id.PasswordInput);
        dbHelper.updateUserInfo(username, password.getText().toString(), firstname.getText().toString(), lastname.getText().toString());
        Toast.makeText(this, "Profile has been updated", Toast.LENGTH_LONG).show();
    }


}
