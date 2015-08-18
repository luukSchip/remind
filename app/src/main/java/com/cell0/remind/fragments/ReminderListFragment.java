package com.cell0.remind.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.cell0.remind.R;

import com.cell0.remind.adapters.ReminderListAdapter;
import com.cell0.remind.models.Reminder;
import com.cell0.remind.models.ReminderSet;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ReminderListFragment extends FABFragment implements AbsListView.OnItemClickListener,
AlertDialog.OnCancelListener, AlertDialog.OnDismissListener, AlertDialog.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ReminderSet reminderSet;
    private AbsListView mListView;
    private ReminderListAdapter mAdapter;
    private String TAG = "ReminderListFragment";
    private TextView reminderSetTitleTextView;
    private TextView reminderSetDescriptionTextView;

    // TODO: Rename and change types of parameters
    public static ReminderListFragment newInstance(String param1, String param2) {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReminderListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAdapter = new ReminderListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminderlist, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        reminderSetTitleTextView = (TextView) view.findViewById(R.id.reminder_set_title);
        reminderSetDescriptionTextView = (TextView) view.findViewById(R.id.reminder_set_description);
        mListView.setOnItemClickListener(this);

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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void setReminderSetById(int id) {
        Realm realm = Realm.getInstance(getActivity());
        reminderSet = realm.where(ReminderSet.class).equalTo("id",id).findFirst();

        if(reminderSet != null){
            if(reminderSetTitleTextView != null)
                reminderSetTitleTextView.setText(reminderSet.getTitle());
            if(reminderSetDescriptionTextView != null)
                reminderSetDescriptionTextView.setText(reminderSet.getDescription());
            updateReminders();
        }


    }

    private void updateReminders() {
        if(reminderSet != null){
            Realm realm = Realm.getInstance(getActivity());
            RealmResults<Reminder> reminders = realm.where(Reminder.class).equalTo("reminderSet.id",reminderSet.getId()).findAllSorted("id");
            mAdapter.setData(reminders);
            mAdapter.setData(reminders);
            mAdapter.notifyDataSetChanged();
            mListView.invalidate();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    @Override
    public void onClickFAB() {
        super.onClickFAB();
        Log.d(TAG, "onClickFAB");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create new Reminder");
        View v = getLayoutInflater(null).inflate(R.layout.create_reminder_dialog,null);
        builder.setView(v);
        builder.setCancelable(true);
        builder.setPositiveButton("Save", this);
        builder.setNegativeButton("Cancel", this);
        builder.setOnDismissListener(this);
        builder.show();
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
        realm.copyToRealm(reminder);
        realm.commitTransaction();
        updateReminders();
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dialog.dismiss();
    }

}
