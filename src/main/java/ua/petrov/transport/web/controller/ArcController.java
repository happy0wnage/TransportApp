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
import ua.petrov.transport.core.constants.CoreConsts.ErrorMsg;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Route;
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.CollectionUtil;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.db.constants.DbTables.ArcFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.ValidateException;
import ua.petrov.transport.service.arc.IArcService;
import ua.petrov.transport.service.route.IRouteService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.List;
import java.util.Map;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.ARC)
public class ArcController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArcController.class);
    public static final String ARC_IS_ALREADY_EXISTS = "Arc is already exists";
    private static final String FIRST_REMOVE_THE_ROUTES = "First, remove the routes";

    @Autowired
    private IArcService arcService;

    @Autowired
    private IRouteService routeService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView addArc(HttpServletRequest request, HttpSession session) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        ModelAndView modelAndView = createMaV(header);

        Arc arc;
        try {
            arc = getArc(modelMap);
        } catch (ValidateException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
            return modelAndView;
        }

        Map<String, List<String>> errors = beanValidator.validateBean(arc);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, session, header);
        }
        try {
            if (isArcExists(arc)) {
                LOGGER.error(ARC_IS_ALREADY_EXISTS);
                session.setAttribute(Message.ERROR_MESSAGE, ARC_IS_ALREADY_EXISTS);
                return modelAndView;
            }
            arcService.add(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        }

        return modelAndView;
    }

    private boolean isArcExists(Arc checkArc) {
        for (Arc arc : arcService.getAll()) {
            if (arc.getFromStation().getId() == checkArc.getFromStation().getId() &&
                    arc.getToStation().getId() == checkArc.getToStation().getId()) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteArc(HttpServletRequest request, HttpSession session, @RequestParam(value = "id_arc") int id) {
        String header = getHeader(request);
        Arc arc = arcService.getArcById(id);
        try {
            if (checkToDelete(arc)) {
                arcService.delete(id);
            } else {
                session.setAttribute(Message.ERROR_MESSAGE, FIRST_REMOVE_THE_ROUTES);
                return header;
            }
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        }
        return header;
    }

    private boolean checkToDelete(Arc arc) {
        List<Route> routes = routeService.getAll();
        for (Route route : routes) {
            if (route.getArcList().contains(arc)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateArc(HttpServletRequest request, HttpSession session, @RequestParam(name = ArcFields.ID_ARC) int id) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Arc arc = getArc(modelMap);
        Map<String, List<String>> errors = beanValidator.validateBean(arc);
        ModelAndView modelAndView = createMaV(header);

        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString() + " occurred while login.");
            return getModelWithErrors(errors, modelAndView, session, header);
        }

        arc.setId(id);
        try {
            arcService.update(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        }

        return modelAndView;
    }

    private Arc getArc(ModelMap modelMap) {
        Arc arc = new Arc();

        int idStationFrom = Integer.parseInt((String) modelMap.get(ArcFields.FROM_STATION));
        int idStationTo = Integer.parseInt((String) modelMap.get(ArcFields.TO_STATION));
        String travelTime = (String) modelMap.get(ArcFields.TRAVEL_TIME);

        Time travel;
        if (TimeUtil.isCorrectTime(travelTime)) {
            travel = Time.valueOf(travelTime);
        } else {
            throw new ValidateException(ErrorMsg.ILLEGAL_TIME);
        }

        Station stationFrom = new Station(idStationFrom);
        Station stationTo = new Station(idStationTo);

        arc.setFromStation(stationFrom);
        arc.setToStation(stationTo);
        arc.setTravelTime(travel);

        return arc;
    }
}
