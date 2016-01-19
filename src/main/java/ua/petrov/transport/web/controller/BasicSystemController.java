package ua.petrov.transport.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.db.dao.arc.IArcDAO;
import ua.petrov.transport.db.dao.bus.IBusDAO;
import ua.petrov.transport.db.dao.route.IRouteDAO;
import ua.petrov.transport.db.dao.station.IStationDAO;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.View;

import java.util.List;

/**
 * @author Vladyslav
 */
@Controller
public class BasicSystemController {

    @Autowired
    private IStationDAO stationDAO;

    @Autowired
    private IRouteDAO routeDAO;

    @Autowired
    private IBusDAO busDAO;

    @Autowired
    private IArcDAO arcDAO;


    @RequestMapping(value = Constants.INDEX, method = RequestMethod.GET)
    public ModelAndView getAll() {
        ModelAndView modelAndView = new ModelAndView(View.INDEX);

        List<Route> routeList = routeDAO.getAll();
        List<Bus> busList = busDAO.getAll();
        getBusCount(busList, routeList);
        modelAndView.addObject(Entities.ROUTE, routeList);
        modelAndView.addObject(Entities.BUS, busList);
        modelAndView.addObject(Entities.ARC, arcDAO.getAll());
        modelAndView.addObject(Entities.STATION, stationDAO.getAll());
        return modelAndView;
    }

    private void getBusCount(List<Bus> buses, List<Route> routes) {
        for (Route route : routes) {
            int busCount = 0;
            for (Bus bus : buses) {
                if (route.equals(bus.getRoute())) {
                    busCount++;
                }
            }
            route.setBusCount(busCount);
        }
    }

}
