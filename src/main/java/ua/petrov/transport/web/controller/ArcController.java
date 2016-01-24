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
import ua.petrov.transport.db.constants.DbTables.ArcFields;
import ua.petrov.transport.db.dao.arc.IArcDAO;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;

/**
 * Created by Владислав on 10.01.2016.
 */
@Controller
@RequestMapping(Mapping.ARC)
public class ArcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArcController.class);

    @Autowired
    private IArcDAO arcDAO;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String addArc(HttpServletRequest request) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Arc arc = getArc(modelMap);
        try {
            arcDAO.add(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteArc(HttpServletRequest request, @RequestParam(value = "id_arc") int id) {
        try {
            arcDAO.delete(id);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public String updateArc(HttpServletRequest request, @RequestParam(name = ArcFields.ID_ARC) int id) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Arc arc = getArc(modelMap);
        arc.setId(id);
        try {
            arcDAO.update(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    private Arc getArc(ModelMap modelMap) {
        Arc arc = new Arc();

        int idStationFrom = Integer.parseInt((String) modelMap.get(ArcFields.FROM_STATION));
        int idStationTo = Integer.parseInt((String) modelMap.get(ArcFields.TO_STATION));
        String travelTime = (String) modelMap.get(ArcFields.TRAVEL_TIME);

        Time travel = Time.valueOf(travelTime);
        Station stationFrom = new Station(idStationFrom);
        Station stationTo = new Station(idStationTo);

        arc.setFromStation(stationFrom);
        arc.setToStation(stationTo);
        arc.setTravelTime(travel);

        return arc;
    }
}
