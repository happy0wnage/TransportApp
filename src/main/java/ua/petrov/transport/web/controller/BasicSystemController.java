package ua.petrov.transport.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.service.arc.IArcService;
import ua.petrov.transport.service.bus.IBusService;
import ua.petrov.transport.service.route.IRouteService;
import ua.petrov.transport.service.station.IStationService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.View;

import java.util.List;

/**
 * @author Vladyslav
 */
@Controller
public class BasicSystemController extends AbstractController {

    @Autowired
    private IStationService stationService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IBusService busService;

    @Autowired
    private IArcService arcService;


    @RequestMapping(value = Constants.INDEX, method = RequestMethod.GET)
    public ModelAndView getAll() {
        ModelAndView modelAndView = createMaV(View.INDEX);

        List<Route> routeList = routeService.getAll();
        List<Bus> busList = busService.getAll();
        List<Arc> arcList = arcService.getAll();
        List<Station> stationList = stationService.getAll();

        getBusCount(busList, routeList);
        modelAndView.addObject(Entities.ROUTE, routeList);
        modelAndView.addObject(Entities.BUS, busList);
        modelAndView.addObject(Entities.ARC, arcList);
        modelAndView.addObject(Entities.STATION, stationList);
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
