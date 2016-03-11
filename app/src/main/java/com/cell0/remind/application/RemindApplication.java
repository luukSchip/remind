package com.cell0.remind.application;

import android.app.Application;
import android.content.Intent;

import com.cell0.remind.services.ReminderService;

/**
 * Created by luuk on 10-9-2015.
 */
public class RemindApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Intent startServiceIntent = new Intent(getApplicationContext(), ReminderService.class);
        getApplicationContext().startService(startServiceIntent);
    }
}
