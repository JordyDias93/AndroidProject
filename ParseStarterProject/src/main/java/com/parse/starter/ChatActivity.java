package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseUser;

/**
 * Created by Dr.Dias on 10/12/2015.
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private String user_to_send_id="";
    private String user_to_send_username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_global);

        user_to_send_id= this.getIntent().getStringExtra(Const.USER_ID);
        user_to_send_username= this.getIntent().getStringExtra(Const.USER_DATA);
        this.setTitle(user_to_send_username);
        Log.v(TAG, "ID : " + user_to_send_id);
        Log.v(TAG, "Username : "+user_to_send_username);
    }

}
