/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  /**
   * The Chat list.
   */
  private ArrayList<ParseUser> uList;
  public static ParseUser user2;

  /**
   * The user.
   */
  public static ParseUser user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    user = ParseUser.getCurrentUser();
    Log.v(TAG, user.getObjectId().toString());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  public void GoSearch()
  {
    Log.v(TAG, "aaaa");
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("Enter the username here");
    // Set up the input
    final EditText input = new EditText(this);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    alertDialogBuilder.setView(input);
    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        String receiver = input.getText().toString();
        Log.v(TAG, "test" + receiver);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", receiver);
        query.findInBackground(new FindCallback<ParseUser>() {
          public void done(List<ParseUser> objects, ParseException e) {
            if (objects != null) {
              user2 = objects.get(0);
              Log.v(TAG, "User found" + user2.getUsername());
              Intent intent = new Intent(MainActivity.this, ChatActivity.class);
              intent.putExtra(Const.USER_DATA, user2.getUsername());
              intent.putExtra(Const.USER_ID, user2.getObjectId());
              startActivity(intent);
            } else {
              Log.v(TAG, "search user failed " + e.toString());
            }
          }
        });
      }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
    //EditText rate = (EditText) alertDialog.findViewById((R.id.));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.OpenNewTabChat) {
      GoSearch();
      Log.v(TAG, "bbbb");
      return true;
    }
    if (id == R.id.logout) {
      ParseUser.logOut();
      Intent intent = new Intent(MainActivity.this, AcceuilActivity.class);
      startActivity(intent);
      return true;
    }

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
