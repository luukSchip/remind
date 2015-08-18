package com.cell0.remind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luuk on 12-8-15.
 */
public class Condition extends RealmObject {
    @PrimaryKey
    private int id;
    private String type;
    private ReminderSet reminderSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReminderSet getReminderSet() {
        return reminderSet;
    }

    public void setReminderSet(ReminderSet reminderSet) {
        this.reminderSet = reminderSet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
