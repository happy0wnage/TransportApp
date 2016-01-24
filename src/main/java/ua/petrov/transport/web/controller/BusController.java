package ua.petrov.transport.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.db.constants.DbTables.BusFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.service.bus.IBusService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.BUS)
public class BusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusController.class);

    @Autowired
    private IBusService busService;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String addBus(HttpServletRequest request, @RequestParam int count) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Bus bus = getBus(modelMap);
        try {
            for (int i = 0; i < count; i++) {
                busService.add(bus);
            }
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public String updateBus(HttpServletRequest request, @RequestParam(name = BusFields.ID_BUS) int id) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Bus bus = getBus(modelMap);
        bus.setId(id);
        try {
            busService.update(bus);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteBus(HttpServletRequest request, @RequestParam(value = BusFields.ID_BUS) int id) {
        try {
            busService.delete(id);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    private Bus getBus(ModelMap modelMap) {
        Bus bus = new Bus();

        int idRoute = Integer.parseInt((String) modelMap.get(BusFields.ID_ROUTE));
        int seats = Integer.parseInt((String) modelMap.get(BusFields.SEAT));

        Route route = new Route(idRoute);
        bus.setRoute(route);
        bus.setSeat(seats);

        return bus;
    }
}
