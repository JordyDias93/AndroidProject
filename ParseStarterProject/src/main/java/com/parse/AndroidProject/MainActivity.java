/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.AndroidProject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  /**
   * The Chat list.
   */
  private ArrayList<ParseUser> Contact_list;
  private ArrayList<String> String_buffer_list;
  public static ParseUser user2;
  Intent intentS = null;

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

  @Override
  protected void onResume()
  {
    super.onResume();
    Log.v(TAG, "test resume");
    if(intentS!=null)
    stopService(intentS);
    Log.v(TAG, "test resume");
    loadUserList();
  }

  protected void onPause()
  {
    super.onPause();
    Log.v(TAG, "test pause");
    intentS = new Intent(this, ServiceNotif.class);
    startService(intentS);
  }

  protected void onDestroy()
  {
    super.onPause();
    Log.v(TAG, "test pause");
    stopService(intentS);
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
      return true;
    }
    if (id == R.id.logout) {
      ParseUser.logOut();
      Intent intent = new Intent(MainActivity.this, AcceuilActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      if(intentS!=null)
        stopService(intentS);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void loadUserList()
  {

    Log.v(TAG,  user.getUsername());
    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    final ProgressDialog dia = ProgressDialog.show(this, null,
            "Loading");

    ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
    q.whereContains("Sender", user.getUsername());
    ParseQuery<ParseObject> q2 = ParseQuery.getQuery("Chat");
    q2.whereContains("Receiver", user.getUsername());

    List<ParseQuery<ParseObject>> queries = new ArrayList<>();
    queries.add(q);
    queries.add(q2);

    ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
    mainQuery.orderByAscending("createdAt");
    mainQuery.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        dia.dismiss();
        AlertDialog alert;
        alert = alertDialogBuilder.create();
        if (objects != null) {
          String_buffer_list = new ArrayList<>();
          Log.v(TAG, "value first request " + user.getUsername()+ "test");
          for (int a = 0; a < objects.size(); a++) {
            ParseObject po = objects.get(a);
            if (!String_buffer_list.contains(po.getString("Sender"))) {
              String_buffer_list.add(po.getString("Sender"));
            } else if (!String_buffer_list.contains(po.getString("Receiver"))) {
              String_buffer_list.add(po.getString("Receiver"));
            }
          }
        } else {
          alert.show();
          e.printStackTrace();
        }
        String_buffer_list.remove(user.getUsername());
        Log.v(TAG, String.valueOf(String_buffer_list));
        ParseUser.getQuery().whereContainedIn("username", String_buffer_list)
                .findInBackground(new FindCallback<ParseUser>() {

                  @Override
                  public void done(List<ParseUser> li, ParseException e) {
                    dia.dismiss();
                    AlertDialog alert;
                    alert = alertDialogBuilder.create();
                    if (li != null) {
                      if (li.size() == 0)
                        Toast.makeText(MainActivity.this,
                                "User_Not_Found",
                                Toast.LENGTH_SHORT).show();

                      Contact_list = new ArrayList<>(li);
                      ListView list = (ListView) findViewById(R.id.list);
                      list.setAdapter(new HistorisationChatList());
                      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0,
                                                View arg1, int pos, long arg3) {
                          startActivity(new Intent(MainActivity.this,
                                  ChatActivity.class).putExtra(
                                  Const.USER_DATA, Contact_list.get(pos)
                                          .getUsername()));
                        }
                      });
                    } else {
                      alert.show();
                      e.printStackTrace();
                    }
                  }
                });
      }
    });

  }

  private class HistorisationChatList extends BaseAdapter
  {

    @Override
    public int getCount() {
      return Contact_list.size();
    }

    @Override
    public ParseUser getItem(int position) {
      return Contact_list.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null)
        convertView = getLayoutInflater().inflate(R.layout.chat_item, null);

      ParseUser c = getItem(position);
      TextView lbl = (TextView) convertView;
      lbl.setText(c.getUsername());
      return convertView;
    }
  }

}
