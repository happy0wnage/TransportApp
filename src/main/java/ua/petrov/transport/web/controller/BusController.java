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
import ua.petrov.transport.core.entity.Bus;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.util.CollectionUtil;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.db.constants.DbTables.BusFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.service.bus.IBusService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.BUS)
public class BusController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusController.class);

    @Autowired
    private IBusService busService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView addBus(HttpServletRequest request, HttpSession session, @RequestParam int count) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        ModelAndView modelAndView = createMaV(header);
        Bus bus = getBus(modelMap);
        Map<String, List<String>> errors = beanValidator.validateBean(bus);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, session, header);
        }
        try {
            for (int i = 0; i < count; i++) {
                busService.add(bus);
            }
        } catch (DBLayerException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
            LOGGER.error(ex.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateBus(HttpServletRequest request, HttpSession session, @RequestParam(name = BusFields.ID_BUS) int id) {
        String header = getHeader(request);
        ModelAndView modelAndView = createMaV(header);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Bus bus = getBus(modelMap);
        Map<String, List<String>> errors = beanValidator.validateBean(bus);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, session, header);
        }

        bus.setId(id);
        try {
            busService.update(bus);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        }
        return modelAndView;
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
