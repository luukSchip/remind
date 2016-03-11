package com.cell0.remind.utils;

import com.cell0.remind.models.TimeConditionConfiguration;

import java.util.Calendar;

/**
 * Created by luuk on 10-9-2015.
 */
public class Strings {
    public static String convertTimeConditionConfigurationToString(TimeConditionConfiguration tcc){
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(tcc.getStartTime());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(tcc.getEndTime());

        return tcc.getFrequency() + " times between " +
                startCal.get(Calendar.HOUR_OF_DAY) + " and " +
                endCal.get(Calendar.HOUR_OF_DAY) + " hrs";

    }
}
