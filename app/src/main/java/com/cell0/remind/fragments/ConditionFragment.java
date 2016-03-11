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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;

import com.cell0.remind.R;

import com.cell0.remind.activities.ConditionActivity;
import com.cell0.remind.adapters.ConditionAdapter;
import com.cell0.remind.adapters.ReminderAdapter;
import com.cell0.remind.interfaces.OnAppBarResizedListener;
import com.cell0.remind.models.Condition;
import com.cell0.remind.models.Reminder;
import com.cell0.remind.models.ReminderSet;
import com.cell0.remind.models.TimeConditionConfiguration;
import com.cell0.remind.views.layoutmanager.LinearLayoutManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class ConditionFragment extends Fragment implements AbsListView.OnItemClickListener,
        AlertDialog.OnCancelListener, AlertDialog.OnDismissListener, AlertDialog.OnClickListener, OnAppBarResizedListener {

    private static final String ARG_REMINDER_SET_ID = "reminderSetId";
    private static final int REQUEST_CONDITION = 0;

    private Integer reminderSetId;

    private OnFragmentInteractionListener mListener;

    private ReminderSet reminderSet;
    private RecyclerView recyclerView;
    private ConditionAdapter conditionAdapter;
    private String TAG = "ConditionFragment";

    public static ConditionFragment newInstance(Integer reminderSetId) {
        ConditionFragment fragment = new ConditionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_REMINDER_SET_ID, reminderSetId);
        fragment.setArguments(args);
        return fragment;
    }

    public ConditionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conditionAdapter = new ConditionAdapter();
        reminderSetId = getArguments().getInt(ARG_REMINDER_SET_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminderset, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.reminder_list);
        recyclerView.setAdapter(conditionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setReminderSetById(reminderSetId);

        return view;
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

    public void setReminderSetById(int id) {
        Realm realm = Realm.getInstance(getActivity());
        reminderSet = realm.where(ReminderSet.class).equalTo("id",id).findFirst();

        if(reminderSet != null){
            updateConditions();
        }
    }

    private void updateConditions() {
        if(reminderSet != null){
            Realm realm = Realm.getInstance(getActivity());
            RealmResults<Condition> conditions = realm.where(Condition.class)
                    .equalTo("reminderSet.id", reminderSet.getId()).findAllSorted("id");
            conditionAdapter.setData(conditions);
            conditionAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
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
        updateConditions();
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
            Intent intent = new Intent(getActivity(), ConditionActivity.class);
            Log.d(TAG, "starting activity");
            getParentFragment().startActivityForResult(intent, REQUEST_CONDITION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            int minTime = data.getIntExtra("minTime",0);
            int maxTime = data.getIntExtra("maxTime",0);
            int frequency = data.getIntExtra("frequency",0);

            createNewTimeCondition(minTime,maxTime,frequency);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createNewTimeCondition(int minTime, int maxTime, int frequency) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.HOUR_OF_DAY, minTime);
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.HOUR_OF_DAY, maxTime);

        Condition condition = createNewCondition();
        condition.setType("time");

        TimeConditionConfiguration timeConditionConfiguration = new TimeConditionConfiguration();

        Realm realm = Realm.getInstance(getActivity());
        int nextID = (int) (realm.where(TimeConditionConfiguration.class).maximumInt("id") + 1);

        timeConditionConfiguration.setId(nextID);
        timeConditionConfiguration.setStartTime(startCal.getTime());
        timeConditionConfiguration.setEndTime(endCal.getTime());
        timeConditionConfiguration.setFrequency(frequency);
        timeConditionConfiguration.setCondition(condition);

        condition.setTimeConditionConfiguration(timeConditionConfiguration);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(timeConditionConfiguration);
        realm.commitTransaction();
        updateConditions();
    }

    private Condition createNewCondition() {
        Condition condition = new Condition();
        Realm realm = Realm.getInstance(getActivity());
        int nextID = (int) (realm.where(Condition.class).maximumInt("id") + 1);
        condition.setId(nextID);
        condition.setReminderSet(reminderSet);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(condition);
        realm.commitTransaction();
        return condition;
    }
}
