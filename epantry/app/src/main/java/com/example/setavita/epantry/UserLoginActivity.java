/**
 * TODO: save new <User> signup in db, method to check login matches existing, method that blank fields make button disabled
 * test method for creating new login and making sure method returns match
 * test method for successful login to new activity?
 * test method for any blank fields that button is disabled
 */

package com.example.setavita.epantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.setavita.database.DatabaseHandler;

import com.example.setavita.database.UserTable;
import com.example.setavita.models.User;

public class UserLoginActivity extends AppCompatActivity {

    EditText TFloginUsername;
    EditText TFloginPassword;

    DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Log.i("UserLoginActivity", "Hello");

//        dbHandler.addHandle(new User("username", "password", "email"));
    }

    public void onClick(View v) {
        if(v.getId() == R.id.Bsignup) {
            Intent i = new Intent(UserLoginActivity.this, SignupFormActivity.class);
            startActivity(i);
        }
        if(v.getId() == R.id.Blogin) {
            EditText a = (EditText) findViewById(R.id.TFloginUsername);
            String strUser = a.getText().toString();
            EditText b = (EditText) findViewById(R.id.TFloginPassword);
            String strPass = b.getText().toString();
            UserTable userDB = new UserTable();

            User currentUser = userDB.checkLogin(strUser, strPass, dbHandler);


            if(currentUser != null) {
                Intent i = new Intent(UserLoginActivity.this, LandingPageActivity.class);
                startActivity(i);
                i.putExtra("USERID", currentUser.getUserID());
            } else {
                Toast noMatch = Toast.makeText(UserLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT);
                noMatch.show();
            }
        }
        if(v.getId() == R.id.BtoSignup) {
            Intent i = new Intent(UserLoginActivity.this, SignupFormActivity.class);
            startActivity(i);
        }
    }
}
