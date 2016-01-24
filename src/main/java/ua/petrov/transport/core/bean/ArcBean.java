package ua.petrov.transport.core.bean;

import ua.petrov.transport.core.entity.Station;

import java.sql.Time;

/**
 * Created by Владислав on 24.01.2016.
 */
public class ArcBean {

    private Station fromStation;

    private Station toStation;

    private Time travelTime;

    public ArcBean() {
    }

    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    public Time getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Time travelTime) {
        this.travelTime = travelTime;
    }
}
