package com.cell0.remind.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Reminder> reminders = null;
    private ReminderDeleteListener reminderDeleteListener;

    public ReminderAdapter() {
    }

    public void setData(List<Reminder> details) {
        this.reminders = details;
    }


    public Reminder getItem(int position) {
        if (reminders == null || reminders.get(position) == null) {
            return null;
        }
        return reminders.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminderset_listitem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTitleTextView().setText(reminders.get(position).getText());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(reminders == null){
            return 0;
        }
        return reminders.size();
    }
/*
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
    }*/

    private class OnClickDeleteButton implements View.OnClickListener {
        int position;

        public OnClickDeleteButton(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(reminderDeleteListener != null){
                reminderDeleteListener.onDeleteReminderSet(position);
            }
        }
    }

    public interface ReminderDeleteListener {
        public void onDeleteReminderSet(int position);
    }

    public void setReminderDeleteListener(ReminderDeleteListener reminderDeleteListener) {
        this.reminderDeleteListener = reminderDeleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView titleTextView;
        public ViewHolder(View v){
            super(v);
            this.view = v;
            this.titleTextView = (TextView) v.findViewById(R.id.title);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public void setTitleTextView(TextView titleTextView) {
            this.titleTextView = titleTextView;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }
}