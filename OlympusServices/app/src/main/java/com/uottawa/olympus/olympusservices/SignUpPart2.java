package com.uottawa.olympus.olympusservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
        String description = ((EditText) findViewById(R.id.DescriptionInput)).getText().toString();

        if(companyname.length()>0 && address.length()>0 && phonenumber.length()>0
                && companyname.matches("^[a-zA-Z0-9_ ]*$") && address.matches("^[a-zA-Z0-9_ ]*$")
                && phonenumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$")
                && description.matches("^[a-zA-Z0-9_ ]*$")
                && companyname.replaceAll("\\s+","").length()>0
                && address.replaceAll("\\s+","").length()>0) {

            ServiceProvider serviceProvider = new ServiceProvider(username, password, firstname, lastname,
                    address, phonenumber, companyname, licensed, description);
            if(dbHelper.addUser(serviceProvider)){
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this,"Could not create account",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Fields cannot be empty and must be formatted correctly", Toast.LENGTH_LONG).show();
        }

    }
}
