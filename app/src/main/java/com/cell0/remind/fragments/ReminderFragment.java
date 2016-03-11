package com.cell0.remind.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;

import com.cell0.remind.R;

import com.cell0.remind.adapters.ReminderAdapter;
import com.cell0.remind.interfaces.OnAppBarResizedListener;
import com.cell0.remind.models.Reminder;
import com.cell0.remind.models.ReminderSet;
import com.cell0.remind.views.layoutmanager.LinearLayoutManager;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReminderFragment extends Fragment implements AbsListView.OnItemClickListener,
        AlertDialog.OnCancelListener, AlertDialog.OnDismissListener, AlertDialog.OnClickListener, OnAppBarResizedListener {

    private static final String ARG_REMINDER_SET_ID = "reminderSetId";

    private Integer reminderSetId;

    private OnFragmentInteractionListener mListener;

    private ReminderSet reminderSet;
    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private String TAG = "ReminderFragment";

    public static ReminderFragment newInstance(Integer reminderSetId) {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REMINDER_SET_ID, reminderSetId);
        fragment.setArguments(args);
        return fragment;
    }

    public ReminderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderAdapter = new ReminderAdapter();
        reminderSetId = getArguments().getInt(ARG_REMINDER_SET_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminderset, container, false);

        reminderRecyclerView = (RecyclerView) view.findViewById(R.id.reminder_list);
        reminderRecyclerView.setAdapter(reminderAdapter);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setReminderSetById(reminderSetId);
        return view;
    }

    public void setReminderSetById(int id) {
        Realm realm = Realm.getInstance(getActivity());
        reminderSet = realm.where(ReminderSet.class).equalTo("id",id).findFirst();
        if(reminderSet != null){
            updateReminders();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {

        }
    }

    private void updateReminders() {
        if(reminderSet != null){
            Realm realm = Realm.getInstance(getActivity());
            RealmResults<Reminder> reminders = realm.where(Reminder.class)
                    .equalTo("reminderSet.id", reminderSet.getId()).findAllSorted("id");
            reminderAdapter.setData(reminders);
            reminderAdapter.setData(reminders);
            reminderAdapter.notifyDataSetChanged();
            reminderRecyclerView.invalidate();
        }
    }

    @Override
    public void onAppBarResized(AppBarLayout appBar) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
        public void onSetToolbarMenu(Integer menuResId, Integer navigationIconResId,
             Toolbar.OnMenuItemClickListener onMenuItemClickListener,
             View.OnClickListener navigationOnClickListener);
        public void onSetFabListener(View.OnClickListener fabListener);
        public void addViewToAppBar(View v);
        public void addViewToAppBar(int resId);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        mListener.onFragmentInteraction("restoreFab");
    }

    //on save reminder
    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText edittext = (EditText) ((AlertDialog) dialog).findViewById(R.id.editText);
        String text = edittext.getText().toString();
        Reminder reminder = new Reminder();
        reminder.setText(text);
        Realm realm = Realm.getInstance(getActivity());
        int nextID = (int) (realm.where(Reminder.class).maximumInt("id") + 1);
        reminder.setId(nextID);
        reminder.setReminderSet(reminderSet);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(reminder);
        realm.commitTransaction();
        updateReminders();
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dialog.dismiss();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mListener.onSetFabListener(new FabListener());
        }
        else {  }
    }

    private class FabListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClickFAB");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Create new Reminder");
            View view = getLayoutInflater(null).inflate(R.layout.create_reminder_dialog,null);
            builder.setView(view);
            builder.setCancelable(true);
            builder.setPositiveButton("Save", ReminderFragment.this);
            builder.setNegativeButton("Cancel", ReminderFragment.this);
            builder.setOnDismissListener(ReminderFragment.this);
            builder.show();
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"activity result: " + data.getStringExtra("data"));
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
