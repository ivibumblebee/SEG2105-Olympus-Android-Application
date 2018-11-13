package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SignUpPart2 extends AppCompatActivity {
    private String username;
    private String password;
    private String firstname;
    private String lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_part2);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");
    }

    public void SignUp(View view){
        DBHelper dbHelper = new DBHelper(this);
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        String companyname = ((EditText) findViewById(R.id.CompanyNameInput)).getText().toString();
        String phonenumber = ((EditText) findViewById(R.id.PhoneNumberInput)).getText().toString();
        String address = ((EditText) findViewById(R.id.AddressInput)).getText().toString();
        boolean licensed = ((CheckBox) findViewById(R.id.LicensedInput)).isChecked();

        ServiceProvider serviceProvider = new ServiceProvider(username, password, firstname, lastname,
                address, phonenumber, companyname, licensed);

    }
}
