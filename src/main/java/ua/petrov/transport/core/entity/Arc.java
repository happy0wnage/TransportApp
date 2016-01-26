package ua.petrov.transport.core.entity;

import ua.petrov.transport.core.JAXB.adapter.TimeAdapter;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.annotation.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Time;
import java.time.LocalTime;

@XmlType(name = "arc",
        propOrder = {
                "fromStation",
                "toStation",
                "travelTime",
        })
public class Arc extends Entity implements Comparable<Arc>, ViewBean {

    @NotNull
    private Station fromStation;

    @NotNull
    private Station toStation;

    @NotNull
    private Time travelTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    public Arc() {
        super(1);
    }

    public Arc(int id) {
        super(id);
    }

    @XmlElement(name = "station_from")
    public Station getFromStation() {
        return fromStation;
    }

    public void setFromStation(Station fromStation) {
        this.fromStation = fromStation;
    }

    @XmlElement(name = "station_to")
    public Station getToStation() {
        return toStation;
    }

    public void setToStation(Station toStation) {
        this.toStation = toStation;
    }

    @XmlElement(name = "travel_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arc arc = (Arc) o;

        if (fromStation != null ? !fromStation.equals(arc.fromStation) : arc.fromStation != null) return false;
        if (toStation != null ? !toStation.equals(arc.toStation) : arc.toStation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromStation != null ? fromStation.hashCode() : 0;
        result = 31 * result + (toStation != null ? toStation.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Arc o) {
        return Long.compare(getTravelTimeLong(), o.getTravelTimeLong());
    }

    @Override
    public String toString() {
        return "Arc{" +
                "fromStation=" + fromStation +
                ", toStation=" + toStation +
                ", travelTime=" + travelTime +
                '}';
    }
}
