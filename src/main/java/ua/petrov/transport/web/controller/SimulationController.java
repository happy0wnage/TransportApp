package ua.petrov.transport.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.core.JAXB.ParserUtil;
import ua.petrov.transport.core.JAXB.event.ResultEvent;
import ua.petrov.transport.core.JAXB.event.Results;
import ua.petrov.transport.core.JAXB.passengers.ArrivalPeriod;
import ua.petrov.transport.core.JAXB.passengers.DailyFlow;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.sorter.BusSorter;
import ua.petrov.transport.db.constants.DbTables.BusFields;
import ua.petrov.transport.db.constants.DbTables.StationFields;
import ua.petrov.transport.service.bus.IBusService;
import ua.petrov.transport.service.results.IResultsService;
import ua.petrov.transport.service.route.IRouteService;
import ua.petrov.transport.service.station.IStationService;
import ua.petrov.transport.simulation.controller.Simulation;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.ParserPath;
import ua.petrov.transport.web.Constants.View;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by Владислав on 07.01.2016.
 */
@Controller
@RequestMapping(Mapping.SIMULATION)
public class SimulationController {

    @Autowired
    private IBusService busService;

    @Autowired
    private IStationService stationService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IResultsService resultsService;

    @RequestMapping(value = Constants.VIEW, method = RequestMethod.GET)
    public ModelAndView getAll() {

        List<Bus> buses = busService.getAll();
        List<Route> routes = routeService.getAll();

        DailyFlow dailyFlow = ParserUtil.unmarshal(DailyFlow.class, ParserPath.INPUT_PASSENGERS);
        ModelAndView modelAndView = new ModelAndView(View.SIMULATION);
        modelAndView.addObject(Entities.BUS, buses);
        modelAndView.addObject(Entities.ROUTE, routes);
        modelAndView.addObject(Entities.RESULTS, resultsService.getAll());
        modelAndView.addObject(Entities.DAILY_FLOW, dailyFlow.getPeriods());

        return modelAndView;
    }

    @RequestMapping(value = Constants.START, method = RequestMethod.GET)
    public String start(@RequestParam int speedValue, HttpServletRequest request) {
        List<Bus> buses = busService.getAll();
        List<Route> routes = routeService.getAll();
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        DailyFlow dailyFlow = getFlow(modelMap);
        HttpSession session = request.getSession();
        Simulation simulation = new Simulation(buses, routes, dailyFlow);
        session.setAttribute(Entities.SIMULATION_PROCESS, simulation);
        simulation.start(LocalTime.of(22, 30).toSecondOfDay(), speedValue);
        uploadResults(simulation.getEventLogger());
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }

    @RequestMapping(value = Constants.PAUSE, method = RequestMethod.GET)
    @ResponseBody
    public List<Bus> pause(HttpSession session) {
        Simulation simulation = (Simulation) session.getAttribute(Entities.SIMULATION_PROCESS);
        List<Bus> buses = simulation.getBusesOnRoute();
        Collections.sort(buses, BusSorter.SORT_BY_TIME_TO_STATION);
        simulation.setPauseFlag();
        return buses;
    }

    @RequestMapping(value = Constants.ADD_BUS, method = RequestMethod.POST)
    @ResponseBody
    public String addBus(HttpServletRequest request, HttpSession session) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Bus bus = getBus(modelMap);
        Simulation simulation = (Simulation) session.getAttribute(Entities.SIMULATION_PROCESS);

        String message;
        if (simulation.isPauseFlag()) {
            simulation.addBus(bus);
            message = "The bus has been added to the route";
        } else {
            message = "Error to add the bus";
        }

        simulation.setPauseFlag();
        return message;
    }

    private Bus getBus(ModelMap modelMap) {
        Bus bus = new Bus();

        Route route = routeService.getRouteById(Integer.parseInt((String) modelMap.get(BusFields.ID_ROUTE)));
        Station station = stationService.getStationById(Integer.parseInt((String) modelMap.get(StationFields.ID_STATION)));
        int seat = Integer.parseInt((String) modelMap.get(BusFields.SEAT));

        bus.setRoute(route);
        bus.setCurrentStation(station);
        bus.setSeat(seat);

        return bus;
    }

    private void uploadResults(ResultEvent event) {
        resultsService.add(event);
        Results results = new Results();
        results.setEventList(resultsService.getAll());
        ParserUtil.marshal(results, ParserPath.RESULTS_XML);
    }

    private DailyFlow getFlow(ModelMap modelMap) {
        DailyFlow flow = ParserUtil.unmarshal(DailyFlow.class, ParserPath.INPUT_PASSENGERS);
        List<ArrivalPeriod> arrivalPeriods = flow.getPeriods();
        int i = 1;
        for (ArrivalPeriod arrivalPeriod : arrivalPeriods) {
            String value = (String) modelMap.get(String.valueOf(i));
            arrivalPeriod.setPassengersCount(Integer.parseInt(value));
            i++;
        }
        return flow;
    }

}
