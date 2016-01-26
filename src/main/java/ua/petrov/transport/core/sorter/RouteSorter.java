package ua.petrov.transport.core.sorter;


import ua.petrov.transport.core.entity.Route;

import java.util.Comparator;

public class RouteSorter {

    public static final Comparator<Route> SORT_BY_FINISH_TIME = (o1, o2) -> Long.compare(o1.getLastBusTimeLong(), o2.getLastBusTimeLong());

    public static final Comparator<Route> SORT_BY_START_TIME = (o1, o2) -> Long.compare(o1.getFirstBusTimeLong(), o2.getFirstBusTimeLong());
}
