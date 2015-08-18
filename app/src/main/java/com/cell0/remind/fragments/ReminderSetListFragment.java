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

import com.cell0.remind.adapters.ReminderSetAdapter;
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
public class ReminderSetListFragment extends FABFragment implements AbsListView.OnItemClickListener, ReminderSetAdapter.ReminderSetDeleteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ReminderSetAdapter mAdapter;
    private String TAG = "RSetListFragment";

    // TODO: Rename and change types of parameters
    public static ReminderSetListFragment newInstance(String param1, String param2) {
        ReminderSetListFragment fragment = new ReminderSetListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReminderSetListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAdapter = new ReminderSetAdapter(getActivity());
        mAdapter.setReminderSetDeleteListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remindersetlist, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        updateReminderSets();
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
            mListener.onClickReminderSet(mAdapter.getItem(position).getId());
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

    @Override
    public void onClickFAB() {
        super.onClickFAB();
        Log.d(TAG, "onClickFAB");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create new Reminder Set");
        View v = getLayoutInflater(null).inflate(R.layout.create_reminderset_dialog,null);
        builder.setView(v);
        builder.setCancelable(true);
        builder.setPositiveButton("Save", new OnSaveReminderSet());
        builder.setNegativeButton("Cancel", new OnCancelDialog());
        builder.setOnDismissListener(new OnDismissDialog());
        builder.show();
    }

    public void updateReminderSets() {
        Realm realm = Realm.getInstance(getActivity());

        // Pull all the cities from the realm
        RealmResults<ReminderSet> reminderSets = realm.where(ReminderSet.class).findAllSorted("id");

        // Put these items in the Adapter
        mAdapter.setData(reminderSets);
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
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
        public void onClickReminderSet(int id);
    }

    private class OnSaveReminderSet implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            EditText titleEditText = (EditText) ((AlertDialog) dialog).findViewById(R.id.titleEditText);
            EditText descriptionEditText = (EditText) ((AlertDialog) dialog).findViewById(R.id.descriptionEditText);
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            ReminderSet reminderSet = new ReminderSet();
            reminderSet.setTitle(title);
            reminderSet.setDescription(description);
            Realm realm = Realm.getInstance(getActivity());
            int nextID = (int) (realm.where(ReminderSet.class).maximumInt("id") + 1);
            reminderSet.setId(nextID);
            realm.beginTransaction();
            realm.copyToRealm(reminderSet);
            realm.commitTransaction();
            updateReminderSets();
            dialog.dismiss();
        }
    }

    private class OnCancelDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private class OnDismissDialog implements DialogInterface.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            mListener.onFragmentInteraction("restoreFab");
        }
    }

    public void onDeleteReminderSet(int id){
        Log.i(TAG,"onDeleteReminderSet " + id);
        Realm realm = Realm.getInstance(getActivity());
        ReminderSet reminderSet = realm.where(ReminderSet.class).equalTo("id",id).findFirst();
        realm.beginTransaction();
        reminderSet.removeFromRealm();
        realm.commitTransaction();
        updateReminderSets();
    }
}
