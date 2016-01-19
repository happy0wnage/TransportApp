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
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.ROUTE)
public class RouteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    private static final String ARC_LIST = "arcList";
    private static final String VALUE = "First you have to remove the buses on the route";

    @Autowired
    private IRouteDAO routeDAO;

    @Autowired
    private IArcDAO arcDAO;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String addRoute(HttpServletRequest request) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Route route = getRoute(modelMap, request);
        try {
            routeDAO.add(route);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
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

    private Route getRoute(ModelMap modelMap, HttpServletRequest request) {
        Route route = new Route();

        String routingNumber = (String) modelMap.get(RouteFields.ROUTING_NUMBER);
        double price = Double.parseDouble((String) modelMap.get(RouteFields.PRICE));
        int minutes = Integer.parseInt((String) modelMap.get(RouteFields.DEPOT_STOP_TIME));
        Time depotStopTime = Time.valueOf(LocalTime.of(0, minutes));
        String[] idArcs = request.getParameterValues(ARC_LIST);

        List<Arc> arcList = new ArrayList<>();
        for (String id : idArcs) {
            Arc arc = arcDAO.getArcById(Integer.parseInt(id));
            arcList.add(arc);
        }

        Time lastBusTime = Time.valueOf((String) modelMap.get(RouteFields.LAST_BUS_TIME));
        Time firstBusTime = Time.valueOf((String) modelMap.get(RouteFields.FIRST_BUS_TIME));

        Station start = arcList.get(0).getFromStation();
        Station end = arcList.get(arcList.size() - 1).getToStation();

        route.setRoutingNumber(routingNumber);
        route.setStartStation(start);
        route.setEndStation(end);
        route.setPrice(price);
        route.setType();
        route.setDepotStopTime(depotStopTime);
        route.setLastBusTime(lastBusTime);
        route.setFirstBusTime(firstBusTime);
        route.setArcList(arcList);

        return route;
    }
}
