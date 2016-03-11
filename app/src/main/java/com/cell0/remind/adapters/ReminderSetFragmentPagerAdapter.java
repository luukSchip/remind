package com.cell0.remind.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cell0.remind.fragments.ConditionFragment;
import com.cell0.remind.fragments.ReminderFragment;

/**
 * Created by luuk on 8-9-2015.
 */
public class ReminderSetFragmentPagerAdapter extends FragmentPagerAdapter{
    private Integer reminderSetId;

    public ReminderSetFragmentPagerAdapter(FragmentManager fm, Integer reminderSetId) {
        super(fm);
        this.reminderSetId = reminderSetId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ReminderFragment.newInstance(reminderSetId);
            case 1:
                return ConditionFragment.newInstance(reminderSetId);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Reminders";
            case 1:
                return "Conditions";
            default:
                return super.getPageTitle(position);
        }
    }
}
