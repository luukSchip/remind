package com.cell0.remind.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cell0.remind.R;
import com.cell0.remind.adapters.MainFragmentPagerAdapter;
import com.cell0.remind.fragments.FABFragment;
import com.cell0.remind.fragments.ReminderListFragment;
import com.cell0.remind.fragments.ReminderSetListFragment;
import com.cell0.remind.views.CustomFAB;


public class RemindActivity extends FragmentActivity
        implements ReminderSetListFragment.OnFragmentInteractionListener,
        ReminderListFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "RemindActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CustomFAB fab;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private int selectedPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_test);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        fab = (CustomFAB) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mainFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new OnClickFab());

        toolbar.setTitle("ReMind");
    }

    @Override
    public void onFragmentInteraction(String id) {
        switch(id){
            case "restoreFab":
                restoreFab();
                break;
            default:
                return;
        }
    }

    @Override
    public void onClickReminderSet(int id) {
        viewPager.setCurrentItem(1,true);
        ((ReminderListFragment)getFragmentForPosition(1)).setReminderSetById(id);
    }

    private void restoreFab() {
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        },500);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected " + position);
        selectedPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG,"onPageScrollStateChanged " + state);
        switch(state){
            case ViewPager.SCROLL_STATE_IDLE:
                fab.show();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                fab.hide();
        }
    }

    private class OnClickFab implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            fab.hide();
            FABFragment fabFrag = (FABFragment) getFragmentForPosition(selectedPage);
            if (fabFrag != null) {
                fabFrag.onClickFAB();
            } else {
            }
        }
    }

    public @Nullable Fragment getFragmentForPosition(int position)
    {
        String tag = mainFragmentPagerAdapter.makeFragmentName(
                viewPager.getId(), mainFragmentPagerAdapter.getItemId(position));
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment;
    }


}
