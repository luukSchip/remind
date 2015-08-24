package com.cell0.remind.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cell0.remind.R;
import com.cell0.remind.models.ReminderSet;

import java.util.List;

/**
 * Created by luuk on 17-8-15.
 */
public class ReminderSetAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<ReminderSet> reminderSets = null;

    private ReminderSetDeleteListener reminderSetDeleteListener;

    public ReminderSetAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ReminderSet> details) {
        this.reminderSets = details;
    }

    @Override
    public int getCount() {
        if(reminderSets == null){
            return 0;
        }
        return reminderSets.size();
    }

    @Override
    public ReminderSet getItem(int position) {
        if (reminderSets == null || reminderSets.get(position) == null) {
            return null;
        }
        return reminderSets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.reminderset_listitem, parent, false);
            ((ViewGroup)currentView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }

        ReminderSet reminderSet = reminderSets.get(position);

        if (reminderSet != null) {
            ((TextView) currentView.findViewById(R.id.title)).setText(reminderSet.getTitle());
        }


        return currentView;
    }
    private class OnClickDeleteButton implements View.OnClickListener {
        int position;

        public OnClickDeleteButton(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(reminderSetDeleteListener != null){
                reminderSetDeleteListener.onDeleteReminderSet(position);
            }
        }
    }

    public interface ReminderSetDeleteListener {
        public void onDeleteReminderSet(int position);
    }

    public void setReminderSetDeleteListener(ReminderSetDeleteListener reminderSetDeleteListener) {
        this.reminderSetDeleteListener = reminderSetDeleteListener;
    }

}
