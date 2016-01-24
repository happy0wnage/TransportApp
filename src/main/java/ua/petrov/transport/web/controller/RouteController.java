package ua.petrov.transport.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.db.constants.DbTables.RouteFields;
import ua.petrov.transport.db.dao.arc.IArcDAO;
import ua.petrov.transport.db.dao.route.IRouteDAO;
import ua.petrov.transport.db.dao.station.IStationDAO;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.IncorrectRouteException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.ROUTE)
public class RouteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    private static final String VALUE = "First you have to remove the buses on the route";
    private static final String WRONG_ORDER = "Wrong station order";

    @Autowired
    private IRouteDAO routeDAO;

    @Autowired
    private IArcDAO arcDAO;

    @Autowired
    private IStationDAO stationDAO;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String addRoute(HttpServletRequest request, HttpSession session) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        try {
            Route route = getRoute(modelMap);
            routeDAO.add(route);
        } catch (DBLayerException | IncorrectRouteException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Constants.ERROR_MESSAGE, ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public String updateRoute(HttpServletRequest request, HttpSession session, @RequestParam(name = RouteFields.ID_ROUTE) int id) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        try {
            Route route = getRoute(modelMap);
            route.setId(id);
            routeDAO.update(route);
        } catch (DBLayerException | IncorrectRouteException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Constants.ERROR_MESSAGE, ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteRoute(HttpServletRequest request, HttpSession session, @RequestParam(value = "id_route") int id, @RequestParam int count) {
        if (count == 0) {
            try {
                routeDAO.delete(id);
            } catch (DBLayerException ex) {
                LOGGER.error(ex.getMessage());
            }
        } else {
            LOGGER.error(VALUE);
            session.setAttribute(Constants.ERROR_MESSAGE, VALUE);
        }
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }

    private Route getRoute(ModelMap modelMap) {
        Route route = new Route();

        String routingNumber = (String) modelMap.get(RouteFields.ROUTING_NUMBER);
        double price = Double.parseDouble((String) modelMap.get(RouteFields.PRICE));
        Time depotStopTime = Time.valueOf((String) modelMap.get(RouteFields.DEPOT_STOP_TIME));
        Time lastBusTime = Time.valueOf((String) modelMap.get(RouteFields.LAST_BUS_TIME));
        Time firstBusTime = Time.valueOf((String) modelMap.get(RouteFields.FIRST_BUS_TIME));

        List<Arc> arcList = getConfiguredArcs(modelMap, route);
        route.setArcList(arcList);

        route.setRoutingNumber(routingNumber);
        route.setPrice(price);
        route.setDepotStopTime(depotStopTime);
        route.setLastBusTime(lastBusTime);
        route.setFirstBusTime(firstBusTime);

        return route;
    }

    private List<Station> getStations(ModelMap modelMap) {
        List<Station> stations = new ArrayList<>();
        modelMap.entrySet().stream().filter(entry -> entry.getKey().contains("idst")).forEach(entry -> {
            int idStation = Integer.parseInt((String) entry.getValue());
            if (idStation != 0) {
                stations.add(stationDAO.getStationById(idStation));
            }
        });
        return stations;
    }

    private List<Arc> getConfiguredArcs(ModelMap modelMap, Route route) {
        List<Station> stations = getStations(modelMap);
        List<Arc> newArcs = new ArrayList<>();

        final int first = 0;
        final int last = stations.size() - 1;

        Station startStation = stationDAO.getStationById(stations.get(first).getId());
        Station endStation = stationDAO.getStationById(stations.get(last).getId());

        route.setStartStation(startStation);
        route.setEndStation(endStation);
        route.setType();

        for (int i = 0; i < stations.size() - 1; i++) {
            Station curr = stations.get(i);
            Station next = stations.get(i + 1);
            boolean flag = false;
            for (Arc arc : arcDAO.getAll()) {
                if (arc.getFromStation().equals(curr) && arc.getToStation().equals(next)) {
                    newArcs.add(arc);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new IncorrectRouteException(WRONG_ORDER + ".\t" + curr.getName() + " - " + next.getName());
            }
        }


        return newArcs;
    }

}
