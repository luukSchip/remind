package com.cell0.remind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luuk on 12-8-15.
 */
public class ReminderSet extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String description;

    public ReminderSet(ReminderSet reminderSet) {
        this.id = reminderSet.getId();
        this.title = reminderSet.getTitle();
        this.description = reminderSet.getDescription();
    }

    public ReminderSet() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
