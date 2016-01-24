package ua.petrov.transport.core.sorter;


import ua.petrov.transport.core.entity.Route;

import java.util.Comparator;

/**
 * Created by Владислав on 15.01.2016.
 */
public class RouteSorter {

    public static final Comparator<Route> SORT_BY_FINISH_TIME = new Comparator<Route>() {

        @Override
        public int compare(Route o1, Route o2) {
            return Long.compare(o1.getLastBusTimeLong(), o2.getLastBusTimeLong());
        }
    };

    public static final Comparator<Route> SORT_BY_START_TIME = new Comparator<Route>() {

        @Override
        public int compare(Route o1, Route o2) {
            return Long.compare(o1.getFirstBusTimeLong(), o2.getFirstBusTimeLong());
        }
    };
}
