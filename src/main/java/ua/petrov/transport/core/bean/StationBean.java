package ua.petrov.transport.core.bean;

import java.sql.Time;

/**
 * Created by Владислав on 24.01.2016.
 */
public class StationBean {

    private String name;

    private Time stopTime;

    public StationBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getStopTime() {
        return stopTime;
    }

    public void setStopTime(Time stopTime) {
        this.stopTime = stopTime;
    }
}
