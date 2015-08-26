package com.cell0.remind.fragments;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.cell0.remind.adapters.ConditionAdapter;
import com.cell0.remind.adapters.ReminderAdapter;
import com.cell0.remind.interfaces.OnAppBarResizedListener;
import com.cell0.remind.models.Condition;
import com.cell0.remind.models.Reminder;
import com.cell0.remind.models.ReminderSet;
import com.cell0.remind.views.layoutmanager.LinearLayoutManager;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReminderSetFragment extends Fragment implements AbsListView.OnItemClickListener,
        AlertDialog.OnCancelListener, AlertDialog.OnDismissListener, AlertDialog.OnClickListener, OnAppBarResizedListener {

    private static final String ARG_REMINDER_SET_ID = "reminderSetId";

    private Integer reminderSetId;

    private OnFragmentInteractionListener mListener;

    private ReminderSet reminderSet;
    private ReminderSet reminderSetBackup;
    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private String TAG = "ReminderSetFragment";
    private EditText reminderSetTitleEditText;
    private EditText reminderSetDescriptionEditText;

    public static ReminderSetFragment newInstance(Integer reminderSetId) {
        ReminderSetFragment fragment = new ReminderSetFragment();
        Bundle args = new Bundle();
        if(reminderSetId != null){
            args.putInt(ARG_REMINDER_SET_ID, reminderSetId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    public ReminderSetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reminderAdapter = new ReminderAdapter();
        if (getArguments() != null) {
            reminderSetId = getArguments().getInt(ARG_REMINDER_SET_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminderset, container, false);

        reminderRecyclerView = (RecyclerView) view.findViewById(R.id.reminder_list);
        reminderRecyclerView.setAdapter(reminderAdapter);
        reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        View appBarView = View.inflate(getActivity(),R.layout.reminderset_appbar,null);
        reminderSetTitleEditText = (EditText) appBarView.findViewById(R.id.reminderSetTitleEditText);
        reminderSetDescriptionEditText =
                (EditText) appBarView.findViewById(R.id.reminderSetDescriptionEditText);
        mListener.addViewToAppBar(appBarView);

        if(reminderSetId != null) {
            setReminderSetById(reminderSetId);
        }else{
            createNewReminderSet();
        }

        reminderSetTitleEditText.addTextChangedListener(new TitleWatcher());
        reminderSetDescriptionEditText.addTextChangedListener(new DescriptionWatcher());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.onFragmentInteraction("restoreFab");
        mListener.onSetFabListener(new FabListener());
        mListener.onSetToolbarMenu(R.menu.menu_reminderset, R.drawable.ic_action_navigation_check,
                new OnMenuItenClickListener(), new OnNavigationClickListener());
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

    private void createNewReminderSet() {

        reminderSet = new ReminderSet();
        Realm realm = Realm.getInstance(getActivity());
        int nextID = (int) (realm.where(ReminderSet.class).maximumInt("id") + 1);
        reminderSet.setId(nextID);
        saveReminderSet();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {

        }
    }

    public void setReminderSetById(int id) {
        Realm realm = Realm.getInstance(getActivity());
        reminderSet = realm.where(ReminderSet.class).equalTo("id",id).findFirst();
        reminderSetBackup = new ReminderSet(reminderSet);

        if(reminderSet != null){
            if(reminderSetTitleEditText != null)
                reminderSetTitleEditText.setText(reminderSet.getTitle());
            if(reminderSetDescriptionEditText != null)
                reminderSetDescriptionEditText.setText(reminderSet.getDescription());
            updateReminders();
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

    private void saveReminderSet(){
        if(reminderSet != null){
            Log.d(TAG,"saving reminder set");
            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(reminderSet);
            realm.commitTransaction();
        }
    }

    private void removeReminderSet(){
        if(reminderSet != null){
            Realm realm = Realm.getInstance(getActivity());
            realm.beginTransaction();
            reminderSet.removeFromRealm();
            realm.commitTransaction();
        }
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


    private class TitleWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String title = s.toString();
            if(reminderSet != null){
                Realm realm = Realm.getInstance(getActivity());
                realm.beginTransaction();
                reminderSet.setTitle(title);
                realm.copyToRealmOrUpdate(reminderSet);
                realm.commitTransaction();
            }
        }
    }

    private class DescriptionWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String description = s.toString();
            if(reminderSet != null){
                Realm realm = Realm.getInstance(getActivity());
                realm.beginTransaction();
                reminderSet.setDescription(description);
                realm.copyToRealmOrUpdate(reminderSet);
                realm.commitTransaction();
            }
        }
    }

    private class OnNavigationClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            saveReminderSet();
            //TODO: replace getActivity with something that won't become null
            getActivity().onBackPressed();
        }
    }

    private class OnMenuItenClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(TAG,"onMenuItemClick");
            switch (item.getItemId()){
                case R.id.action_cancel:
                    AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(getActivity());
                    cancelBuilder.setMessage("Discard changes?");
                    cancelBuilder.setCancelable(true);
                    cancelBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    cancelBuilder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(reminderSetBackup == null){
                                removeReminderSet();
                            }else{
                                reminderSet = reminderSetBackup;
                                saveReminderSet();
                            }
                            dialog.dismiss();
                            getActivity().onBackPressed();
                        }
                    });
                    cancelBuilder.show();
                    break;
                case R.id.action_remove:
                    AlertDialog.Builder removeBuilder = new AlertDialog.Builder(getActivity());
                    removeBuilder.setMessage("You are about to remove this reminderSet");
                    removeBuilder.setCancelable(true);
                    removeBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    removeBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeReminderSet();
                            dialog.dismiss();
                            getActivity().onBackPressed();
                        }
                    });
                    removeBuilder.show();
            }
            return false;
        }
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
            builder.setPositiveButton("Save", ReminderSetFragment.this);
            builder.setNegativeButton("Cancel", ReminderSetFragment.this);
            builder.setOnDismissListener(ReminderSetFragment.this);
            builder.show();
        }
    }
}
