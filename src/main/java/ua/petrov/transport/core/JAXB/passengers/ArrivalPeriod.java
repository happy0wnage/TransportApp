package ua.petrov.transport.core.JAXB.passengers;

import ua.petrov.transport.core.JAXB.adapter.LocalTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;

@XmlType(name = "period", propOrder = {
        "timeFrom",
        "timeTo",
        "passengersCount"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrivalPeriod {

    @XmlAttribute
    private int id;

    @XmlElement(name = "time_from")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime timeFrom;

    @XmlElement(name = "time_to")
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime timeTo;

    @XmlElement(name = "passengers")
    private int passengersCount;

    public ArrivalPeriod() {
    }

    public ArrivalPeriod(LocalTime timeFrom, LocalTime timeTo, int count) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.passengersCount = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return "ArrivalPeriod{" +
                "timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", passengersCount=" + passengersCount +
                '}';
    }
}



