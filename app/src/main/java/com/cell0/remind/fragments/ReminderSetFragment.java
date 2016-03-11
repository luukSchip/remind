package com.cell0.remind.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cell0.remind.R;
import com.cell0.remind.adapters.ReminderSetFragmentPagerAdapter;
import com.cell0.remind.models.ReminderSet;

import io.realm.Realm;

public class ReminderSetFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String ARG_REMINDER_SET_ID = "reminderSetId";
    private static final String TAG = "ReminderSetFragment";

    private ReminderSet reminderSet;
    private ReminderSet reminderSetBackup;

    private Integer reminderSetId;

    private ViewPager pager;
    private TabLayout tabLayout;
    private EditText reminderSetTitleEditText;
    private EditText reminderSetDescriptionEditText;

    private OnFragmentInteractionListener mListener;

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

        if (getArguments() != null) {
            reminderSetId = getArguments().getInt(ARG_REMINDER_SET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_set, container, false);

        pager = (ViewPager) v.findViewById(R.id.pager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);

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

        pager.setAdapter(
                new ReminderSetFragmentPagerAdapter(getChildFragmentManager(), reminderSetId));
        pager.addOnPageChangeListener(this);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(pager);

        reminderSetTitleEditText.addTextChangedListener(new TitleWatcher());
        reminderSetDescriptionEditText.addTextChangedListener(new DescriptionWatcher());

        return v;
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state){
            case ViewPager.SCROLL_STATE_DRAGGING:
                mListener.onFragmentInteraction("hideFab");
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                mListener.onFragmentInteraction("restoreFab");
                break;
        }
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
    public void onStart() {
        super.onStart();
        mListener.onSetToolbarMenu(R.menu.menu_reminderset, R.drawable.ic_action_navigation_check,
                new OnMenuItemClickListener(), new OnNavigationClickListener());
    }



    private class OnNavigationClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            saveReminderSet();
            //TODO: replace getActivity with something that won't become null
            getActivity().onBackPressed();
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
            updateData();
        }
    }

    private void updateData() {
        //TODO: iets
    }

    private void createNewReminderSet() {
        reminderSet = new ReminderSet();
        Realm realm = Realm.getInstance(getActivity());
        int nextID = (int) (realm.where(ReminderSet.class).maximumInt("id") + 1);
        reminderSet.setId(nextID);
        reminderSetId = nextID;
        saveReminderSet();
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

    private class OnMenuItemClickListener implements Toolbar.OnMenuItemClickListener {
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
}
