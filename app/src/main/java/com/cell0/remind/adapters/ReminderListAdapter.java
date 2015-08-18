package com.cell0.remind.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cell0.remind.R;
import com.cell0.remind.models.Reminder;

import java.util.List;

/**
 * Created by luuk on 17-8-15.
 */
public class ReminderListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Reminder> reminders = null;

    public ReminderListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Reminder> details) {
        this.reminders = details;
    }

    @Override
    public int getCount() {
        if(reminders == null){
            return 0;
        }
        return reminders.size();
    }

    @Override
    public Reminder getItem(int position) {
        if (reminders == null || reminders.get(position) == null) {
            return null;
        }
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.reminderset_listitem, parent, false);
        }

        Reminder reminder = reminders.get(position);

        if (reminder != null) {
            ((TextView) currentView.findViewById(R.id.title)).setText(reminder.getText());
        }

        return currentView;
    }
}