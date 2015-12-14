package com.parse.starter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
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

        if(ParseUser.getCurrentUser()!=null)
        {
            Intent intent = new Intent(AcceuilActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_acceuil);
            Log.v(TAG, "Welcome here ! ");
            buttonlogin = (Button) findViewById(R.id.buttonlogin);
            buttonsignup = (Button) findViewById(R.id.buttonsignup);
            final EditText login = (EditText) findViewById(R.id.usernameG);
            final EditText pass = (EditText) findViewById(R.id.passwordG);

            buttonlogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final ProgressDialog dia = ProgressDialog.show(AcceuilActivity.this, null,"Loading");
                    String LOGIN = login.getText().toString().replaceAll("\\s+$", "");
                    String PASSWORD = pass.getText().toString().replaceAll("\\s+$", "");
                    login.setText("");
                    pass.setText("");

                    ParseUser.logInInBackground(LOGIN, PASSWORD, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, com.parse.ParseException e) {
                            dia.dismiss();
                            if (user != null) {
                                // Hooray! The user is logged in.
                                Log.v(TAG, "sucess login");
                                Intent intent = new Intent(AcceuilActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                Toast.makeText(AcceuilActivity.this,
                                        e.toString(),
                                        Toast.LENGTH_SHORT).show();
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

}
