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
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.core.util.CollectionUtil;
import ua.petrov.transport.core.util.TimeUtil;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.db.constants.DbTables.StationFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.IllegalTimeValueException;
import ua.petrov.transport.service.arc.IArcService;
import ua.petrov.transport.service.station.IStationService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.STATION)
public class StationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationController.class);

    private static final String VALUE = "First you have to remove arc";
    private static final String ILLEGAL_TIME_VALUE = "Illegal time value";
    private static final String STOP_TIME_IS_TOO_BIG = "Stop time is too big. Max value - 00:02:00";

    @Autowired
    private IStationService stationService;

    @Autowired
    private IArcService arcService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView addStation(HttpServletRequest request, HttpSession session) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        ModelAndView modelAndView = createMaV(header);

        Station station;
        try {
            station = getStation(modelMap);
        } catch (IllegalTimeValueException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
            return modelAndView;
        }

        Map<String, List<String>> errors = beanValidator.validateBean(station);

        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, session, header);
        }

        try {
            stationService.add(station);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
        }
        return modelAndView;

    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateStation(HttpServletRequest request, HttpSession session, @RequestParam(name = StationFields.ID_STATION) int id) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        ModelAndView modelAndView = createMaV(header);

        Station station;
        try {
            station = getStation(modelMap);
        } catch (IllegalTimeValueException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, ex.getMessage());
            return modelAndView;
        }

        Map<String, List<String>> errors = beanValidator.validateBean(station);

        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, session, header);
        }

        station.setId(id);

        try {
            stationService.update(station);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        }
        return modelAndView;

    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteStation(HttpServletRequest request, HttpSession session, @RequestParam(value = StationFields.ID_STATION) int id) {
        List<Arc> arcList = arcService.getArcsByStationId(id);
        if (arcList.size() == 0) {
            try {
                stationService.delete(id);
            } catch (DBLayerException ex) {
                LOGGER.error(ex.getMessage());
            }
        } else {
            LOGGER.error(VALUE);
            session.setAttribute(Message.ERROR_MESSAGE, VALUE);
        }
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }

    private Station getStation(ModelMap modelMap) {
        Station station = new Station();

        String name = (String) modelMap.get(StationFields.NAME);
        String stopTime = (String) modelMap.get(StationFields.STOP_TIME);
        Time stop;

        if (TimeUtil.isCorrectTime(stopTime)) {
            stop = Time.valueOf(stopTime);
            if (stop.after(Time.valueOf(LocalTime.of(0, 2)))) {
                throw new IllegalTimeValueException(STOP_TIME_IS_TOO_BIG);
            }
        } else {
            throw new IllegalTimeValueException(ILLEGAL_TIME_VALUE);
        }

        station.setName(name);
        station.setStopTime(stop);

        return station;
    }

}
