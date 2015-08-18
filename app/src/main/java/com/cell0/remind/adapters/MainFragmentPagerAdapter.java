package com.cell0.remind.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cell0.remind.fragments.ReminderListFragment;
import com.cell0.remind.fragments.ReminderSetListFragment;

/**
 * Created by luuk on 17-8-15.
 */
public class MainFragmentPagerAdapter extends CustomFragmentPagerAdapter{
    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ReminderSetListFragment.newInstance(null,null);
            case 1: default:
                return ReminderListFragment.newInstance(null,null);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Sets";
            case 1:
                return "Reminders";
            default:
                return "Something";
        }
    }
}
