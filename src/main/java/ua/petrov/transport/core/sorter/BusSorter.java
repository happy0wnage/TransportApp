package ua.petrov.transport.core.sorter;

import ua.petrov.transport.core.entity.Bus;

import java.util.Comparator;

/**
 * Created by Владислав on 10.01.2016.
 */
public class BusSorter {

    public static final Comparator<Bus> SORT_BY_ROUTE = new Comparator<Bus>() {

        @Override
        public int compare(Bus o1, Bus o2) {
            return Integer.compare(o1.getRoute().getId(), o2.getRoute().getId());
        }
    };

    public static final Comparator<Bus> SORT_BY_PRICE = new Comparator<Bus>() {
        @Override
        public int compare(Bus o1, Bus o2) {
            return Double.compare(o1.getRoute().getPrice(), o2.getRoute().getPrice());
        }
    };

    public static final Comparator<Bus> SORT_BY_TIME_TO_STATION = new Comparator<Bus>() {
        @Override
        public int compare(Bus o1, Bus o2) {
            return Long.compare(o1.getTimeToStationLong(), o2.getTimeToStationLong());
        }
    };

    public static final Comparator<Bus> SORT_BY_TRAVEL_TIME = new Comparator<Bus>() {
        @Override
        public int compare(Bus o1, Bus o2) {
            return Long.compare(o1.getTravelTimeLong(), o2.getTravelTimeLong());
        }
    };

    public static final Comparator<Bus> SORT_BY_START_TIME = new Comparator<Bus>() {
        @Override
        public int compare(Bus o1, Bus o2) {
            return Long.compare(o1.getStartTimeLong(), o2.getStartTimeLong());
        }
    };

    public static final Comparator<Bus> SORT_BY_WAITING_TIME = new Comparator<Bus>() {
        @Override
        public int compare(Bus o1, Bus o2) {
            return Long.compare(o1.getWaitingTimeLong(), o2.getWaitingTimeLong());
        }
    };


}
