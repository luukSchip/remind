package com.cell0.remind.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by luuk on 17-8-15.
 */
public class CreateReminderSetDialog extends AlertDialog{

    protected CreateReminderSetDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create Reminder Set");
        setMessage("Bla bla bla");
    }

}
