package ua.petrov.transport.core.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ua.petrov.transport.core.JAXB.adapter.TimeAdapter;
import ua.petrov.transport.core.constants.CoreConsts.Pattern;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.annotation.MatchPattern;
import ua.petrov.transport.core.validator.annotation.NotNull;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Владислав on 08.01.2016.
 */
@XmlType(name = "route",
        propOrder = {
                "startStation",
                "endStation",
                "type",
                "routingNumber",
                "price",
                "depotStopTime",
                "firstBusTime",
                "lastBusTime",
                "arcList"
        })
@XmlAccessorType(XmlAccessType.FIELD)
public class Route extends Entity {

    private static final String PRICE_MESSAGE = "Price must be greater than 0";
    private static final String BUS_COUNT_MESSAGE = "Bus count must be greater than 0";

    @NotNull
    @XmlElement(name = "routing_number")
    private String routingNumber;

    @NotNull
    @XmlElement(name = "start_station")
    private Station startStation;

    @NotNull
    @XmlElement(name = "end_station")
    private Station endStation;

    @MatchPattern(pattern = Pattern.PRICE, message = PRICE_MESSAGE)
    @XmlElement(name = "price")
    private double price;

    @NotNull
    @XmlTransient
    private Type type;

    @NotNull
    @XmlElement(name = "depot_stop_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time depotStopTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    @NotNull
    @XmlElementWrapper(name = "arcs")
    @XmlElement(name = "arc")
    private List<Arc> arcList = new ArrayList<>();

    @NotNull
    @XmlTransient
    private List<Station> stations = new ArrayList<>();

    @MatchPattern(pattern = Pattern.GREATER_ZERO, message = BUS_COUNT_MESSAGE)
    @XmlTransient
    private int busCount;

    @NotNull
    @XmlElement(name = "last_bus_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time lastBusTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    @NotNull
    @XmlElement(name = "first_bus_time")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    private Time firstBusTime = Time.valueOf(LocalTime.ofSecondOfDay(0));

    public Route(int id) {
        super(id);
    }

    public Route() {
        super(1);
    }

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }

    public long getLastBusTimeLong() {
        return lastBusTime.toLocalTime().toSecondOfDay();
    }

    public void setLasBusTime(long lastBusTime) {
        this.lastBusTime = TimeUtil.getSqlTime(lastBusTime);
    }

    public Time getLastBusTime() {
        return lastBusTime;
    }

    public void setLastBusTime(Time lastBusTime) {
        this.lastBusTime = lastBusTime;
    }

    @JsonGetter("firstBusTime")
    @JsonProperty("firstBusTime")
    public long getFirstBusTimeLong() {
        return firstBusTime.toLocalTime().toSecondOfDay();
    }

    public void setFirstBusTime(long firstBusTime) {
        this.firstBusTime = TimeUtil.getSqlTime(firstBusTime);
    }

    public Time getFirstBusTime() {
        return firstBusTime;
    }

    public void setFirstBusTime(Time firstBusTime) {
        this.firstBusTime = firstBusTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getDepotStopTimeLong() {
        return depotStopTime.toLocalTime().toSecondOfDay();
    }

    public void setDepotStopTime(long depotStopTime) {
        this.depotStopTime = TimeUtil.getSqlTime(depotStopTime);
    }

    public Time getDepotStopTime() {
        return depotStopTime;
    }

    public void setDepotStopTime(Time depotStopTime) {
        this.depotStopTime = depotStopTime;
    }

    public List<Arc> getArcList() {
        return arcList;
    }

    public void setArcList(List<Arc> arcList) {
        this.arcList = arcList;
    }

    public Type getType() {
        return type;
    }

    public void setType() {
        if (startStation.equals(endStation)) {
            type = Type.CIRCLE;
        } else {
            type = Type.DIRECT;
        }
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        Set<Station> stationSet = arcList.stream().map(Arc::getFromStation).collect(Collectors.toSet());
        stationSet.add(arcList.get(arcList.size() - 1).getToStation());
        return new ArrayList<>(stationSet);
    }

    public Arc getArcBetweenTwoStations(Station from, Station to) {
        for (Arc arc : arcList) {
            if (arc.getFromStation().equals(from) && arc.getToStation().equals(to)) {
                return arc;
            } else if (arc.getFromStation().equals(to) && arc.getToStation().equals(from)) {
                return arc;
            }
        }
        return null;
    }

    public List<Arc> getArcsForStationForCycle(Station stationFrom) {
        List<Arc> arcs = new ArrayList<>();

        Station current = stationFrom;
        for (Arc arc : arcList) {
            if (current.equals(arc.getFromStation())) {
                current = arc.getToStation();
                arcs.add(arc);
                if (current.equals(endStation)) {
                    break;
                }
            }
        }
        return arcs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (Integer.compare(route.getId(), getId()) != 0) return false;
        if (Double.compare(route.price, price) != 0) return false;
        if (depotStopTime != null ? !depotStopTime.equals(route.depotStopTime) : route.depotStopTime != null)
            return false;
        if (firstBusTime != null ? !firstBusTime.equals(route.firstBusTime) : route.firstBusTime != null) return false;
        if (lastBusTime != null ? !lastBusTime.equals(route.lastBusTime) : route.lastBusTime != null) return false;
        if (routingNumber != null ? !routingNumber.equals(route.routingNumber) : route.routingNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = routingNumber != null ? routingNumber.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (depotStopTime != null ? depotStopTime.hashCode() : 0);
        result = 31 * result + (lastBusTime != null ? lastBusTime.hashCode() : 0);
        result = 31 * result + (firstBusTime != null ? firstBusTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routingNumber='" + routingNumber + '\'' +
                ", startStation=" + startStation +
                ", endStation=" + endStation +
                ", price=" + price +
                ", type=" + type +
                ", depotStopTime=" + depotStopTime +
                ", arcList=" + arcList +
                ", stations=" + stations +
                ", busCount=" + busCount +
                ", lastBusTime=" + lastBusTime +
                ", firstBusTime=" + firstBusTime +
                '}';
    }
}
