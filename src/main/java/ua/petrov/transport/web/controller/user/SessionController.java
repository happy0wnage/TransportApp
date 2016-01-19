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
import ua.petrov.transport.core.entity.User;
import ua.petrov.transport.db.dao.user.IUserDAO;
import ua.petrov.transport.exception.DBLayerException;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Entities;
import ua.petrov.transport.web.Constants.Mapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Vladyslav
 */
@Controller
public class SessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger("loggerControl");
    private static final String INCORRECT_LOGIN_PASSWORD_VALUE = "Incorrect login/password value";

    @Autowired
    private IUserDAO userDAO;

    @RequestMapping(value = Mapping.LOGOUT, method = RequestMethod.GET)
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute(Entities.LOGGED_USER);
        LOGGER.info("User [" + user.getLogin() + "] is logged out!");
        session.invalidate();
        return Constants.REDIRECT + Constants.INDEX;
    }

    @RequestMapping(value = Mapping.LOGIN, method = RequestMethod.POST)
    public String login(HttpServletRequest request) {

        String login = request.getParameter("inputLogin");
        String pass = request.getParameter("inputPassword");

        HttpSession session = request.getSession();
        try {
            User user = userDAO.getUserByLogin(login);
            if (user.getPassword().equals(pass)) {
                session.setAttribute(Entities.LOGGED_USER, user);
                LOGGER.info("User [" + login + "] is logged in!");
            } else {
                session.setAttribute(Constants.ERROR_MESSAGE, INCORRECT_LOGIN_PASSWORD_VALUE);
            }
        } catch (DBLayerException ex) {
            session.setAttribute(Constants.ERROR_MESSAGE, INCORRECT_LOGIN_PASSWORD_VALUE);
            LOGGER.error(ex.getMessage());
        } finally {
            return Constants.REDIRECT + request.getHeader(Constants.REFERER);
        }

    }

}
