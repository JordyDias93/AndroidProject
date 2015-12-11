package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

/**
 * Created by Dr.Dias on 09/12/2015.
 */
public class SignUpActivity extends Activity {

    private static final String TAG = "Sign Up Activity";
    Button SignUpReal;
    ParseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        user = new ParseUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.v(TAG, "Welcome here ! ");
        SignUpReal = (Button)findViewById(R.id.SignUpReal);
        final EditText login = (EditText) findViewById(R.id.TUsername);
        final EditText pass = (EditText) findViewById(R.id.TPassword);
        final EditText mail = (EditText) findViewById(R.id.TMail);

        SignUpReal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.setUsername(login.getText().toString());
                user.setPassword(pass.getText().toString());
                user.setEmail(mail.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.v(TAG,e.toString());
                        }
                    }
                });
            }
        });
    }

}
