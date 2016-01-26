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
import ua.petrov.transport.web.Constants.View;
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
@RequestMapping(value = Mapping.USER)
public class UserController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String USER_IS_ALREADY_EXISTS = "User is already exists";
    private static final String INPUT_LOGIN = "inputLogin";
    private static final String INPUT_PASSWORD = "inputPassword";

    @Autowired
    private IUserService userService;

    @Autowired
    private IBeanValidator beanValidator;

    @RequestMapping(value = Mapping.REGISTER, method = RequestMethod.GET)
    public String getRegisterPage() {
        return View.REGISTER;
    }

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public ModelAndView createNew(HttpServletRequest request, HttpSession session) {
        ModelMap modelMap = RequestConverter.convertToModelMap(request);
        User user = getUser(modelMap);
        String redirectPage = Constants.REDIRECT + Constants.INDEX;

        Map<String, List<String>> errors = beanValidator.validateBean(user);
        ModelAndView modelAndView = createMaV();

        if (CollectionUtil.isNotEmpty(errors)) {
            LOGGER.debug(errors.toString() + " occurred while registration.");
            return getModelWithErrors(errors, modelAndView, session, redirectPage);
        }

        List<User> users = userService.getAll();
        if (users.contains(user)) {
            session.setAttribute(Message.ERROR_MESSAGE, USER_IS_ALREADY_EXISTS);
            return getModelToTheSamePage(modelAndView, request);
        }

        try {
            user.setId(userService.add(user));
            session.setAttribute(Entities.LOGGED_USER, user);
            LOGGER.debug(user.getLogin() + " registered.");
        } catch (DBLayerException e) {
            LOGGER.error(e.getMessage());
        }
        modelAndView.setViewName(redirectPage);
        return modelAndView;
    }

    private User getUser(ModelMap modelMap) {
        User user = new User();
        user.setLogin((String)modelMap.get(INPUT_LOGIN));
        user.setPassword((String) modelMap.get(INPUT_PASSWORD));
        return user;
    }
}
