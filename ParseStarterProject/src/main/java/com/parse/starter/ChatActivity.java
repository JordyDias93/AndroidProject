package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

/**
 * Created by Dr.Dias on 10/12/2015.
 */
public class ChatActivity extends Activity{

    private static final String TAG = "Chat activity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_global);
        Log.v(TAG, "create chat");
    }

}
