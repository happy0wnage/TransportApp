package ua.petrov.transport.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
import ua.petrov.transport.core.entity.*;
import ua.petrov.transport.db.constants.DbTables.*;
import ua.petrov.transport.db.dao.route.IRouteDAO;
import ua.petrov.transport.db.dao.station.IStationDAO;
import ua.petrov.transport.exception.DataExtractionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Владислав on 06.01.2016.
 */
@Service
public class Extractor {

    @Autowired
    private IStationDAO stationDAO;

    @Autowired
    private IRouteDAO routeDAO;

    public Arc extractArc(ResultSet resultSet) {
        Arc arc = new Arc();

        try {
            arc.setId(resultSet.getInt(ArcFields.ID_ARC));

            Station fromStation = stationDAO.getStationById(resultSet.getInt(ArcFields.FROM_STATION));
            arc.setFromStation(fromStation);

            Station toStation = stationDAO.getStationById(resultSet.getInt(ArcFields.TO_STATION));
            arc.setToStation(toStation);

            arc.setTravelTime(resultSet.getTime(ArcFields.TRAVEL_TIME));
        } catch (SQLException e) {
            throw new DataExtractionException("Arc not extracted from query result.", e);
        }
        return arc;
    }

    public Bus extractBus(ResultSet resultSet) {
        Bus bus = new Bus();

        try {
            bus.setId(resultSet.getInt(BusFields.ID_BUS));
            Route route = routeDAO.getRouteById(resultSet.getInt(BusFields.ID_ROUTE));
            bus.setRoute(route);

            bus.setSeat(resultSet.getInt(BusFields.SEAT));
        } catch (SQLException e) {
            throw new DataExtractionException("Bus not extracted from query result.", e);
        }
        return bus;
    }

    public User extractUser(ResultSet resultSet) {
        User user = new User();

        try {
            user.setId(resultSet.getInt(UserFields.ID_USER));
            user.setLogin(resultSet.getString(UserFields.LOGIN));
            user.setPassword(resultSet.getString(UserFields.PASSWORD));
        } catch (SQLException e) {
            throw new DataExtractionException("User not extracted from query result.", e);
        }
        return user;
    }

    public Route extractRoute(ResultSet resultSet) {
        Route route = new Route();
        try {
            route.setId(resultSet.getInt(RouteFields.ID_ROUTE));
            route.setRoutingNumber(resultSet.getString(RouteFields.ROUTING_NUMBER));

            List<Arc> arcList = routeDAO.getArcByIdRoute(route.getId());
            route.setStartStation(arcList.get(0).getFromStation());
            route.setEndStation(arcList.get(arcList.size() - 1).getToStation());
            route.setType();
            route.setPrice(resultSet.getDouble(RouteFields.PRICE));
            route.setDepotStopTime(resultSet.getTime(RouteFields.DEPOT_STOP_TIME));
            route.setLastBusTime(resultSet.getTime(RouteFields.LAST_BUS_TIME));
            route.setFirstBusTime(resultSet.getTime(RouteFields.FIRST_BUS_TIME));

        } catch (SQLException e) {
            throw new DataExtractionException("Route not extracted from query result.", e);
        }
        return route;
    }

    public Station extractStation(ResultSet resultSet) {
        Station station = new Station();
        try {
            station.setId(resultSet.getInt(StationFields.ID_STATION));
            station.setName(resultSet.getString(StationFields.NAME));
            station.setStopTime(resultSet.getTime(StationFields.STOP_TIME));
        } catch (SQLException e) {
            throw new DataExtractionException("Station not extracted from query result.", e);
        }
        return station;
    }

    public ResultEvent extractEvent(ResultSet resultSet) {
        ResultEvent event = new ResultEvent();
        try {
            event.setId(resultSet.getInt(ResultsFields.ID_RESULTS));
            event.setStartDate(resultSet.getTimestamp(ResultsFields.START_DATE));
            event.setEndDate(resultSet.getTimestamp(ResultsFields.END_DATE));
            event.setLoadingPercent(resultSet.getDouble(ResultsFields.LOADING_PERCENT));
            event.setRoutes(resultSet.getString(ResultsFields.ROUTES));
            event.setSatisfiedCount(resultSet.getInt(ResultsFields.SATISFIED_COUNT));
            event.setDissatisfiedCount(resultSet.getInt(ResultsFields.DISSATISFIED_COUNT));
        } catch (SQLException e) {
            throw new DataExtractionException("Event not extracted from query result.", e);
        }
        return event;
    }
}
