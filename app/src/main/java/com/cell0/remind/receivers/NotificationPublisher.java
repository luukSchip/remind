package com.cell0.remind.receivers;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPublisher extends BroadcastReceiver {

    private static final String TAG = "NotificationPublisher";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"publishing notification");
        /*NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);*/

    }
}