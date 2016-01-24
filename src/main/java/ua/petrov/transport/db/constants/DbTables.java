package ua.petrov.transport.db.constants;

/**
 * Created by ��������� on 25 ��� 2015 �..
 */
public class DbTables {

    public class UserFields {
        public static final String ID_USER = "id_user";
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
    }

    public class ArcFields {
        public static final String ID_ARC = "id_arc";
        public static final String FROM_STATION = "id_station_from";
        public static final String TO_STATION = "id_station_to";
        public static final String TRAVEL_TIME = "travel_time";
    }

    public class BusFields {
        public static final String ID_BUS = "id_bus";
        public static final String ID_ROUTE = "id_route";
        public static final String SEAT = "seat";
    }

    public class RouteFields {
        public static final String ID_ROUTE = "id_route";
        public static final String ROUTING_NUMBER = "routing_number";
        public static final String PRICE = "price";
        public static final String DEPOT_STOP_TIME = "depot_stop_time";
        public static final String LAST_BUS_TIME = "last_bus_time";
        public static final String FIRST_BUS_TIME = "first_bus_time";
        public static final String ID_START_STATION = "id_start_station";
        public static final String ID_END_STATION = "id_end_station";
    }

    public class ResultsFields {
        public static final String ID_RESULTS = "id_results";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String LOADING_PERCENT = "loading_percent";
        public static final String ROUTES = "routes";
        public static final String SATISFIED_COUNT = "satisfied_count";
        public static final String DISSATISFIED_COUNT = "dissatisfied_count";
    }

    public class StationFields {
        public static final String ID_STATION = "id_station";
        public static final String NAME = "name";
        public static final String STOP_TIME = "stop_time";

    }
}
