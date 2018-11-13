package com.uottawa.olympus.olympusservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jaredrummler.materialspinner.MaterialSpinner;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

/**
 * This class is used to create new userType where the user
 * fills in his information and his data is sent to to the database
 * to create a new userType object.
 *
 */

public class SignUp extends AppCompatActivity {

    /**
     * On creation of this class the xml page is loaded up onto the app and
     * the EditTexts are set up to accept the information of the user.
     *
     * @param savedInstanceState Bundle for transferring information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        MaterialSpinner spinner = findViewById(R.id.RoleInput);
        spinner.setItems("Home Owner", "Service Provider");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    /**
     * On click creates a new user if the the user information
     * fits the criteria specified by the documentation and is
     * not a duplicate of another user.
     *
     * @param view View object that contains all the editText and buttons used.
     */
    public void onClickSignUp(View view){
        UserType newUser;
        String username = ((EditText) findViewById(R.id.UsernameInput)).getText().toString();
        String password = ((EditText) findViewById(R.id.PasswordInput)).getText().toString();
        String firstname = ((EditText) findViewById(R.id.FirstNameInput)).getText().toString();
        String lastname = ((EditText) findViewById(R.id.LastNameInput)).getText().toString();
        MaterialSpinner spinner = findViewById(R.id.RoleInput);

        if(username.length()>=5 && password.length()>5 && firstname.length()>0 && lastname.length()>0 && username.matches("[a-zA-Z0-9]*") && password.matches("[a-zA-Z0-9]*")
                && firstname.matches("[a-zA-Z]*") && lastname.matches("[a-zA-Z]*")){
            DBHelper dbHelper = new DBHelper(this);
            Intent intent = new Intent(getApplicationContext(),LogIn.class);
            switch(spinner.getText().toString()){
                case "Home Owner":
                    newUser = new HomeOwner(username,password,firstname,lastname);
                    if(dbHelper.addUser(newUser)){
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this,"Username is taken",Toast.LENGTH_LONG).show();
                    }
                    break;
                case "Service Provider":
                    if(dbHelper.findUserByUsername(username)==null) {
                        Intent intent2 = new Intent(getApplicationContext(), SignUpPart2.class);
                        intent2.putExtra("firstname", firstname);
                        intent2.putExtra("lastname", lastname);
                        intent2.putExtra("username", username);
                        intent2.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this,"Username is taken",Toast.LENGTH_LONG).show();
                    }
                    break;

                default:
                    newUser = new HomeOwner(username,password,firstname,lastname); //if nothing is enter then defaults to user role.

                    if(dbHelper.addUser(newUser)){
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this,"Username is taken",Toast.LENGTH_LONG).show();
                    }
                    break;

            }

        }
        else if(firstname.length()==0 || lastname.length()==0 || username.length()==0 || password.length()==0){
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (username.length()<5 || password.length()<5 ){
            Toast.makeText(this, "Password and username must be longer than 4 characters", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Fields may only contain alphanumeric values", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * app closes the the activity when back is pressed and
     * no user information written down is saved in the EditText for
     * security purposes.
     */

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }



}
