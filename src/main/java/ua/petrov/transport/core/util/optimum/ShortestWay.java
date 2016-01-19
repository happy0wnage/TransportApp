package ua.petrov.transport.core.util.optimum;

import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Владислав on 10.01.2016.
 */
public class ShortestWay {

    private List<Route> routeList;
    private List<ArcDijkstra> allArc;

    private int n; // количество перегонов
    private int m; // количество вершин

    private StationDijkstra startStation;
    private StationDijkstra endStation;

    public ShortestWay(List<Route> routeList, Station startStation, Station endStation) {
        this.startStation = (StationDijkstra) startStation;
        this.startStation.setWeight(0);
        this.endStation = (StationDijkstra) endStation;
        this.routeList = routeList;

        this.allArc = getAllArcs(routeList);

        n = allArc.size();
        m = n + 1;
    }

    public List<Arc> getShortestWay() {
        List<Arc> optimalWay = new ArrayList<>();

        return optimalWay;
    }



    private List<ArcDijkstra> getAllArcs(List<Route> routes) {
        Set<ArcDijkstra> arcSet = new HashSet<>();
        for (Route route : routes) {
            arcSet.addAll(getArcsDijkstra(route.getArcList()));
        }
        return new ArrayList<>(arcSet);
    }

    private List<ArcDijkstra> getArcsDijkstra(List<Arc> arcs) {
        return arcs.stream().map(this::getArcDijkstra).collect(Collectors.toList());
    }

    private ArcDijkstra getArcDijkstra(Arc arc) {
        ArcDijkstra arcDijkstra = new ArcDijkstra();
        arcDijkstra.setFromStation(getStationDijkstra(arc.getFromStation()));
        arcDijkstra.setToStation(getStationDijkstra(arc.getToStation()));
        arcDijkstra.setTravelTime(arc.getTravelTime().toLocalTime().toSecondOfDay());
        return arcDijkstra;
    }

    private StationDijkstra getStationDijkstra(Station station) {
        StationDijkstra stationDijkstra = new StationDijkstra();
        stationDijkstra.setId(station.getId());
        stationDijkstra.setName(station.getName());
        stationDijkstra.setStopTime(station.getStopTime());
        stationDijkstra.setWeight(Integer.MAX_VALUE);
        return stationDijkstra;
    }

    private class ArcDijkstra {

        private StationDijkstra fromStation;

        private StationDijkstra toStation;

        private long travelTime;

        public StationDijkstra getFromStation() {
            return fromStation;
        }

        public void setFromStation(StationDijkstra fromStation) {
            this.fromStation = fromStation;
        }

        public StationDijkstra getToStation() {
            return toStation;
        }

        public void setToStation(StationDijkstra toStation) {
            this.toStation = toStation;
        }

        public long getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(long travelTime) {
            this.travelTime = travelTime;
        }
    }

    private class StationDijkstra extends Station implements Comparable<StationDijkstra> {

        private int weight = Integer.MAX_VALUE;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public int compareTo(StationDijkstra o) {
            return Integer.compare(weight, o.getWeight());
        }
    }
}
