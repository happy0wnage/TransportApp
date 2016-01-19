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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.db.dao.user.IUserDAO;
import ua.petrov.transport.db.validator.EntityValidator;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.Mapping;
import ua.petrov.transport.web.Constants.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Vladyslav
 */
@Controller
@RequestMapping(value = Mapping.USER)
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String USER_IS_ALREADY_EXISTS = "User is already exists";
    private static final String INCORRECT_LOGIN_PASSWORD_VALUE = "Incorrect login/password value";

    @Autowired
    private IUserDAO userDAO;

    @RequestMapping(value = Mapping.REGISTER, method = RequestMethod.GET)
    public String getRegisterPage() {
        return View.REGISTER;
    }

    @RequestMapping(value = Constants.ADD, method = RequestMethod.POST)
    public String createNew(HttpServletRequest request, @RequestParam(name = "inputLogin") String login, @RequestParam(name = "inputPassword") String password) {
        HttpSession session = request.getSession();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        if (!EntityValidator.validateUser(user)) {
            session.setAttribute(Constants.ERROR_MESSAGE, INCORRECT_LOGIN_PASSWORD_VALUE);
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }

        List<User> users = userDAO.getAll();
        if (users.contains(user)) {
            session.setAttribute(Constants.ERROR_MESSAGE, USER_IS_ALREADY_EXISTS);
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }

        try {
            user.setId(userDAO.add(user));
            session.setAttribute(Entities.LOGGED_USER, user);
            LOGGER.debug(user.getLogin() + " registered.");
        } catch (DBLayerException e) {
            LOGGER.error(e.getMessage());
        } finally {
            return Constants.REDIRECT + Constants.INDEX;
        }
    }
}
