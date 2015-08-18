package com.cell0.remind.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luuk on 12-8-15.
 */
public class LocationConditionConfiguration extends RealmObject {
    @PrimaryKey
    private int id;
    private long latitude;
    private long longitue;
    private int radius;
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitue() {
        return longitue;
    }

    public void setLongitue(long longitue) {
        this.longitue = longitue;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
