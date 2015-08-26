package com.cell0.remind.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cell0.remind.R;
import com.cell0.remind.fragments.ReminderSetListFragment;
import com.cell0.remind.models.ReminderSet;

import java.util.List;

/**
 * Created by luuk on 17-8-15.
 */
public class ReminderSetAdapter extends RecyclerView.Adapter<ReminderSetAdapter.ViewHolder> {


    private List<ReminderSet> reminderSets = null;

    private ReminderSetDeleteListener reminderSetDeleteListener;
    private View.OnClickListener onItemclickListener;

    public void setData(List<ReminderSet> details) {
        this.reminderSets = details;
    }

    public ReminderSet getItem(int position) {
        if (reminderSets == null || reminderSets.get(position) == null) {
            return null;
        }
        return reminderSets.get(position);
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
        holder.getView().setTag(position);
        if(onItemclickListener != null){
            holder.view.setOnClickListener(onItemclickListener);
        }
        holder.getTitleTextView().setText(reminderSets.get(position).getTitle());
        holder.getDescriptionTextView().setText(reminderSets.get(position).getDescription());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(reminderSets == null){
            return 0;
        }
        return reminderSets.size();
    }

    public void setOnItemclickListener(View.OnClickListener onItemclickListener) {
        this.onItemclickListener = onItemclickListener;
    }
/*
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
    }*/

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView titleTextView;
        private TextView descriptionTextView;
        public ViewHolder(View v){
            super(v);
            this.view = v;
            this.titleTextView = (TextView) v.findViewById(R.id.title);
            this.descriptionTextView = (TextView) v.findViewById(R.id.description);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public void setTitleTextView(TextView titleTextView) {
            this.titleTextView = titleTextView;
        }

        public TextView getDescriptionTextView() {
            return descriptionTextView;
        }

        public void setDescriptionTextView(TextView descriptionTextView) {
            this.descriptionTextView = descriptionTextView;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

}
