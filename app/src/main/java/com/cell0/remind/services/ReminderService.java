package com.cell0.remind.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.cell0.remind.R;
import com.cell0.remind.activities.RemindActivity;
import com.cell0.remind.models.ReminderSet;
import com.cell0.remind.receivers.NotificationPublisher;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by luuk on 10-9-2015.
 */
public class ReminderService extends Service{

    private static final String TAG = "ReminderService";


    public ReminderService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void setReminders() {
        // find active reminderSets
        Realm realm = Realm.getInstance(getApplicationContext());
        RealmResults<ReminderSet> reminderSets = realm.where(ReminderSet.class).findAll();
        // set first notification
//        for(int i = 0; i < reminderSets.size(); i++){
//        }
        if(reminderSets.size() > 0){
            scheduleNextReminder(reminderSets.get(0));
        }
        Log.d(TAG,"wassup");
    }

    private void scheduleNextReminder(ReminderSet reminderSet) {
        Log.d(TAG,"scheduling notification");
        scheduleNotification(getNotification(reminderSet.getTitle()),5000);
    }


    private void scheduleNotification(final Notification notification, int delay) {
        /*Intent notificationIntent = new Intent(getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"notifying...");

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify(1, notification);
            }
        },delay);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText(content);
        Intent resultIntent = new Intent(this, RemindActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RemindActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder.build();
    }


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            /*// Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);*/
            setReminders();
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",Thread.MAX_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
