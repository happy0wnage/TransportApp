/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ua.petrov.transport.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.core.util.CollectionUtil;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.service.user.IUserService;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.Message;
import ua.petrov.transport.web.controller.AbstractController;
import ua.petrov.transport.web.converter.RequestConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author Vladyslav
 */
@Controller
public class SessionController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger("loggerControl");
    private static final String INCORRECT_LOGIN_PASSWORD_VALUE = "Incorrect login/password value";

    private static final String INPUT_LOGIN = "inputLogin";
    private static final String INPUT_PASSWORD = "inputPassword";

    @Autowired
    private IUserService userService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Mapping.LOGOUT, method = RequestMethod.GET)
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute(Entities.LOGGED_USER);
        LOGGER.info("User [" + user.getLogin() + "] is logged out!");
        session.invalidate();
        return Constants.REDIRECT + Constants.INDEX;
    }

    @RequestMapping(value = Mapping.LOGIN, method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpSession session) {
        String header = getHeader(request);
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        User userBean = getUser(modelMap);
        Map<String, List<String>> errors = beanValidator.validateBean(userBean);

        ModelAndView modelAndView = createMaV();
        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.debug(errors.toString() + " occurred while login.");
            return getModelWithErrors(errors, modelAndView, header);
        }

        try {
            checkUserInDb(userBean, session);
        } catch (DBLayerException ex) {
            session.setAttribute(Message.ERROR_MESSAGE, INCORRECT_LOGIN_PASSWORD_VALUE);
            LOGGER.error(ex.getMessage());
        }
        modelAndView.setViewName(header);
        return modelAndView;
    }

    private boolean checkUserInDb(User checkedUser, HttpSession session) {
        User user = userService.getUserByLogin(checkedUser.getLogin());
        if (user.getPassword().equals(checkedUser.getPassword())) {
            session.setAttribute(Entities.LOGGED_USER, user);
            LOGGER.info("User [" + user.getLogin() + "] is logged in!");
            return true;
        } else {
            session.setAttribute(Message.ERROR_MESSAGE, INCORRECT_LOGIN_PASSWORD_VALUE);
            return false;
        }
    }

    private User getUser(ModelMap modelMap) {
        User user = new User();
        user.setLogin((String) modelMap.get(INPUT_LOGIN));
        user.setPassword((String) modelMap.get(INPUT_PASSWORD));
        return user;
    }
}
