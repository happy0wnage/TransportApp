package ua.petrov.transport.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.CollectionUtil;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.db.constants.DbTables.RouteFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.IncorrectRouteException;
import ua.petrov.transport.service.arc.IArcService;
import ua.petrov.transport.service.route.IRouteService;
import ua.petrov.transport.service.station.IStationService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.ROUTE)
public class RouteController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    private static final String VALUE = "First you have to remove the buses on the route";
    private static final String WRONG_ORDER = "Wrong station order";

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IArcService arcService;

    @Autowired
    private IStationService stationService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView addRoute(HttpServletRequest request, HttpSession session) {
        String header = getHeader(request);
        ModelAndView modelAndView = createMaV(header);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Route route = getRoute(modelMap);

        Map<String, List<String>> errors = beanValidator.validateBean(route);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, header);
        }

        try {
            routeService.add(route);
        } catch (DBLayerException | IncorrectRouteException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateRoute(HttpServletRequest request, HttpSession session, @RequestParam(name = RouteFields.ID_ROUTE) int id) {
        String header = getHeader(request);
        ModelAndView modelAndView = createMaV(header);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);

        Route route;
        try {
            route = getRoute(modelMap);
        } catch (IncorrectRouteException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
            return modelAndView;
        }

        Map<String, List<String>> errors = beanValidator.validateBean(route);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, header);
        }
        try {
            route.setId(id);
            routeService.update(route);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteRoute(HttpServletRequest request, HttpSession session, @RequestParam(value = "id_route") int id, @RequestParam int count) {
        if (count == 0) {
            try {
                routeService.delete(id);
            } catch (DBLayerException ex) {
                LOGGER.error(ex.getMessage());
            }
        } else {
            LOGGER.error(VALUE);
            session.setAttribute(Message.ERROR_MESSAGE, VALUE);
        }
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }

    private Route getRoute(ModelMap modelMap) throws IncorrectRouteException {
        Route route = new Route();

        String routingNumber = (String) modelMap.get(RouteFields.ROUTING_NUMBER);
        double price = Double.parseDouble((String) modelMap.get(RouteFields.PRICE));
        Time depotStopTime = Time.valueOf((String) modelMap.get(RouteFields.DEPOT_STOP_TIME));
        Time lastBusTime = Time.valueOf((String) modelMap.get(RouteFields.LAST_BUS_TIME));
        Time firstBusTime = Time.valueOf((String) modelMap.get(RouteFields.FIRST_BUS_TIME));

        List<Arc> arcList;
        try {
            arcList = getConfiguredArcs(modelMap, route);
        } catch (IncorrectRouteException ex) {
            LOGGER.error(ex.getMessage());
            return null;
        }
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
                stations.add(stationService.getStationById(idStation));
            }
        });
        return stations;
    }

    private List<Arc> getConfiguredArcs(ModelMap modelMap, Route route) {
        List<Station> stations = getStations(modelMap);
        List<Arc> newArcs = new ArrayList<>();

        final int first = 0;
        final int last = stations.size() - 1;

        Station startStation = stationService.getStationById(stations.get(first).getId());
        Station endStation = stationService.getStationById(stations.get(last).getId());

        route.setStartStation(startStation);
        route.setEndStation(endStation);
        route.setType();

        for (int i = 0; i < stations.size() - 1; i++) {
            Station curr = stations.get(i);
            Station next = stations.get(i + 1);
            boolean flag = false;
            for (Arc arc : arcService.getAll()) {
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
