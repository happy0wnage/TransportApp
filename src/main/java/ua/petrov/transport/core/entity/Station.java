package ua.petrov.transport.core.entity;

import ua.petrov.transport.core.JAXB.adapter.TimeAdapter;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.annotation.NotNull;
import ua.petrov.transport.core.validator.annotation.StringNotEmpty;

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
@XmlType(name = "station")
@XmlAccessorType(XmlAccessType.FIELD)
public class Station extends Entity {

    @NotNull
    @StringNotEmpty
    @XmlElement(name = "name")
    private String name;

    @NotNull
    @XmlElement(name = "stop_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time stopTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    public Station() {
        super(1);
    }

    public Station(int id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = TimeUtil.getSqlTime(stopTime);
    }

    public long getStopTimeLong() {
        return stopTime.toLocalTime().toSecondOfDay();
    }

    public Time getStopTime() {
        return stopTime;
    }

    public void setStopTime(Time stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (name != null ? !name.equals(station.name) : station.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", stopTime=" + stopTime +
                '}';
    }
}
