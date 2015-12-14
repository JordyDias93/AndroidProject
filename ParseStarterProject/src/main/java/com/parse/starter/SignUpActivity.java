package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        final EditText tconfirmmail = (EditText) findViewById(R.id.TConfirmmail);

        SignUpReal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String LOGIN = login.getText().toString().replaceAll("\\s+$", "");
                String PASSWORD = pass.getText().toString().replaceAll("\\s+$", "");
                String MAIL = mail.getText().toString().replaceAll("\\s+$", "");
                String MAIL2 = tconfirmmail.getText().toString().replaceAll("\\s+$", "");
                user.setUsername(LOGIN);
                user.setPassword(PASSWORD);
                user.setEmail(MAIL);
                if(MAIL.equals(MAIL2)) {
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.v(TAG, e.toString());
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignUpActivity.this,
                            "Mail non identique",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
