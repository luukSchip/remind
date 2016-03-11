package com.cell0.remind.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cell0.remind.services.ReminderService;

/**
 * Created by luuk on 10-9-2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ReminderService.class);
        context.startService(startServiceIntent);
    }
}