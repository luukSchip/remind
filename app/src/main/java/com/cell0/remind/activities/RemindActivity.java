package com.cell0.remind.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.cell0.remind.R;
import com.cell0.remind.fragments.ReminderSetFragment;
import com.cell0.remind.fragments.ReminderSetListFragment;
import com.cell0.remind.interfaces.OnAppBarResizedListener;
import com.cell0.remind.views.CustomFAB;


public class RemindActivity extends ActionBarActivity
        implements ReminderSetListFragment.OnFragmentInteractionListener,
        ReminderSetFragment.OnFragmentInteractionListener {

    private static final String TAG = "RemindActivity";
    private static final String TAG_FAB_FRAGMENT = "FAB_FRAGMENT";
    private Toolbar toolbar;
    private CustomFAB fab;
    private AppBarLayout appBar;
    private FrameLayout fragmentContainer;
    private OnAppBarResizedListener onAppBarResizedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        fab = (CustomFAB) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        toolbar.setTitle("ReMind");
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null)
                return;

            ReminderSetListFragment frag = new ReminderSetListFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, frag, TAG_FAB_FRAGMENT).commit();
        }
    }

    @Override
    public void onFragmentInteraction(String id) {
        switch(id){
            case "restoreFab":
                restoreFab();
                break;
            case "back":
                onBackPressed();
                break;
            case ReminderSetListFragment.FRAGMENT_INTERACTION_NEW_REMINDER_SET:
                ReminderSetFragment frag = ReminderSetFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag, TAG_FAB_FRAGMENT)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null).commit();
                break;
            default:
                return;
        }
    }

    @Override
    public void onSetToolbarMenu(Integer menuResId, Integer navigationIconResId,
                Toolbar.OnMenuItemClickListener onMenuItemClickListener,
                View.OnClickListener navigationOnClickListener) {
        toolbar.getMenu().clear();
        if(menuResId != null){
            toolbar.inflateMenu(menuResId);
        }
        if(navigationIconResId == null){
            toolbar.setNavigationIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }else{
            toolbar.setNavigationIcon(navigationIconResId);
        }
        toolbar.setNavigationOnClickListener(navigationOnClickListener);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    public void onSetFabListener(View.OnClickListener fabListener) {
        fab.setOnClickListener(fabListener);
    }

    @Override
    public void addViewToAppBar(View v) {
        removeAddedViewFromAppBar();
        v.setTag("addedToAppBar");
        appBar.addView(v);
/*
        //adjust fragmentContainer top margin
        appBar.measure(0,0);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,appBar.getMeasuredHeight(),
                layoutParams.rightMargin,layoutParams.bottomMargin);
        fragmentContainer.setLayoutParams(layoutParams);*/
        /*if(onAppBarResizedListener != null){
            onAppBarResizedListener.onAppBarResized(appBar);
        }*/
    }

    private void removeAddedViewFromAppBar() {
        View viewToRemove = appBar.findViewWithTag("addedToAppBar");
        if(viewToRemove != null){
            appBar.removeView(viewToRemove);
        }
    }

    @Override
    public void addViewToAppBar(int resId) {
        View v = View.inflate(this,resId,null);
        addViewToAppBar(v);
    }

    @Override
    public void setAppBarResizedListener(OnAppBarResizedListener onAppBarResizedListener) {
        this.onAppBarResizedListener = onAppBarResizedListener;
    }

    @Override
    public void onClickReminderSet(int id) {
        ReminderSetFragment frag = ReminderSetFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frag, TAG_FAB_FRAGMENT)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();
    }


    private void restoreFab() {
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 500);
    }


}
