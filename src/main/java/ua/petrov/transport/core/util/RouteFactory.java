package ua.petrov.transport.core.util;

import ua.petrov.transport.core.entity.*;
import ua.petrov.transport.core.entity.util.Direction;
import ua.petrov.transport.core.sorter.BusSorter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Владислав on 08.01.2016.
 */
public class RouteFactory {

    public static List<Bus> fillBuses(List<Bus> allBuses, List<Route> allRoutes) {
        List<Bus> buses = new ArrayList<>();
        long breakTime;
        for (Route route : allRoutes) {
            long busTime = route.getFirstBusTimeLong();
            breakTime = route.getDepotStopTimeLong();
            for (Bus bus : allBuses) {
                Route route1 = bus.getRoute();
                boolean flag = route1.equals(route);

                if (bus.getRoute().equals(route)) {
                    bus.getRoute().setArcList(route.getArcList());
                    bus.setCurrentStation(route.getStartStation());
                    bus.setStartTime(busTime);
                    bus.setDirection(Direction.STRAIGHT);
                    busTime += breakTime;
                    buses.add(bus);
                }
            }
        }
        Collections.sort(buses, BusSorter.SORT_BY_START_TIME);
        return buses;
    }

    public static List<Double> getPrices(List<Route> routes) {
        return routes.stream().map(Route::getPrice).collect(Collectors.toList());
    }

    public static List<Route> getRouteByStation(List<Route> routes, Station station) {
        Set<Route> routeSet = new HashSet<>();
        for (Route route : routes) {
            List<Arc> arcList = route.getArcList();

            for (Arc arc : arcList) {
                switch (route.getType()) {
                    case DIRECT:
                        if (arc.getToStation().equals(station) || arc.getFromStation().equals(station)) {
                            routeSet.add(route);
                        }
                        break;
                    case CIRCLE:
                        if (arc.getFromStation().equals(station)) {
                            routeSet.add(route);
                        }
                        break;
                }
            }
        }
        return new ArrayList<>(routeSet);
    }

    public static List<Route> routesByTwoStations(List<Route> routes, Station stationFrom, Station stationTo, Direction direction) {
        List<Route> routeList = new ArrayList<>();

        for (Route route : routes) {
            boolean flag = false;
            for (Arc arc : route.getArcList()) {
                if (route.getType().equals(Type.CIRCLE)) {
                    if (arc.getFromStation().equals(stationFrom)) flag = true;
                    if (arc.getToStation().equals(stationTo) && flag) {
                        routeList.add(route);
                        break;
                    }
                } else if (route.getType().equals(Type.DIRECT)) {
                    if (direction.equals(Direction.STRAIGHT)) {
                        if (arc.getFromStation().equals(stationFrom)) flag = true;
                        if (arc.getToStation().equals(stationTo) && flag) {
                            routeList.add(route);
                            break;
                        }
                    } else {
                        if (arc.getFromStation().equals(stationTo)) flag = true;
                        if (arc.getToStation().equals(stationFrom) && flag) {
                            routeList.add(route);
                            break;
                        }
                    }
                }
            }
        }
        return routeList;
    }

    public static String printRoutingNumbers(List<Route> routes) {
        StringBuilder str = new StringBuilder();
        for (Route route : routes) {
            str.append(route.getRoutingNumber()).append(", ");
        }
        str.delete(str.length() - 2, str.length() - 1);
        return str.toString();
    }
}
