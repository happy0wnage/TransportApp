package ua.petrov.transport.web.controller;

import org.springframework.web.servlet.ModelAndView;
import ua.petrov.transport.web.Constants;
import ua.petrov.transport.web.Constants.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author Vladyslav
 */
public abstract class AbstractController {

    protected ModelAndView createMaV(String viewName) {
        return new ModelAndView(viewName);
    }

    protected ModelAndView createMaV() {
        return new ModelAndView();
    }

    protected void setErrorsToModel(Map<String, List<String>> errors, ModelAndView modelAndView, String viewName) {
        modelAndView.setViewName(viewName);
        modelAndView.addObject(Message.VALIDATION_ERRORS, errors);
    }

    protected void setErrorsToModel(Map<String, List<String>> errors, ModelAndView modelAndView) {
        modelAndView.addObject(Message.VALIDATION_ERRORS, errors);
    }

    protected void setErrorToModel(String error, ModelAndView modelAndView) {
        modelAndView.addObject(Message.ERROR_MESSAGE, error);
    }

    protected ModelAndView getModelWithErrors(Map<String, List<String>> errors, ModelAndView modelAndView, String url) {
        setErrorsToModel(errors, modelAndView, url);
        return modelAndView;
    }

    protected ModelAndView getModelToTheSamePage(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName(Constants.REDIRECT + request.getHeader(Constants.REFERER));
        return modelAndView;
    }

    protected void clearSessionFromObj(HttpSession session, String beanName) {
        session.removeAttribute(beanName);
    }
    protected String getHeader(HttpServletRequest request) {
        return Constants.REDIRECT + request.getHeader(Constants.REFERER);
    }
}
