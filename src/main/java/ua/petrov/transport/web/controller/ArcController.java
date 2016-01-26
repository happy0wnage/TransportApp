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
import ua.petrov.transport.db.constants.DbTables.ArcFields;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.exception.ValidateException;
import ua.petrov.transport.service.arc.IArcService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
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
    private static final String ILLEGAL_TIME_FORMAT = "Illegal time format";

    @Autowired
    private IArcService arcService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView addArc(HttpServletRequest request) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        ModelAndView modelAndView = createMaV(header);
        Arc arc;
        try {
            arc = getArc(modelMap);
        } catch (ValidateException ex) {
            modelAndView.addObject(Message.VALIDATION_ERRORS, ex.getMessage());
            return modelAndView;
        }
        Map<String, List<String>> errors = beanValidator.validateBean(arc);

        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString());
            return getModelWithErrors(errors, modelAndView, header);
        }
        try {
            arcService.add(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
            setErrorToModel(ex.getMessage(), modelAndView);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = Constants.DELETE, method = RequestMethod.GET)
    public String deleteArc(HttpServletRequest request, @RequestParam(value = "id_arc") int id) {
        try {
            arcService.delete(id);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }
    }

    @RequestMapping(value = Constants.UPDATE, method = RequestMethod.POST)
    public ModelAndView updateArc(HttpServletRequest request, @RequestParam(name = ArcFields.ID_ARC) int id) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        Arc arc = getArc(modelMap);
        Map<String, List<String>> errors = beanValidator.validateBean(arc);
        ModelAndView modelAndView = createMaV(header);
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.error(errors.toString() + " occurred while login.");
            return getModelWithErrors(errors, modelAndView, header);
        }
        arc.setId(id);
        try {
            arcService.update(arc);
        } catch (DBLayerException ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            return modelAndView;
        }
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
            throw new ValidateException(ILLEGAL_TIME_FORMAT);
        }

        Station stationFrom = new Station(idStationFrom);
        Station stationTo = new Station(idStationTo);

        arc.setFromStation(stationFrom);
        arc.setToStation(stationTo);
        arc.setTravelTime(travel);

        return arc;
    }
}
