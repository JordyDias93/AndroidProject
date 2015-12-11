package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import java.text.ParseException;

/**
 * Created by Dr.Dias on 10/12/2015.
 */
public class AcceuilActivity  extends Activity {
    private static final String TAG = "Acceuil_Activity";
    Button buttonlogin;
    Button buttonsignup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        Log.v(TAG, "Welcome here ! ");
        buttonlogin = (Button)findViewById(R.id.buttonlogin);
        buttonsignup= (Button)findViewById(R.id.buttonsignup);
        final EditText login = (EditText) findViewById(R.id.usernameG);
        final EditText pass = (EditText) findViewById(R.id.passwordG);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseUser.logInInBackground(login.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.
                            Intent intent = new Intent(AcceuilActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            Log.v(TAG,e.toString());
                        }
                    }
                });
            }
        });
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AcceuilActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}
