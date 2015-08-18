package com.cell0.remind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reminder extends RealmObject {
    @PrimaryKey
    private int id;
    private String text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
