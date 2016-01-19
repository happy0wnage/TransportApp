package ua.petrov.transport.core.entity;

import ua.petrov.transport.core.JAXB.adapter.TimeAdapter;
import ua.petrov.transport.core.util.TimeUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Created by Владислав on 08.01.2016.
 */
@XmlType(name = "arc",
        propOrder = {
                "fromStation",
                "toStation",
                "travelTime",
        })
@XmlAccessorType(XmlAccessType.FIELD)
public class Arc extends Entity implements Comparable<Arc>{

    @XmlElement(name = "station_from")
    private Station fromStation;

    @XmlElement(name = "station_to")
    private Station toStation;

    @XmlElement(name = "travel_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time travelTime;

    {
        travelTime = Time.valueOf(LocalTime.ofSecondOfDay(0));
    }

    public Arc() {
        super(1);
    }

    public Arc(int id) {
        super(id);
    }

    public Arc(Station from, Station to) {
        super(1);
        this.fromStation = from;
        this.toStation = to;
    }

    public Arc(Station from, Station to, Time travelTime) {
        super(1);
        this.fromStation = from;
        this.toStation = to;
        this.travelTime = travelTime;
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

    public long getTravelTimeLong() {
        return travelTime.toLocalTime().toSecondOfDay();
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = TimeUtil.getSqlTime(travelTime);
    }

    @Override
    public String toString() {
        return "Arc{" +
                "fromStation=" + fromStation +
                ", toStation=" + toStation +
                ", travelTime=" + travelTime +
                '}';
    }

    @Override
    public int compareTo(Arc o) {
        return Long.compare(getTravelTimeLong(), o.getTravelTimeLong());
    }
}
