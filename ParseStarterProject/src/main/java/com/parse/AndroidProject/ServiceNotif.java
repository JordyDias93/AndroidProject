package com.parse.AndroidProject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Dr.Dias on 14/12/2015.
 */
public class ServiceNotif extends Service {

    private static final String TAG = "ServiceNotif";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    private Looper mServiceLooper;
    ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public Message msg;
        public int id=1;
        public boolean tryagain=true;
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void test()
        {

        }

        @Override
        public void handleMessage(final Message msg) {
            this.msg=msg;
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds
            final Date[] test = {new Date()};
            while(tryagain) {

                ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
                Log.v(TAG, "Check if new message for " + ParseUser.getCurrentUser().getUsername().toString());
                Log.v(TAG, "Check if new message AT " + test[0]);
                q.whereGreaterThan("createdAt", test[0]);
                q.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername().toString());
                q.orderByDescending("createdAt");
                q.setLimit(2);
                q.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects != null && objects.size() > 0) {
                            Log.v(TAG, "DES MESSAGES !!!!");

                            ParseObject po = objects.get(0);
                            Single_Message SM = new Single_Message(po
                                    .getString("Message"), po.getCreatedAt(), po
                                    .getString("Sender"));
                            Log.v(TAG, "New Sender " + po.getString("Sender"));
                            if (((StarterApplication) (ServiceNotif.this).getApplication()).DernierMessageDate == null
                                    || ((StarterApplication) (ServiceNotif.this).getApplication()).DernierMessageDate.before(SM.date)) {
                                ((StarterApplication) (ServiceNotif.this).getApplication()).DernierMessageDate = SM.date;
                            }
                            NewMessageNotification.notify((ServiceNotif.this).getApplicationContext(),"New Message On PChat",SM.sender,SM.msg,id);
                            test[0] =new Date();
                            id++;
                        } else {
                            Log.v(TAG, "Nope");
                        }
                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(msg.arg1);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
        }
    }


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it

        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Thread.MAX_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        this.getApplicationContext();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        mServiceHandler.tryagain=false;
    }

}
