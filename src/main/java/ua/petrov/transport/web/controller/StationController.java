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
import ua.petrov.transport.core.entity.Station;
import ua.petrov.transport.db.constants.DbTables.StationFields;
import ua.petrov.transport.db.dao.arc.IArcDAO;
import ua.petrov.transport.db.dao.station.IStationDAO;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.STATION)
public class StationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationController.class);

    private static final String VALUE = "First you have to remove arc";

    @Autowired
    private IStationDAO stationDAO;

    @Autowired
    private IArcDAO arcDAO;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String addStation(HttpServletRequest request) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Station station = getStation(modelMap);
        try {
            stationDAO.add(station);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }

    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public String updateStation(HttpServletRequest request, @RequestParam(name = StationFields.ID_STATION) int id) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Station station = getStation(modelMap);
        station.setId(id);
        try {
            stationDAO.update(station);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }

    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteStation(HttpServletRequest request, HttpSession session, @RequestParam(value = StationFields.ID_STATION) int id) {
        List<Arc> arcList = arcDAO.getArcsByStationId(id);
        if (arcList.size() == 0) {
            try {
                stationDAO.delete(id);
            } catch (DBLayerException ex) {
                LOGGER.error(ex.getMessage());
            }
        } else {
            LOGGER.error(VALUE);
            session.setAttribute(Constants.ERROR_MESSAGE, VALUE);
        }
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }

    private Station getStation(ModelMap modelMap) {
        Station station = new Station();

        String name = (String) modelMap.get(StationFields.NAME);
        String stopTime = (String) modelMap.get(StationFields.STOP_TIME);
        Time stop = Time.valueOf(stopTime);

        station.setName(name);
        station.setStopTime(stop);

        return station;
    }

}
