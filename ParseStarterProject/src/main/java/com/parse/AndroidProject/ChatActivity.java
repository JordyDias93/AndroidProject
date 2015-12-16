package com.parse.AndroidProject;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dr.Dias on 10/12/2015.
 */
public class ChatActivity extends AppCompatActivity {

    private ArrayList<Single_Message> Conversation;
    private static final String TAG = "ChatActivity";
    private String user_to_send_username="";
    HistorisationChat Historique;
    /** The handler. */
    private static Handler handler;
    boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_global);

        Conversation = new ArrayList<>();
        ListView list = (ListView) findViewById(R.id.List_Single_Message);
        Historique = new HistorisationChat();
        list.setAdapter(Historique);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        user_to_send_username= this.getIntent().getStringExtra(Const.USER_DATA);
        this.setTitle(user_to_send_username);
        Log.v(TAG, "Username : " + user_to_send_username);

        Button button_send = (Button) findViewById(R.id.button_send);
        final EditText messagetext = (EditText) findViewById(R.id.messagetext);

        button_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "Try Sending");
                sendMessage(messagetext);
            }
        });
        //loadConversation();
        handler = new Handler();
    }

    /* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        loadConversation();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        loadConversation();
    }

    private void loadConversation() {
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if (Conversation.size() == 0) {
            Log.v(TAG, "Load EVERYTHING");
            ArrayList<String> al = new ArrayList<String>();
            al.add(user_to_send_username);
            al.add(ParseUser.getCurrentUser().getUsername().toString());
            q.whereContainedIn("Sender", al);
            q.whereContainedIn("Receiver", al);
        } else {
            Log.v(TAG, "Load New message");
            if (((StarterApplication)this.getApplication()).DernierMessageDate != null)
            q.whereGreaterThan("createdAt", ((StarterApplication)this.getApplication()).DernierMessageDate);
            q.whereEqualTo("Sender", user_to_send_username);
            q.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername().toString());
        }
        q.orderByDescending("createdAt");
        q.setLimit(30);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.v(TAG, "object size "+Integer.toString(objects.size()));
                if (objects != null && objects.size() > 0)
                {
                    Log.v(TAG, "DES MESSAGES !!!!");
                    for (int i = objects.size() - 1; i >= 0; i--)
                    {

                        ParseObject po = objects.get(i);
                        Single_Message SM = new Single_Message(po
                                .getString("Message"), po.getCreatedAt(), po
                                .getString("Sender"));
                        Log.v(TAG, "New Sender " + po.getString("Sender"));
                        Conversation.add(SM);
                        if (((StarterApplication)(ChatActivity.this).getApplication()).DernierMessageDate == null
                                || ((StarterApplication)(ChatActivity.this).getApplication()).DernierMessageDate.before(SM.date)) {
                            ((StarterApplication)(ChatActivity.this).getApplication()).DernierMessageDate = SM.date;
                        }
                        NotificationCompat.Builder notification =new NotificationCompat.Builder(ChatActivity.this);
                        notification.setTicker("New Notification!!!!");
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("New Message from " + SM.sender);
                        notification.setContentText(SM.msg);
                        Historique.notifyDataSetChanged();
                    }
                }

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                        if (isRunning)
                            loadConversation();
                    }
                }, 4000);
            }
        });
    }

    private void sendMessage(EditText messagetext)
    {
        if (messagetext.length() == 0)
            return;

        String s = messagetext.getText().toString();
        messagetext.setText(null);

        ParseObject po = new ParseObject("Chat");
        po.put("Sender", ParseUser.getCurrentUser().getUsername().toString());
        po.put("Receiver", user_to_send_username);
        po.put("Message", s);
        po.put("status", Single_Message.STATUS_SENDING);
        //Creation du message
        final Single_Message single_message = new Single_Message(s, new Date(),
                ParseUser.getCurrentUser().getUsername().toString());
        single_message.status=(Single_Message.STATUS_SENDING);
        Conversation.add(single_message);
        Log.v(TAG, "Try Saving Message");
        po.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                if (e == null)
                    single_message.status=(Single_Message.STATUS_SENT);
                else
                    single_message.status=(Single_Message.STATUS_FAILED);
                Historique.notifyDataSetChanged();
            }
        });
    }

    private class HistorisationChat extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return Conversation.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Single_Message getItem(int arg0)
        {
            return Conversation.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Single_Message SM = getItem(position);
            if (ParseUser.getCurrentUser().getUsername().equals(SM.sender)) {
                convertView = getLayoutInflater().inflate(R.layout.chat_item_send, null);
            }
            else {
                convertView = getLayoutInflater().inflate(R.layout.chat_item_received, null);
            }

            TextView lbl = (TextView) convertView.findViewById(R.id.TextContact);
            if (ParseUser.getCurrentUser().getUsername()!=user_to_send_username) {
                lbl.setText("");
            }
            else {
                lbl.setText(user_to_send_username);
            }
            lbl = (TextView) convertView.findViewById(R.id.TextMessage);
            lbl.setText(SM.msg);

            lbl = (TextView) convertView.findViewById(R.id.TextTime);
            if (SM.status==-1)
            {
                if (SM.status == SM.STATUS_SENT)
                    lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, SM
                                    .date.getTime(), DateUtils.SECOND_IN_MILLIS,
                            DateUtils.DAY_IN_MILLIS, 0)+"Delivered");
                else if (SM.status == SM.STATUS_SENDING)
                    lbl.setText("Sending...");
                else if (SM.status == SM.STATUS_READED)
                    lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, SM
                                    .date.getTime(), DateUtils.SECOND_IN_MILLIS,
                            DateUtils.DAY_IN_MILLIS, 0)+"Readed");
                else
                    lbl.setText("Failed");
            }
            else
                lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, SM
                                .date.getTime(), DateUtils.SECOND_IN_MILLIS,
                        DateUtils.DAY_IN_MILLIS, 0));

            return convertView;
        }

    }

}
