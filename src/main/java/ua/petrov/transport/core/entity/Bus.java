package ua.petrov.transport.core.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ua.petrov.transport.core.constants.CoreConsts.Pattern;
import ua.petrov.transport.core.entity.util.Direction;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.annotation.MatchPattern;
import ua.petrov.transport.core.validator.annotation.NotNull;

import javax.xml.bind.annotation.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 08.01.2016.
 */
@XmlType(name = "bus",
        propOrder = {
                "seat",
                "route",
        })
@XmlAccessorType(XmlAccessType.FIELD)
public class Bus extends Entity {

    @NotNull
    @XmlElement(name = "route")
    private Route route;

    @NotNull
    @XmlTransient
    private Station currentStation;

    @XmlElement(name = "seat")
    @MatchPattern(pattern = Pattern.SEAT, message = "The number of seats should be in the interval  [10-35]")
    private int seat;

    @NotNull
    @XmlTransient
    private Time timeToStation = Time.valueOf(LocalTime.ofSecondOfDay(0));

    @NotNull
    @XmlTransient
    private Time travelTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    @NotNull
    @XmlTransient
    private Time waitingTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    @NotNull
    @XmlTransient
    private List<Passenger> passengerList = new ArrayList<>();

    @NotNull
    @XmlTransient
    private Direction direction;

    @NotNull
    @XmlTransient
    private Time startTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    public Bus() {
        super(1);
    }

    @JsonGetter("waitingTime")
    @JsonProperty("waitingTime")
    public long getWaitingTimeLong() {
        return waitingTime.toLocalTime().toSecondOfDay();
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = TimeUtil.getSqlTime(waitingTime);
    }

    public void addTravelTime(long travelTime) {
        this.travelTime = TimeUtil.getSqlTime(getTravelTimeLong() + travelTime);
    }

    public void minusWaitingTime(long waitingTime) {
        this.waitingTime = TimeUtil.getSqlTime(getWaitingTimeLong() - waitingTime);
    }

    public void minusTimeToStation(long timeToStation) {
        this.timeToStation = TimeUtil.getSqlTime(getTimeToStationLong() - timeToStation);
    }

    public Time getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Time waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = TimeUtil.getSqlTime(startTime);
    }

    @JsonGetter("startTime")
    @JsonProperty("startTime")
    public long getStartTimeLong() {
        return startTime.toLocalTime().toSecondOfDay();
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = TimeUtil.getSqlTime(travelTime);
    }

    @JsonGetter("travelTime")
    @JsonProperty("travelTime")
    public long getTravelTimeLong() {
        return travelTime.toLocalTime().toSecondOfDay();
    }

    public Time getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Time travelTime) {
        this.travelTime = travelTime;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setTimeToStation(long timeToStation) {
        this.timeToStation = TimeUtil.getSqlTime(timeToStation);
    }

    @JsonGetter("timeToStation")
    @JsonProperty("timeToStation")
    public long getTimeToStationLong() {
        return timeToStation.toLocalTime().toSecondOfDay();
    }

    public Time getTimeToStation() {
        return timeToStation;
    }

    public void setTimeToStation(Time timeToStation) {
        this.timeToStation = timeToStation;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void addToPassengerList(Passenger passenger) {
        passengerList.add(passenger);
    }

    public void removeFromPassengersList(Passenger passenger) {
        passengerList.remove(passenger);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bus bus = (Bus) o;

        if (startTime != null ? !startTime.equals(bus.startTime) : bus.startTime != null) return false;
        if (timeToStation != null ? !timeToStation.equals(bus.timeToStation) : bus.timeToStation != null) return false;

        return true;
    }

    public Station next(Station station) {
        List<Arc> arcList = route.getArcList();

        if (route.getType() == Type.DIRECT) {
            switch (direction) {
                case BACK:
                    if (route.getStartStation().equals(station)) {
                        direction = Direction.STRAIGHT;
                        return nextStation(station);
                    }
                    return previousStation(station);
                case STRAIGHT:
                    if (route.getEndStation().equals(station)) {
                        direction = Direction.BACK;
                        return previousStation(station);
                    }
                    return nextStation(station);
            }
        }

        for (Arc arc : arcList) {
            if (arc.getFromStation().equals(station)) {
                return arc.getToStation();
            }
        }
        return null;
    }


    private Station nextStation(Station station) {
        List<Arc> arcList = route.getArcList();
        for (Arc arc : arcList) {
            if (arc.getFromStation().equals(station)) {
                return arc.getToStation();
            }
        }
        return null;
    }

    private Station previousStation(Station station) {
        List<Arc> arcList = route.getArcList();
        for (Arc arc : arcList) {
            if (arc.getToStation().equals(station)) {
                return arc.getFromStation();
            }
        }
        return null;
    }


    @Override
    public int hashCode() {
        int result = timeToStation != null ? timeToStation.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "route=" + route +
                ", seat=" + seat +
                ", timeToStation=" + timeToStation +
                ", travelTime=" + travelTime +
                ", currentStation=" + currentStation +
                ", passengerList=" + passengerList +
                ", direction=" + direction +
                ", startTime=" + startTime +
                '}';
    }
}
