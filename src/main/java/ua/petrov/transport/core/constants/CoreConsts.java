package ua.petrov.transport.core.constants;

/**
 * @author Vladyslav
 */
public class CoreConsts {

    public static class Pattern {
        private static final String INTERNATIONAL_SYMBOL = "[А-ЯA-Zа-яa-z]";

        public static final String PASS = "^[a-zA-Z0-9\\_\\-\\.]{3,16}$";
        public static final String SEAT = "^1[0-9]$|^2[0-9]$|^3[0-5]$";
        public static final String GREATER_ZERO = "^[1-9][0-9]*$";
        public static final String PRICE = "^\\d{1,2}.\\d{1,2}$";
        public static final String STRING_VAL = "^" + INTERNATIONAL_SYMBOL + "{1,20}$";
    }

    public static class ErrorMsg {

        public static final String EMPTY_STATION = "Station can not be empty";
        public static final String EMPTY_DESTINATION = "Destination can not be empty";
        public static final String EMPTY_TRAVEL_TIME = "Travel time can not be empty";
        public static final String EMPTY_ROUTE = "Route can not be empty";
        public static final String EMPTY_TIME_TO_STATION = "Time to station can not be empty";
        public static final String EMPTY_WAITING_TIME = "Waiting time can not be empty";
        public static final String EMPTY_PASSENGER = "Passenger list can not be empty";
        public static final String EMPTY_START_TIME = "Start time can not be empty";
        public static final String EMPTY_ROUTING_NUMBER = "Routing number can not be empty";
        public static final String EMPTY_START_STATION = "Start station can not be empty";
        public static final String EMPTY_END_STATION = "End station can not be empty";
        public static final String EMPTY_TYPE = "Type can not be empty";
        public static final String EMPTY_STOP_TIME = "Stop time can not be empty";
        public static final String EMPTY_ARC_LIST = "Arc list can not be empty";
        public static final String EMPTY_STATION_LIST = "Stations list can not be empty";
        public static final String EMPTY_LAST_BUS_TIME = "Time of the last bus can not be empty";
        public static final String EMPTY_FIRST_BUS_TIME = "Time of the first bus can not be empty";
        public static final String EMPTY_STATION_NAME = "Station name can not be empty";
        public static final String ILLEGAL_TIME = "Illegal time value";

        public static final String SEAT_NUMBER = "The number of seats should be in the interval  [10-35]";

        public static final String PRICE_MESSAGE = "Price must be greater than 0";
        public static final String BUS_COUNT_MESSAGE = "Bus count must be greater than 0";

        public static final String LOGIN = "Incorrect login value";
        public static final String PASSWORD = "Incorrect password value";
    }
}
